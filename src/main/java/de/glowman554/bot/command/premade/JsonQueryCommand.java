package de.glowman554.bot.command.premade;

import de.glowman554.bot.command.*;
import de.glowman554.bot.utils.HttpClient;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public abstract class JsonQueryCommand extends SchemaCommand {
    private final String url;

    public JsonQueryCommand(String shortHelp, String permission, Group group, String url) {
        super(shortHelp, "Usage: <command> [query]", permission, group);
        this.url = url;
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            commandContext.reply("Invalid arguments");
        } else {
            doSend(commandContext, String.join(" ", arguments));
        }
    }

    private void doSend(IReply reply, String query) throws IOException {
        String res = HttpClient.get(encodeUrl(query));

        Json json = Json.json();
        JsonNode root = json.parse(res);


        reply.reply(extractText(root));
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "query", "Search query", false).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        doSend(commandContext, commandContext.get("query").asString());
    }

    private String encodeUrl(String query) {
        return url.replace("<q>", URLEncoder.encode(query, Charset.defaultCharset()));
    }

    public abstract String extractText(JsonNode root);
}
