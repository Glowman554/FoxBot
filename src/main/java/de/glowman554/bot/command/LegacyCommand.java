package de.glowman554.bot.command;

import de.glowman554.bot.logging.Logger;

public abstract class LegacyCommand {
    private final String shortHelp;
    private final String longHelp;
    private final String permission;
    private final Group group;

    public LegacyCommand(String shortHelp, String longHelp, String permission, Group group) {
        this.shortHelp = shortHelp;
        this.longHelp = longHelp;
        this.permission = permission;
        this.group = group;

        if (!shortHelp.endsWith(".")) {
            Logger.log("[WARNING (%s)] shortHelp should end with a period.", getClass().getSimpleName());
        }
    }

    public String getShortHelp() {
        return shortHelp;
    }

    public String getLongHelp() {
        return longHelp;
    }

    public String getPermission() {
        return permission;
    }

    public Group getGroup() {
        return group;
    }

    public abstract void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception;

    public enum Group {
        DEVELOPMENT("Development"), ANIMAL("Animal"), FUN("Fun"), TOOLS("Tool"), TESTING("Testing");

        private final String displayName;

        Group(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
