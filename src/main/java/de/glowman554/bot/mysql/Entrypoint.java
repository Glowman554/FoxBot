package de.glowman554.bot.mysql;

import de.glowman554.bot.Main;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.registry.Registries;
import de.glowman554.config.ConfigFile;
import de.glowman554.config.ConfigManager;
import de.glowman554.config.auto.Saved;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Entrypoint extends Thread {
    private Connection connection;
    private final Config config = new Config();

    public static void main(String[] args) throws Exception {
        new Entrypoint().entrypoint();
        Main.main(args);
    }

    @de.glowman554.bot.plugin.Entrypoint
    public void entrypoint() {
        try {
            config.load();
            Class.forName("com.mysql.cj.jdbc.Driver");

            Logger.log("Connecting to mysql database %s:%s", config.host, config.database);

            Runtime.getRuntime().addShutdownHook(this);
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s&autoReconnect=true", config.host, config.database, config.username, config.password));
            MySQLPermissionProvider provider = new MySQLPermissionProvider(connection);
            if (config.insertDefaultRoles) {
                provider.insertDefaultRoles();
            }
            Registries.PERMISSION_PROVIDER.set(provider);
            Registries.TODO_PROVIDER.set(new MySQLTodoProvider(connection));

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void run() {
        Logger.log("Closing database connection.");
        try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }

    public static class Config extends ConfigFile {
        @Saved
        public String database = "foxbot";
        @Saved
        public String host = "localhost";
        @Saved
        public String username = "foxbot";
        @Saved
        public String password = "foxbot";
        @Saved
        public boolean insertDefaultRoles = false;

        public Config() {
            super(new File(ConfigManager.BASE_FOLDER, "mysql.json"));
        }
    }
}
