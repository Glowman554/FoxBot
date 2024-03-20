package de.glowman554.bot.api;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.impl.UptimeCommand;
import de.glowman554.bot.registry.Registries;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import spark.Request;
import spark.Response;
import spark.Route;

public class StatsApiEndpoint implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.header("Content-Type", "application/json");
        JsonNode root = JsonNode.object();

        root.set("commands", Registries.COMMANDS.getRegistry().size());
        root.set("platforms", Registries.PLATFORMS.getRegistry().size());
        root.set("uptime", System.currentTimeMillis() - UptimeCommand.startingTime);
        root.set("prefix", Main.config.getPrefix());

        return Json.json().serialize(root);
    }
}
