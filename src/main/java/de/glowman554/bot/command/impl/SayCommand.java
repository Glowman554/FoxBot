package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.CommandContext;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.command.Schema;
import de.glowman554.bot.command.SchemaCommand;

public class SayCommand extends SchemaCommand {
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

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "text", "Message to send", false).register();
    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        commandContext.reply(commandContext.get("text").asString());
    }
}
