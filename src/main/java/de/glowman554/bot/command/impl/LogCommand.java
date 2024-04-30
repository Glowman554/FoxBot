package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.utils.StreamedFile;

public class LogCommand extends SchemaCommand {
    public LogCommand() {
        super("Download current log file.", "Usage: <command>", "log", Group.DEVELOPMENT);
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            commandContext.reply(Constants.NO_ARGUMENTS);
        } else {
            try (StreamedFile file = new StreamedFile(Logger.getCurrentLogFile())) {
                commandContext.replyFile(file, MediaType.DOCUMENT, false);
            }
        }
    }

    @Override
    public void loadSchema(Schema schema) {

    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        try (StreamedFile file = new StreamedFile(Logger.getCurrentLogFile())) {
            commandContext.replyFile(file, MediaType.DOCUMENT, false);
        }
    }
}
