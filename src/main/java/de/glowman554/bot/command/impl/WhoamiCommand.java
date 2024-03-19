package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;

public class WhoamiCommand extends Command {
    public WhoamiCommand() {
        super("Information's about yourself.", "Usage: <command>", null, Group.TOOLS);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            message.reply(Constants.NO_ARGUMENTS);
        } else {
            message.reply("displayName: " + message.formatCode(message.getDisplayName()) + "\n" + "userId: " + message.formatCode(message.getUserId()));
        }
    }
}
