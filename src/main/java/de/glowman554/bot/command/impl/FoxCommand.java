package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.premade.JsonPictureCommand;
import net.shadew.json.JsonNode;

public class FoxCommand extends JsonPictureCommand {
    public FoxCommand() {
        super("See a cute fox.", null, Group.ANIMAL, "https://randomfox.ca/floof/");
    }


    @Override
    public String extractUrl(JsonNode root) {
        return root.get("image").asString();
    }
}
