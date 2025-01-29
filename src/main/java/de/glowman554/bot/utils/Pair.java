package de.glowman554.bot.utils;

public record Pair<T1, T2>(T1 t1, T2 t2) {

    @Override
    public String toString() {
        return "Pair{" +
                "t1=" + t1 +
                ", t2=" + t2 +
                '}';
    }
}
