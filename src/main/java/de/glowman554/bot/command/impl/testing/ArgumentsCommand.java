package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;

public class ArgumentsCommand extends Command {
    public ArgumentsCommand() {
        super(Constants.TESTING, Constants.TESTING, "testing", Group.TESTING);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        message.reply(String.join(", ", arguments));
    }
}
