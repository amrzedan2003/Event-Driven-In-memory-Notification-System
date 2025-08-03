package ps.exalt.notification;

import ps.exalt.events.Event;
import ps.exalt.events.enums.EventType;
import ps.exalt.notification.filters.EventFilter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton NotificationManager that implements the Observer pattern
 */
public class NotificationManager implements EventPublisher {
    private static NotificationManager instance;

    private final Map<EventType, List<SubscriberInfo>> subscribers;
    private final List<Event> eventHistory;

    private NotificationManager() {
        this.subscribers = new HashMap<>();
        this.eventHistory = new ArrayList<>();
    }

    /**
     * Gets the singleton instance
     * 
     * @return The single NotificationManager instance
     */
    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    @Override
    public void subscribe(EventSubscriber subscriber, List<EventType> eventTypes) {
        for (EventType eventType : eventTypes) {
            List<SubscriberInfo> subscriberList = subscribers.get(eventType);
            if (subscriberList == null) {
                subscriberList = new ArrayList<>();
                subscribers.put(eventType, subscriberList);
            }
            subscriberList.add(new SubscriberInfo(subscriber));
            System.out.printf("Subscriber '%s' subscribed to %s events%n",
                    subscriber.getSubscriberName(), eventType);
        }
    }

    /**
     * Subscribe with custom filters
     * 
     * @param subscriber The subscriber
     * @param filter     The filter to apply
     * @param eventTypes The event types to subscribe to
     */
    public void subscribe(EventSubscriber subscriber, EventFilter filter, List<EventType> eventTypes) {
        for (EventType eventType : eventTypes) {
            List<SubscriberInfo> subscriberList = subscribers.get(eventType);
            if (subscriberList == null) {
                subscriberList = new ArrayList<>();
                subscribers.put(eventType, subscriberList);
            }
            subscriberList.add(new SubscriberInfo(subscriber, filter));
            System.out.printf("Subscriber '%s' subscribed to %s events with filter: %s%n",
                    subscriber.getSubscriberName(), eventType, filter.getInfo());
        }
    }

    @Override
    public void unsubscribe(EventSubscriber subscriber, List<EventType> eventTypes) {
        for (EventType eventType : eventTypes) {
            List<SubscriberInfo> subscriberList = subscribers.get(eventType);
            if (subscriberList != null) {
                subscriberList.removeIf(info -> info.getSubscriber().getSubscriberId()
                        .equals(subscriber.getSubscriberId()));
                System.out.printf("Subscriber '%s' unsubscribed from %s events%n",
                        subscriber.getSubscriberName(), eventType);
            }
        }
    }

    @Override
    public void unsubscribeAll(EventSubscriber subscriber) {
        subscribers.values()
                .forEach(subscriberList -> subscriberList.removeIf(info -> info.getSubscriber().getSubscriberId()
                        .equals(subscriber.getSubscriberId())));
        System.out.printf("Subscriber '%s' unsubscribed from all events%n",
                subscriber.getSubscriberName());
    }

    @Override
    public void publishEvent(Event event) {
        // Add to history
        eventHistory.add(event);

        // Get subscribers for this event type
        List<SubscriberInfo> eventSubscribers = subscribers.get(event.getEventType());
        if (eventSubscribers != null) {
            int notifiedCount = 0;
            for (SubscriberInfo subscriberInfo : eventSubscribers) {
                if (subscriberInfo.allows(event)) {
                    try {
                        subscriberInfo.getSubscriber().onEvent(event);
                        notifiedCount++;
                    } catch (Exception e) {
                        System.err.printf("Error notifying subscriber %s: %s%n",
                                subscriberInfo.getSubscriber().getSubscriberName(), e.getMessage());
                    }
                }
            }
            System.out.printf("Event published: %s - Notified %d subscribers%n",
                    event.getEventType(), notifiedCount);
        } else {
            System.out.printf("Event published: %s - No subscribers%n", event.getEventType());
        }
        System.out.println("====================================================================");
    }

    /**
     * Get events from history
     * 
     * @param since Events since this time
     * @return List of events since the specified time
     */
    public List<Event> getEventsSince(LocalDateTime since) {
        return eventHistory.stream()
                .filter(event -> event.getTimestamp().isAfter(since))
                .collect(Collectors.toList());
    }

    /**
     * Get events grouped by type
     * 
     * @return Map of event types to their events
     */
    public Map<EventType, List<Event>> getEventsGroupedByType() {
        return eventHistory.stream()
                .collect(Collectors.groupingBy(Event::getEventType));
    }

    /**
     * Get sorted events by priority and timestamp
     * 
     * @return List of events sorted by priority (descending) then timestamp
     *         (ascending)
     */
    public List<Event> getEventsSortedByPriority() {
        return eventHistory.stream()
                .sorted(Comparator.comparing(Event::getPriority,
                        Comparator.comparing(priority -> -priority.getLevel()))
                        .thenComparing(Event::getTimestamp))
                .collect(Collectors.toList());
    }

    /**
     * Get total event count
     * 
     * @return Total number of events in history
     */
    public long getTotalEventCount() {
        return eventHistory.size();
    }

    /**
     * Get subscriber count for a specific event type
     * 
     * @param eventType The event type
     * @return Number of subscribers for that event type
     */
    public int getSubscriberCount(EventType eventType) {
        List<SubscriberInfo> subscriberList = subscribers.get(eventType);
        return subscriberList != null ? subscriberList.size() : 0;
    }
}
