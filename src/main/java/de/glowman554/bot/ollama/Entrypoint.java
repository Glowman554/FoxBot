package de.glowman554.bot.ollama;

import de.glowman554.bot.Feature;
import de.glowman554.bot.Main;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.ollama.commands.SetModelCommand;
import de.glowman554.bot.registry.Registries;
import de.glowman554.config.ConfigFile;
import de.glowman554.config.ConfigManager;
import de.glowman554.config.auto.Saved;
import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Entrypoint {
    private final Config config = new Config();
    private final HashMap<String, Chat> instances = new HashMap<>();
    private OllamaAPI api;

    private Timer timer = new Timer("chat clean");

    public static void main(String[] args) throws Exception {
        new Entrypoint().entrypoint();
        Main.main(args);
    }

    @de.glowman554.bot.plugin.Entrypoint
    public void entrypoint() {
        config.load();

        api = new OllamaAPI(config.host);
        api.setRequestTimeoutSeconds(60 * 10);
        api.setVerbose(true);

        new Thread(() -> {
            Logger.log("Sending pull request for %s", config.model);
            try {
                api.pullModel(config.model);
            } catch (OllamaBaseException | InterruptedException | URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
            Logger.log("Pull completed.");

        }).start();

        register();
    }

    private void register() {
        EventManager.register(this);

        Registries.FEATURES.register("ollama", new Feature("Ai chatbot",
                String.format("Use '%s' in front of a message to talk to a chatbot.", config.prefix)));
        Registries.COMMANDS.register("set_model", new SetModelCommand(config));

        Logger.log("Scheduling chat clean task");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                clean();
            }
        }, 0, 1000 * 60);
    }

    public void clean() {
        long now = Instant.now().toEpochMilli();

        for (String user : instances.keySet()) {
            Chat chat = instances.get(user);

            if (now - chat.getLastMessage() > (1000 * 60 * 60)) {
                Logger.log("Deleting chat with %s.", user);
                instances.remove(user);
            }
        }
    }

    public String response(String message, String user) throws OllamaBaseException, IOException, InterruptedException {
        if (!instances.containsKey(user)) {
            instances.put(user, new Chat(api, config.model, config.system));
        }
        return instances.get(user).response(message);
    }

    @EventTarget
    public void onMessage(Message message) {
        String messageString = message.getMessage();
        if (messageString.startsWith(config.prefix + " ")) {
            messageString = messageString.substring(config.prefix.length() + 1);
            String finalMessageString = messageString;
            new Thread(() -> {
                try {
                    message.reply(response(finalMessageString, message.getUserId()));
                } catch (OllamaBaseException | InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public static class Config extends ConfigFile {
        @Saved
        public String host = "http://localhost:11434";
        @Saved
        public String model = "llama2";
        @Saved
        public String prefix = "@chatbot";
        @Saved
        public String system = "You are a person called FoxBot. You are a furry. Keep your answers short and simple.";

        public Config() {
            super(new File(ConfigManager.BASE_FOLDER, "ollama.json"));
        }
    }
}
