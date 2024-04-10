package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;

public class WhoamiCommand extends SchemaCommand {
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

    @Override
    public void loadSchema(Schema schema) {

    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        commandContext.reply("displayName: " + commandContext.formatCode(commandContext.displayName) + "\n" + "userId: " + commandContext.formatCode(commandContext.userId));

    }
}
