package de.glowman554.bot.database;

import de.glowman554.bot.utils.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IDatabaseInterface {

    Connection getConnection();

    default void update(String query) {
        update(query, (Object[]) null);
    }

    void update(String query, Object... args);

    default <T> List<T> query(String query, QueryListMapper<T> mapper) {
        return query(query, mapper, (Object[]) null);
    }

    default <K, T> Map<K, T> queryMap(String query, QueryMapMapper<K, T> mapper) {
        return queryMap(query, mapper, (Object[]) null);
    }


    <T> List<T> query(String query, QueryListMapper<T> mapper, Object... args);

    <K, T> HashMap<K, T> queryMap(String query, QueryMapMapper<K, T> mapper, Object... args);

    void applyMigration(Migration migration);

    String getFlavour();

    void insertUser(String id);

    interface QueryListMapper<T> {
        T apply(ResultSet rs);
    }

    interface QueryMapMapper<K, T> {
        Pair<K, T> apply(ResultSet rs);
    }
}

