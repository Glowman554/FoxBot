package de.glowman554.bot.database.provider;

import de.glowman554.bot.command.PermissionProvider;
import de.glowman554.bot.database.IDatabaseInterface;
import de.glowman554.bot.database.Migration;
import de.glowman554.bot.utils.Pair;

import java.sql.SQLException;
import java.util.List;

public class DefaultPermissionProvider extends PermissionProvider {
    private final IDatabaseInterface database;

    public DefaultPermissionProvider(IDatabaseInterface database) {
        this.database = database;

        database.applyMigration(new Migration("initial_permission_tables", db -> {
            if (db.getFlavour().equals("sqlite")) {
                db.update("CREATE TABLE IF NOT EXISTS role (roleName text NOT NULL, PRIMARY KEY (roleName))");
                db.update("CREATE TABLE IF NOT EXISTS permission (roleName text NOT NULL, permission text NOT NULL, PRIMARY KEY (permission,roleName), FOREIGN KEY (roleName) REFERENCES role(roleName) ON DELETE CASCADE ON UPDATE CASCADE)");
                db.update("CREATE TABLE IF NOT EXISTS userRoles (userId text NOT NULL, roleName text, PRIMARY KEY (userId), FOREIGN KEY (roleName) REFERENCES role(roleName), FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE ON UPDATE CASCADE)");
            } else {
                throw new RuntimeException("Unsupported database " + db.getFlavour());
            }
        }));

    }

    @Override
    public boolean hasPermission(String userId, String permission) {
        if (permission == null) {
            return true;
        }

        if (database.getFlavour().equals("sqlite")) {
            return database.query("select count(*) from userRoles, role, permission where userId = ? and permission = ? and userRoles.roleName = role.roleName and role.roleName = permission.roleName", rs -> {
                try {
                    return rs.getInt(1) > 0;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, userId, permission).get(0);
        }
        throw new RuntimeException("Unsupported database " + database.getFlavour());
    }

    @Override
    public void setRole(String userId, String role) {
        if (database.getFlavour().equals("sqlite")) {
            database.update("insert into userRoles (userId, roleName) values (?, ?) on conflict (userId) do update set roleName = ?", userId, role, role);
        } else {
            throw new RuntimeException("Unsupported database " + database.getFlavour());
        }
    }

    @Override
    public void createRole(String roleName) {
        if (database.getFlavour().equals("sqlite")) {
            database.update("insert into role (roleName) values (?)", roleName);
        } else {
            throw new RuntimeException("Unsupported database " + database.getFlavour());
        }
    }

    @Override
    public void addPermission(String roleName, String permission) {
        if (database.getFlavour().equals("sqlite")) {
            database.update("insert into permission (roleName, permission) values (?, ?)", roleName, permission);
        } else {
            throw new RuntimeException("Unsupported database " + database.getFlavour());
        }
    }

    @Override
    public void removeRole(String roleName) {
        if (database.getFlavour().equals("sqlite")) {
            database.update("delete from role where roleName = ?", roleName);
        } else {
            throw new RuntimeException("Unsupported database " + database.getFlavour());
        }
    }

    public List<String> getPermissions(String roleName) {
        if (database.getFlavour().equals("sqlite")) {
            return database.query("select permission from permission where roleName = ?", rs -> {
                try {
                    return rs.getString("permission");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, roleName);
        }
        throw new RuntimeException("Unsupported database " + database.getFlavour());
    }

    @Override
    public List<Pair<String, List<String>>> getRoles() {
        if (database.getFlavour().equals("sqlite")) {
            return database.query("select roleName from role", rs -> {
                try {
                    String roleName = rs.getString("roleName");
                    return new Pair<>(roleName, getPermissions(roleName));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        throw new RuntimeException("Unsupported database " + database.getFlavour());
    }
}
