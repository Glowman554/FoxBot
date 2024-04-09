package de.glowman554.bot.platform.web;

import de.glowman554.bot.command.Attachment;
import de.glowman554.bot.utils.StreamedFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Base64;

public class WebAttachment extends Attachment {
    private final String name;
    private final String file;

    public WebAttachment(String name, String file) {
        this.name = name;
        this.file = file;
    }

    @Override
    public void download(File output) {
        try (StreamedFile file = download()) {
            file.save(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StreamedFile download() {
        InputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(file));
        return new StreamedFile(stream, name);
    }

    @Override
    public String getName() {
        return name;
    }
}
