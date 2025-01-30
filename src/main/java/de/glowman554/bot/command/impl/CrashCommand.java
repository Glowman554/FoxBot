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
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length != 1) {
            commandContext.reply("Command takes exactly 1 argument");
        } else {
            common(commandContext, arguments[0]);
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "id", "Crash id", false).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        common(commandContext, commandContext.get("id").asString());
    }

    private void common(IContext context, String id) throws Exception {
        try (StreamedFile file = new StreamedFile(new File(Logger.logDirectory, id + ".log"))) {
            context.replyFile(file, MediaType.DOCUMENT, false);
        }
    }
}
