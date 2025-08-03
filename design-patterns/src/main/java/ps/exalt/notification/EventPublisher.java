package ps.exalt.notification;

import ps.exalt.events.Event;
import ps.exalt.events.enums.EventType;
import java.util.List;

/**
 * Publisher interface for the Observer pattern
 */
public interface EventPublisher {
    /**
     * Subscribe to specific event types
     * 
     * @param subscriber The subscriber to add
     * @param eventTypes The event types to subscribe
     */
    void subscribe(EventSubscriber subscriber, List<EventType> eventTypes);

    /**
     * Unsubscribe from specific event types
     * 
     * @param subscriber The subscriber to remove
     * @param eventTypes The event types to unsubscribe
     */
    void unsubscribe(EventSubscriber subscriber, List<EventType> eventTypes);

    /**
     * Unsubscribe from all event types
     * 
     * @param subscriber The subscriber to remove completely
     */
    void unsubscribeAll(EventSubscriber subscriber);

    /**
     * Publish an event to all subscribers
     * 
     * @param event The event to publish
     */
    void publishEvent(Event event);
}
