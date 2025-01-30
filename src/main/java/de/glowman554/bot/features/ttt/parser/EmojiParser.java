package de.glowman554.bot.features.ttt.parser;

import de.glowman554.bot.features.ttt.Game;
import de.glowman554.bot.features.ttt.Parser;

public class EmojiParser extends Parser {
    @Override
    public boolean parse(String s) {
        if (s == null) {
            return false;
        }
        String[] split = s.split("\n");
        if (split.length != 3) {
            return false;
        }

        for (int i = 0; i < split.length; i++) {
            String[] line = split[i].replace(" ", "").split("");
            if (line.length != 3) {
                return false;
            }

            for (int j = 0; j < line.length; j++) {
                switch (line[j]) {
                    case "❔":
                    case "❓":
                        field[i][j] = Game.Field.FIELD_EMPTY;
                        break;
                    case "⭕":
                        field[i][j] = Game.Field.FIELD_O;
                        break;
                    case "❌":
                        field[i][j] = Game.Field.FIELD_X;
                        break;
                    default:
                        return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Game.Field[] fields : field) {
            for (Game.Field value : fields) {
                switch (value) {
                    case FIELD_EMPTY:
                        sb.append("❔");
                        break;
                    case FIELD_O:
                        sb.append("⭕");
                        break;
                    case FIELD_X:
                        sb.append("❌");
                        break;
                }
                sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}