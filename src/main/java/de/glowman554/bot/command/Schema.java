package de.glowman554.bot.command;

import de.glowman554.bot.utils.StreamedFile;

public abstract class Schema {
    public final String name;
    public final String description;

    protected Schema(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract Argument addArgument(Argument.Type type, String name, String description, boolean optional);

    public abstract static class Argument {
        public final Type type;
        public final String name;
        public final String description;
        public final boolean optional;

        protected Argument(Type type, String name, String description, boolean optional) {
            this.type = type;
            this.name = name;
            this.description = description;
            this.optional = optional;
        }

        public abstract Argument addOption(String name, Value value);

        public abstract void register();

        public enum Type {
            STRING, INTEGER, BOOLEAN, NUMBER, ATTACHMENT
        }
    }


    public record Value(Object value) {
        public String asString() {
            return (String) value;
        }

        public int asInteger() {
            return (int) value;
        }

        public StreamedFile asAttachment() {
            return (StreamedFile) value;
        }

        public double asNumber() {
            return (double) value;
        }

        public boolean asBoolean() {
            return (boolean) value;
        }
    }

}
