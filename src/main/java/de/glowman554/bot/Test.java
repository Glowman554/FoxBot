package de.glowman554.bot;

import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.utils.api.YiffAPI;

public class Test {
    public static void main(String[] args) {
        YiffAPI yiffAPI = new YiffAPI();
        for (YiffAPI.YiffCategory category : yiffAPI.getCategoriesNSFW()) {
            Logger.log("%s", category);
            Logger.log("%s", category.download());
        }
    }
}
