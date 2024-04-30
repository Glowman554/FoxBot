package de.glowman554.bot.utils.compiler;

import de.glowman554.bot.Main;
import de.glowman554.bot.utils.HttpClient;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.IOException;

public class RemoteExecutor {
    public static String execute(String command) throws IOException {
        return HttpClient.post(Main.config.getCompilerBackend() + "api/run",
                RequestBody.create(command, MediaType.parse("application/text")));
    }

}
