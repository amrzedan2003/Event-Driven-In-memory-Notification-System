package ps.exalt.notification;

import ps.exalt.events.Event;
import ps.exalt.notification.filters.EventFilter;

/**
 * This class to hold subscriber information with their filter
 */
public class SubscriberInfo {
    private final EventSubscriber subscriber;
    private final EventFilter filter;

    public SubscriberInfo(EventSubscriber subscriber) {
        this(subscriber, null);
    }

    public SubscriberInfo(EventSubscriber subscriber, EventFilter filter) {
        this.subscriber = subscriber;
        this.filter = filter;
    }

    public EventSubscriber getSubscriber() {
        return subscriber;
    }

    public EventFilter getFilter() {
        return filter;
    }

    public boolean allows(Event event) {
        return filter == null || filter.allows(event);
    }
}
