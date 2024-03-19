package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.command.PermissionProvider;
import de.glowman554.bot.registry.Registries;
import de.glowman554.bot.utils.Pair;

import java.util.List;

public class RoleCommand extends Command {
    public RoleCommand() {
        super("Change roles.", """
                Usage: <command> list
                Usage: <command> add [role_name] [permissions]
                Usage: <command> remove [role_name]
                Usage: <command> [user_id] set [role_name]
                """, null, Group.TOOLS);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        // role list
        // role remove test
        // role <id> set test
        // role add test no_limit test

        if (arguments.length >= 3 && arguments[0].equals("add")) {
            PermissionProvider.RoleBuilder builder = PermissionProvider.RoleBuilder.begin(arguments[1], Registries.PERMISSION_PROVIDER.get());
            for (int i = 2; i < arguments.length; i++) {
                builder.permission(arguments[i]);
            }
            message.reply("Created new role " + arguments[1]);
        } else {

            switch (arguments.length) {
                case 1:
                    if (arguments[0].equals("list")) {
                        List<Pair<String, List<String>>> roles = Registries.PERMISSION_PROVIDER.get().getRoles();
                        StringBuilder result = new StringBuilder();
                        for (Pair<String, List<String>> role : roles) {
                            result.append(role.t1).append(": ").append(String.join(", ", role.t2)).append("\n");
                        }
                        message.reply(result.toString());
                    } else {
                        fail(message);
                    }
                    break;

                case 2:
                    if (arguments[0].equals("remove")) {
                        Registries.PERMISSION_PROVIDER.get().removeRole(arguments[1]);
                        message.reply("Removed role " + arguments[1]);
                    } else {
                        fail(message);
                    }
                    break;

                case 3:
                    if (arguments[1].equals("set")) {
                        Registries.PERMISSION_PROVIDER.get().setRole(arguments[0], arguments[2]);
                        message.reply("Set " + arguments[0] + "'s role to " + arguments[1]);
                    } else {
                        fail(message);
                    }
            }
        }
    }

    private void fail(Message message) {
        message.reply("Invalid arguments.");
    }
}
