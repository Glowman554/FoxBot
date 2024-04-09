package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.utils.StreamedFile;

public class LogCommand extends Command {
    public LogCommand() {
        super("Download current log file.", "Usage: <command>", "log", Group.DEVELOPMENT);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            message.reply(Constants.NO_ARGUMENTS);
        } else {
            try (StreamedFile file = new StreamedFile(Logger.getCurrentLogFile())) {
                message.replyFile(file, Message.Type.DOCUMENT, false);
            }
        }
    }
}
