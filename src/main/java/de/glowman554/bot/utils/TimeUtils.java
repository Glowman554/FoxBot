package de.glowman554.bot.utils;

public class TimeUtils {
    public static String seconToDhms(int seconds) {
        int day = (seconds / (3600 * 24));
        int hour = (seconds % (3600 * 24) / 3600);
        int minute = (seconds % 3600 / 60);
        int second = (seconds % 60);

        String dayS = day > 0 ? day + (day == 1 ? " day, " : " days, ") : "";
        String hourS = hour > 0 ? hour + (hour == 1 ? " hour, " : " hours, ") : "";
        String minuteS = minute > 0 ? minute + (minute == 1 ? " minute, " : " minutes, ") : "";
        String secondS = second > 0 ? second + (second == 1 ? " second" : " seconds") : "";

        StringBuilder result = new StringBuilder((dayS + hourS + minuteS + secondS).strip());

        if (result.charAt(result.length() - 1) == ',') {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }

    public static String millisecondToDhms(long milliseconds) {
        return seconToDhms((int) (milliseconds / 1000));
    }
}
