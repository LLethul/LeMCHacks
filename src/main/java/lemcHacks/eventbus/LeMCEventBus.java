package lemcHacks.eventbus;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.Logger;

import lemcHacks.event.Event;
import lemcHacks.eventbus.handler.EventHandler;

public class LeMCEventBus {
    private EventHandler handler;
    private final AtomicLong eventsPosted = new AtomicLong();

    private Logger logger;

    public LeMCEventBus(EventHandler handler, Logger logger) {
        this.handler = handler;
        this.logger = logger;
    }

    public boolean subscribe(Object object) {
        return handler.subscribe(object);
    }

    public boolean unsubscribe(Object object) {
        return handler.unsubscribe(object);
    }

    public void post(Event event) {
        handler.post(event, logger);
        eventsPosted.getAndIncrement();
    }

    public long getEventsPosted() {
        return eventsPosted.get();
    }
}