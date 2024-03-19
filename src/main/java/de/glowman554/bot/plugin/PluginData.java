package de.glowman554.bot.plugin;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class PluginData extends AutoSavable {
    @Saved
    private String entrypoint;
    @Saved
    private String name;
    @Saved
    private String author;
    @Saved
    private String version;

    public String getEntrypoint() {
        return entrypoint;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }
}
