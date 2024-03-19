package de.glowman554.bot.command.impl;

import de.glowman554.bot.Feature;
import de.glowman554.bot.Main;
import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.registry.Registries;

public class FeaturesCommand extends Command {
    public FeaturesCommand() {
        super("List the features of the bot.", "Usage: <command>", null, Group.TOOLS);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            message.reply(Constants.NO_ARGUMENTS);
        } else {
            StringBuilder result = new StringBuilder();

            for (Feature feature : Registries.FEATURES.getRegistry().values()) {
                result.append(message.formatBold(feature.name() + ":")).append("\n").append(feature.description().replace("<prefix>", Main.config.getPrefix())).append("\n\n");
            }

            message.reply(result.toString());
        }
    }
}
