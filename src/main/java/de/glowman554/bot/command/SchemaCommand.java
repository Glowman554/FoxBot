package de.glowman554.bot.command;

public abstract class SchemaCommand extends LegacyCommand {
    public SchemaCommand(String shortHelp, String longHelp, String permission, Group group) {
        super(shortHelp, longHelp, permission, group);
    }

    public abstract void loadSchema(Schema schema);

    public abstract void execute(SchemaCommandContext commandContext) throws Exception;
}
