package de.glowman554.bot.event;

import java.lang.reflect.Method;

public record EventData(Object source, Method target, byte priority) {
}
