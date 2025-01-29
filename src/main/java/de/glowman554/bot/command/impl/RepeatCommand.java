package de.glowman554.bot.command.impl;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.*;
import de.glowman554.bot.command.Schema.Argument.Type;
import de.glowman554.bot.registry.Registries;
import de.glowman554.bot.utils.StreamedFile;

import java.util.ArrayList;

public class RepeatCommand extends SchemaCommand {
    public RepeatCommand() {
        super("Repeat a command.", "Usage: <command> [command]", null, Group.TOOLS);
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length < 2) {
            commandContext.reply("Not enough arguments");
        } else {
            int count = Integer.parseInt(arguments[0]);

            String[] newArguments = new String[arguments.length - 1];
            System.arraycopy(arguments, 1, newArguments, 0, arguments.length - 1);

            if (!newArguments[0].startsWith(Main.config.getPrefix())) {
                newArguments[0] = Main.config.getPrefix() + newArguments[0];
            }

            if (newArguments[0].equals(Main.config.getPrefix() + "repeat")) {
                commandContext.reply("You can't repeat a repeat command");
            } else {
                if (count < 11
                        || Registries.PERMISSION_PROVIDER.get().hasPermission(commandContext.getUserId(), "no_limit")) {

                    if (count < 0) {
                        commandContext.reply("Count must be greater than 0");
                    } else {
                        commandContext.modifyMessage(String.join(" ", newArguments));

                        for (int i = 0; i < count; i++) {
                            commandContext.call(LegacyCommandContext.class);
                        }
                    }
                } else {
                    commandContext.reply("Count must be less than 10");
                }
            }
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Type.STRING, "command", "Command to repeat", false).register();
        schema.addArgument(Type.INTEGER, "amount", "Amount of repetitions", false).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        String command = commandContext.get("command").asString();
        if (!command.startsWith(Main.config.getPrefix())) {
            command = Main.config.getPrefix() + command;
        }
        int amount = commandContext.get("amount").asInteger();

        if (amount < 11
                || Registries.PERMISSION_PROVIDER.get().hasPermission(commandContext.getUserId(), "no_limit")) {

            FakeLegacyCommandContext context = new FakeLegacyCommandContext(commandContext, command);

            if (amount < 0) {
                commandContext.reply("Count must be greater than 0");
            } else {
                for (int i = 0; i < amount; i++) {
                    context.call(LegacyCommandContext.class);
                }
            }
        }
    }

    private static class FakeLegacyCommandContext extends LegacyCommandContext {
        private final SchemaCommandContext context;

        protected FakeLegacyCommandContext(SchemaCommandContext context, String message) {
            super(message, null, new ArrayList<>(), context.getUserId(), context.getDisplayName());
            this.context = context;
        }

        @Override
        public void reply(String reply) {
            context.reply(reply);
        }

        @Override
        public void replyFile(StreamedFile file, MediaType type, boolean nsfw) {
            context.replyFile(file, type, nsfw);
        }

        @Override
        public void replyFile(StreamedFile file, MediaType type, boolean nsfw, String caption) {
            context.replyFile(file, type, nsfw, caption);
        }

        @Override
        public String formatBold(String text) {
            return context.formatBold(text);
        }

        @Override
        public String formatItalic(String text) {
            return context.formatItalic(text);
        }

        @Override
        public String formatCode(String text) {
            return context.formatCode(text);
        }

        @Override
        public String formatCodeBlock(String text) {
            return context.formatCodeBlock(text);
        }
    }
}
