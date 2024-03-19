package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.premade.JsonQueryCommand;
import net.shadew.json.JsonNode;

public class YunaCommand extends JsonQueryCommand {
    public YunaCommand() {
        super("Search using the yuna search engine.", null, Group.TOOLS, "https://yuna.glowman554.de/search_json.php?search=<q>");
    }

    @Override
    public String extractText(JsonNode root) {
        StringBuilder result = new StringBuilder();
        for (JsonNode entry : root) {
            result.append(entry.get("title").asString()).append(": ").append(entry.get("link").asString()).append("\n");
        }

        return result.toString();
    }
}
