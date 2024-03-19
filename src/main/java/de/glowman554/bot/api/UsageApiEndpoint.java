package de.glowman554.bot.api;

import de.glowman554.bot.Main;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;

public class UsageApiEndpoint implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        JsonNode root = JsonNode.object();

        HashMap<String, Integer> uses = Main.commandManager.getUsage();

        for (String key : uses.keySet()) {
            root.set(key, uses.get(key));
        }

        return Json.json().serialize(root);
    }
}
