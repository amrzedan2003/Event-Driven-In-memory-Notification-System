package ps.exalt;

import ps.exalt.events.Event;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;
import ps.exalt.events.types.NewTaskEvent;
import ps.exalt.events.types.ReminderEvent;
import ps.exalt.notification.NotificationManager;
import ps.exalt.notification.EventSubscriber;
import ps.exalt.notification.filters.PriorityFilter;
import ps.exalt.notification.filters.WorkingHoursFilter;
import ps.exalt.scheduling.EventScheduler;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting Event-Driven Notification System");
        System.out.println("=========================================");

        // Get singleton notification manager
        NotificationManager notificationManager = NotificationManager.getInstance();

        // Create event scheduler for time-based events
        EventScheduler scheduler = new EventScheduler(notificationManager);

        // Create subscribers with different preferences
        createSubscribers(notificationManager);

        // Task Alerts
        System.out.println("\n Task Alerts");
        System.out.println("----------------------------------------------------");
        publishTaskAlerts(notificationManager);

        // Time-Based Event Trigger
        System.out.println("\n Time-Based Event Triggers");
        System.out.println("----------------------------------------------------");
        publishTimeBasedEvents(scheduler);

        // Personalized Subscriptions
        System.out.println("\n Personalized Subscriptions");
        System.out.println("----------------------------------------------------");
        createPersonalizedSubscriptions(notificationManager);

        // Scheduling events
        System.out.println("\nRunning scheduled events for 15 seconds...");
        try {
            Thread.sleep(15000); // 15 sec
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Event History and Reporting
        System.out.println("\nEvent History and Reporting");
        System.out.println("----------------------------------------------------");
        displayEventHistory(notificationManager);

        // Cleanup
        System.out.println("\nShutting down system...");
        scheduler.stop();
        System.out.println("====================================================");
    }

    /**
     * Create subscribers with different filtering preferences
     */
    private static void createSubscribers(NotificationManager manager) {
        // Amr: Wants all events (no filter)
        EventSubscriber amr = new EventSubscriber("Amr");
        manager.subscribe(amr, Arrays.asList(EventType.TASK, EventType.REMINDER, EventType.SYSTEM_HEARTBEAT));

        // Bilal: Only high-priority events
        EventSubscriber bilal = new EventSubscriber("Bilal");
        PriorityFilter highPriorityFilter = new PriorityFilter(Priority.HIGH);
        manager.subscribe(bilal, highPriorityFilter, Arrays.asList(EventType.TASK, EventType.REMINDER));

        // Menna: Only events during working hours
        EventSubscriber menna = new EventSubscriber("Menna");
        WorkingHoursFilter workingHoursFilter = new WorkingHoursFilter(
                LocalTime.of(10, 30),
                LocalTime.of(20, 45));
        manager.subscribe(menna, workingHoursFilter, Arrays.asList(EventType.TASK));

        // Noor: Only heartbeat events
        EventSubscriber noor = new EventSubscriber("Noor");
        manager.subscribe(noor, Arrays.asList(EventType.SYSTEM_HEARTBEAT));
        System.out.println("Subscribers Created Successfully!!\n");
    }

    /**
     * Simulate task alert events
     */
    private static void publishTaskAlerts(NotificationManager manager) {
        // Create task events with different priorities
        Event taskHighPriority = new NewTaskEvent(
                "Fix Production Bug",
                "Amr",
                "Bug found in authentication",
                Priority.HIGH);

        Event taskMediumPriority = new NewTaskEvent(
                "Update Documentation",
                "Bilal",
                "Update API documentation for new features",
                Priority.MEDIUM);

        Event taskLowPriority = new NewTaskEvent(
                "Code Review",
                "Menna",
                "Review PR for UI improvements",
                Priority.LOW);

        // Publish the events
        manager.publishEvent(taskHighPriority);
        manager.publishEvent(taskMediumPriority);
        manager.publishEvent(taskLowPriority);

        System.out.println("Published 3 task events with different priorities!!");
    }

    /**
     * Simulate time-based scheduled events
     */
    private static void publishTimeBasedEvents(EventScheduler scheduler) {
        scheduler.start();

        // Schedule heartbeat
        scheduler.scheduleHeartbeat("WebServer", 5);
        scheduler.scheduleHeartbeat("Database", 5);

        System.out.println("-----------------------");

        // Schedule reminders
        scheduler.scheduleReminder("Daily Meeting", 10);
        scheduler.scheduleReminder("Check System Health", 8);
    }

    /**
     * Create personalized subscription filtering
     */
    private static void createPersonalizedSubscriptions(NotificationManager manager) {
        Event highPriorityReminder = new ReminderEvent(
                "Server maintenance in 3 hour",
                "All",
                Priority.HIGH);

        System.out.println("Publishing high-priority reminder...");
        manager.publishEvent(highPriorityReminder);

        Event lowPriorityReminder = new ReminderEvent(
                "EXALT Team lunch next Sunday In HQ Building",
                "All",
                Priority.LOW);

        System.out.println("Publishing low-priority reminder...");
        manager.publishEvent(lowPriorityReminder);
    }

    /**
     * Event history and reporting
     */
    private static void displayEventHistory(NotificationManager manager) {
        System.out.println("Event History Analysis:");
        System.out.println("-----------------------");

        // Total events
        System.out.printf("Total events published: %d%n", manager.getTotalEventCount());

        // Events in last hour
        LocalDateTime lastOneHour = LocalDateTime.now().minusHours(1);
        System.out.printf("Events count in last hour: %d%n", manager.getEventsSince(lastOneHour).size());

        // Events grouped by type
        Map<EventType, List<Event>> groupedEvents = manager.getEventsGroupedByType();
        System.out.println("Events count by type:");
        for (EventType type : groupedEvents.keySet()) {
            List<Event> events = groupedEvents.get(type);
            System.out.printf("  %s: %d events%n", type, events.size());
        }

        // Events sorted by priority
        List<Event> sortedEvents = manager.getEventsSortedByPriority();
        System.out.println("Sorted events by priority:");
        sortedEvents.stream()
                .forEach(event -> System.out.printf("  %s - %s%n",
                        event.getPriority(),
                        event.getEventType()));

        // Subscriber statistics
        System.out.println("Subscriber counts by event type:");
        for (EventType type : EventType.values()) {
            System.out.printf("  %s: %d subscribers%n", type, manager.getSubscriberCount(type));
        }
    }
}
