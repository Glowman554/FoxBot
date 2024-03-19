package de.glowman554.bot.event.impl;

import de.glowman554.bot.event.Event;

public class SparkSetupEvent extends Event {
    private final Step step;

    public SparkSetupEvent(Step step) {
        this.step = step;
    }

    public Step getStep() {
        return step;
    }

    public static enum Step {
        SOCKET, API
    }
}
