package de.glowman554.bot.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public static final File logDirectory = new File("logs");
    private static File currentLogFile;
    private static FileWriter fileWriter;

    public static void log(String format, Object... args) {
        if (fileWriter == null) {
            if (!logDirectory.exists()) {
                if (!logDirectory.mkdir()) {
                    throw new RuntimeException("Could not create " + logDirectory.getPath());
                }
            }
            currentLogFile = new File(logDirectory, System.currentTimeMillis() + ".log");
            try {
                if (!currentLogFile.createNewFile()) {
                    throw new RuntimeException("Could not create logfile");
                }
                fileWriter = new FileWriter(currentLogFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String message = format;
        if (args.length > 0) {
            message = String.format(format, args);
        }

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuilder newMessage = new StringBuilder();
        for (String line : message.split("\n")) {
            newMessage.append(String.format("[%s::%s at %s:%s] %s\n", stackTraceElements[2].getClassName(), stackTraceElements[2].getMethodName(), stackTraceElements[2].getFileName(), stackTraceElements[2].getLineNumber(), line));
        }

        System.out.println(newMessage.toString().strip());
        try {
            fileWriter.append(newMessage.toString().strip()).append("\n");
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exception(Exception e) {
        log("Exception: %s", e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log("    at %s.%s(%s:%s)", element.getClassName(), element.getMethodName(), element.getFileName(), element.getLineNumber());
        }
    }

    public static File getCurrentLogFile() {
        return currentLogFile;
    }
}
