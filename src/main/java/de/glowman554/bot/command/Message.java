package de.glowman554.bot.command;

import de.glowman554.bot.event.Event;

import java.io.File;
import java.util.ArrayList;

public abstract class Message extends Event {
    private final Message quote;
    private final ArrayList<Attachment> attachments;
    private final String userId;
    private final String displayName;
    private String message;

    protected Message(String message, Message quote, ArrayList<Attachment> attachments, String userId, String displayName) {
        this.message = message;
        this.quote = quote;
        this.attachments = attachments;
        this.userId = userId;
        this.displayName = displayName;
    }

    public abstract void reply(String reply);

    public String getMessage() {
        return message;
    }

    public Message getQuote() {
        return quote;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public abstract void replyFile(File file, Type type, boolean nsfw);

    public abstract void replyFile(File file, Type type, boolean nsfw, String caption);

    public abstract String formatBold(String text);

    public abstract String formatItalic(String text);

    public abstract String formatCode(String text);

    public abstract String formatCodeBlock(String text);

    @Override
    public String toString() {
        return String.format("[%s (%s)] %s", displayName, userId, message);
    }

    public void modifyMessage(String newMessage) {
        message = newMessage;
    }


    public enum Type {
        IMAGE, VIDEO, AUDIO, DOCUMENT
    }
}
