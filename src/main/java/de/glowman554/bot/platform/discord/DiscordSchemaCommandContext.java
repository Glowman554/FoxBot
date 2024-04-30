package de.glowman554.bot.platform.discord;

import de.glowman554.bot.command.MediaType;
import de.glowman554.bot.command.Schema;
import de.glowman554.bot.command.SchemaCommandContext;
import de.glowman554.bot.utils.StreamedFile;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.utils.FileUpload;

public class DiscordSchemaCommandContext extends SchemaCommandContext {
    private final SlashCommandInteractionEvent event;
    private boolean usedInitialReply = false;

    public DiscordSchemaCommandContext(SlashCommandInteractionEvent event) {
        super(event.getUser().getId() + "@discord", event.getUser().getEffectiveName());
        this.event = event;
    }

    @Override
    public Value loadArgument(Argument.Type type, String name, boolean optional) {
        OptionMapping mapping = event.getOption(name);
        if (mapping == null) {
            if (!optional) {
                throw new RuntimeException("?");
            }
            return null;
        }
        return switch (type) {
            case ATTACHMENT ->
                    new Schema.Value(new StreamedFile(mapping.getAsAttachment().getProxy().download().join(), mapping.getAsAttachment().getFileName()));
            case INTEGER -> new Schema.Value(mapping.getAsInt());
            case BOOLEAN -> new Schema.Value(mapping.getAsBoolean());
            case STRING -> new Schema.Value(mapping.getAsString());
            case NUMBER -> new Schema.Value(mapping.getAsDouble());
        };
    }

    @Override
    public void reply(String reply) {
        if (usedInitialReply) {
            event.getHook().sendMessage(reply).complete();
        } else {
            event.reply(reply).complete();
            usedInitialReply = true;
        }
    }

    @Override
    public void replyFile(StreamedFile file, MediaType type, boolean nsfw) {
        FileUpload upload = FileUpload.fromData(file.getStream(), file.getName());
        if (nsfw) {
            upload.asSpoiler();
        }
        if (usedInitialReply) {
            event.getHook().sendFiles(upload).complete();
        } else {
            event.replyFiles(upload).complete();
            usedInitialReply = true;
        }
    }

    @Override
    public void replyFile(StreamedFile file, MediaType type, boolean nsfw, String caption) {
        FileUpload upload = FileUpload.fromData(file.getStream(), file.getName());
        if (nsfw) {
            upload.asSpoiler();
        }
        if (usedInitialReply) {
            event.getHook().sendFiles(upload).setContent(caption).complete();
        } else {
            event.replyFiles(upload).setContent(caption).complete();
            usedInitialReply = true;
        }

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
