package de.glowman554.bot.api;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.LegacyCommand;
import de.glowman554.bot.registry.Registries;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class HelpEndpoint implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {
        context.header("Content-Type", "application/json");
        JsonNode root = JsonNode.array();

        HashMap<String, LegacyCommand> commands = Registries.COMMANDS.getRegistry();
        for (String key : commands.keySet()) {
            LegacyCommand command = commands.get(key);

            JsonNode object = JsonNode.object();
            object.set("command", key);
            object.set("shortHelp", command.getShortHelp());
            object.set("longHelp", command.getLongHelp().replace("<command>", Main.config.getPrefix() + key));
            object.set("permission", command.getPermission());
            object.set("group", command.getGroup().toString());
            root.add(object);
        }

        context.result(Json.json().serialize(root));
    }
}
