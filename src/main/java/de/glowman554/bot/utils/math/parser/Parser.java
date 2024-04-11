package de.glowman554.bot.utils.math.parser;

import de.glowman554.bot.utils.Pair;
import de.glowman554.bot.utils.math.lexer.LexerToken;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    private final LexerToken[] tokens;
    private final HashMap<String, Double> definedVariables = new HashMap<>();
    private int tokensPos = -1;
    private LexerToken currentToken = null;

    public Parser(LexerToken[] tokens) {
        this.tokens = tokens;
        next();

        loadDefinedVariables();
    }

    private void loadDefinedVariables() {
        definedVariables.put("pi", 355d / 113d);
    }

    private void fail() {
        throw new IllegalStateException("Invalid syntax! " + currentToken.toString());
    }

    private void next() {
        tokensPos++;

        if (tokens.length <= tokensPos) {
            currentToken = null;
        } else {
            currentToken = tokens[tokensPos];
        }
    }

    private void reverse() {
        tokensPos--;

        if (tokens.length <= tokensPos) {
            currentToken = null;
        } else {
            currentToken = tokens[tokensPos];
        }
    }

    public ParserNode parse() {
        return expr();
    }

    public ParserNode expr() {
        ParserNode result = term();

        while (currentToken != null && (currentToken.getType() == LexerToken.LexerTokenType.PLUS || currentToken.getType() == LexerToken.LexerTokenType.MINUS)) {
            if (currentToken.getType() == LexerToken.LexerTokenType.MINUS) {
                next();
                result = new ParserNode(ParserNode.ParserNodeType.SUBTRACT_NODE, result, term());
            } else if (currentToken.getType() == LexerToken.LexerTokenType.PLUS) {
                next();
                result = new ParserNode(ParserNode.ParserNodeType.ADD_NODE, result, term());
            } else {
                fail();
            }
        }

        return result;
    }

    public ParserNode term() {
        ParserNode result = power();

        while (currentToken != null && (currentToken.getType() == LexerToken.LexerTokenType.MULTIPLY || currentToken.getType() == LexerToken.LexerTokenType.DIVIDE || currentToken.getType() == LexerToken.LexerTokenType.MODULO)) {
            if (currentToken.getType() == LexerToken.LexerTokenType.MULTIPLY) {
                next();
                result = new ParserNode(ParserNode.ParserNodeType.MULTIPLY_NODE, result, power());
            } else if (currentToken.getType() == LexerToken.LexerTokenType.DIVIDE) {
                next();
                result = new ParserNode(ParserNode.ParserNodeType.DIVIDE_NODE, result, power());
            } else if (currentToken.getType() == LexerToken.LexerTokenType.MODULO) {
                next();
                result = new ParserNode(ParserNode.ParserNodeType.MODULO_NODE, result, power());
            } else {
                fail();
            }
        }

        return result;
    }

    public ParserNode power() {
        ParserNode result = factor();

        while (currentToken != null && (currentToken.getType() == LexerToken.LexerTokenType.POW)) {
            if (currentToken.getType() == LexerToken.LexerTokenType.POW) {
                next();
                result = new ParserNode(ParserNode.ParserNodeType.POW_NODE, result, factor());
            } else {
                fail();
            }
        }

        return result;
    }

    public ParserNode factor() {
        LexerToken token = currentToken;

        if (token.getType() == LexerToken.LexerTokenType.LPAREN) {
            next();
            ParserNode result = expr();

            if (currentToken.getType() != LexerToken.LexerTokenType.RPAREN) {
                fail();
            }

            next();

            return result;
        } else if (token.getType() == LexerToken.LexerTokenType.NUMBER) {
            next();
            return new ParserNode(ParserNode.ParserNodeType.NUMBER_NODE, token.getValue());
        } else if (token.getType() == LexerToken.LexerTokenType.PLUS) {
            next();
            return new ParserNode(ParserNode.ParserNodeType.PLUS_NODE, factor());
        } else if (token.getType() == LexerToken.LexerTokenType.MINUS) {
            next();
            return new ParserNode(ParserNode.ParserNodeType.MINUS_NODE, factor());
        } else if (token.getType() == LexerToken.LexerTokenType.ID) {
            next();
            if (currentToken != null && currentToken.getType() == LexerToken.LexerTokenType.LPAREN) {
                next();
                if (currentToken.getType() == LexerToken.LexerTokenType.RPAREN) {
                    next();
                    return new ParserNode(ParserNode.ParserNodeType.FCALL_NODE, new Pair<>(new ArrayList<ParserNode>(), token.getValue()));
                }

                reverse();

                ArrayList<ParserNode> result = new ArrayList<>();

                do {
                    next();
                    result.add(expr());
                } while (currentToken.getType() == LexerToken.LexerTokenType.COMMA);

                if (currentToken.getType() != LexerToken.LexerTokenType.RPAREN) {
                    fail();
                }

                next();
                return new ParserNode(ParserNode.ParserNodeType.FCALL_NODE, new Pair<>(result, token.getValue()));
            } else {
                reverse();
                Double fromLookup = definedVariables.get((String) token.getValue());

                if (fromLookup == null) {
                    throw new IllegalStateException("Could not complete lookup for " + token.getValue());
                }

                next();
                return new ParserNode(ParserNode.ParserNodeType.NUMBER_NODE, fromLookup);
            }
        } else {
            fail();
        }

        return null;
    }
}
