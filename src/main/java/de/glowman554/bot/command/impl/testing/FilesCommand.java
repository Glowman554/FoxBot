package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.*;
import de.glowman554.bot.utils.StreamedFile;

public class FilesCommand extends SchemaCommand {
    public FilesCommand() {
        super(Constants.TESTING, Constants.TESTING, "testing", Command.Group.TESTING);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (message.getAttachments().isEmpty()) {
            message.reply("No files attached!");
        }

        for (Attachment attachment : message.getAttachments()) {
            message.reply(String.format("%s: %s", attachment.getName(), attachment.getType()));

            try (StreamedFile file = attachment.download()) {
                if (attachment.getType() != null) {
                    MediaType type = switch (attachment.getType()) {
                        case AUDIO -> MediaType.AUDIO;
                        case IMAGE -> MediaType.IMAGE;
                        case VIDEO -> MediaType.VIDEO;
                    };
                    message.replyFile(file, type, false);
                }
            }
        }

    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.ATTACHMENT, "file", Constants.TESTING, false).register();
    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        try (StreamedFile file = commandContext.get("file").asAttachment()) {
            commandContext.replyFile(file, MediaType.DOCUMENT, false);
        }
    }
}
