package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.premade.JsonPictureCommand;
import net.shadew.json.JsonNode;

public class MemeCommand extends JsonPictureCommand {
    public MemeCommand() {
        super("See a meme.", null, Group.FUN, "https://meme-api.com/gimme");
    }


    @Override
    public String extractUrl(JsonNode root) {
        return root.get("url").asString();
    }
}
