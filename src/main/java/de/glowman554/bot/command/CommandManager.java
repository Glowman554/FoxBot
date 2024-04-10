package de.glowman554.bot.command;

import de.glowman554.bot.Main;
import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.registry.Registries;
import de.glowman554.config.ConfigManager;
import de.glowman554.config.Savable;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CommandManager implements Savable {
    private final File usageFile = new File(ConfigManager.BASE_FOLDER, "usage.json");
    private HashMap<String, Integer> usage = new HashMap<>();

    public CommandManager() {
        EventManager.register(this);
        load();
    }

    @EventTarget
    public void onMessage(Message message) {
        if (message.getMessage() == null) {
            return;
        }

        Logger.log("%s", message);

        if (message.getMessage().startsWith(Main.config.getPrefix())) {
            String[] arguments = message.getMessage().split(" ");
            arguments[0] = arguments[0].substring(Main.config.getPrefix().length());

            if (Registries.COMMANDS.has(arguments[0])) {
                if (usage.containsKey(arguments[0])) {
                    usage.put(arguments[0], usage.get(arguments[0]) + 1);
                } else {
                    usage.put(arguments[0], 1);
                }
                save();

                try {
                    Command command = Registries.COMMANDS.get(arguments[0]);

                    if (Registries.PERMISSION_PROVIDER.get().hasPermission(message.getUserId(), command.getPermission())) {
                        String[] commandArguments = new String[arguments.length - 1];
                        System.arraycopy(arguments, 1, commandArguments, 0, arguments.length - 1);
                        command.execute(message, commandArguments);
                    } else {
                        message.reply("You are not allowed to use this command!");
                    }
                } catch (Exception exception) {
                    Main.handleException(exception, message);
                }
            }
        }
    }

    public HashMap<String, Integer> getUsage() {
        return usage;
    }

    @Deprecated
    public PermissionProvider getPermissionProvider() {
        return Registries.PERMISSION_PROVIDER.get();
    }

    @Override
    public void fromJSON(JsonNode jsonNode) {
        usage = new HashMap<>();
        for (String key : jsonNode.keySet()) {
            usage.put(key, jsonNode.get(key).asInt());
        }
    }

    @Override
    public JsonNode toJSON() {
        JsonNode object = JsonNode.object();
        for (String key : usage.keySet()) {
            object.set(key, usage.get(key));
        }
        return object;
    }

    public void save() {
        try {
            Json.json().serialize(toJSON(), usageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try {
            fromJSON(Json.json().parse(usageFile));
        } catch (IOException ignored) {
        }
        save();
    }

    public void execute(String name, SchemaCommand command, CommandContext context) {
        if (usage.containsKey(name)) {
            usage.put(name, usage.get(name) + 1);
        } else {
            usage.put(name, 1);
        }
        save();

        try {

            if (Registries.PERMISSION_PROVIDER.get().hasPermission(context.userId, command.getPermission())) {
                command.execute(context);
            } else {
                context.reply("You are not allowed to use this command!");
            }
        } catch (Exception exception) {
            Main.handleException(exception, context);
        }
    }
}
