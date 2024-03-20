package de.glowman554.bot.platform.web;

import de.glowman554.bot.Main;
import de.glowman554.bot.utils.StreamedFile;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.utils.FileUtils;
import de.glowman554.bot.utils.TemporaryFile;
import io.javalin.websocket.WsMessageContext;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WebMessage extends Message {
    private final WsMessageContext session;

    protected WebMessage(String message, String userId, String displayName, WsMessageContext session) {
        super(message, null, new ArrayList<>(), userId, displayName);
        this.session = session;
    }

    @Override
    public void reply(String reply) {
        JsonNode root = JsonNode.object();
        root.set("type", "reply");
        root.set("message", reply);
        session.send(Json.json().serialize(root));
    }

    private String staticFile(StreamedFile file) throws IOException {
        String outputName = TemporaryFile.createTemporaryFileName(FileUtils.getFileExtension(file.getName()));
        File output = new File(Main.staticFolder, outputName);
        file.save(output);
        return outputName;
    }

    @Override
    public void replyFile(StreamedFile file, Type type, boolean nsfw) {
        replyFile(file, type, nsfw, null);
    }

    @Override
    public void replyFile(StreamedFile file, Type type, boolean nsfw, String caption) {
        try {
            JsonNode root = JsonNode.object();
            root.set("type", "replyFile");
            root.set("file", staticFile(file));
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
