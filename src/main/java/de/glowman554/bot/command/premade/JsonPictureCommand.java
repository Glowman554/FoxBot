package de.glowman554.bot.command.premade;

import de.glowman554.bot.command.*;
import de.glowman554.bot.utils.HttpClient;
import de.glowman554.bot.utils.StreamedFile;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

public abstract class JsonPictureCommand extends SchemaCommand {
    private final String url;

    public JsonPictureCommand(String shortHelp, String permission, Group group, String url) {
        super(shortHelp, "Usage: <command>", permission, group);
        this.url = url;
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length != 0) {
            message.reply(Constants.NO_ARGUMENTS);
        } else {
            doSend(message);
        }
    }

    private void doSend(Reply reply) throws Exception {
        String res = HttpClient.get(url);

        Json json = Json.json();
        JsonNode root = json.parse(res);

        String pictureUrl = extractUrl(root);

        try (StreamedFile file = HttpClient.download(pictureUrl)) {
            reply.replyFile(file, MediaType.IMAGE, false);
        }
    }

    @Override
    public void loadSchema(Schema schema) {

    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        doSend(commandContext);
    }

    public abstract String extractUrl(JsonNode root);
}
