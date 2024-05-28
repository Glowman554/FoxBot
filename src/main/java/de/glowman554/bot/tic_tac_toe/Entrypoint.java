package de.glowman554.bot.tic_tac_toe;

import de.glowman554.bot.Feature;
import de.glowman554.bot.Main;
import de.glowman554.bot.command.LegacyCommandContext;
import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.registry.Registries;
import de.glowman554.bot.tic_tac_toe.parser.EmojiParser;
import de.glowman554.bot.utils.Pair;

public class Entrypoint {
    public static void main(String[] args) throws Exception {
        new Entrypoint().entrypoint();
        Main.main(args);
    }

    @de.glowman554.bot.plugin.Entrypoint
    public void entrypoint() {
        Parser.addParser(new EmojiParser());
        EventManager.register(this);
        Registries.FEATURES.register("de/glowman554/bot/tic_tac_toe", new Feature("Tic tac toe", """
                To play tic tac toke with the bot send the following message:
                ❓❓❓
                ❓❓❓
                ❓❓❓
                The bot will reply with a new game field. Just copy and paste it and set your ❌ wherever you want."""));
    }

    @EventTarget
    public void onMessage(LegacyCommandContext message) {
        Parser parser = Parser.tryParse(message.getMessage());

        if (parser != null) {

            Game ai = new Game(parser.field);

            Pair<Integer, Integer> move = ai.getMove();

            if (move == null) {
                message.reply("GG");
            } else {
                parser.field[move.t1][move.t2] = Game.Field.FIELD_O;

                message.reply(parser.toString());

                Pair<Boolean, Game.Field> gameOver = ai.isGameOver();
                if (gameOver.t1) {
                    if (gameOver.t2 == Game.Field.FIELD_X) {
                        message.reply("You won! How???");
                    } else if (gameOver.t2 == Game.Field.FIELD_O) {
                        message.reply("I won!");
                    } else {
                        message.reply("GG");
                    }
                }
            }
        }
    }
}
