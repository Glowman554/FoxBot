package de.glowman554.bot.ollama.commands;

import de.glowman554.bot.command.LegacyCommandContext;
import de.glowman554.bot.command.Schema;
import de.glowman554.bot.command.SchemaCommand;
import de.glowman554.bot.command.SchemaCommandContext;
import de.glowman554.bot.ollama.Entrypoint;

public class SetModelCommand extends SchemaCommand {
    private final Entrypoint.Config config;

    public SetModelCommand(Entrypoint.Config config) {
        super("Set ai model being used.", "Usage: <command> [model]", "no_jail", Group.TOOLS);
        this.config = config;
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "model", "Model being used", false)
                .addOption("llama2", new Schema.Value("llama2"))
                .addOption("orca-mini", new Schema.Value("orca-mini"))
                .addOption("tinydolphin", new Schema.Value("tinydolphin"))
                .addOption("tinyllama", new Schema.Value("tinyllama")).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        config.model = commandContext.get("model").asString();
        config.save();
        commandContext.reply("Successfully updated model.");
    }

    @Override
    public void execute(LegacyCommandContext message, String[] arguments) throws Exception {
        if (arguments.length != 1) {
            message.reply("Command takes exactly 1 argument!");
        } else {
            config.model = arguments[0];
            config.save();
            message.reply("Successfully updated model.");
        }
    }
}
