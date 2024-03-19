package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.premade.JsonQueryCommand;
import net.shadew.json.JsonNode;

public class WikipediaCommand extends JsonQueryCommand {
    public WikipediaCommand() {
        super("Search wikipedia.", null, Group.TOOLS, "https://en.wikipedia.org/api/rest_v1/page/summary/<q>");
    }


    @Override
    public String extractText(JsonNode root) {
        return root.get("extract").asString() + "\n" + root.get("content_urls").get("desktop").get("page").asString();
    }
}
