package de.glowman554.bot.command.premade;

import de.glowman554.bot.command.*;
import de.glowman554.bot.utils.HttpClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

public abstract class JsonTextCommand extends SchemaCommand {
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
            doSend(message);
        }
    }

    private void doSend(Reply reply) throws Exception {
        String res = HttpClient.get(url);

        Json json = Json.json();
        JsonNode root = json.parse(res);

        reply.reply(extractText(root));
    }

    @Override
    public void loadSchema(Schema schema) {

    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        doSend(commandContext);
    }

    public abstract String extractText(JsonNode root);
}
