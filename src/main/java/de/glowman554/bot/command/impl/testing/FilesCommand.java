package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.*;
import de.glowman554.bot.utils.StreamedFile;

public class FilesCommand extends SchemaCommand {
    public FilesCommand() {
        super(Constants.TESTING, Constants.TESTING, "testing", LegacyCommand.Group.TESTING);
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (commandContext.getAttachments().isEmpty()) {
            commandContext.reply("No files attached!");
        }

        for (Attachment attachment : commandContext.getAttachments()) {
            commandContext.reply(String.format("%s: %s", attachment.getName(), attachment.getType()));

            try (StreamedFile file = attachment.download()) {
                if (attachment.getType() != null) {
                    MediaType type = attachment.getType();
                    commandContext.replyFile(file, type, false);
                }
            }
        }

    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.ATTACHMENT, "file", Constants.TESTING, false).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        try (StreamedFile file = commandContext.get("file").asAttachment()) {
            commandContext.replyFile(file, MediaType.DOCUMENT, false);
        }
    }
}
