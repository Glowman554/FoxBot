package de.glowman554.bot.api;

import de.glowman554.bot.Main;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UsageEndpoint implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {
        context.header("Content-Type", "application/json");
        JsonNode root = JsonNode.object();

        HashMap<String, Integer> uses = Main.commandManager.getUsage();

        for (String key : uses.keySet()) {
            root.set(key, uses.get(key));
        }

        context.result(Json.json().serialize(root));
    }
}
