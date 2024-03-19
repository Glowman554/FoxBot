package de.glowman554.bot.command.premade;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.utils.HttpClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

public abstract class JsonTextCommand extends Command {
    private final String url;

    public JsonTextCommand(String shortHelp, String permission, Command.Group group, String url) {
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

            message.reply(extractText(root));
        }
    }

    public abstract String extractText(JsonNode root);
}
