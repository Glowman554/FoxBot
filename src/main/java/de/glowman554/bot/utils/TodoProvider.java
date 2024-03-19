package de.glowman554.bot.utils;

import java.util.HashMap;

public abstract class TodoProvider {
    public abstract void deleteTodo(String userId, int todoId);

    public abstract void changeTodoDone(String userId, int todoId, boolean done);

    public abstract void createTodo(String userId, String todo);

    public abstract HashMap<Integer, Pair<Boolean, String>> loadTodo(String userId);
}
