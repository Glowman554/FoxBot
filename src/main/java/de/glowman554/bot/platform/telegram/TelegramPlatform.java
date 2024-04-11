package de.glowman554.bot.platform.telegram;

import de.glowman554.bot.Platform;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.logging.Logger;
import de.glowman554.config.ConfigManager;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramPlatform extends Platform {
    private static TelegramBotsApi telegramBotsApi;
    private static TelegramLongPollingBot telegramBot;

    public static TelegramLongPollingBot getTelegramBot() {
        return telegramBot;
    }

    public static TelegramBotsApi getTelegramBotsApi() {
        return telegramBotsApi;
    }

    @Override
    public void init(ConfigManager configManager) {
        Config config = new Config();
        try {
            config = (Config) configManager.loadValue("telegram", config);
        } catch (IllegalArgumentException ignored) {
        }
        configManager.setValue("telegram", config);

        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            Config finalConfig = config;
            telegramBot = new TelegramLongPollingBot(finalConfig.token) {
                @Override
                public void onUpdateReceived(Update update) {
                    if (update.hasMessage()) {
                        TelegramMessage.create(update.getMessage(), finalConfig.token).call(Message.class);
                    }
                }

                @Override
                public String getBotUsername() {
                    return "null";
                }
            };
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        Logger.log("Telegram ready!");
    }

    private static class Config extends AutoSavable {
        @Saved
        private String token = "";
    }
}
