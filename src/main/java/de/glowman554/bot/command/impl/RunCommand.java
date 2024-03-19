package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.registry.Registries;
import de.glowman554.bot.utils.compiler.Executor;

public class RunCommand extends Command {
    public RunCommand() {
        super("Execute a command.", "Usage: <command> [command]", "execute", Group.DEVELOPMENT);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            message.reply("Missing command.");
        } else {
            if (Registries.PERMISSION_PROVIDER.get().hasPermission(message.getUserId(), "no_jail")) {
                message.reply(Executor.executeUnsafe(String.join(" ", arguments)));
            } else {
                message.reply(Executor.execute(String.join(" ", arguments)));
            }
        }
    }
}
