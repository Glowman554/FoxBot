package de.glowman554.bot.utils;

public class FileUtils {
    public static String getFileExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot == -1) {
            return "";
        }

        return fileName.substring(dot + 1);
    }
}
