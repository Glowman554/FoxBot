package de.glowman554.bot.features;

import de.glowman554.bot.Platform;
import de.glowman554.bot.command.LegacyCommandContext;
import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.utils.api.openai.ChatBotManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChatBot {
    private final ChatBotManager chatBotManager;
    private final String prefix;

    public ChatBot(String apiKey, String system, String prefix) {
        chatBotManager = new ChatBotManager(apiKey, system);
        this.prefix = prefix;

        Platform.accept(this);
        EventManager.register(this);
    }

    @Platform.Raw
    public void discord(MessageReceivedEvent event) {
        if (event.getChannel().getName().contains("chatbot")) {
            new Thread(() -> {
                if (event.getAuthor().isBot()) {
                    return;
                }
                event.getChannel().sendMessage(chatBotManager.requestCompletion(event.getChannel().getId(), event.getMessage().getContentDisplay()).getContent()).queue();
            }).start();
        }
    }

    @EventTarget
    public void onMessage(LegacyCommandContext message) {
        String messageString = message.getMessage();
        if (messageString.startsWith(prefix + " ")) {
            messageString = messageString.substring(prefix.length() + 1);
            String finalMessageString = messageString;
            new Thread(() -> message.reply(chatBotManager.requestCompletion(message.getUserId(), finalMessageString).getContent())).start();
        }
    }

}
