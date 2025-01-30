package de.glowman554.bot.features.ttt;

import de.glowman554.bot.command.LegacyCommandContext;
import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.features.ttt.parser.EmojiParser;
import de.glowman554.bot.utils.Pair;

public class TicTacToe {
    public TicTacToe() {
        Parser.addParser(new EmojiParser());
        EventManager.register(this);
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
                parser.field[move.t1()][move.t2()] = Game.Field.FIELD_O;

                message.reply(parser.toString());

                Pair<Boolean, Game.Field> gameOver = ai.isGameOver();
                if (gameOver.t1()) {
                    if (gameOver.t2() == Game.Field.FIELD_X) {
                        message.reply("You won! How???");
                    } else if (gameOver.t2() == Game.Field.FIELD_O) {
                        message.reply("I won!");
                    } else {
                        message.reply("GG");
                    }
                }
            }
        }
    }
}
