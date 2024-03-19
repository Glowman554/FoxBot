package de.glowman554.bot.plugin;

import de.glowman554.bot.logging.Logger;
import net.shadew.json.Json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public class PluginLoader {
    private static final ArrayList<URLClassLoader> loader = new ArrayList<>();
    private final File pluginDirectory;

    public PluginLoader(File pluginDirectory) throws IOException {
        this.pluginDirectory = pluginDirectory;

        if (!this.pluginDirectory.exists()) {
            if (!this.pluginDirectory.mkdir()) {
                throw new RuntimeException("Could not create " + this.pluginDirectory.getPath());
            }
            Logger.log("Created directory %s.", this.pluginDirectory.getPath());
        }

        load();

        Logger.log("Loaded %d external jar's", loader.size());
    }

    private void load() throws IOException {
        try (Stream<Path> pathStream = Files.walk(pluginDirectory.toPath())) {
            pathStream.forEach(this::loader);
        }
    }

    private void loader(Path path) {
        File file = path.toFile();
        if (file.isFile()) {

            try {
                URLClassLoader child = new URLClassLoader(new URL[]{new URL("jar:file:" + file.getAbsolutePath() + "!/")}, this.getClass().getClassLoader());
                loader.add(child);
                try (InputStream pluginDataStream = child.getResourceAsStream("plugin.json")) {
                    PluginData pluginData = new PluginData();
                    pluginData.fromJSON(Json.json().parse(pluginDataStream));

                    Logger.log("[%s] Loading plugin %s@%s by %s", file.getName(), pluginData.getName(), pluginData.getVersion(), pluginData.getAuthor());

                    Class<?> mainClass = child.loadClass(pluginData.getEntrypoint());

                    Object instance = mainClass.getDeclaredConstructor().newInstance();
                    for (Method method : mainClass.getMethods()) {
                        if (method.isAnnotationPresent(Entrypoint.class)) {
                            method.invoke(instance);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
