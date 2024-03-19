package de.glowman554.bot.platform.discord;

import de.glowman554.bot.Main;
import de.glowman554.bot.Platform;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.logging.Logger;
import de.glowman554.config.ConfigManager;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

public class DiscordPlatform extends Platform implements EventListener {

    @Override
    public void init(ConfigManager configManager) {
        Config config = new Config();
        try {
            config = (Config) configManager.loadValue("discord", config);
        } catch (IllegalArgumentException ignored) {
        }
        configManager.setValue("discord", config);


        try {
            JDABuilder.createDefault(config.token)
                    .addEventListeners(this)
                    .setActivity(Activity.streaming(Main.config.getPrefix() + "help", "https://www.twitch.tv/glowman434"))
                    .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                    .build().awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if (genericEvent instanceof ReadyEvent) {
            Logger.log("Discord ready!");
        } else if (genericEvent instanceof MessageReceivedEvent messageReceivedEvent) {
            if (messageReceivedEvent.getAuthor().isBot()) {
                return;
            }
            DiscordMessage.create(messageReceivedEvent.getMessage()).call(Message.class);
        }
    }

    private static class Config extends AutoSavable {
        @Saved
        private String token = "";
    }
}
