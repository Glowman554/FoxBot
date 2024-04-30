package de.glowman554.bot.command;

import java.util.HashSet;
import java.util.List;

public enum MediaType {
    IMAGE(new HashSet<>(List.of("jpg", "jpeg", "png", "gif", "webp", "tiff", "svg", "apng"))), VIDEO(new HashSet<>(List.of("webm", "flv", "vob", "avi", "mov", "wmv", "amv", "mp4", "mpg", "mpeg", "gifv"))), AUDIO(new HashSet<>(List.of("mp3"))), DOCUMENT(new HashSet<>(List.of())) // TODO
    ;

    private final HashSet<String> extensions;

    MediaType(HashSet<String> extensions) {
        this.extensions = extensions;
    }

    public HashSet<String> getExtensions() {
        return extensions;
    }
}