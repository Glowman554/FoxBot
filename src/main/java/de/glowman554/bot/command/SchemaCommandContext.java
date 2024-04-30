package de.glowman554.bot.command;

import java.util.HashMap;

public abstract class SchemaCommandContext extends Schema implements IContext {

    private final HashMap<String, Schema.Value> values = new HashMap<>();
    private final String userId;
    private final String displayName;

    public SchemaCommandContext(String userId, String displayName) {
        super(null, null);
        this.userId = userId;
        this.displayName = displayName;
    }

    public abstract Value loadArgument(Argument.Type type, String name, boolean optional);

    public Schema.Value get(String option) {
        return values.get(option);
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
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
