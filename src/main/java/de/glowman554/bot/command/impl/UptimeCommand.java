package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;
import de.glowman554.bot.utils.TimeUtils;

public class UptimeCommand extends SchemaCommand {
    public static final long startingTime = System.currentTimeMillis();

    public UptimeCommand() {
        super("See the uptime of the bot.", "Usage: <command>", null, Group.TOOLS);
    }


    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            commandContext.reply(Constants.NO_ARGUMENTS);
        } else {
            commandContext.reply(TimeUtils.millisecondToDhms(System.currentTimeMillis() - startingTime));
        }
    }

    @Override
    public void loadSchema(Schema schema) {

    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        commandContext.reply(TimeUtils.millisecondToDhms(System.currentTimeMillis() - startingTime));
    }
}
