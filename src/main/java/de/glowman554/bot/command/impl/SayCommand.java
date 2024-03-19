package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Message;

public class SayCommand extends Command {
    public SayCommand() {
        super("Let the bot say something.", "Usage: <command> [text]", null, Group.TOOLS);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            message.reply("You need to specify a message to say.");
        } else {
            message.reply(String.join(" ", arguments));
        }
    }
}
