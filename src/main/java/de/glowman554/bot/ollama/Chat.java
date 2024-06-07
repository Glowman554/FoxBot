package de.glowman554.bot.ollama;

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatMessage;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatMessageRole;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatResult;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final OllamaAPI api;
    private final String model;
    private long lastMessage;
    private List<OllamaChatMessage> history = new ArrayList<>();

    public Chat(OllamaAPI api, String model, String system) {
        this.api = api;
        this.model = model;
        lastMessage = Instant.now().toEpochMilli();
        history.add(new OllamaChatMessage(OllamaChatMessageRole.SYSTEM, system));
    }

    public long getLastMessage() {
        return lastMessage;
    }

    public String response(String message) throws OllamaBaseException, IOException, InterruptedException {
        history.add(new OllamaChatMessage(OllamaChatMessageRole.USER, message));
        lastMessage = Instant.now().toEpochMilli();
        OllamaChatResult result = api.chat(model, history);
        history = result.getChatHistory();
        return result.getResponse();
    }

    public JsonNode toJson() {
        JsonNode root = JsonNode.array();

        for (OllamaChatMessage message : history) {
            JsonNode entry = JsonNode.object();
            entry.set("role", message.getRole().name());
            entry.set("content", message.getContent());
            root.add(entry);
        }

        return root;
    }
}
