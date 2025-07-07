package de.glowman554.bot.database.provider;

import de.glowman554.bot.database.IDatabaseInterface;
import de.glowman554.bot.database.Migration;
import de.glowman554.bot.utils.Pair;

import java.sql.SQLException;
import java.util.HashMap;

public class DefaultTodoProvider extends TodoProvider {
    private final IDatabaseInterface database;

    public DefaultTodoProvider(IDatabaseInterface database) {
        this.database = database;

        database.applyMigration(new Migration("initial_todo_table", db -> {
            if (db.getFlavour().equals("sqlite")) {
                db.update("CREATE TABLE IF NOT EXISTS todo (userId text NOT NULL, todoId INT NOT NULL, todo TEXT NOT NULL, done INT NOT NULL, PRIMARY KEY (userId, todoId), FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE ON UPDATE CASCADE)");
            } else {
                throw new RuntimeException("Unsupported database " + db.getFlavour());
            }
        }));
    }

    @Override
    public void deleteTodo(String userId, int todoId) {
        if (database.getFlavour().equals("sqlite")) {
            database.update("delete from todo where userId = ? and todoId = ?", userId, todoId);
        } else {
            throw new RuntimeException("Unsupported database " + database.getFlavour());
        }
    }

    @Override
    public void changeTodoDone(String userId, int todoId, boolean done) {
        if (database.getFlavour().equals("sqlite")) {
            database.update("update todo set done = ? where userId = ? and todoId = ?", done ? 1 : 0, userId, todoId);
        } else {
            throw new RuntimeException("Unsupported database " + database.getFlavour());
        }
    }

    private int getMaxTodoId(String userId) {
        if (database.getFlavour().equals("sqlite")) {
            return database.query("select max(todoId) from todo where userId = ?", resultSet -> {
                try {
                    return resultSet.getInt(1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, userId).get(0);
        }
        throw new RuntimeException("Unsupported database " + database.getFlavour());
    }

    @Override
    public void createTodo(String userId, String todo) {
        if (database.getFlavour().equals("sqlite")) {
            int newId = getMaxTodoId(userId) + 1;
            database.update("insert into todo (userId, todoId, todo, done) values (?, ?, ?, 0)", userId, newId, todo);
        } else {
            throw new RuntimeException("Unsupported database " + database.getFlavour());
        }
    }

    @Override
    public HashMap<Integer, Pair<Boolean, String>> loadTodo(String userId) {
        if (database.getFlavour().equals("sqlite")) {
            return database.queryMap("select todoId, done, todo from todo where userId = ?", rs -> {
                try {
                    return new Pair<>(rs.getInt("todoId"), new Pair<>(rs.getInt("done") == 1, rs.getString("todo")));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, userId);
        }
        throw new RuntimeException("Unsupported database " + database.getFlavour());
    }

}
