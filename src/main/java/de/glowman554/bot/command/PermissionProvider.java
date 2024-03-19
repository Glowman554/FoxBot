package de.glowman554.bot.command;

import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.utils.Pair;

import java.util.List;

public abstract class PermissionProvider {
    public abstract boolean hasPermission(String userId, String permission);

    public abstract void setRole(String userId, String role);

    public abstract void createRole(String roleName);

    public abstract void addPermission(String roleName, String permission);

    public abstract void removeRole(String roleName);

    public abstract List<Pair<String, List<String>>> getRoles();

    public void insertDefaultRoles() {
        RoleBuilder.begin("admin", this)
                .permission("log")
                .permission("stop")
                .permission("no_limit")
                .permission("testing")
                .permission("no_jail")
                .permission("execute")
                .user("584344177257480192@discord")
                .user("1784982409@telegram");

    }

    public static class RoleBuilder {
        private final String role;
        private final PermissionProvider permissionProvider;

        private RoleBuilder(String role, PermissionProvider permissionProvider) {
            this.role = role;
            this.permissionProvider = permissionProvider;
            this.permissionProvider.createRole(role);
            Logger.log("[%s] created role", role);
        }

        public static RoleBuilder begin(String role, PermissionProvider permissionProvider) {
            return new RoleBuilder(role, permissionProvider);
        }

        public RoleBuilder permission(String permission) {
            this.permissionProvider.addPermission(role, permission);
            Logger.log("[%s] added permission %s", role, permission);
            return this;
        }

        public RoleBuilder user(String user) {
            this.permissionProvider.setRole(user, role);
            Logger.log("[%s] added user %s", role, user);

            return this;
        }
    }
}
