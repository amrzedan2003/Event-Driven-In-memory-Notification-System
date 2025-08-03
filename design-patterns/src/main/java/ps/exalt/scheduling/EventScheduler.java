package ps.exalt.scheduling;

import ps.exalt.notification.NotificationManager;
import ps.exalt.scheduling.eventTypes.HeartbeatType;
import ps.exalt.scheduling.eventTypes.ReminderType;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

/**
 * Event Scheduler that handles time-based event triggering
 */
public class EventScheduler {
    private final ScheduledExecutorService scheduler;
    private final NotificationManager notificationManager;

    public EventScheduler(NotificationManager notificationManager) {
        this.scheduler = Executors.newScheduledThreadPool(5);
        this.notificationManager = notificationManager;
    }

    /**
     * Schedule a heartbeat event to run at fixed intervals
     * 
     * @param systemComponent The system component name
     * @param intervalSeconds Interval in seconds
     * @return Task ID for managing the scheduled task
     */
    public String scheduleHeartbeat(String systemComponent, long intervalSeconds) {
        String eventId = UUID.randomUUID().toString();

        HeartbeatType heartbeatCommand = new HeartbeatType(systemComponent, intervalSeconds, notificationManager);

        scheduler.scheduleAtFixedRate(
                heartbeatCommand::execute,
                0,
                intervalSeconds,
                TimeUnit.SECONDS);

        System.out.printf("Scheduled heartbeat: %s (Task ID: %s)%n", heartbeatCommand.getDescription(), eventId);

        return eventId;
    }

    /**
     * Schedule a reminder event to run at fixed intervals
     * 
     * @param reminderText    The reminder message
     * @param intervalSeconds Interval in seconds
     * @return Task ID for managing the scheduled task
     */
    public String scheduleReminder(String reminderText, long intervalSeconds) {
        String eventId = UUID.randomUUID().toString();

        ReminderType reminderCommand = new ReminderType(reminderText, intervalSeconds, notificationManager);

        scheduler.scheduleAtFixedRate(
                reminderCommand::execute,
                intervalSeconds,
                intervalSeconds,
                TimeUnit.SECONDS);

        System.out.printf("Scheduled reminder: %s (Task ID: %s)%n", reminderCommand.getDescription(), eventId);

        return eventId;
    }

    public void start() {
        System.out.println("Event Scheduler started");
    }

    /**
     * Stop all scheduled tasks
     */
    public void stop() {
        // Shutdown the scheduler
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Event Scheduler stopped");
    }
}
