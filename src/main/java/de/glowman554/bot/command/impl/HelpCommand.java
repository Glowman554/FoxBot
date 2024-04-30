package de.glowman554.bot.command.impl;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.*;
import de.glowman554.bot.registry.Registries;

import java.util.List;

public class HelpCommand extends SchemaCommand {
    public HelpCommand() {
        super("Get help with commands.", "Usage: <command> [command?]", null, Group.TOOLS);
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            doHelp(commandContext);
        } else if (arguments.length == 1) {
            doCommandHelp(arguments[0], commandContext);
        } else {
            commandContext.reply("Command takes exactly 0 or 1 argument!");
        }
    }

    private void doHelp(IContext context) {
        StringBuilder response = new StringBuilder();

        for (Group group : Group.values()) {
            List<String> groupCommands = Registries.COMMANDS.getRegistry().keySet().stream().filter(key -> {
                LegacyCommand command = Registries.COMMANDS.get(key);
                if (command.getGroup() != group) {
                    return false;
                }
                return Registries.PERMISSION_PROVIDER.get().hasPermission(context.getUserId(), command.getPermission());
            }).toList();

            if (groupCommands.isEmpty()) {
                continue;
            }

            response.append(group.getDisplayName()).append(" commands:\n");
            for (String key : groupCommands) {
                LegacyCommand command = Registries.COMMANDS.get(key);
                response.append(context.formatBold(Main.config.getPrefix() + key)).append(": ").append(command.getShortHelp()).append("\n");
            }

            response.append("\n");
        }

        context.reply(response.toString());
    }

    private void doCommandHelp(String commandStr, IReply reply) {
        if (commandStr.startsWith(Main.config.getPrefix())) {
            commandStr = commandStr.substring(Main.config.getPrefix().length());
        }
        try {
            LegacyCommand command = Registries.COMMANDS.get(commandStr);
            reply.reply(reply.formatBold(Main.config.getPrefix() + commandStr + " (" + command.getGroup().getDisplayName() + " command)") + "\n\n" + command.getLongHelp().replace("<command>", Main.config.getPrefix() + commandStr));
        } catch (IllegalArgumentException e) {
            reply.reply("Command " + commandStr + " not found!");
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "command", "Command to get help", true).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        Schema.Value command = commandContext.get("command");
        if (command == null) {
            doHelp(commandContext);
        } else {
            doCommandHelp(command.asString(), commandContext);
        }
    }
}
