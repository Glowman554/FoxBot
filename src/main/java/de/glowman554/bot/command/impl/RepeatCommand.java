package de.glowman554.bot.command.impl;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.LegacyCommand;
import de.glowman554.bot.command.LegacyCommandContext;
import de.glowman554.bot.registry.Registries;

public class RepeatCommand extends LegacyCommand {
    public RepeatCommand() {
        super("Repeat a command.", "Usage: <command> [command]", null, Group.TOOLS);
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length < 2) {
            commandContext.reply("Not enough arguments");
        } else {
            int count = Integer.parseInt(arguments[0]);

            String[] newArguments = new String[arguments.length - 1];
            System.arraycopy(arguments, 1, newArguments, 0, arguments.length - 1);

            if (!newArguments[0].startsWith(Main.config.getPrefix())) {
                newArguments[0] = Main.config.getPrefix() + newArguments[0];
            }

            if (newArguments[0].equals(Main.config.getPrefix() + "repeat")) {
                commandContext.reply("You can't repeat a repeat command");
            } else {
                if (count < 11 || Registries.PERMISSION_PROVIDER.get().hasPermission(commandContext.getUserId(), "no_limit")) {

                    if (count < 0) {
                        commandContext.reply("Count must be greater than 0");
                    } else {
                        commandContext.modifyMessage(String.join(" ", newArguments));

                        for (int i = 0; i < count; i++) {
                            commandContext.call(LegacyCommandContext.class);
                        }
                    }
                } else {
                    commandContext.reply("Count must be less than 10");
                }
            }
        }
    }
}
