package de.glowman554.bot.registry;

public class SingleSet<T> {
    private T value = null;

    public void set(T value) {
        if (this.value != null) {
            throw new RuntimeException("Value already set!");
        }
        this.value = value;
    }

    public T get() {
        if (value == null) {
            throw new RuntimeException("Value not set!");
        }
        return value;
    }
}
