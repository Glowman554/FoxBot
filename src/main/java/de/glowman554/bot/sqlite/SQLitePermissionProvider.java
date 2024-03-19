package de.glowman554.bot.sqlite;

import de.glowman554.bot.command.PermissionProvider;
import de.glowman554.bot.utils.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLitePermissionProvider extends PermissionProvider {
    private final Connection connection;

    public SQLitePermissionProvider(Connection connection) {
        this.connection = connection;

        try {
            PreparedStatement role = connection.prepareStatement("CREATE TABLE IF NOT EXISTS \"role\" (roleName TEXT NOT NULL, PRIMARY KEY (roleName))");
            role.execute();
            role.close();

            PreparedStatement permission = connection.prepareStatement("CREATE TABLE IF NOT EXISTS permission (roleName TEXT NOT NULL, permission TEXT NOT NULL, PRIMARY KEY (permission,roleName), FOREIGN KEY (roleName) REFERENCES \"role\"(roleName))");
            permission.execute();
            permission.close();

            PreparedStatement user = connection.prepareStatement("CREATE TABLE IF NOT EXISTS \"user\" (userId TEXT NOT NULL, roleName TEXT, PRIMARY KEY (userId), FOREIGN KEY (roleName) REFERENCES \"role\"(roleName))");
            user.execute();
            user.close();
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
            PreparedStatement statement = connection.prepareStatement("select count(*) from user, role, permission where userId = ? and permission = ? and user.roleName = role.roleName and role.roleName = permission.roleName");
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
            PreparedStatement statement = connection.prepareStatement("insert or replace into user (userId, roleName) values (?, ?)");
            statement.setString(1, userId);
            statement.setString(2, role);
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
            PreparedStatement statement = connection.prepareStatement("insert into permission (roleName, permission) values (?, ?)");
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
            PreparedStatement statement = connection.prepareStatement("select permission from permission where roleName = ?");
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
