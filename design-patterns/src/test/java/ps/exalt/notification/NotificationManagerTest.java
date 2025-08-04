package ps.exalt.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ps.exalt.events.types.*;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

@DisplayName("NotificationManager Tests")
class NotificationManagerTest {

    private NotificationManager notificationManager;
    private EventSubscriber subscriber1;
    private EventSubscriber subscriber2;

    @BeforeEach
    void setUp() {
        notificationManager = NotificationManager.getInstance();
        notificationManager.clearSubscribers();
    }

    @Test
    @DisplayName("Should return same singleton instance")
    void shouldReturnSameSingletonInstance() {
        NotificationManager instance1 = NotificationManager.getInstance();
        NotificationManager instance2 = NotificationManager.getInstance();

        assertSame(instance1, instance2);
    }

    @Test
    @DisplayName("Should subscribe and unsubscribe correctly")
    void shouldSubscribeAndUnsubscribeCorrectly() {
        subscriber1 = new EventSubscriber("User1");
        subscriber2 = new EventSubscriber("User2");
        notificationManager.subscribe(subscriber1, Arrays.asList(EventType.TASK));

        assertEquals(1, notificationManager.getSubscriberCount(EventType.TASK));

        notificationManager.unsubscribe(subscriber1, Arrays.asList(EventType.TASK));

        assertEquals(0, notificationManager.getSubscriberCount(EventType.TASK));
    }

    @Test
    @DisplayName("Should publish events to subscribers")
    void shouldPublishEventsToSubscribers() {
        subscriber1 = new EventSubscriber("User1");
        subscriber2 = new EventSubscriber("User2");

        notificationManager.subscribe(subscriber1, Arrays.asList(EventType.TASK));
        notificationManager.subscribe(subscriber2, Arrays.asList(EventType.REMINDER));

        NewTaskEvent taskEvent = new NewTaskEvent("Task1", "User", "Description", Priority.HIGH);
        ReminderEvent reminderEvent = new ReminderEvent("Reminder1", "User", Priority.MEDIUM);

        notificationManager.publishEvent(taskEvent);
        notificationManager.publishEvent(reminderEvent);

        assertEquals(1, subscriber1.getReceivedEventCount());
        assertEquals(1, subscriber2.getReceivedEventCount());
    }
}
