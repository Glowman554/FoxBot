package de.glowman554.bot.command;

import de.glowman554.bot.utils.StreamedFile;

public interface Reply {
    void reply(String reply);

    void replyFile(StreamedFile file, MediaType type, boolean nsfw);

    void replyFile(StreamedFile file, MediaType type, boolean nsfw, String caption);

    String formatBold(String text);

    String formatItalic(String text);

    String formatCode(String text);

    String formatCodeBlock(String text);

}
