package de.glowman554.bot.command.premade;

import de.glowman554.bot.utils.StreamedFile;
import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.utils.HttpClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

public abstract class JsonPictureCommand extends Command {
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
            String res = HttpClient.get(url);

            Json json = Json.json();
            JsonNode root = json.parse(res);

            String pictureUrl = extractUrl(root);

            try (StreamedFile file = HttpClient.download(pictureUrl)) {
                message.replyFile(file, Message.Type.IMAGE, false);
            }
        }
    }

    public abstract String extractUrl(JsonNode root);
}
