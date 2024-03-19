package de.glowman554.bot.utils;

import de.glowman554.config.auto.AutoSavable;
import net.shadew.json.Json;

import java.io.File;
import java.io.IOException;

public class AutoFileSavable extends AutoSavable {
    public void save(File file) {
        try {
            Json.json().serialize(toJSON(), file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(File file) {
        try {
            fromJSON(Json.json().parse(file));
        } catch (IOException ignored) {

        }
        save(file);
    }
}
