package de.glowman554.bot.command.impl;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.registry.Registries;

public class RepeatCommand extends Command {
    public RepeatCommand() {
        super("Repeat a command.", "Usage: <command> [command]", null, Group.TOOLS);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length < 2) {
            message.reply("Not enough arguments");
        } else {
            int count = Integer.parseInt(arguments[0]);

            String[] newArguments = new String[arguments.length - 1];
            System.arraycopy(arguments, 1, newArguments, 0, arguments.length - 1);

            if (!newArguments[0].startsWith(Main.config.getPrefix())) {
                newArguments[0] = Main.config.getPrefix() + newArguments[0];
            }

            if (newArguments[0].equals(Main.config.getPrefix() + "repeat")) {
                message.reply("You can't repeat a repeat command");
            } else {
                if (count < 11 || Registries.PERMISSION_PROVIDER.get().hasPermission(message.getUserId(), "no_limit")) {

                    if (count < 0) {
                        message.reply("Count must be greater than 0");
                    } else {
                        message.modifyMessage(String.join(" ", newArguments));

                        for (int i = 0; i < count; i++) {
                            message.call(Message.class);
                        }
                    }
                } else {
                    message.reply("Count must be less than 10");
                }
            }
        }
    }
}
