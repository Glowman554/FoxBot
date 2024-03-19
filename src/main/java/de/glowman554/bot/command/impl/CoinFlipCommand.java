package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;

public class CoinFlipCommand extends Command {
    public CoinFlipCommand() {
        super("Flip a coin.", "Usage: <command>", null, Group.FUN);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            message.reply(Constants.NO_ARGUMENTS);
            return;
        }

        boolean random = Math.random() < 0.5;

        if (random) {
            message.reply("You've landed on heads!");
        } else {
            message.reply("You've landed on tails!");
        }
    }
}
