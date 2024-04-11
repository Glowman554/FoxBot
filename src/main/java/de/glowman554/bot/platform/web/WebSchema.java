package de.glowman554.bot.platform.web;

import de.glowman554.bot.command.Schema;
import net.shadew.json.JsonNode;

public class WebSchema extends Schema {
    private final JsonNode schema = JsonNode.object();

    protected WebSchema(String name, String description) {
        super(name, description);
        schema.set("name", name);
        schema.set("description", description);
        schema.set("arguments", JsonNode.array());
    }

    @Override
    public Argument addArgument(Argument.Type type, String name, String description, boolean optional) {
        return new WebArgument(type, name, description, optional, this);
    }

    public JsonNode getSchema() {
        return schema;
    }

    public static class WebArgument extends Argument {
        private final WebSchema schema;
        private final JsonNode argument = JsonNode.object();

        protected WebArgument(Type type, String name, String description, boolean optional, WebSchema schema) {
            super(type, name, description, optional);
            this.schema = schema;
            argument.set("type", type.toString());
            argument.set("name", name);
            argument.set("description", description);
            argument.set("optional", optional);
            argument.set("options", JsonNode.array());
        }

        @Override
        public Argument addOption(String name, Value value) {
            JsonNode option = JsonNode.object();
            option.set("name", name);
            switch (type) {
                case INTEGER -> option.set("value", value.asInteger());
                case NUMBER -> option.set("value", value.asNumber());
                case STRING -> option.set("value", value.asString());
            }
            argument.get("options").add(option);
            return this;
        }

        @Override
        public void register() {
            schema.schema.get("arguments").add(argument);
        }
    }
}
