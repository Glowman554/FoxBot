package de.glowman554.bot.platform.web;

import de.glowman554.bot.command.CommandContext;
import de.glowman554.bot.command.MediaType;
import de.glowman554.bot.utils.StreamedFile;
import io.javalin.websocket.WsMessageContext;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Base64;

public class WebCommandContext extends CommandContext {
    private final WsMessageContext session;
    private final JsonNode message;

    public WebCommandContext(String userId, String displayName, WsMessageContext session, JsonNode message) {
        super(userId, displayName);
        this.session = session;
        this.message = message;
    }

    private StreamedFile decode(JsonNode node) {
        InputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(node.get("file").asString()));
        return new StreamedFile(stream, node.get("name").asString());
    }

    @Override
    public Value loadArgument(Argument.Type type, String name, boolean optional) {
        if (message.has(name)) {
            return switch (type) {
                case STRING -> new Value(message.get(name).asString());
                case NUMBER -> new Value(message.get(name).asDouble());
                case INTEGER -> new Value(message.get(name).asInt());
                case BOOLEAN -> new Value(message.get(name).asBoolean());
                case ATTACHMENT -> new Value(decode(message.get(name)));
            };
        } else {
            return null;
        }
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
