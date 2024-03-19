package de.glowman554.bot.utils.math.parser;

import java.util.Objects;

public class ParserNode {
    private final ParserNodeType type;
    private final ParserNode nodeA;
    private final ParserNode nodeB;

    private final Object value;

    public ParserNode(ParserNodeType type, ParserNode nodeA, ParserNode nodeB) {
        this.type = type;

        if (type.singleElement) {
            if (nodeB != null) {
                throw new IllegalArgumentException("Node takes only 1 argument!");
            }
        }

        this.nodeA = nodeA;
        this.nodeB = nodeB;

        value = null;
    }

    public ParserNode(ParserNodeType type, Object value) {
        this.type = type;

        if (!type.singleElement) {
            throw new IllegalArgumentException("Node does not take only 1 argument!");
        }

        this.nodeA = null;
        this.nodeB = null;

        this.value = value;
    }

    public ParserNode(ParserNodeType type, ParserNode nodeA, Object value) {
        this.type = type;

        if (type.singleElement) {
            throw new IllegalArgumentException("Node takes only 1 argument!");
        }

        this.nodeA = nodeA;
        this.nodeB = null;

        this.value = value;
    }


    public Object getValue() {
        return value;
    }

    public ParserNode getNodeA() {
        return nodeA;
    }

    public ParserNode getNodeB() {
        return nodeB;
    }

    public ParserNodeType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParserNode that = (ParserNode) o;
        return type == that.type && Objects.equals(nodeA, that.nodeA) && Objects.equals(nodeB, that.nodeB) && Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return "ParserNode{" +
                "type=" + type +
                ", nodeA=" + nodeA +
                ", nodeB=" + nodeB +
                ", value=" + value +
                '}';
    }

    public enum ParserNodeType {
        NUMBER_NODE(true),
        ADD_NODE(false),
        SUBTRACT_NODE(false),
        MULTIPLY_NODE(false),
        DIVIDE_NODE(false),
        MODULO_NODE(false),
        POW_NODE(false),
        PLUS_NODE(true),
        FCALL_NODE(true),
        MINUS_NODE(true);

        public final boolean singleElement;

        ParserNodeType(boolean singleElement) {
            this.singleElement = singleElement;
        }
    }
}
