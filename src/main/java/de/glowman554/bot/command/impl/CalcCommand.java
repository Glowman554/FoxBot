package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.CommandContext;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.command.Schema;
import de.glowman554.bot.command.SchemaCommand;
import de.glowman554.bot.utils.math.MathInterpreter;

public class CalcCommand extends SchemaCommand {
    public CalcCommand() {
        super("Calculate something.", "Usage: <command> [expression]", null, Group.TOOLS);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            message.reply("Expected at leas t 1 argument!");
        } else {
            String expr = String.join(" ", arguments);

            message.reply("The result is: " + MathInterpreter.eval(expr));
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "expression", "Expression to evaluate", false).register();
    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        commandContext.reply("The result is: " + MathInterpreter.eval(commandContext.get("expression").asString()));

    }
}
