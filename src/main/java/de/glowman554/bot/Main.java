package de.glowman554.bot;

import de.glowman554.bot.api.HelpEndpoint;
import de.glowman554.bot.api.StatsEndpoint;
import de.glowman554.bot.api.UsageEndpoint;
import de.glowman554.bot.command.CommandManager;
import de.glowman554.bot.command.LegacyCommand;
import de.glowman554.bot.command.LegacyCommandContext;
import de.glowman554.bot.command.SchemaCommandContext;
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
import de.glowman554.config.ConfigFile;
import de.glowman554.config.ConfigManager;
import de.glowman554.config.Savable;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.*;
import java.util.List;

public class Main {
    public static final Config config = new Config();
    public static CommandManager commandManager;

    public static void main(String[] args) throws Exception {
        Logger.log("Starting...");

        if (!ConfigManager.BASE_FOLDER.exists()) {
            if (!ConfigManager.BASE_FOLDER.mkdir()) {
                throw new RuntimeException("Could not create " + ConfigManager.BASE_FOLDER.getPath());
            }
            Logger.log("Created directory %s", ConfigManager.BASE_FOLDER.getPath());
        }
        commandManager = new CommandManager();
        ConfigManager platformConfigs = new ConfigManager("platform", false);
        config.load();

        if (config.useBuiltinDatabase) {
            new SQLiteDatabase();
        }

        new PluginLoader(new File("plugins"));

        registerPlatforms();
        registerCommands();
        registerFeatures();

        Registries.PLATFORMS.getRegistry().values().forEach(platform -> platform.init(platformConfigs));

        if (config.api) {
            startJavalin();
        }
        complete();
    }

    private static void startJavalin() {
        File frontend = new File(config.webserver.frontendPath);

        Javalin app = Javalin.create(javalinConfig -> {
            if (frontend.exists()) {
                javalinConfig.staticFiles.add(staticFileConfig -> {
                    staticFileConfig.hostedPath = "/";
                    staticFileConfig.directory = frontend.getAbsolutePath();
                    staticFileConfig.location = Location.EXTERNAL;
                });
            } else {
                Logger.log("[WARNING] Frontend path not found!");
            }

            if (config.webserver.isSSL()) {
                javalinConfig.jetty.addConnector((server, httpConfiguration) -> {
                    ServerConnector sslConnector = new ServerConnector(server, getSslContextFactory());
                    sslConnector.setPort(443);
                    return sslConnector;
                });
            } else {
                javalinConfig.jetty.addConnector((server, httpConfiguration) -> {
                    ServerConnector connector = new ServerConnector(server);
                    connector.setPort(config.webserver.port);
                    return connector;
                });
            }
        });

        app.exception(Exception.class, (exception, context) -> {
            exception.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);

            exception.printStackTrace(pw);

            context.status(500);
            context.result("<h1>Internal server error!</h1>\n<span style=\"white-space: pre-line\"><code>"
                    + sw.getBuffer().toString() + "</code></span>");
        });

        app.error(404, context -> {
            context.header("Content-Type", "text/html");
            context.result(new FileInputStream(new File(frontend, "404.html")));
        });

        app.before(context -> context.header("Access-Control-Allow-Origin", "*"));

        app.get("/api/help", new HelpEndpoint());
        app.get("/api/usage", new UsageEndpoint());
        app.get("/api/stats", new StatsEndpoint());

        new JavalinEvent(app).call();

        app.start();
        Logger.log("Listening on port %d", config.webserver.port);
    }

    private static void registerPlatforms() {
        Registries.PLATFORMS.register(DiscordPlatform.class, new DiscordPlatform());
        Registries.PLATFORMS.register(TelegramPlatform.class, new TelegramPlatform());
        Registries.PLATFORMS.register(WebPlatform.class, new WebPlatform());
    }

    private static void registerFeatures() {
        Registries.FEATURES.register("commands", new Feature("Commands",
                "The bot supports many different commands.\nTo get a list of all commands use <prefix>help.\nTo get help for a specific command use <prefix>help [command]."));
        Registries.FEATURES.register("roles", new Feature("Roles",
                "The bot supports a role system which allows granting specific permissions to roles.\nA user can have exactly 1 role or no role at all."));

        if (config.compiler) {
            Registries.FEATURES.register("compiler", new Feature("Compiler",
                    "Use the '<prefix>compile' command to compile and / or execute the attached file."));
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
        // Registries.COMMANDS.register("yuna", new YunaCommand());
        Registries.COMMANDS.register("role", new RoleCommand());
        Registries.COMMANDS.register("spotify", new SpotifyCommand());
        Registries.COMMANDS.register("run", new RunCommand());
        Registries.COMMANDS.register("todo", new TodoCommand());
        Registries.COMMANDS.register("sticker", new StickerCommand());

        if (config.compiler) {
            Registries.COMMANDS.register("compile", new CompileCommand());
        }

        if (config.testing) {
            Testing.register();
        }
    }

    private static void complete() {
        for (LegacyCommand.Group group : LegacyCommand.Group.values()) {
            List<String> groupCommands = Registries.COMMANDS.getRegistry().keySet().stream()
                    .filter(key -> Registries.COMMANDS.get(key).getGroup() == group).toList();
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

    private static String saveCrash(Exception exception) {
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
        return id;
    }

    public static void handleException(Exception exception, LegacyCommandContext commandContext) {
        String id = saveCrash(exception);
        commandContext.reply("There was an error executing your request. Use "
                + commandContext.formatCode(config.getPrefix() + "crash " + id) + " to upload the stack trace!");
    }

    public static void handleException(Exception exception, SchemaCommandContext context) {
        String id = saveCrash(exception);
        context.reply("There was an error executing your request. Use crash id " + context.formatCode(id)
                + " to upload the stack trace!");
    }

    private static SslContextFactory.Server getSslContextFactory() {
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(config.webserver.keystoreFile);
        sslContextFactory.setKeyStorePassword(config.webserver.keystorePassword);
        return sslContextFactory;
    }

    public static class Config extends ConfigFile {
        @Saved
        private String prefix = "owo!";
        @Saved
        private boolean testing = false;
        @Saved
        private boolean compiler = true;
        @Saved
        private String compilerBackend = "http://localhost:1234/";
        @Saved
        private boolean useBuiltinDatabase = true;
        @Saved(remap = Savable.class)
        private Spotify spotify = new Spotify();
        @Saved(remap = Savable.class)
        private WebserverConfig webserver = new WebserverConfig();
        @Saved
        private boolean api = true;
        @Saved
        private String telegramToken = "";

        public Config() {
            super(new File(ConfigManager.BASE_FOLDER, "config.json"));
        }

        public String getPrefix() {
            return prefix;
        }

        public Spotify getSpotify() {
            return spotify;
        }

        public String getCompilerBackend() {
            return compilerBackend;
        }

        public String getTelegramToken() {
            return telegramToken;
        }

        public static class WebserverConfig extends AutoSavable {
            @Saved
            private int port = 8888;
            @Saved
            private String keystoreFile = "";
            @Saved
            private String keystorePassword = "";
            @Saved
            private String frontendPath = "frontend/out";

            public boolean isSSL() {
                return !(keystoreFile.isEmpty() || keystorePassword.isEmpty());
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
