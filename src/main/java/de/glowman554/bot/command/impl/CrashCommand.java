package de.glowman554.bot.command.impl;

import de.glowman554.bot.utils.StreamedFile;
import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.logging.Logger;

import java.io.File;

public class CrashCommand extends Command {
    public CrashCommand() {
        super("Download crash log file.", "Usage: <command> [crashId]", "log", Group.DEVELOPMENT);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length != 1) {
            message.reply("Command takes exactly 1 argument");
        } else {
            try (StreamedFile file = new StreamedFile(new File(Logger.logDirectory, arguments[0] + ".log"))) {
                message.replyFile(file, Message.Type.DOCUMENT, false);
            }
        }
    }
}
