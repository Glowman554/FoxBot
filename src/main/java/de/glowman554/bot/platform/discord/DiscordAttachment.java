package de.glowman554.bot.platform.discord;

import de.glowman554.bot.utils.StreamedFile;
import de.glowman554.bot.command.Attachment;
import net.dv8tion.jda.api.entities.Message;

import java.io.File;

public class DiscordAttachment extends Attachment {
    private final Message.Attachment attachment;

    public DiscordAttachment(Message.Attachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public void download(File output) {
        attachment.getProxy().downloadToFile(output).join();
    }

    @Override
    public StreamedFile download() {
        return new StreamedFile(attachment.getProxy().download().join(), getName());
    }

    @Override
    public String getName() {
        return attachment.getFileName();
    }
}
