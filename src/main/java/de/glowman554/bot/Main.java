package de.glowman554.bot;

import de.glowman554.bot.api.HelpEndpoint;
import de.glowman554.bot.api.StatsEndpoint;
import de.glowman554.bot.api.UsageEndpoint;
import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.CommandManager;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.command.impl.*;
import de.glowman554.bot.command.impl.testing.Testing;
import de.glowman554.bot.event.impl.JavalinEvent;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.platform.discord.DiscordPlatform;
import de.glowman554.bot.platform.telegram.TelegramPlatform;
import de.glowman554.bot.platform.web.WebPlatform;
import de.glowman554.bot.plugin.PluginLoader;
import de.glowman554.bot.registry.Registries;
import de.glowman554.bot.sqlite.SQLiteDatabase;
import de.glowman554.bot.utils.AutoFileSavable;
import de.glowman554.config.ConfigManager;
import de.glowman554.config.Savable;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;
import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;
import io.javalin.http.staticfiles.Location;

import java.io.*;
import java.util.List;

public class Main {
    public static final File configFile = new File(ConfigManager.BASE_FOLDER, "config.json");

    public static final Config config = new Config();
    public static final CommandManager commandManager = new CommandManager();
    public static final File staticFolder = new File("host");

    public static void main(String[] args) throws Exception {
        Logger.log("Starting...");

        if (!ConfigManager.BASE_FOLDER.exists()) {
            if (!ConfigManager.BASE_FOLDER.mkdir()) {
                throw new RuntimeException("Could not create " + ConfigManager.BASE_FOLDER.getPath());
            }
            Logger.log("Created directory %s", ConfigManager.BASE_FOLDER.getPath());
        }
        ConfigManager platformConfigs = new ConfigManager("platform", false);
        config.load(configFile);

        if (config.useBuiltinDatabase) {
            new SQLiteDatabase();
        }

        if (config.api) {

        }


        new PluginLoader(new File("plugins"));

        registerPlatforms();
        registerCommands();
        registerFeatures();

        Registries.PLATFORMS.getRegistry().values().forEach(platform -> platform.init(platformConfigs));

        startJavalin();
        complete();
    }

    private static void startJavalin() {
        if (!staticFolder.exists()) {
            Logger.log("Creating %s", staticFolder.getPath());
            if (!staticFolder.mkdir()) {
                throw new RuntimeException("Could not create " + staticFolder.getPath());
            }
        }

        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = staticFolder.getAbsolutePath();
                staticFileConfig.location = Location.EXTERNAL;
            });
            javalinConfig.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = new File("frontend/out").getAbsolutePath();
                staticFileConfig.location = Location.EXTERNAL;
            });

            if (config.webserver.isSSL()) {
                SslPlugin plugin = new SslPlugin(sslConfig -> {
                    sslConfig.pemFromPath(config.webserver.certificate, config.webserver.privatekey);
                });
                javalinConfig.registerPlugin(plugin);
            }
        });

        app.exception(Exception.class, (exception, context) -> {
            exception.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);

            exception.printStackTrace(pw);

            context.status(500);
            context.result("<h1>Internal server error!</h1>\n<span style=\"white-space: pre-line\"><code>" + sw.getBuffer().toString() + "</code></span>");
        });

        app.error(404, context -> {
            context.header("Content-Type", "text/html");
            context.result(new FileInputStream("frontend/out/404.html"));
        });


        app.before(context -> context.header("Access-Control-Allow-Origin", "*"));

        app.get("/api/help", new HelpEndpoint());
        app.get("/api/usage", new UsageEndpoint());
        app.get("/api/stats", new StatsEndpoint());

        new JavalinEvent(app).call();

        app.start(config.webserver.port);
        Logger.log("Listening on port %d", config.webserver.port);
    }

    private static void registerPlatforms() {
        Registries.PLATFORMS.register(DiscordPlatform.class, new DiscordPlatform());
        Registries.PLATFORMS.register(TelegramPlatform.class, new TelegramPlatform());
        Registries.PLATFORMS.register(WebPlatform.class, new WebPlatform());
    }

    private static void registerFeatures() {
        Registries.FEATURES.register("commands", new Feature("Commands", "The bot supports many different commands.\nTo get a list of all commands use <prefix>help.\nTo get help for a specific command use <prefix>help [command]."));
        Registries.FEATURES.register("roles", new Feature("Roles", "The bot supports a role system which allows granting specific permissions to roles.\nA user can have exactly 1 role or no role at all."));

        if (config.compiler) {
            Registries.FEATURES.register("compiler", new Feature("Compiler", "Use the '<prefix>compile' command to compile and / or execute the attached file."));
        }
    }

    private static void registerCommands() {
        Registries.COMMANDS.register("help", new HelpCommand());
        Registries.COMMANDS.register("cat", new CatCommand());
        Registries.COMMANDS.register("dog", new DogCommand());
        Registries.COMMANDS.register("fox", new FoxCommand());
        Registries.COMMANDS.register("coinflip", new CoinFlipCommand());
        Registries.COMMANDS.register("repeat", new RepeatCommand());
        Registries.COMMANDS.register("commit", new CommitCommand());
        Registries.COMMANDS.register("fact", new FactCommand());
        Registries.COMMANDS.register("say", new SayCommand());
        Registries.COMMANDS.register("whoami", new WhoamiCommand());
        Registries.COMMANDS.register("stop", new StopCommand());
        Registries.COMMANDS.register("log", new LogCommand());
        Registries.COMMANDS.register("crash", new CrashCommand());
        Registries.COMMANDS.register("calc", new CalcCommand());
        Registries.COMMANDS.register("furry", new FurryCommand());
        Registries.COMMANDS.register("joke", new JokeCommand());
        Registries.COMMANDS.register("magic8", new Magic8Command());
        Registries.COMMANDS.register("uptime", new UptimeCommand());
        Registries.COMMANDS.register("dice", new DiceCommand());
        Registries.COMMANDS.register("features", new FeaturesCommand());
        Registries.COMMANDS.register("wikipedia", new WikipediaCommand());
        Registries.COMMANDS.register("meme", new MemeCommand());
        Registries.COMMANDS.register("yuna", new YunaCommand());
        Registries.COMMANDS.register("role", new RoleCommand());
        Registries.COMMANDS.register("spotify", new SpotifyCommand());
        Registries.COMMANDS.register("run", new RunCommand());
        Registries.COMMANDS.register("todo", new TodoCommand());

        if (config.compiler) {
            Registries.COMMANDS.register("compile", new CompileCommand());
        }

        if (config.testing) {
            Testing.register();
        }
    }

    private static void complete() {
        for (Command.Group group : Command.Group.values()) {
            List<String> groupCommands = Registries.COMMANDS.getRegistry().keySet().stream().filter(key -> Registries.COMMANDS.get(key).getGroup() == group).toList();
            Logger.log("%s: %d commands", group.getDisplayName(), groupCommands.size());
            for (String key : groupCommands) {
                Logger.log("%s%s", config.prefix, key);
            }
            Logger.log("");
        }

        Logger.log("Features:");
        for (Feature feature : Registries.FEATURES.getRegistry().values()) {
            Logger.log("%s", feature.name());
        }

        Logger.log("Startup complete.");
    }

    public static void handleException(Exception exception, Message message) {
        String id = "crash_" + System.currentTimeMillis();

        try {
            FileWriter fileWriter = new FileWriter(new File(Logger.logDirectory, id + ".log"));
            PrintWriter pw = new PrintWriter(fileWriter);
            exception.printStackTrace(pw);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ignored) {
            exception.printStackTrace();
        }

        message.reply("There was an error executing your request. Use " + message.formatCode(config.getPrefix() + "crash " + id) + " to upload the stack trace!");
    }


    public static class Config extends AutoFileSavable {
        @Saved
        private String prefix = "owo!";
        @Saved
        private boolean testing = false;
        @Saved
        private boolean compiler = true;
        @Saved
        private boolean useBuiltinDatabase = true;
        @Saved(remap = Savable.class)
        private Spotify spotify = new Spotify();
        @Saved(remap = Savable.class)
        private WebserverConfig webserver = new WebserverConfig();
        @Saved
        private boolean api = true;

        public String getPrefix() {
            return prefix;
        }

        public Spotify getSpotify() {
            return spotify;
        }

        public static class WebserverConfig extends AutoSavable {
            @Saved
            private int port = 8888;
            @Saved
            private String certificate = "";
            @Saved
            private String privatekey = "";

            public boolean isSSL() {
                return !(certificate.isEmpty() || privatekey.isEmpty());
            }
        }

        public static class Spotify extends AutoSavable {
            @Saved
            private String clientId = "";
            @Saved
            private String clientSecret = "";
            @Saved
            private String redirectUrl = "http://localhost:8888/callback";

            public String getClientId() {
                return clientId;
            }

            public String getClientSecret() {
                return clientSecret;
            }

            public String getRedirectUrl() {
                return redirectUrl;
            }
        }
    }
}
