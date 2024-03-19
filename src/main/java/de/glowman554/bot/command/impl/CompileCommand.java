package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Attachment;
import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.utils.FileUtils;
import de.glowman554.bot.utils.TemporaryFile;
import de.glowman554.bot.utils.compiler.CompilerManager;

import java.util.List;

public class CompileCommand extends Command {
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
}
