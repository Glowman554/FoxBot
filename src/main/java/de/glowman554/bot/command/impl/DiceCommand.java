package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.CommandContext;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.command.Schema;
import de.glowman554.bot.command.SchemaCommand;

public class DiceCommand extends SchemaCommand {
    public DiceCommand() {
        super("Roll a dice.", "Usage: <command> [sides?]", null, Group.TOOLS);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (!(arguments.length == 0 || arguments.length == 1)) {
            message.reply("Expected 0 or 1 arguments!");
        } else {
            int numSides = 6;

            if (arguments.length == 1) {
                numSides = Integer.parseInt(arguments[0]);
            }

            message.reply("You rolled a " + ((int) (Math.random() * numSides) + 1));
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.INTEGER, "sides", "Sides of dice", true).register();
    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        int numSides = 6;
        Schema.Value sidesValue = commandContext.get("sides");
        if (sidesValue != null) {
            numSides = sidesValue.asInteger();
        }
        commandContext.reply("You rolled a " + ((int) (Math.random() * numSides) + 1));
    }
}
