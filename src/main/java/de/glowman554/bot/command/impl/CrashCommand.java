package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.utils.StreamedFile;

import java.io.File;

public class CrashCommand extends SchemaCommand {
    public CrashCommand() {
        super("Download crash log file.", "Usage: <command> [crashId]", "log", Group.DEVELOPMENT);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length != 1) {
            message.reply("Command takes exactly 1 argument");
        } else {
            try (StreamedFile file = new StreamedFile(new File(Logger.logDirectory, arguments[0] + ".log"))) {
                message.replyFile(file, MediaType.DOCUMENT, false);
            }
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "id", "Crash id", false).register();
    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        try (StreamedFile file = new StreamedFile(new File(Logger.logDirectory, commandContext.get("id").asString() + ".log"))) {
            commandContext.replyFile(file, MediaType.DOCUMENT, false);
        }
    }
}
