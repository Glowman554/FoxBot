package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.premade.JsonTextCommand;
import net.shadew.json.JsonNode;

public class FactCommand extends JsonTextCommand {
    public FactCommand() {
        super("Get a random fact.", null, Group.FUN, "https://uselessfacts.jsph.pl/api/v2/facts/random?language=en");
    }

    @Override
    public String extractText(JsonNode root) {
        return root.get("text").asString();
    }
}
