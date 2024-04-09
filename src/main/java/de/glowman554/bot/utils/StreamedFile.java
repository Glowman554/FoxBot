package de.glowman554.bot.utils;

import java.io.*;

public class StreamedFile implements AutoCloseable {
    private String name;
    private InputStream stream;

    public StreamedFile(InputStream stream, String name) {
        this.stream = stream;
        this.name = name;
    }

    @Deprecated
    public StreamedFile(File input) throws FileNotFoundException {
        this.stream = new FileInputStream(input);
        this.name = input.getName();
    }

    public InputStream getStream() {
        return stream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save(File output) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(output)) {
            stream.transferTo(outputStream);
        }
    }

    @Override
    public void close() throws Exception {
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }
}
