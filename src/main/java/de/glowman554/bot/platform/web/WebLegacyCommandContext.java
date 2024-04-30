package de.glowman554.bot.platform.web;

import de.glowman554.bot.command.Attachment;
import de.glowman554.bot.command.LegacyCommandContext;
import de.glowman554.bot.command.MediaType;
import de.glowman554.bot.utils.StreamedFile;
import io.javalin.websocket.WsMessageContext;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;

public class WebLegacyCommandContext extends LegacyCommandContext {
    private final WsMessageContext session;

    protected WebLegacyCommandContext(String message, String userId, String displayName, WsMessageContext session, ArrayList<Attachment> attachments) {
        super(message, null, attachments, userId, displayName);
        this.session = session;
    }

    @Override
    public void reply(String reply) {
        JsonNode root = JsonNode.object();
        root.set("type", "reply");
        root.set("message", reply);
        session.send(Json.json().serialize(root));
    }

    private String encode(StreamedFile file) throws IOException {
        String mime = URLConnection.getFileNameMap().getContentTypeFor(file.getName());

        try (BufferedInputStream buffer = new BufferedInputStream(file.getStream())) {
            return "data:" + mime + ";base64," + Base64.getEncoder().encodeToString(buffer.readAllBytes());
        }
    }

    @Override
    public void replyFile(StreamedFile file, MediaType type, boolean nsfw) {
        replyFile(file, type, nsfw, null);
    }

    @Override
    public void replyFile(StreamedFile file, MediaType type, boolean nsfw, String caption) {
        try {
            JsonNode root = JsonNode.object();
            root.set("type", "replyFile");
            root.set("file", encode(file));
            root.set("fileName", file.getName());
            root.set("fileType", type.toString());
            root.set("nsfw", nsfw);
            root.set("caption", caption);
            session.send(Json.json().serialize(root));
        } catch (IOException e) {
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
