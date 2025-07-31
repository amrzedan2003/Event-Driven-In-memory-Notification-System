package ps.exalt.notification.filters;

import ps.exalt.events.Event;

/**
 * Implements Strategy pattern for different filtering criteria
 */
public interface EventFilter {
    /**
     * Determines if an event should be delivered to a subscriber
     * @param event The event to check
     * @return true if the event should be delivered, false o.w
     */
    boolean allows(Event event);

    /**
     * Gets a information of this filter
     * @return Filter description
     */
    String getInfo();
}
