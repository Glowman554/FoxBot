package de.glowman554.bot.api;

import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.event.impl.SparkSetupEvent;
import de.glowman554.bot.logging.Logger;
import spark.Spark;

public class Api {
    public Api() {
        EventManager.register(this);
    }

    @EventTarget
    public void onSparkSetup(SparkSetupEvent event) {
        if (event.getStep() == SparkSetupEvent.Step.API) {
            Logger.log("Adding api endpoints.");
            Spark.get("/api/usage", new UsageApiEndpoint());
            Spark.get("/api/help", new HelpApiEndpoint());
            Spark.get("/api/stats", new StatsApiEndpoint());
        }
    }
}
