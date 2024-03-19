package de.glowman554.bot.platform.web;

import de.glowman554.bot.Platform;
import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.event.impl.SparkSetupEvent;
import de.glowman554.bot.logging.Logger;
import de.glowman554.config.ConfigManager;
import spark.Spark;

public class WebPlatform extends Platform {

    @Override
    public void init(ConfigManager configManager) {
        EventManager.register(this);
        Logger.log("Web ready.");
    }

    @EventTarget
    public void onSparkSetup(SparkSetupEvent event) {
        if (event.getStep() == SparkSetupEvent.Step.SOCKET) {
            Spark.webSocket("/web", WebInstance.class);
        }
    }
}
