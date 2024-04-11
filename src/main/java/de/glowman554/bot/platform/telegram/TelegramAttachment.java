package de.glowman554.bot.platform.telegram;

import de.glowman554.bot.command.Attachment;
import de.glowman554.bot.utils.HttpClient;
import de.glowman554.bot.utils.StreamedFile;

import java.io.File;
import java.io.IOException;

public class TelegramAttachment extends Attachment {
    private final org.telegram.telegrambots.meta.api.objects.File file;
    private final String botToken;

    public TelegramAttachment(org.telegram.telegrambots.meta.api.objects.File file, String botToken) {
        super();
        this.file = file;
        this.botToken = botToken;
    }

    @Override
    public void download(File output) {
        try {
            HttpClient.download(output, file.getFileUrl(botToken));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StreamedFile download() {
        try {
            return HttpClient.download(file.getFileUrl(botToken));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return file.getFilePath();
    }
}
