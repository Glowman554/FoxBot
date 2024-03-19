package de.glowman554.bot.command;

import de.glowman554.bot.Main;
import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.registry.Registries;

import java.util.HashMap;

public class CommandManager {

    private final HashMap<String, Integer> usage = new HashMap<>();

    public CommandManager() {
        EventManager.register(this);
    }

    @EventTarget
    public void onMessage(Message message) {
        if (message.getMessage() == null) {
            return;
        }

        Logger.log("%s", message);

        if (message.getMessage().startsWith(Main.config.getPrefix())) {
            String[] arguments = message.getMessage().split(" ");
            arguments[0] = arguments[0].substring(Main.config.getPrefix().length());

            if (Registries.COMMANDS.has(arguments[0])) {
                if (usage.containsKey(arguments[0])) {
                    usage.put(arguments[0], usage.get(arguments[0]) + 1);
                } else {
                    usage.put(arguments[0], 1);
                }

                try {
                    Command command = Registries.COMMANDS.get(arguments[0]);

                    if (Registries.PERMISSION_PROVIDER.get().hasPermission(message.getUserId(), command.getPermission())) {
                        String[] commandArguments = new String[arguments.length - 1];
                        System.arraycopy(arguments, 1, commandArguments, 0, arguments.length - 1);
                        command.execute(message, commandArguments);
                    } else {
                        message.reply("You are not allowed to use this command!");
                    }
                } catch (Exception exception) {
                    Main.handleException(exception, message);
                }
            }
        }
    }

    public HashMap<String, Integer> getUsage() {
        return usage;
    }

    @Deprecated
    public PermissionProvider getPermissionProvider() {
        return Registries.PERMISSION_PROVIDER.get();
    }
}
