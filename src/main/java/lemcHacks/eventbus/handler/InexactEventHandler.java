package lemcHacks.eventbus.handler;

import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import lemcHacks.event.Event;
import lemcHacks.eventbus.LeMCSubscribe;
import lemcHacks.eventbus.LeMCSubscriber;

public class InexactEventHandler extends EventHandler{
    private final Map<Class<?>, List<LeMCSubscriber>> subscribers = new ConcurrentHashMap<>();

    public InexactEventHandler(String id) {
        super(id);
    }

    public boolean subscribe(Object object) {
        boolean added = false;
        for (Method m: object.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(LeMCSubscribe.class) && m.getParameters().length != 0) {
                subscribers.computeIfAbsent(m.getParameters()[0].getType(), k -> new CopyOnWriteArrayList<>()).add(new LeMCSubscriber(object, m));
                added = true;
            }
        }

        return added;
    }

    public boolean unsubscribe(Object object) {
        boolean[] removed = new boolean[1];
        subscribers.values().removeIf(v -> {
            removed[0] |= v.removeIf(s -> object.getClass().equals(s.getTargetClass()));
            return v.isEmpty();
        });

        return removed[0];
    }

    public void post(Event event, Logger logger) {
        for (Map.Entry<Class<?>, List<LeMCSubscriber>> entry: subscribers.entrySet()) {
            if (entry.getKey().isAssignableFrom(event.getClass())) {
                for (LeMCSubscriber s: entry.getValue()) {
                    try {
                        s.callSubscriber(event);
                    } catch (Throwable t) {
                        //a
                    }
                }
            }
        }
    }
}