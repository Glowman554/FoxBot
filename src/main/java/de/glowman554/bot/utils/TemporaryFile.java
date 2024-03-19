package de.glowman554.bot.utils;

import de.glowman554.bot.logging.Logger;

import java.io.File;

public class TemporaryFile implements AutoCloseable {
    public static final File BASE = new File("tmp");

    private final File file;

    public TemporaryFile(String fileExtension) {
        if (!BASE.exists()) {
            if (!BASE.mkdir()) {
                throw new RuntimeException("Could not create " + BASE.getPath());
            }
            Logger.log("Created directory %s", BASE.getPath());

        }
        file = new File(BASE, createTemporaryFileName(fileExtension));
        // Logger.log("New temporary file %s", file.getPath());
    }

    public static String createTemporaryFileName(String fileExtension) {
        return System.currentTimeMillis() + "_" + Math.random() + "." + fileExtension;
    }

    public File getFile() {
        return file;
    }

    @Override
    public void close() throws Exception {
        if (!file.delete()) {
            file.deleteOnExit();
        }
    }
}
