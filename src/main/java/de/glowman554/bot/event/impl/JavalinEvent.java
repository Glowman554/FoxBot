package de.glowman554.bot.event.impl;

import de.glowman554.bot.event.Event;
import io.javalin.Javalin;

public class JavalinEvent extends Event {
    private final Javalin javalin;

    public JavalinEvent(Javalin javalin) {
        this.javalin = javalin;
    }

    public Javalin getJavalin() {
        return javalin;
    }

    public enum Step {
        SOCKET, API
    }
}
