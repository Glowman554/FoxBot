package de.glowman554.bot.utils.api.spotify;

import de.glowman554.bot.utils.HttpClient;
import de.glowman554.bot.utils.StreamedFile;

import java.io.IOException;

public record Song(String url, String title, String preview) {
    public StreamedFile stream() throws IOException {
        StreamedFile file = HttpClient.download(preview);
        file.setName(title + ".mp3");
        return file;
    }
}
