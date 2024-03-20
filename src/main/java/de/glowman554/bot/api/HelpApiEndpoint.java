package de.glowman554.bot.api;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.Command;
import de.glowman554.bot.registry.Registries;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;

public class HelpApiEndpoint implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.header("Content-Type", "application/json");
        JsonNode root = JsonNode.array();

        HashMap<String, Command> commands = Registries.COMMANDS.getRegistry();
        for (String key : commands.keySet()) {
            Command command = commands.get(key);

            JsonNode object = JsonNode.object();
            object.set("command", key);
            object.set("shortHelp", command.getShortHelp());
            object.set("longHelp", command.getLongHelp().replace("<command>", Main.config.getPrefix() + key));
            object.set("permission", command.getPermission());
            object.set("group", command.getGroup().toString());
            root.add(object);
        }

        return Json.json().serialize(root);
    }
}
