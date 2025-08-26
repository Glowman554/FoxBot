package de.glowman554.bot.utils.api.openai;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatBotManager {
    private final ChatCompletion chatCompletion;
    private final HashMap<String, Chat> chats = new HashMap<>();
    private final String system;

    public ChatBotManager(String apiKey, String system, String model) {
        chatCompletion = new ChatCompletion(apiKey, model);
        this.system = system;
    }

    private void cleanup() {
        chats.entrySet()
                .removeIf(entry -> entry.getValue().getLastMessageTime() + 1000 * 60 * 15 < System.currentTimeMillis());
    }

    public ChatCompletion.Message requestCompletion(String id, String message) {
        cleanup();

        if (!chats.containsKey(id)) {
            chats.put(id, new Chat(chatCompletion, system));
        }

        return chats.get(id).requestCompletion(message);
    }

    public static class Chat {
        private final ChatCompletion chatCompletion;
        private long lastMessage;
        private final List<ChatCompletion.Message> history = new ArrayList<>();

        public Chat(ChatCompletion chatCompletion, String system) {
            this.chatCompletion = chatCompletion;
            lastMessage = Instant.now().toEpochMilli();
            history.add(new ChatCompletion.Message("system", system));
        }

        public long getLastMessage() {
            return lastMessage;
        }

        public ChatCompletion.Message requestCompletion(String message) {
            history.add(new ChatCompletion.Message("user", message));

            ChatCompletion.Message[] completions = chatCompletion
                    .requestCompletion(history.toArray(ChatCompletion.Message[]::new));
            ChatCompletion.Message completion = completions[0];
            history.add(completion);
            lastMessage = Instant.now().toEpochMilli();
            return completion;
        }

        public long getLastMessageTime() {
            return lastMessage;
        }
    }
}
