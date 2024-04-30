package de.glowman554.bot.utils.compiler;

import de.glowman554.bot.Main;
import de.glowman554.bot.utils.HttpClient;
import de.glowman554.bot.utils.StreamedFile;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.IOException;

public class RemoteCompiler {
    public static String run(StreamedFile file) throws IOException {
        String s = new String(file.getStream().readAllBytes());

        return HttpClient.post(Main.config.getCompilerBackend() + "api/compile?filename=" + file.getName(),
                RequestBody.create(s, MediaType.parse("application/text")));
    }
}
