package de.glowman554.bot.features.ttt;

import de.glowman554.bot.utils.Pair;

public class Game {
    public Field[][] field;

    public Game(Field[][] field) {
        this.field = field;

        assert field.length == 3;
        assert field[0].length == 3;
    }

    public Pair<Boolean, Field> isGameOver() {
        // check rows
        for (Field[] item : field) {
            if (item[0] != Field.FIELD_EMPTY && item[0] == item[1] && item[1] == item[2]) {
                return new Pair<>(true, item[0]);
            }
        }

        // check columns
        for (int i = 0; i < field[0].length; i++) {
            if (field[0][i] != Field.FIELD_EMPTY && field[0][i] == field[1][i] && field[1][i] == field[2][i]) {
                return new Pair<>(true, field[0][i]);
            }
        }

        // check diagonals
        if (field[0][0] != Field.FIELD_EMPTY && field[0][0] == field[1][1] && field[1][1] == field[2][2]) {
            return new Pair<>(true, field[0][0]);
        }

        if (field[0][2] != Field.FIELD_EMPTY && field[0][2] == field[1][1] && field[1][1] == field[2][0]) {
            return new Pair<>(true, field[0][2]);
        }

        // check if there is any empty field
        for (Field[] fields : field) {
            for (Field value : fields) {
                if (value == Field.FIELD_EMPTY) {
                    return new Pair<>(false, null);
                }
            }
        }

        return new Pair<>(true, null);
    }

    private int minmax(Field player, int depth, boolean isMaximizing) {
        Pair<Boolean, Field> gameOver = isGameOver();
        if (gameOver.t1()) {
            if (gameOver.t2() == Field.FIELD_X) {
                return -10 + depth;
            } else if (gameOver.t2() == Field.FIELD_O) {
                return 10 - depth;
            } else {
                return 0;
            }
        }

        int best;
        if (isMaximizing) {
            best = -1000;
            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[i].length; j++) {
                    if (field[i][j] == Field.FIELD_EMPTY) {
                        field[i][j] = player;
                        best = Math.max(best, minmax(player == Field.FIELD_X ? Field.FIELD_O : Field.FIELD_X, depth + 1, false));
                        field[i][j] = Field.FIELD_EMPTY;
                    }
                }
            }
        } else {
            best = 1000;
            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[i].length; j++) {
                    if (field[i][j] == Field.FIELD_EMPTY) {
                        field[i][j] = player;
                        best = Math.min(best, minmax(player == Field.FIELD_X ? Field.FIELD_O : Field.FIELD_X, depth + 1, true));
                        field[i][j] = Field.FIELD_EMPTY;
                    }
                }
            }
        }
        return best;
    }

    public Pair<Integer, Integer> getMove() {
        int bestScore = -1000;
        int bestX = -1;
        int bestY = -1;

        boolean foundValidMove = false; // Flag to track if any valid move exists

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == Field.FIELD_EMPTY) {
                    field[i][j] = Field.FIELD_O;
                    int score = minmax(Field.FIELD_X, 0, false);
                    field[i][j] = Field.FIELD_EMPTY;

                    if (score > bestScore) {
                        bestScore = score;
                        bestX = i;
                        bestY = j;
                    }

                    foundValidMove = true; // Mark that a valid move exists
                }
            }
        }

        if (foundValidMove) {
            field[bestX][bestY] = Field.FIELD_O;
            return new Pair<>(bestX, bestY);
        } else {
            return null; // No valid moves available
        }
    }

    public enum Field {
        FIELD_EMPTY, FIELD_X, FIELD_O
    }
}