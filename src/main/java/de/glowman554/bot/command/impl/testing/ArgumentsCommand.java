package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.*;

public class ArgumentsCommand extends SchemaCommand {
    public ArgumentsCommand() {
        super(Constants.TESTING, Constants.TESTING, "testing", Group.TESTING);
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        commandContext.reply(String.join(", ", arguments));
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "arg", Constants.TESTING, false).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) {
        commandContext.reply(commandContext.get("arg").asString());
    }
}
