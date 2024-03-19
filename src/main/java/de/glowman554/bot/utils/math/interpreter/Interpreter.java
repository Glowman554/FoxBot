package de.glowman554.bot.utils.math.interpreter;

import de.glowman554.bot.utils.Pair;
import de.glowman554.bot.utils.math.MathInterpreter;
import de.glowman554.bot.utils.math.parser.ParserNode;

import java.util.ArrayList;
import java.util.HashMap;

public class Interpreter {
    private final HashMap<String, MathFunction> mathFunctions = new HashMap<>();

    public Interpreter() {
        loadMathFunctions();
    }

    private void fail(int numArgsExpected) {
        throw new IllegalArgumentException("Expected " + numArgsExpected + " arguments!");
    }

    private void loadMathFunctions() {
        mathFunctions.put("sqrt", in -> {
            if (in.length != 1) {
                fail(1);
            }
            return Math.sqrt(in[0]);
        });

        mathFunctions.put("pow", in -> {
            if (in.length != 2) {
                fail(2);
            }
            return Math.pow(in[0], in[1]);
        });

        mathFunctions.put("random", in -> {
            if (in.length != 0) {
                fail(0);
            }
            return Math.random();
        });

        mathFunctions.put("sin", in -> {
            if (in.length != 1) {
                fail(1);
            }
            return Math.sin(in[0]);
        });

        mathFunctions.put("cos", in -> {
            if (in.length != 1) {
                fail(1);
            }
            return Math.cos(in[0]);
        });

        mathFunctions.put("tan", in -> {
            if (in.length != 1) {
                fail(1);
            }
            return Math.tan(in[0]);
        });
    }

    public double interpret(ParserNode root, MathInterpreter.DebugPrint dbg) {
        dbg.log("Interpreting " + root.getType().toString());
        switch (root.getType()) {
            case NUMBER_NODE:
                return (double) root.getValue();
            case ADD_NODE:
                return interpret(root.getNodeA(), dbg) + interpret(root.getNodeB(), dbg);
            case SUBTRACT_NODE:
                return interpret(root.getNodeA(), dbg) - interpret(root.getNodeB(), dbg);
            case MULTIPLY_NODE:
                return interpret(root.getNodeA(), dbg) * interpret(root.getNodeB(), dbg);
            case DIVIDE_NODE:
                return interpret(root.getNodeA(), dbg) / interpret(root.getNodeB(), dbg);
            case MODULO_NODE:
                return interpret(root.getNodeA(), dbg) % interpret(root.getNodeB(), dbg);
            case POW_NODE:
                return Math.pow(interpret(root.getNodeA(), dbg), interpret(root.getNodeB(), dbg));
            case FCALL_NODE: {
                Pair<ArrayList<ParserNode>, String> argNamePair = (Pair<ArrayList<ParserNode>, String>) root.getValue();
                MathFunction function = mathFunctions.get(argNamePair.t2);

                if (function == null) {
                    throw new IllegalStateException("Could not find function " + argNamePair.t2);
                }

                double[] result = new double[argNamePair.t1.size()];

                for (int i = 0; i < argNamePair.t1.size(); i++) {
                    result[i] = interpret(argNamePair.t1.get(i), dbg);
                }

                return function.call(result);
            }
        }

        throw new IllegalStateException("Unreachable!");
    }

    public interface MathFunction {
        double call(double[] in);
    }
}
