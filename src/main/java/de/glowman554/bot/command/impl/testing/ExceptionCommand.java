package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.*;

public class ExceptionCommand extends SchemaCommand {
    public ExceptionCommand() {
        super(Constants.TESTING, Constants.TESTING, "testing", Group.TESTING);
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        throw new Exception("Test");
    }

    @Override
    public void loadSchema(Schema schema) {
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        throw new Exception("Test");
    }
}
