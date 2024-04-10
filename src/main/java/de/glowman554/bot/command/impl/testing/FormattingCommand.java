package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.*;

public class FormattingCommand extends SchemaCommand {
    public FormattingCommand() {
        super(Constants.TESTING, Constants.TESTING, "testing", Group.TESTING);
    }


    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        message.reply(message.formatBold("bold"));
        message.reply(message.formatItalic("italic"));
        message.reply(message.formatCode("code"));
        message.reply(message.formatCodeBlock("code block"));
    }

    @Override
    public void loadSchema(Schema schema) {

    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        commandContext.reply(commandContext.formatBold("bold"));
        commandContext.reply(commandContext.formatItalic("italic"));
        commandContext.reply(commandContext.formatCode("code"));
        commandContext.reply(commandContext.formatCodeBlock("code block"));
    }
}
