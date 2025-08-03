package ps.exalt.scheduling.eventTypes;

import ps.exalt.events.Event;
import ps.exalt.events.types.HeartbeatEvent;
import ps.exalt.notification.NotificationManager;
import ps.exalt.scheduling.ScheduledEvent;

/**
 * Concrete command for heartbeat events
 * Implements Command pattern for scheduled heartbeat publishing
 */
public class HeartbeatType implements ScheduledEvent {
    private final String systemComponent;
    private final long intervalSeconds;
    private final NotificationManager notificationManager;

    public HeartbeatType(String systemComponent, long intervalSeconds, NotificationManager notificationManager) {
        this.systemComponent = systemComponent;
        this.intervalSeconds = intervalSeconds;
        this.notificationManager = notificationManager;
    }

    @Override
    public void execute() {
        Event heartbeat = new HeartbeatEvent(systemComponent, intervalSeconds * 1000);
        notificationManager.publishEvent(heartbeat);
    }

    @Override
    public Event getEvent() {
        return new HeartbeatEvent(systemComponent, intervalSeconds * 1000);
    }

    @Override
    public String getDescription() {
        return "Heartbeat from " + systemComponent + " every " + intervalSeconds + " seconds";
    }
}
