package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Message;

public class Magic8Command extends Command {
    private final String[] answers = new String[]{"No", "Yes", "Maybe", "Think about is a bit more then try again...", "Absolutely", "Not at all", "Of couse!", "As it seems... Yes", "As it seems... No", "Could be", "Hell NO!"};

    public Magic8Command() {
        super("Ask the magic 8 ball a question.", "Usage: <command> [question]", null, Group.FUN);
    }


    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            message.reply("You should ask a question didn't you know?");
        } else {
            String response = answers[(int) (Math.random() * answers.length)];

            message.reply(response);
        }
    }
}
