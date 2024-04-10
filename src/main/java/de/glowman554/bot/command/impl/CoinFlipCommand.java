package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;

public class CoinFlipCommand extends SchemaCommand {
    public CoinFlipCommand() {
        super("Flip a coin.", "Usage: <command>", null, Group.FUN);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            message.reply(Constants.NO_ARGUMENTS);
            return;
        }

        doFlip(message);
    }

    private void doFlip(Reply reply) {
        boolean random = Math.random() < 0.5;

        if (random) {
            reply.reply("You've landed on heads!");
        } else {
            reply.reply("You've landed on tails!");
        }
    }

    @Override
    public void loadSchema(Schema schema) {

    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        doFlip(commandContext);
    }
}
