package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;
import de.glowman554.bot.registry.Registries;
import de.glowman554.bot.utils.compiler.Executor;
import de.glowman554.bot.utils.compiler.RemoteExecutor;

import java.io.IOException;

public class RunCommand extends SchemaCommand {
    public RunCommand() {
        super("Execute a command.", "Usage: <command> [command]", null, Group.DEVELOPMENT);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            message.reply("Missing command.");
        } else {
            doRun(message, String.join(" ", arguments), message.getUserId());
        }
    }

    private void doRun(Reply reply, String command, String userId) throws IOException {
        if (Registries.PERMISSION_PROVIDER.get().hasPermission(userId, "no_jail")) {
            reply.reply(Executor.executeUnsafe(command));
        } else {
            reply.reply(RemoteExecutor.execute(command));
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "command", "Command to run", false).register();
    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        doRun(commandContext, commandContext.get("command").asString(), commandContext.userId);
    }
}
