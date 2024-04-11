package de.glowman554.bot.platform.web;

import de.glowman554.bot.Platform;
import de.glowman554.bot.command.SchemaCommand;
import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.event.impl.JavalinEvent;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.registry.Registries;
import de.glowman554.config.ConfigManager;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

public class WebPlatform extends Platform {
    private final JsonNode schemas = JsonNode.array();

    @Override

    public void init(ConfigManager configManager) {
        EventManager.register(this);

        Registries.COMMANDS.iterate((name, command) -> {
            if (command instanceof SchemaCommand schemaCommand) {
                WebSchema schema = new WebSchema(name, command.getShortHelp());
                schemaCommand.loadSchema(schema);
                schemas.add(schema.getSchema());
            }
        });
        
        Logger.log("Web ready.");
    }

    @EventTarget
    public void onJavalin(JavalinEvent event) {
        WebManager webManager = new WebManager(schemas);
        event.getJavalin().ws("/web", webManager::bind);
    }
}
