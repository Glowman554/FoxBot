package de.glowman554.bot.utils.api.openai;

import de.glowman554.bot.Platform;
import de.glowman554.bot.command.LegacyCommandContext;
import de.glowman554.bot.event.EventTarget;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatBotManager {
    private final ChatCompletion chatCompletion;
    private final HashMap<String, Chat> chats = new HashMap<>();
    private final String system;
    private final String prefix ;

    public ChatBotManager(String apiKey, String system, String prefix) {
        chatCompletion = new ChatCompletion(apiKey);
        this.system = system;
        this.prefix = prefix;
    }

    private void cleanup() {
        chats.entrySet().removeIf(entry -> entry.getValue().getLastMessageTime() + 1000 * 60 * 15 < System.currentTimeMillis());
    }

    public ChatCompletion.Message requestCompletion(String id, String message) {
        cleanup();

        if (!chats.containsKey(id)) {
            chats.put(id, new Chat(chatCompletion, system));
        }

        return chats.get(id).requestCompletion(message);
    }

    @Platform.Raw
    public void discord(MessageReceivedEvent event) {
        if (event.getChannel().getName().contains("chatbot")) {
            new Thread(() -> {
                if (event.getAuthor().isBot()) {
                    return;
                }
                event.getChannel()
                        .sendMessage(requestCompletion(event.getChannel().getId(), event.getMessage().getContentDisplay()).getContent())
                        .queue();
            }).start();
        }
    }

    @EventTarget
    public void onMessage(LegacyCommandContext message) {
        String messageString = message.getMessage();
        if (messageString.startsWith(prefix + " ")) {
            messageString = messageString.substring(prefix.length() + 1);
            String finalMessageString = messageString;
            new Thread(() -> {
                message.reply(requestCompletion(message.getUserId(), finalMessageString).getContent());
            }).start();
        }
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

            ChatCompletion.Message[] completions = chatCompletion.requestCompletion(history.toArray(ChatCompletion.Message[]::new));
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
