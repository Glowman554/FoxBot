package de.glowman554.bot.utils.math.lexer;

import java.util.Objects;

public class LexerToken {
    private final LexerTokenType type;
    private final Object value;

    public LexerToken(LexerTokenType type) {
        this.type = type;
        this.value = null;
    }

    public LexerToken(LexerTokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public LexerTokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "LexerToken{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LexerToken that = (LexerToken) o;

        return type == that.type && Objects.equals(value, that.value);
    }

    public enum LexerTokenType {
        NUMBER,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        MODULO,
        POW,
        LPAREN,
        RPAREN,
        COMMA,
        ID
    }
}
