package de.glowman554.bot.database.sqlite;

import de.glowman554.bot.database.IDatabaseInterface;
import de.glowman554.bot.database.Migration;
import de.glowman554.bot.database.provider.DefaultPermissionProvider;
import de.glowman554.bot.database.provider.DefaultTodoProvider;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.registry.Registries;
import de.glowman554.bot.utils.Pair;
import de.glowman554.config.ConfigManager;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLiteDatabase extends Thread implements IDatabaseInterface {
    private final Connection connection;

    public SQLiteDatabase() {
        Runtime.getRuntime().addShutdownHook(this);
        File databaseFile = new File(ConfigManager.BASE_FOLDER, "data.db");
        boolean insertDefaultRoles = !databaseFile.exists();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getPath());
            createMigrationTable();

            DefaultPermissionProvider provider = new DefaultPermissionProvider(this);
            if (insertDefaultRoles) {
                provider.insertDefaultRoles();
            }
            Registries.PERMISSION_PROVIDER.set(provider);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        applyMigration(new Migration("initial_users_table", db -> {
            db.update("CREATE TABLE IF NOT EXISTS \"users\" (userId TEXT NOT NULL, PRIMARY KEY (userId))");
        }));

        Registries.TODO_PROVIDER.set(new DefaultTodoProvider(this));
    }

    @Override
    public void run() {
        Logger.log("Closing database connection.");
        try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void update(String query, Object... args) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    statement.setObject(i + 1, args[i]);
                }
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public <T> List<T> query(String query, QueryListMapper<T> mapper, Object... args) {
        List<T> list = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    statement.setObject(i + 1, args[i]);
                }
            }

            ResultSet hasResult = statement.executeQuery();

            while (hasResult.next()) {
                list.add(mapper.apply(hasResult));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public <K, T> HashMap<K, T> queryMap(String query, QueryMapMapper<K, T> mapper, Object... args) {
        HashMap<K, T> map = new HashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    statement.setObject(i + 1, args[i]);
                }
            }

            ResultSet hasResult = statement.executeQuery();

            while (hasResult.next()) {
                Pair<K, T> pair = mapper.apply(hasResult);
                map.put(pair.t1(), pair.t2());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    private void createMigrationTable() {
        update("CREATE TABLE IF NOT EXISTS __migrations (id VARCHAR(255) PRIMARY KEY)");
    }

    private boolean isMigrationApplied(Migration migration) {
        return query("SELECT count(*) FROM __migrations WHERE id = ?", rs -> {
            try {
                return rs.getInt(1) > 0;
            } catch (SQLException e) {
                return false;
            }
        }, migration.id()).get(0);
    }


    @Override
    public void applyMigration(Migration migration) {
        if (!isMigrationApplied(migration)) {
            Logger.log("Applying migration %s", migration.id());

            migration.apply().execute(this);

            update("INSERT INTO __migrations (id) VALUES (?)", migration.id());
        }
    }

    @Override
    public String getFlavour() {
        return "sqlite";
    }

    @Override
    public void insertUser(String id) {
        update("insert into users (userId) values (?) on conflict (userId) do nothing", id);
    }
}
