package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;

public class WhoamiCommand extends SchemaCommand {
    public WhoamiCommand() {
        super("Information's about yourself.", "Usage: <command>", null, Group.TOOLS);
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            commandContext.reply(Constants.NO_ARGUMENTS);
        } else {
            common(commandContext);
        }
    }

    private void common(IContext context) {
        context.reply("displayName: " + context.formatCode(context.getDisplayName()) + "\n" + "userId: " + context.formatCode(context.getUserId()));
    }

    @Override
    public void loadSchema(Schema schema) {

    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        common(commandContext);
    }
}
