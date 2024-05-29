package de.glowman554.bot.postgresql;

import de.glowman554.bot.command.PermissionProvider;
import de.glowman554.bot.utils.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLPermissionProvider extends PermissionProvider {
    private final Connection connection;

    public PostgreSQLPermissionProvider(Connection connection) {
        this.connection = connection;

        try {
            PreparedStatement role = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS role (roleName text NOT NULL, PRIMARY KEY (roleName))");
            role.execute();
            role.close();

            PreparedStatement permission = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS permission (roleName text NOT NULL, permission text NOT NULL, PRIMARY KEY (permission,roleName), FOREIGN KEY (roleName) REFERENCES role(roleName) ON DELETE CASCADE ON UPDATE CASCADE)");
            permission.execute();
            permission.close();

            PreparedStatement users = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS users (userId text NOT NULL, roleName text, PRIMARY KEY (userId), FOREIGN KEY (roleName) REFERENCES role(roleName))");
            users.execute();
            users.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasPermission(String userId, String permission) {
        if (permission == null) {
            return true;
        }

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) from users, role, permission where userId = ? and permission = ? and users.roleName = role.roleName and role.roleName = permission.roleName");
            statement.setString(1, userId);
            statement.setString(2, permission);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            boolean has = resultSet.getInt(1) > 0;
            resultSet.close();
            statement.close();
            return has;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setRole(String userId, String role) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into users (userId, roleName) values (?, ?) on conflict (userId) do update set roleName = ?");
            statement.setString(1, userId);
            statement.setString(2, role);
            statement.setString(3, role);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createRole(String roleName) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into role (roleName) values (?)");
            statement.setString(1, roleName);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addPermission(String roleName, String permission) {
        try {
            PreparedStatement statement = connection
                    .prepareStatement("insert into permission (roleName, permission) values (?, ?)");
            statement.setString(1, roleName);
            statement.setString(2, permission);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeRole(String roleName) {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from role where roleName = ?");
            statement.setString(1, roleName);
            statement.execute();
            statement.close();

            statement = connection.prepareStatement("delete from permission where roleName = ?");
            statement.setString(1, roleName);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getPermissions(String roleName) {
        List<String> result = new ArrayList<>();

        try {
            PreparedStatement statement = connection
                    .prepareStatement("select permission from permission where roleName = ?");
            statement.setString(1, roleName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(resultSet.getString("permission"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public List<Pair<String, List<String>>> getRoles() {
        List<Pair<String, List<String>>> result = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("select roleName from role");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String roleName = resultSet.getString("roleName");
                result.add(new Pair<>(roleName, getPermissions(roleName)));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
