package de.glowman554.bot.ollama;

import java.io.IOException;

import de.glowman554.bot.Platform.Raw;
import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RawAcceptors {
    private final Entrypoint main;

    public RawAcceptors(Entrypoint main) {
        this.main = main;
    }

    @Raw
    public void discord(MessageReceivedEvent event) {
        if (event.getChannel().getName().contains("chatbot")) {
            new Thread(() -> {
                try {
                    if (event.getAuthor().isBot()) {
                        return;
                    }
                    event.getChannel().sendMessage(
                            main.response(event.getMessage().getContentDisplay(), event.getChannel().getId()))
                            .complete();
                } catch (OllamaBaseException | InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
