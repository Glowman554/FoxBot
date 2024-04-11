package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;
import de.glowman554.bot.registry.Registries;
import de.glowman554.bot.utils.Pair;

import java.util.List;

public class RoleCommand extends SchemaCommand {
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

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "subcommand", "Subcommand to execute", false).addOption("Create new role", new Schema.Value("add")).addOption("List role's", new Schema.Value("list")).addOption("Remove role", new Schema.Value("remove")).addOption("Set user's role", new Schema.Value("set")).register();
        schema.addArgument(Schema.Argument.Type.STRING, "rolename", "Role name", true).register();
        schema.addArgument(Schema.Argument.Type.STRING, "userid", "User id", true).register();
        schema.addArgument(Schema.Argument.Type.STRING, "permissions", "Permissions", true).register();
    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {

        switch (commandContext.get("subcommand").asString()) {
            case "list":
                List<Pair<String, List<String>>> roles = Registries.PERMISSION_PROVIDER.get().getRoles();
                StringBuilder result = new StringBuilder();
                for (Pair<String, List<String>> role : roles) {
                    result.append(role.t1).append(": ").append(String.join(", ", role.t2)).append("\n");
                }
                commandContext.reply(result.toString());
                break;
            case "remove":
                Registries.PERMISSION_PROVIDER.get().removeRole(commandContext.get("rolename").asString());
                commandContext.reply("Removed role " + commandContext.get("rolename").asString());
                break;
            case "set":
                Registries.PERMISSION_PROVIDER.get().setRole(commandContext.get("userid").asString(), commandContext.get("rolename").asString());
                commandContext.reply("Set " + commandContext.get("userid").asString() + "'s role to " + commandContext.get("rolename").asString());
                break;
            case "add":
                PermissionProvider.RoleBuilder builder = PermissionProvider.RoleBuilder.begin(commandContext.get("rolename").asString(), Registries.PERMISSION_PROVIDER.get());
                for (String permission : commandContext.get("permissions").asString().split(" ")) {
                    builder.permission(permission);
                }
                commandContext.reply("Created new role " + commandContext.get("rolename").asString());
                break;
        }
    }
}
