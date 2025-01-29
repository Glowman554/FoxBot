package de.glowman554.bot;

import de.glowman554.bot.logging.Logger;
import de.glowman554.config.ConfigManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class Platform {
    private static final ArrayList<Acceptor> acceptors = new ArrayList<>();

    public abstract void init(ConfigManager configManager);

    public static void accept(Object obj) {
        for (Method method : obj.getClass().getMethods()) {
            if (method.isAnnotationPresent(Raw.class)) {
                Acceptor acceptor = new Acceptor(obj, method);
                Logger.log("New raw acceptor: %s", acceptor.toString());
                acceptors.add(acceptor);
            }
        }
    }

    public static <T> void call(T data) {
        for (Acceptor acceptor : acceptors) {
            Class<?>[] types = acceptor.method.getParameterTypes();
            if (types.length != 1) {
                Logger.log("[WARNING] acceptor has invalid amount of arguments! %s", acceptor.toString());
                continue;
            }
            if (types[0].isInstance(data)) {
                try {
                    acceptor.method.invoke(acceptor.object, data);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Raw {

    }

    public record Acceptor(Object object, Method method) {
    }

}
