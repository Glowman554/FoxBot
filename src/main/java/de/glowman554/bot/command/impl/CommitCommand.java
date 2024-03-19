package de.glowman554.bot.command.impl;

import de.glowman554.bot.command.premade.JsonTextCommand;
import net.shadew.json.JsonNode;

public class CommitCommand extends JsonTextCommand {
    public CommitCommand() {
        super("Get a random commit message.", null, Group.FUN, "https://whatthecommit.com/index.json");
    }


    @Override
    public String extractText(JsonNode root) {
        return root.get("commit_message").asString();
    }
}
