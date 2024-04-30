package de.glowman554.bot.registry;

import de.glowman554.bot.Feature;
import de.glowman554.bot.Platform;
import de.glowman554.bot.command.LegacyCommand;
import de.glowman554.bot.command.PermissionProvider;
import de.glowman554.bot.command.SchemaCommand;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.utils.TodoProvider;

public class Registries {
    public static final Registry<String, LegacyCommand> COMMANDS = new Registry<>((string, command) -> {
        if (!(command instanceof SchemaCommand)) {
            Logger.log("[WARNING (%s)] Command is not a SchemaCommand.", string);
        }
    });
    public static final Registry<Class<? extends Platform>, Platform> PLATFORMS = new Registry<>((aClass, platform) -> {
    });
    public static final Registry<String, Feature> FEATURES = new Registry<>((string, feature) -> {
    });

    public static final SingleSet<TodoProvider> TODO_PROVIDER = new SingleSet<>();
    public static final SingleSet<PermissionProvider> PERMISSION_PROVIDER = new SingleSet<>();
}
