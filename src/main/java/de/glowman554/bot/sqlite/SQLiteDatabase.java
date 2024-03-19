package de.glowman554.bot.sqlite;

import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.registry.Registries;
import de.glowman554.config.ConfigManager;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase extends Thread {
    private final Connection connection;

    public SQLiteDatabase() {
        Runtime.getRuntime().addShutdownHook(this);
        File databaseFile = new File(ConfigManager.BASE_FOLDER, "data.db");
        boolean insertDefaultRoles = !databaseFile.exists();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getPath());
            SQLitePermissionProvider provider = new SQLitePermissionProvider(connection);
            if (insertDefaultRoles) {
                provider.insertDefaultRoles();
            }
            Registries.PERMISSION_PROVIDER.set(provider);
            Registries.TODO_PROVIDER.set(new SQLiteTodoProvider(connection));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
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
}
