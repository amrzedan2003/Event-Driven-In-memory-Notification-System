package ps.exalt.scheduling;

import ps.exalt.events.Event;
import ps.exalt.notification.NotificationManager;

/**
 * Command interface for scheduled event publishing
 * Concrete implementation of ScheduledEvent
 */
public class PublishEvent implements ScheduledEvent {
    private final Event event;
    private final NotificationManager notificationManager;

    public PublishEvent(Event event, NotificationManager notificationManager) {
        this.event = event;
        this.notificationManager = notificationManager;
    }

    @Override
    public void execute() {
        notificationManager.publishEvent(event);
    }

    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public String getDescription() {
        return "Publish " + event.getEventType() + " event: " + event.getDescription();
    }
}
