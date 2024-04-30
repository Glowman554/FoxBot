package de.glowman554.bot.command;

import de.glowman554.bot.utils.FileUtils;
import de.glowman554.bot.utils.StreamedFile;

import java.io.File;

public abstract class Attachment {
    public abstract void download(File output);

    public abstract StreamedFile download();

    public MediaType getType() {
        String extension = FileUtils.getFileExtension(getName());
        for (MediaType type : MediaType.values()) {
            if (type.getExtensions().contains(extension)) {
                return type;
            }
        }
        return null;
    }

    public abstract String getName();


}
