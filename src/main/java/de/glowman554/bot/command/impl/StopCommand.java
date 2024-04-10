package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;

public class StopCommand extends SchemaCommand {
    public StopCommand() {
        super("Stop the bot.", "Usage: <command>", "stop", Group.DEVELOPMENT);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            message.reply(Constants.NO_ARGUMENTS);
        } else {
            message.reply("Stopping...");
            System.exit(0);
        }
    }

    @Override
    public void loadSchema(Schema schema) {

    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        commandContext.reply("Stopping...");
        System.exit(0);
    }
}
