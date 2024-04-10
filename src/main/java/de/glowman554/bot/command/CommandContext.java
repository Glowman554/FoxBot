package de.glowman554.bot.command;

import de.glowman554.bot.command.impl.Reply;

import java.util.HashMap;

public abstract class CommandContext extends Schema implements Reply {
    public final String userId;
    public final String displayName;

    private final HashMap<String, Schema.Value> values = new HashMap<>();

    public CommandContext(String userId, String displayName) {
        super(null, null);
        this.userId = userId;
        this.displayName = displayName;
    }

    public abstract Value loadArgument(Argument.Type type, String name, boolean optional);

    public Schema.Value get(String option) {
        return values.get(option);
    }

    @Override
    public Argument addArgument(Argument.Type type, String name, String description, boolean optional) {
        Value value = loadArgument(type, name, optional);
        if (value != null) {
            values.put(name, value);
        }

        return new Argument(type, name, description, optional) {
            @Override
            public Argument addOption(String name, Value value) {
                return this;
            }

            @Override
            public void register() {

            }
        };
    }
}
