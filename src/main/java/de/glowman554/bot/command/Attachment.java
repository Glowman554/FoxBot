package de.glowman554.bot.command;

import de.glowman554.bot.utils.FileUtils;
import de.glowman554.bot.utils.StreamedFile;

import java.io.File;
import java.util.HashSet;
import java.util.List;

public abstract class Attachment {
    public abstract void download(File output);

    public abstract StreamedFile download();

    public Type getType() {
        String extension = FileUtils.getFileExtension(getName());
        for (Type type : Type.values()) {
            if (type.getExtensions().contains(extension)) {
                return type;
            }
        }
        return null;
    }

    public abstract String getName();

    public enum Type {
        IMAGE(new HashSet<>(List.of("jpg", "jpeg", "png", "gif", "webp", "tiff", "svg", "apng"))), VIDEO(new HashSet<>(List.of("webm", "flv", "vob", "avi", "mov", "wmv", "amv", "mp4", "mpg", "mpeg", "gifv"))), AUDIO(new HashSet<>(List.of("mp3"))) // TODO
        ;

        private final HashSet<String> extensions;

        Type(HashSet<String> extensions) {
            this.extensions = extensions;
        }

        public HashSet<String> getExtensions() {
            return extensions;
        }
    }
}
