package de.glowman554.bot.features.ttt;

import de.glowman554.bot.logging.Logger;

import java.util.Arrays;

public abstract class Parser {
    public static Parser[] parsers = new Parser[]{};
    public Game.Field[][] field = new Game.Field[3][3];

    public Parser() {
        reset();
    }

    public static void addParser(Parser parser) {
        Parser[] newParsers = new Parser[parsers.length + 1];
        System.arraycopy(parsers, 0, newParsers, 0, parsers.length);
        newParsers[newParsers.length - 1] = parser;
        parsers = newParsers;

        Logger.log("Added parser: %s", parser.getClass().getSimpleName());
    }

    public static Parser tryParse(String s) {
        for (Parser parser : parsers) {
            if (parser.parse(s)) {
                return parser;
            } else {
                parser.reset();
            }
        }

        return null;
    }

    public abstract boolean parse(String s); // returns false if string is unparseable

    public abstract String toString();

    public void reset() {
        for (Game.Field[] fields : field) {
            Arrays.fill(fields, Game.Field.FIELD_EMPTY);
        }
    }
}