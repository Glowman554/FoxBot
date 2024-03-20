package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.utils.TimeUtils;

public class UptimeCommand extends Command {
    public static final long startingTime = System.currentTimeMillis();

    public UptimeCommand() {
        super("See the uptime of the bot.", "Usage: <command>", null, Group.TOOLS);
    }


    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            message.reply(Constants.NO_ARGUMENTS);
        } else {
            message.reply(TimeUtils.millisecondToDhms(System.currentTimeMillis() - startingTime));
        }
    }
}
