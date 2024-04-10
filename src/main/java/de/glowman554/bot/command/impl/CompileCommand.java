package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;
import de.glowman554.bot.utils.FileUtils;
import de.glowman554.bot.utils.StreamedFile;
import de.glowman554.bot.utils.TemporaryFile;
import de.glowman554.bot.utils.compiler.CompilerManager;

import java.util.List;

public class CompileCommand extends SchemaCommand {
    public CompileCommand() {
        super("Compile and run source code files.", "Usage: <command>", null, Group.TOOLS);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            message.reply(Constants.NO_ARGUMENTS);
        } else {
            List<Attachment> attachments = message.getAttachments();
            if (attachments.isEmpty()) {
                message.reply("No file attached.");
            } else {
                for (Attachment attachment : attachments) {
                    try (TemporaryFile file = new TemporaryFile(FileUtils.getFileExtension(attachment.getName()))) {
                        attachment.download(file.getFile());
                        message.reply(message.formatCodeBlock(CompilerManager.run(file)));
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
    public void execute(CommandContext commandContext) throws Exception {
        try (StreamedFile sourceStream = commandContext.get("source").asAttachment()) {
            try (TemporaryFile file = new TemporaryFile(FileUtils.getFileExtension(sourceStream.getName()))) {
                sourceStream.save(file.getFile());
                commandContext.reply(commandContext.formatCodeBlock(CompilerManager.run(file)));
            }
        }
    }
}
