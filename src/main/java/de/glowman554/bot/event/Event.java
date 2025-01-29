package de.glowman554.bot.event;

import de.glowman554.bot.logging.Logger;

import java.util.ArrayList;

public class Event {
    public void call(Class<? extends Event> target) {
        final ArrayList<EventData> dataList = EventManager.get(target);

        if (dataList != null) {
            // toArray needed because of ConcurrentModificationException
            for (EventData data : dataList.toArray(EventData[]::new)) {
                try {
                    data.target().invoke(data.source(), this);
                } catch (Exception e) {
                    Logger.exception(e);
                }
            }
        }
    }

    public void call() {
        call(getClass());
    }
}
