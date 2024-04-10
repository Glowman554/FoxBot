package de.glowman554.bot.command.premade;

import de.glowman554.bot.command.CommandContext;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.command.Schema;
import de.glowman554.bot.command.SchemaCommand;
import de.glowman554.bot.utils.HttpClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.net.URLEncoder;
import java.nio.charset.Charset;

public abstract class JsonQueryCommand extends SchemaCommand {
    private final String url;

    public JsonQueryCommand(String shortHelp, String permission, Group group, String url) {
        super(shortHelp, "Usage: <command> [query]", permission, group);
        this.url = url;
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            message.reply("Invalid arguments");
        } else {
            String res = HttpClient.get(encodeUrl(String.join(" ", arguments)));

            Json json = Json.json();
            JsonNode root = json.parse(res);


            message.reply(extractText(root));
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "query", "Search query", false).register();
    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        String res = HttpClient.get(encodeUrl(commandContext.get("query").asString()));

        Json json = Json.json();
        JsonNode root = json.parse(res);


        commandContext.reply(extractText(root));
    }

    private String encodeUrl(String query) {
        return url.replace("<q>", URLEncoder.encode(query, Charset.defaultCharset()));
    }

    public abstract String extractText(JsonNode root);
}
