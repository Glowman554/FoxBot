package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;

public class FormattingCommand extends Command {
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
}
