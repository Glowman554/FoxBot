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
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            commandContext.reply("Missing command.");
        } else {
            doRun(commandContext, String.join(" ", arguments));
        }
    }

    private void doRun(IContext context, String command) throws IOException {
        if (Registries.PERMISSION_PROVIDER.get().hasPermission(context.getUserId(), "no_jail")) {
            context.reply(Executor.executeUnsafe(command));
        } else {
            context.reply(RemoteExecutor.execute(command));
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "command", "Command to run", false).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        doRun(commandContext, commandContext.get("command").asString());
    }
}
