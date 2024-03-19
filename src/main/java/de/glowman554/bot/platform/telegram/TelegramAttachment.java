package de.glowman554.bot.platform.telegram;

import de.glowman554.bot.command.Attachment;
import de.glowman554.bot.utils.HttpClient;

import java.io.File;
import java.io.IOException;

public class TelegramAttachment extends Attachment {
    private final org.telegram.telegrambots.meta.api.objects.File file;

    public TelegramAttachment(org.telegram.telegrambots.meta.api.objects.File file) {
        super();
        this.file = file;
    }

    @Override
    public void download(File output) {
        try {
            HttpClient.download(output, file.getFileUrl(TelegramPlatform.getTelegramBot().getBotToken()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return file.getFilePath();
    }
}
