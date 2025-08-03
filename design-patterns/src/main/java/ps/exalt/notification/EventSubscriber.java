package ps.exalt.notification;

import ps.exalt.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user that can receive notifications
 */
public class EventSubscriber {
    private final String subscriberId;
    private final String subscriberName;
    private final List<Event> receivedEvents;

    public EventSubscriber(String subscriberName) {
        this.subscriberId = UUID.randomUUID().toString();
        this.subscriberName = subscriberName;
        this.receivedEvents = new ArrayList<>();
    }

    public void onEvent(Event event) {
        receivedEvents.add(event);
        System.out.printf("[%s] Received event: %s at %s%n",
                subscriberName,
                event.getEventType(),
                event.getTimestamp());
        System.out.printf("Event details: %s%n", event.getDescription());
        System.out.println("---");
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    /**
     * Get all events received by this subscriber
     * @return List of received events
     */
    public List<Event> getReceivedEvents() {
        return new ArrayList<>(receivedEvents);
    }

    /**
     * Get count of received events
     * @return Number of events received
     */
    public int getReceivedEventCount() {
        return receivedEvents.size();
    }

    /**
     * Clear the received events history
     */
    public void clearReceivedEvents() {
        receivedEvents.clear();
    }
}
