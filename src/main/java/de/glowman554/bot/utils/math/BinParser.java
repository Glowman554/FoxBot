package de.glowman554.bot.utils.math;

public class BinParser {
    private static int binDigit(char digit) {
        int numDigit = "01".indexOf(digit);

        if (numDigit == -1) {
            throw new IllegalArgumentException("Character " + digit + " not recognized!");
        }

        return numDigit;
    }

    public static int fromBin(String bin) {
        if (bin.startsWith("0b")) {
            bin = bin.substring(2);
        }

        bin = new StringBuilder(bin).reverse().toString();

        int idx = 0;
        int ret = 0;

        for (String s : bin.split("")) {
            char digit = s.charAt(0);
            int num_digit = binDigit(digit);

            ret += (int) (Math.pow(2, idx++) * num_digit);
        }

        return ret;
    }
}
