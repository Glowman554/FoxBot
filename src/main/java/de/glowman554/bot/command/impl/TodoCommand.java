package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.registry.Registries;
import de.glowman554.bot.utils.Pair;

import java.util.HashMap;

public class TodoCommand extends Command {
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
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length >= 2 && arguments[0].equals("add")) {
            String[] todo = new String[arguments.length - 1];
            System.arraycopy(arguments, 1, todo, 0, todo.length);
            Registries.TODO_PROVIDER.get().createTodo(message.getUserId(), String.join(" ", todo));
            message.reply("Added todo.");
        } else {
            switch (arguments.length) {
                case 1:
                    if (arguments[0].equals("list")) {
                        StringBuilder result = new StringBuilder();
                        HashMap<Integer, Pair<Boolean, String>> todos = Registries.TODO_PROVIDER.get().loadTodo(message.getUserId());
                        for (int key : todos.keySet()) {
                            Pair<Boolean, String> entry = todos.get(key);
                            result.append(entry.t1 ? "[x] " : "[ ] ").append(key).append(": ").append(entry.t2).append("\n");
                        }
                        message.reply(message.formatCodeBlock(result.toString()));
                    } else {
                        message.reply("Invalid arguments.");
                    }
                    break;
                case 2:
                    int id = Integer.parseInt(arguments[1]);

                    switch (arguments[0]) {
                        case "done":
                            Registries.TODO_PROVIDER.get().changeTodoDone(message.getUserId(), id, true);
                            message.reply("Changed status.");
                            break;
                        case "pending":
                            Registries.TODO_PROVIDER.get().changeTodoDone(message.getUserId(), id, false);
                            message.reply("Changed status.");
                            break;
                        case "remove":
                            Registries.TODO_PROVIDER.get().deleteTodo(message.getUserId(), id);
                            message.reply("Removed todo.");
                            break;
                        default:
                            message.reply("Invalid arguments.");
                            break;
                    }
                    break;
                default:
                    message.reply("Invalid arguments.");
                    break;
            }
        }
    }
}
