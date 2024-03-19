package de.glowman554.bot.sqlite;

import de.glowman554.bot.utils.Pair;
import de.glowman554.bot.utils.TodoProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SQLiteTodoProvider extends TodoProvider {
    private final Connection connection;

    public SQLiteTodoProvider(Connection connection) {
        this.connection = connection;

        try {
            PreparedStatement role = connection.prepareStatement("CREATE TABLE IF NOT EXISTS \"todo\" (userId TEXT NOT NULL, todoId INT NOT NULL, todo TEXT NOT NULL, done INT NOT NULL, PRIMARY KEY (userId, todoId))");
            role.execute();
            role.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteTodo(String userId, int todoId) {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from todo where userId = ? and todoId = ?");
            statement.setString(1, userId);
            statement.setInt(2, todoId);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void changeTodoDone(String userId, int todoId, boolean done) {
        try {
            PreparedStatement statement = connection.prepareStatement("update todo set done = ? where userId = ? and todoId = ?");
            statement.setInt(1, done ? 1 : 0);
            statement.setString(2, userId);
            statement.setInt(3, todoId);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getMaxTodoId(String userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("select max(todoId) from todo where userId = ?");
            statement.setString(1, userId);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int result = resultSet.getInt(1);
            resultSet.close();
            statement.close();

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void createTodo(String userId, String todo) {
        int newId = getMaxTodoId(userId) + 1;

        try {
            PreparedStatement statement = connection.prepareStatement("insert into todo (userId, todoId, todo, done) values (?, ?, ?, 0)");
            statement.setString(1, userId);
            statement.setInt(2, newId);
            statement.setString(3, todo);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public HashMap<Integer, Pair<Boolean, String>> loadTodo(String userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("select todoId, done, todo from todo where userId = ?");
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            HashMap<Integer, Pair<Boolean, String>> result = new HashMap<>();
            while (resultSet.next()) {
                result.put(resultSet.getInt("todoId"), new Pair<>(resultSet.getInt("done") == 1, resultSet.getString("todo")));
            }
            resultSet.close();
            statement.close();

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
