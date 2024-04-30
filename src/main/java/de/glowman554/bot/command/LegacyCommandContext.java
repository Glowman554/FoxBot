package de.glowman554.bot.command;

import de.glowman554.bot.event.Event;

import java.util.ArrayList;

public abstract class LegacyCommandContext extends Event implements IContext {
    private final LegacyCommandContext quote;
    private final ArrayList<Attachment> attachments;
    private final String userId;
    private final String displayName;
    private String message;

    protected LegacyCommandContext(String message, LegacyCommandContext quote, ArrayList<Attachment> attachments, String userId, String displayName) {
        this.message = message;
        this.quote = quote;
        this.attachments = attachments;
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getMessage() {
        return message;
    }

    public LegacyCommandContext getQuote() {
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


    @Override
    public String toString() {
        return String.format("[%s (%s)] %s", displayName, userId, message);
    }

    public void modifyMessage(String newMessage) {
        message = newMessage;
    }


}
