package de.glowman554.bot.platform.discord;

import de.glowman554.bot.Main;
import de.glowman554.bot.Platform;
import de.glowman554.bot.command.LegacyCommand;
import de.glowman554.bot.command.LegacyCommandContext;
import de.glowman554.bot.command.SchemaCommand;
import de.glowman554.bot.command.SchemaCommandContext;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.registry.Registries;
import de.glowman554.config.ConfigManager;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
            JDA jda = JDABuilder.createDefault(config.token)
                    .addEventListeners(this)
                    .setActivity(Activity.streaming(Main.config.getPrefix() + "help", "https://www.twitch.tv/glowman434"))
                    .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                    .build().awaitReady();

            ArrayList<SlashCommandData> commands = new ArrayList<>();
            Registries.COMMANDS.iterate((name, command) -> {
                if (command instanceof SchemaCommand schemaCommand) {
                    DiscordSchema schema = new DiscordSchema(name, command.getShortHelp());
                    schemaCommand.loadSchema(schema);
                    commands.add(schema.getSlashCommandData());
                }
            });
            jda.updateCommands().addCommands(commands).complete();

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
            DiscordLegacyCommandContext.create(messageReceivedEvent.getMessage()).call(LegacyCommandContext.class);
        } else if (genericEvent instanceof SlashCommandInteractionEvent slashCommandInteractionEvent) {
            LegacyCommand command = Registries.COMMANDS.get(slashCommandInteractionEvent.getFullCommandName());
            if (command instanceof SchemaCommand schemaCommand) {
                SchemaCommandContext context = new DiscordSchemaCommandContext(slashCommandInteractionEvent);
                schemaCommand.loadSchema(context);
                Main.commandManager.execute(slashCommandInteractionEvent.getFullCommandName(), schemaCommand, context);
            }
        }
    }

    private static class Config extends AutoSavable {
        @Saved
        private String token = "";
    }
}
