package de.glowman554.bot.platform.discord;

import de.glowman554.bot.command.Attachment;
import de.glowman554.bot.command.LegacyCommandContext;
import de.glowman554.bot.command.MediaType;
import de.glowman554.bot.utils.StreamedFile;
import net.dv8tion.jda.api.utils.FileUpload;

import java.util.ArrayList;

public class DiscordLegacyCommandContext extends LegacyCommandContext {
    private final net.dv8tion.jda.api.entities.Message discordMessage;

    private DiscordLegacyCommandContext(net.dv8tion.jda.api.entities.Message discordMessage, LegacyCommandContext quote, ArrayList<Attachment> attachments) {
        super(discordMessage.getContentDisplay(), quote, attachments, discordMessage.getAuthor().getId() + "@discord", discordMessage.getAuthor().getEffectiveName());
        this.discordMessage = discordMessage;
    }

    public static DiscordLegacyCommandContext create(net.dv8tion.jda.api.entities.Message message) {
        LegacyCommandContext quote = null;
        net.dv8tion.jda.api.entities.Message referenced = message.getReferencedMessage();
        if (referenced != null) {
            quote = create(referenced);
        }

        ArrayList<Attachment> attachments = new ArrayList<>();
        for (net.dv8tion.jda.api.entities.Message.Attachment attachment : message.getAttachments()) {
            attachments.add(new DiscordAttachment(attachment));
        }

        return new DiscordLegacyCommandContext(message, quote, attachments);
    }

    @Override
    public void reply(String reply) {
        discordMessage.reply(reply).complete();
    }

    @Override
    public void replyFile(StreamedFile file, MediaType type, boolean nsfw) {
        FileUpload upload = FileUpload.fromData(file.getStream(), file.getName());
        if (nsfw) {
            upload.asSpoiler();
        }
        discordMessage.replyFiles(upload).complete();
    }

    @Override
    public void replyFile(StreamedFile file, MediaType type, boolean nsfw, String caption) {
        FileUpload upload = FileUpload.fromData(file.getStream(), file.getName());
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
