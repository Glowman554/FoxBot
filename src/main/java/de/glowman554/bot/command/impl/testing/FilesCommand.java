package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.Attachment;
import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.utils.StreamedFile;

public class FilesCommand extends Command {
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
                    Message.Type type = switch (attachment.getType()) {
                        case AUDIO -> Message.Type.AUDIO;
                        case IMAGE -> Message.Type.IMAGE;
                        case VIDEO -> Message.Type.VIDEO;
                    };
                    message.replyFile(file, type, false);
                }
            }
        }

    }
}
