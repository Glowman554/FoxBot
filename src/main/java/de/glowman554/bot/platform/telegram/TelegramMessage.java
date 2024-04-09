package de.glowman554.bot.platform.telegram;


import de.glowman554.bot.command.Attachment;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.utils.StreamedFile;
import org.telegram.telegrambots.facilities.filedownloader.TelegramFileDownloader;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

public class TelegramMessage extends Message {
    private final org.telegram.telegrambots.meta.api.objects.Message telegramMessage;

    private TelegramMessage(org.telegram.telegrambots.meta.api.objects.Message telegramMessage, Message quote, ArrayList<Attachment> attachments) {
        super(telegramMessage.getText() == null ? telegramMessage.getCaption() : telegramMessage.getText(), quote, attachments, telegramMessage.getFrom().getId().toString() + "@telegram", telegramMessage.getFrom().getUserName());
        this.telegramMessage = telegramMessage;
    }

    public static TelegramMessage create(org.telegram.telegrambots.meta.api.objects.Message message) {
        Message quote = null;
        if (message.getReplyToMessage() != null) {
            quote = create(message.getReplyToMessage());
        }

        TelegramFileDownloader telegramFileDownloader = new TelegramFileDownloader(TelegramPlatform.getTelegramBot()::getBotToken);
        ArrayList<Attachment> attachments = new ArrayList<>();

        if (message.getPhoto() != null) {
            for (PhotoSize photo : message.getPhoto()) {
                attachments.add(new TelegramAttachment(getFile(photo.getFileId())));
            }
        }

        if (message.getVideo() != null) {
            attachments.add(new TelegramAttachment(getFile(message.getVideo().getFileId())));
        }

        if (message.getAudio() != null) {
            attachments.add(new TelegramAttachment(getFile(message.getAudio().getFileId())));
        }

        if (message.getDocument() != null) {
            attachments.add(new TelegramAttachment(getFile(message.getDocument().getFileId())));
        }

        return new TelegramMessage(message, quote, attachments);
    }

    private static org.telegram.telegrambots.meta.api.objects.File getFile(String fileId) {
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);

        try {
            return TelegramPlatform.getTelegramBot().execute(getFile);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reply(String reply) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramMessage.getChatId().toString());
        sendMessage.setReplyToMessageId(telegramMessage.getMessageId());
        sendMessage.setText(reply);
        sendMessage.enableHtml(true);

        try {
            TelegramPlatform.getTelegramBot().execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void replyFile(StreamedFile file, Type type, boolean nsfw) {
        try {
            switch (type) {
                case AUDIO:
                    SendAudio sendAudio = new SendAudio();
                    sendAudio.setChatId(telegramMessage.getChatId().toString());
                    sendAudio.setAudio(new InputFile(file.getStream(), file.getName()));
                    sendAudio.setReplyToMessageId(telegramMessage.getMessageId());
                    TelegramPlatform.getTelegramBot().execute(sendAudio);
                    break;
                case VIDEO:
                    SendVideo sendVideo = new SendVideo();
                    sendVideo.setChatId(telegramMessage.getChatId().toString());
                    sendVideo.setVideo(new InputFile(file.getStream(), file.getName()));
                    sendVideo.setReplyToMessageId(telegramMessage.getMessageId());
                    TelegramPlatform.getTelegramBot().execute(sendVideo);
                    break;
                case IMAGE:
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(telegramMessage.getChatId().toString());
                    sendPhoto.setPhoto(new InputFile(file.getStream(), file.getName()));
                    sendPhoto.setReplyToMessageId(telegramMessage.getMessageId());
                    TelegramPlatform.getTelegramBot().execute(sendPhoto);
                    break;
                case DOCUMENT:
                    SendDocument sendDocument = new SendDocument();
                    sendDocument.setChatId(telegramMessage.getChatId().toString());
                    sendDocument.setDocument(new InputFile(file.getStream(), file.getName()));
                    sendDocument.setReplyToMessageId(telegramMessage.getMessageId());
                    TelegramPlatform.getTelegramBot().execute(sendDocument);
                    break;
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void replyFile(StreamedFile file, Type type, boolean nsfw, String caption) {
        try {
            switch (type) {
                case AUDIO:
                    SendAudio sendAudio = new SendAudio();
                    sendAudio.setChatId(telegramMessage.getChatId().toString());
                    sendAudio.setAudio(new InputFile(file.getStream(), file.getName()));
                    sendAudio.setReplyToMessageId(telegramMessage.getMessageId());
                    sendAudio.setCaption(caption);
                    TelegramPlatform.getTelegramBot().execute(sendAudio);
                    break;
                case VIDEO:
                    SendVideo sendVideo = new SendVideo();
                    sendVideo.setChatId(telegramMessage.getChatId().toString());
                    sendVideo.setVideo(new InputFile(file.getStream(), file.getName()));
                    sendVideo.setReplyToMessageId(telegramMessage.getMessageId());
                    sendVideo.setCaption(caption);
                    TelegramPlatform.getTelegramBot().execute(sendVideo);
                    break;
                case IMAGE:
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(telegramMessage.getChatId().toString());
                    sendPhoto.setPhoto(new InputFile(file.getStream(), file.getName()));
                    sendPhoto.setReplyToMessageId(telegramMessage.getMessageId());
                    sendPhoto.setCaption(caption);
                    TelegramPlatform.getTelegramBot().execute(sendPhoto);
                    break;
                case DOCUMENT:
                    SendDocument sendDocument = new SendDocument();
                    sendDocument.setChatId(telegramMessage.getChatId().toString());
                    sendDocument.setDocument(new InputFile(file.getStream(), file.getName()));
                    sendDocument.setReplyToMessageId(telegramMessage.getMessageId());
                    sendDocument.setCaption(caption);
                    TelegramPlatform.getTelegramBot().execute(sendDocument);
                    break;
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String formatBold(String text) {
        return String.format("<b>%s</b>", text);
    }

    @Override
    public String formatItalic(String text) {
        return String.format("<i>%s</i>", text);
    }

    @Override
    public String formatCode(String text) {
        return String.format("<code>%s</code>", text);
    }

    @Override
    public String formatCodeBlock(String text) {
        return String.format("<code>%s</code>", text);
    }
}
