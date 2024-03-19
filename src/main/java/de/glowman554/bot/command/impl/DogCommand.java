package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.premade.JsonPictureCommand;
import net.shadew.json.JsonNode;

public class DogCommand extends JsonPictureCommand {
    public DogCommand() {
        super("See a cute dog.", null, Group.ANIMAL, "https://dog.ceo/api/breeds/image/random");
    }

    @Override
    public String extractUrl(JsonNode root) {
        return root.get("message").asString();
    }
}
