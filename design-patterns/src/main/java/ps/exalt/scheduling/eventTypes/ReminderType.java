package ps.exalt.scheduling.eventTypes;

import ps.exalt.events.Event;
import ps.exalt.events.types.ReminderEvent;
import ps.exalt.events.enums.Priority;
import ps.exalt.notification.NotificationManager;
import ps.exalt.scheduling.ScheduledEvent;

/**
 * Concrete command for reminder events
 * Implements Command pattern for scheduled reminder publishing
 */
public class ReminderType implements ScheduledEvent {
    private final String reminderText;
    private final long intervalSeconds;
    private final NotificationManager notificationManager;

    public ReminderType(String reminderText, long intervalSeconds, NotificationManager notificationManager) {
        this.reminderText = reminderText;
        this.intervalSeconds = intervalSeconds;
        this.notificationManager = notificationManager;
    }

    @Override
    public void execute() {
        Event reminder = new ReminderEvent(reminderText, "SYSTEM", Priority.MEDIUM);
        notificationManager.publishEvent(reminder);
    }

    @Override
    public Event getEvent() {
        return new ReminderEvent(reminderText, "SYSTEM", Priority.MEDIUM);
    }

    @Override
    public String getDescription() {
        return "Reminder '" + reminderText + "' every " + intervalSeconds + " seconds";
    }
}
