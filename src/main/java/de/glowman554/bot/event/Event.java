package de.glowman554.bot.event;

import java.util.ArrayList;

public class Event {
    public void call(Class<? extends Event> target) {
        final ArrayList<EventData> dataList = EventManager.get(target);

        if (dataList != null) {
            // toArray needed because of ConcurrentModificationException
            for (EventData data : dataList.toArray(EventData[]::new)) {
                try {
                    data.target.invoke(data.source, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void call() {
        call(getClass());
    }
}
