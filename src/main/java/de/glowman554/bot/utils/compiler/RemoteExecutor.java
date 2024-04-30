package de.glowman554.bot.utils.compiler;

import java.io.IOException;

import de.glowman554.bot.Main;
import de.glowman554.bot.utils.HttpClient;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RemoteExecutor {
    public static String execute(String command) throws IOException {
        return HttpClient.post(Main.config.getCompilerBackend() + "api/run",
                RequestBody.create(command, MediaType.parse("application/text")));
    }

}
