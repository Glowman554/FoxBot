package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;

public class DiceCommand extends SchemaCommand {
    public DiceCommand() {
        super("Roll a dice.", "Usage: <command> [sides?]", null, Group.TOOLS);
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (!(arguments.length == 0 || arguments.length == 1)) {
            commandContext.reply("Expected 0 or 1 arguments!");
        } else {
            int numSides = 6;

            if (arguments.length == 1) {
                numSides = Integer.parseInt(arguments[0]);
            }

            common(commandContext, numSides);
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.INTEGER, "sides", "Sides of dice", true).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        int numSides = 6;
        Schema.Value sidesValue = commandContext.get("sides");
        if (sidesValue != null) {
            numSides = sidesValue.asInteger();
        }
        common(commandContext, numSides);
    }

    private void common(IContext context, int numSides) {
        context.reply("You rolled a " + ((int) (Math.random() * numSides) + 1));
    }
}
