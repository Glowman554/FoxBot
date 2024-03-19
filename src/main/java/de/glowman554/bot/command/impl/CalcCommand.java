package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.utils.math.MathInterpreter;

public class CalcCommand extends Command {
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
}
