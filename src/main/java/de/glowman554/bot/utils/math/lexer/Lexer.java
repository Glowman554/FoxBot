package de.glowman554.bot.utils.math.lexer;

import de.glowman554.bot.utils.math.BinParser;
import de.glowman554.bot.utils.math.HexParser;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String text;
    private int textPos = -1;
    private char currentChar = 0;

    public Lexer(String text) {
        this.text = text;
        next();
    }

    private void next() {
        textPos++;

        if (text.length() <= textPos) {
            currentChar = 0;
        } else {
            currentChar = text.charAt(textPos);
        }
    }

    private void reverse() {
        textPos--;

        if (text.length() <= textPos) {
            currentChar = 0;
        } else {
            currentChar = text.charAt(textPos);
        }
    }

    public LexerToken[] tokenize() {
        List<LexerToken> tokens = new ArrayList<>();

        while (currentChar != 0) {
            switch (currentChar) {
                case ' ':
                case '\n':
                case '\t': {
                    next();
                }
                break;

                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.': {
                    tokens.add(number());
                }
                break;

                case '+': {
                    tokens.add(new LexerToken(LexerToken.LexerTokenType.PLUS));
                    next();
                }
                break;

                case '-': {
                    tokens.add(new LexerToken(LexerToken.LexerTokenType.MINUS));
                    next();
                }
                break;

                case '*': {
                    tokens.add(new LexerToken(LexerToken.LexerTokenType.MULTIPLY));
                    next();
                }
                break;

                case '/': {
                    tokens.add(new LexerToken(LexerToken.LexerTokenType.DIVIDE));
                    next();
                }
                break;

                case '%': {
                    tokens.add(new LexerToken(LexerToken.LexerTokenType.MODULO));
                    next();
                }
                break;

                case '^': {
                    tokens.add(new LexerToken(LexerToken.LexerTokenType.POW));
                    next();
                }
                break;

                case '(': {
                    tokens.add(new LexerToken(LexerToken.LexerTokenType.LPAREN));
                    next();
                }
                break;

                case ')': {
                    tokens.add(new LexerToken(LexerToken.LexerTokenType.RPAREN));
                    next();
                }
                break;

                case ',': {
                    tokens.add(new LexerToken(LexerToken.LexerTokenType.COMMA));
                    next();
                }
                break;

                default: {
                    if ("abcdefghijklmnopqrstuvwxyz".contains(String.valueOf(currentChar).toLowerCase())) {
                        tokens.add(id());
                    } else {
                        throw new IllegalStateException("Unknown char: " + currentChar);
                    }
                }
            }
        }

        return tokens.toArray(new LexerToken[0]);
    }

    private LexerToken id() {
        StringBuilder identifier = new StringBuilder();

        while (currentChar != 0 && Character.isLetter(currentChar)) {
            identifier.append(currentChar);
            next();
        }

        return new LexerToken(LexerToken.LexerTokenType.ID, identifier.toString());
    }

    private LexerToken number() {
        int decimalPointCount = 0;
        StringBuilder numberString = new StringBuilder(String.valueOf(currentChar));

        boolean hex = false;
        boolean bin = false;

        if (currentChar == '0') {
            next();
            numberString.append(currentChar);

            switch (currentChar) {
                case 'x': {
                    hex = true;
                }
                break;

                case 'b': {
                    bin = true;
                }
                break;

                default: {
                    reverse();
                }
                break;
            }

            next();
        } else {
            next();
        }

        while (currentChar != 0 && "0123456789.".indexOf(currentChar) != -1) {
            if (currentChar == '.') {
                decimalPointCount++;
                if (decimalPointCount > 1) {
                    break;
                }
            }

            numberString.append(currentChar);
            next();
        }

        if (!bin && !hex) {
            return new LexerToken(LexerToken.LexerTokenType.NUMBER, Double.parseDouble(numberString.toString()));
        } else {
            if (bin) {
                return new LexerToken(LexerToken.LexerTokenType.NUMBER, (double) BinParser.fromBin(numberString.toString()));
            } else {
                return new LexerToken(LexerToken.LexerTokenType.NUMBER, (double) HexParser.fromHex(numberString.toString()));
            }
        }
    }
}
