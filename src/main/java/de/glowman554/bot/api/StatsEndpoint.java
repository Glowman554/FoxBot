package de.glowman554.bot.api;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.impl.UptimeCommand;
import de.glowman554.bot.registry.Registries;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

public class StatsEndpoint implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {
        context.header("Content-Type", "application/json");
        JsonNode root = JsonNode.object();

        root.set("commands", Registries.COMMANDS.getRegistry().size());
        root.set("platforms", Registries.PLATFORMS.getRegistry().size());
        root.set("uptime", System.currentTimeMillis() - UptimeCommand.startingTime);
        root.set("prefix", Main.config.getPrefix());

        context.result(Json.json().serialize(root));
    }
}
