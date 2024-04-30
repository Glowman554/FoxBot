package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.*;

public class FormattingCommand extends SchemaCommand {
    public FormattingCommand() {
        super(Constants.TESTING, Constants.TESTING, "testing", Group.TESTING);
    }


    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        commandContext.reply(commandContext.formatBold("bold"));
        commandContext.reply(commandContext.formatItalic("italic"));
        commandContext.reply(commandContext.formatCode("code"));
        commandContext.reply(commandContext.formatCodeBlock("code block"));
    }

    @Override
    public void loadSchema(Schema schema) {

    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        commandContext.reply(commandContext.formatBold("bold"));
        commandContext.reply(commandContext.formatItalic("italic"));
        commandContext.reply(commandContext.formatCode("code"));
        commandContext.reply(commandContext.formatCodeBlock("code block"));
    }
}
