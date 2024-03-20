package de.glowman554.bot.platform.web;

import de.glowman554.bot.Platform;
import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.event.impl.JavalinEvent;
import de.glowman554.bot.logging.Logger;
import de.glowman554.config.ConfigManager;

public class WebPlatform extends Platform {

    @Override
    public void init(ConfigManager configManager) {
        EventManager.register(this);
        Logger.log("Web ready.");
    }

    @EventTarget
    public void onJavalin(JavalinEvent event) {
        WebManager webManager = new WebManager();
        event.getJavalin().ws("/web", wsConfig -> {
            webManager.bind(wsConfig);
        });
    }
}
