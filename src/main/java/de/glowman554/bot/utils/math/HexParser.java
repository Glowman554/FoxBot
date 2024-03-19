package de.glowman554.bot.utils.math;

public class HexParser {
    private static int hexDigit(char digit) {
        int num_digit = "0123456789abcdef".indexOf(digit);

        if (num_digit == -1) {
            throw new IllegalArgumentException("Character " + digit + " not recognized!");
        }

        return num_digit;
    }

    public static int fromHex(String hex) {
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }

        hex = new StringBuilder(hex).reverse().toString();

        int idx = 0;
        int ret = 0;

        for (String s : hex.split("")) {
            char digit = s.charAt(0);
            int num_digit = hexDigit(digit);

            ret += (int) (Math.pow(16, idx++) * num_digit);
        }

        return ret;
    }
}
