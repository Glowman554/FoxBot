package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Message;

public class DiceCommand extends Command {
    public DiceCommand() {
        super("Roll a dice.", "Usage: <command> [sides?]", null, Group.TOOLS);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (!(arguments.length == 0 || arguments.length == 1)) {
            message.reply("Expected 0 or 1 arguments!");
        } else {
            int numSides = 6;

            if (arguments.length == 1) {
                numSides = Integer.parseInt(arguments[0]);
            }

            message.reply("You rolled a " + ((int) (Math.random() * numSides) + 1));
        }
    }
}
