package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.premade.JsonTextCommand;
import net.shadew.json.JsonNode;

public class JokeCommand extends JsonTextCommand {
    public JokeCommand() {
        super("Get a random joke.", null, Group.FUN, "https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&type=single");
    }

    @Override
    public String extractText(JsonNode root) {
        return root.get("joke").asString();
    }
}
