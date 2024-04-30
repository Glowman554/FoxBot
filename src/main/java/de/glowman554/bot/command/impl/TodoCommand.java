package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.LegacyCommandContext;
import de.glowman554.bot.command.Schema;
import de.glowman554.bot.command.SchemaCommand;
import de.glowman554.bot.command.SchemaCommandContext;
import de.glowman554.bot.registry.Registries;
import de.glowman554.bot.utils.Pair;

import java.util.HashMap;

public class TodoCommand extends SchemaCommand {
    public TodoCommand() {
        super("Manage your todo list.", """
                Usage: <command> list
                Usage: <command> done [id]
                Usage: <command> pending [id]
                Usage: <command> remove [id]
                Usage: <command> add [todo]
                """, null, Group.TOOLS);
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length >= 2 && arguments[0].equals("add")) {
            String[] todo = new String[arguments.length - 1];
            System.arraycopy(arguments, 1, todo, 0, todo.length);
            Registries.TODO_PROVIDER.get().createTodo(commandContext.getUserId(), String.join(" ", todo));
            commandContext.reply("Added todo.");
        } else {
            switch (arguments.length) {
                case 1:
                    if (arguments[0].equals("list")) {
                        StringBuilder result = new StringBuilder();
                        HashMap<Integer, Pair<Boolean, String>> todos = Registries.TODO_PROVIDER.get().loadTodo(commandContext.getUserId());
                        for (int key : todos.keySet()) {
                            Pair<Boolean, String> entry = todos.get(key);
                            result.append(entry.t1 ? "[x] " : "[ ] ").append(key).append(": ").append(entry.t2).append("\n");
                        }
                        commandContext.reply(commandContext.formatCodeBlock(result.toString()));
                    } else {
                        commandContext.reply("Invalid arguments.");
                    }
                    break;
                case 2:
                    int id = Integer.parseInt(arguments[1]);

                    switch (arguments[0]) {
                        case "done":
                            Registries.TODO_PROVIDER.get().changeTodoDone(commandContext.getUserId(), id, true);
                            commandContext.reply("Changed status.");
                            break;
                        case "pending":
                            Registries.TODO_PROVIDER.get().changeTodoDone(commandContext.getUserId(), id, false);
                            commandContext.reply("Changed status.");
                            break;
                        case "remove":
                            Registries.TODO_PROVIDER.get().deleteTodo(commandContext.getUserId(), id);
                            commandContext.reply("Removed todo.");
                            break;
                        default:
                            commandContext.reply("Invalid arguments.");
                            break;
                    }
                    break;
                default:
                    commandContext.reply("Invalid arguments.");
                    break;
            }
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "subcommand", "Subcommand to execute", false).addOption("List todo's", new Schema.Value("list")).addOption("Mark todo done", new Schema.Value("done")).addOption("Mark todo pending", new Schema.Value("pending")).addOption("Remove todo", new Schema.Value("remove")).addOption("Add todo", new Schema.Value("add")).register();
        schema.addArgument(Schema.Argument.Type.INTEGER, "id", "Id of todo you are changing", true).register();
        schema.addArgument(Schema.Argument.Type.STRING, "todo", "Todo you are adding", true).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        switch (commandContext.get("subcommand").asString()) {
            case "list":
                StringBuilder result = new StringBuilder();
                HashMap<Integer, Pair<Boolean, String>> todos = Registries.TODO_PROVIDER.get().loadTodo(commandContext.getUserId());
                for (int key : todos.keySet()) {
                    Pair<Boolean, String> entry = todos.get(key);
                    result.append(entry.t1 ? "[x] " : "[ ] ").append(key).append(": ").append(entry.t2).append("\n");
                }
                commandContext.reply(commandContext.formatCodeBlock(result.toString()));
                break;
            case "done":
                Registries.TODO_PROVIDER.get().changeTodoDone(commandContext.getUserId(), commandContext.get("id").asInteger(), true);
                commandContext.reply("Changed status.");
                break;
            case "pending":
                Registries.TODO_PROVIDER.get().changeTodoDone(commandContext.getUserId(), commandContext.get("id").asInteger(), false);
                commandContext.reply("Changed status.");
                break;
            case "remove":
                Registries.TODO_PROVIDER.get().deleteTodo(commandContext.getUserId(), commandContext.get("id").asInteger());
                commandContext.reply("Removed todo.");
                break;
            case "add":
                Registries.TODO_PROVIDER.get().createTodo(commandContext.getUserId(), commandContext.get("todo").asString());
                commandContext.reply("Added todo.");
                break;
            default:
                commandContext.reply("Invalid arguments.");
                break;
        }
    }
}
