package de.glowman554.bot.platform.discord;

import de.glowman554.bot.command.Attachment;
import de.glowman554.bot.command.Message;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.ArrayList;

public class DiscordMessage extends Message {
    private final net.dv8tion.jda.api.entities.Message discordMessage;

    private DiscordMessage(net.dv8tion.jda.api.entities.Message discordMessage, Message quote, ArrayList<Attachment> attachments) {
        super(discordMessage.getContentDisplay(), quote, attachments, discordMessage.getAuthor().getId() + "@discord", discordMessage.getAuthor().getEffectiveName());
        this.discordMessage = discordMessage;
    }

    public static DiscordMessage create(net.dv8tion.jda.api.entities.Message message) {
        Message quote = null;
        net.dv8tion.jda.api.entities.Message referenced = message.getReferencedMessage();
        if (referenced != null) {
            quote = create(referenced);
        }

        ArrayList<Attachment> attachments = new ArrayList<>();
        for (net.dv8tion.jda.api.entities.Message.Attachment attachment : message.getAttachments()) {
            attachments.add(new DiscordAttachment(attachment));
        }

        return new DiscordMessage(message, quote, attachments);
    }

    @Override
    public void reply(String reply) {
        discordMessage.reply(reply).complete();
    }

    @Override
    public void replyFile(File file, Type type, boolean nsfw) {
        FileUpload upload = FileUpload.fromData(file);
        if (nsfw) {
            upload.asSpoiler();
        }
        discordMessage.replyFiles(upload).complete();
    }

    @Override
    public void replyFile(File file, Type type, boolean nsfw, String caption) {
        FileUpload upload = FileUpload.fromData(file);
        if (nsfw) {
            upload.asSpoiler();
        }
        discordMessage.replyFiles(upload).setContent(caption).complete();
    }

    @Override
    public String formatBold(String text) {
        return String.format("**%s**", text);
    }

    @Override
    public String formatItalic(String text) {
        return String.format("*%s*", text);
    }

    @Override
    public String formatCode(String text) {
        return String.format("`%s`", text);
    }

    @Override
    public String formatCodeBlock(String text) {
        return String.format("```\n%s\n```", text);
    }
}
