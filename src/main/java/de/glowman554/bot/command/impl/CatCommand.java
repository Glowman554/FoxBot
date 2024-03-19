package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.premade.JsonPictureCommand;
import net.shadew.json.JsonNode;

public class CatCommand extends JsonPictureCommand {
    public CatCommand() {
        super("See a cute cat.", null, Group.ANIMAL, "https://api.thecatapi.com/v1/images/search");
    }

    @Override
    public String extractUrl(JsonNode root) {
        return root.get(0).get("url").asString();
    }
}
