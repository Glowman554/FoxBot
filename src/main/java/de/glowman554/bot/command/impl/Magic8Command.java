package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;

public class Magic8Command extends SchemaCommand {
    private final String[] answers = new String[]{"No", "Yes", "Maybe", "Think about is a bit more then try again...", "Absolutely", "Not at all", "Of couse!", "As it seems... Yes", "As it seems... No", "Could be", "Hell NO!"};

    public Magic8Command() {
        super("Ask the magic 8 ball a question.", "Usage: <command> [question]", null, Group.FUN);
    }


    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            commandContext.reply("You should ask a question didn't you know?");
        } else {
            doSend(commandContext);
        }
    }

    private void doSend(IReply reply) {
        String response = answers[(int) (Math.random() * answers.length)];
        reply.reply(response);
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "question", "Question to ask", false).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        doSend(commandContext);
    }
}
