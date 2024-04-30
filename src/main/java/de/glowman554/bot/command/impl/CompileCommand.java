package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;
import de.glowman554.bot.utils.StreamedFile;
import de.glowman554.bot.utils.compiler.RemoteCompiler;

import java.util.List;

public class CompileCommand extends SchemaCommand {
    public CompileCommand() {
        super("Compile and run source code files.", "Usage: <command>", null, Group.TOOLS);
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            commandContext.reply(Constants.NO_ARGUMENTS);
        } else {
            List<Attachment> attachments = commandContext.getAttachments();
            if (attachments.isEmpty()) {
                commandContext.reply("No file attached.");
            } else {
                for (Attachment attachment : attachments) {
                    try (StreamedFile sourceStream = attachment.download()) {
                        commandContext.reply(RemoteCompiler.run(sourceStream));
                    }
                }
            }
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.ATTACHMENT, "source", "Source code file", false).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        try (StreamedFile sourceStream = commandContext.get("source").asAttachment()) {
            commandContext.reply(RemoteCompiler.run(sourceStream));
        }
    }
}
