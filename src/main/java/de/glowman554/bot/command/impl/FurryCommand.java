package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.*;
import de.glowman554.bot.utils.StreamedFile;
import de.glowman554.bot.utils.api.YiffAPI;

import java.util.List;
import java.util.Optional;

public class FurryCommand extends SchemaCommand {
    private final List<YiffAPI.YiffCategory> categories;

    public FurryCommand() {
        super("See a random furry image.", "Usage: <command> [category?]|[list?]", null, Group.ANIMAL);

        YiffAPI yiffAPI = new YiffAPI();
        categories = yiffAPI.getCategories();
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        String categoryString = "furry.fursuit";
        if (arguments.length == 1) {
            if (arguments[0].equals("list")) {
                StringBuilder result = new StringBuilder();

                for (YiffAPI.YiffCategory yiffCategory : categories) {
                    result.append(yiffCategory.name()).append(": ").append(yiffCategory.db()).append("\n");
                }

                message.reply(result.toString());
                return;
            } else {
                categoryString = arguments[0];
            }
        } else if (arguments.length != 0) {
            message.reply("Command takes exactly 0 or 1 arguments");
            return;
        }

        doSend(message, categoryString);
    }

    private void doSend(Reply reply, String categoryString) throws Exception {
        Optional<YiffAPI.YiffCategory> yiffCategory = categories.stream().filter(v -> v.db().equals(categoryString)).findFirst();
        if (yiffCategory.isPresent()) {
            try (StreamedFile file = yiffCategory.get().download()) {
                reply.replyFile(file, MediaType.IMAGE, !yiffCategory.get().sfw());
            }
        } else {
            reply.reply("Category " + categoryString + " not found!");
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        Schema.Argument categoryArgument = schema.addArgument(Schema.Argument.Type.STRING, "category", "Image category", true);
        for (YiffAPI.YiffCategory category : categories) {
            categoryArgument.addOption(category.name(), new Schema.Value(category.db()));
        }
        categoryArgument.register();
    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        String categoryString = "furry.fursuit";
        Schema.Value categoryValue = commandContext.get("category");
        if (categoryValue != null) {
            categoryString = categoryValue.asString();
        }
        doSend(commandContext, categoryString);
    }
}
