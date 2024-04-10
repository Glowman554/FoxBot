package de.glowman554.bot.platform.discord;

import de.glowman554.bot.command.Schema;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class DiscordSchema extends Schema {
    private final SlashCommandData slashCommandData;

    protected DiscordSchema(String name, String description) {
        super(name, description);
        slashCommandData = Commands.slash(name, description);
    }

    @Override
    public Argument addArgument(Argument.Type type, String name, String description, boolean optional) {
        return new DiscordArgument(type, name, description, optional, slashCommandData);
    }

    public SlashCommandData getSlashCommandData() {
        return slashCommandData;
    }

    public static class DiscordArgument extends Argument {
        private final OptionData optionData;
        private final SlashCommandData slashCommandData;

        protected DiscordArgument(Type type, String name, String description, boolean optional, SlashCommandData slashCommandData) {
            super(type, name, description, optional);
            this.slashCommandData = slashCommandData;
            this.optionData = new OptionData(convertSchemeArgumentType(type), name, description, !optional);
        }


        @Override
        public Argument addOption(String name, Value value) {
            switch (type) {
                case NUMBER -> optionData.addChoice(name, value.asNumber());
                case STRING -> optionData.addChoice(name, value.asString());
                case INTEGER -> optionData.addChoice(name, value.asInteger());
            }
            return this;
        }

        @Override
        public void register() {
            slashCommandData.addOptions(optionData);
        }

        private OptionType convertSchemeArgumentType(Argument.Type type) {
            return switch (type) {
                case NUMBER -> OptionType.NUMBER;
                case STRING -> OptionType.STRING;
                case BOOLEAN -> OptionType.BOOLEAN;
                case INTEGER -> OptionType.INTEGER;
                case ATTACHMENT -> OptionType.ATTACHMENT;
            };
        }

    }
}
