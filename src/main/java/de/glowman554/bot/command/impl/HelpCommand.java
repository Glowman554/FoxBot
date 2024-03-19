package de.glowman554.bot.command.impl;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.registry.Registries;

import java.util.List;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("Get help with commands.", "Usage: <command> [command?]", null, Group.TOOLS);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            StringBuilder response = new StringBuilder();

            for (Group group : Group.values()) {
                List<String> groupCommands = Registries.COMMANDS.getRegistry().keySet().stream().filter(key -> {
                    Command command = Registries.COMMANDS.get(key);
                    if (command.getGroup() != group) {
                        return false;
                    }
                    return Registries.PERMISSION_PROVIDER.get().hasPermission(message.getUserId(), command.getPermission());
                }).toList();

                if (groupCommands.isEmpty()) {
                    continue;
                }

                response.append(group.getDisplayName()).append(" commands:\n");
                for (String key : groupCommands) {
                    Command command = Registries.COMMANDS.get(key);
                    response.append(message.formatBold(Main.config.getPrefix() + key)).append(": ").append(command.getShortHelp()).append("\n");
                }

                response.append("\n");
            }

            message.reply(response.toString());
        } else if (arguments.length == 1) {
            String commandStr = arguments[0];
            if (commandStr.startsWith(Main.config.getPrefix())) {
                commandStr = commandStr.substring(Main.config.getPrefix().length());
            }
            try {
                Command command = Registries.COMMANDS.get(commandStr);
                message.reply(message.formatBold(Main.config.getPrefix() + commandStr + " (" + command.getGroup().getDisplayName() + " command)") + "\n\n" + command.getLongHelp().replace(Main.config.getPrefix() + "<command>", commandStr));
            } catch (IllegalArgumentException e) {
                message.reply("Command " + commandStr + " not found!");
            }
        } else {
            message.reply("Command takes exactly 0 or 1 argument!");
        }
    }
}
