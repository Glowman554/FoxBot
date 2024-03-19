package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.registry.Registries;

public class Testing {
    public static void register() {
        Logger.log("Registering testing commands.");

        Registries.COMMANDS.register("exception", new ExceptionCommand());
        Registries.COMMANDS.register("arguments", new ArgumentsCommand());
        Registries.COMMANDS.register("send_files", new SendFilesCommand());
        Registries.COMMANDS.register("files", new FilesCommand());
        Registries.COMMANDS.register("formatting", new FormattingCommand());
    }
}
