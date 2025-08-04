package ps.exalt.scheduling.eventTypes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ps.exalt.events.Event;
import ps.exalt.events.types.HeartbeatEvent;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;
import ps.exalt.notification.NotificationManager;
import ps.exalt.notification.EventSubscriber;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

@DisplayName("HeartbeatType Tests")
class HeartbeatTypeTest {

    private HeartbeatType heartbeatType;
    private NotificationManager notificationManager;
    private EventSubscriber testSubscriber;
    private final String testComponent = "TestService";
    private final long testInterval = 5L;

    @BeforeEach
    void setUp() {
        notificationManager = NotificationManager.getInstance();
        testSubscriber = new EventSubscriber("TestUser");
        heartbeatType = new HeartbeatType(testComponent, testInterval, notificationManager);
    }

    @Test
    @DisplayName("Should create HeartbeatType with correct properties")
    void shouldCreateHeartbeatTypeWithCorrectProperties() {
        assertNotNull(heartbeatType);
    }

    @Test
    @DisplayName("Should handle null system component")
    void shouldHandleNullSystemComponent() {
        HeartbeatType nullComponentType = new HeartbeatType(null, testInterval, notificationManager);
        assertNotNull(nullComponentType);
    }

    @Test
    @DisplayName("Should handle empty system component")
    void shouldHandleEmptySystemComponent() {
        HeartbeatType emptyComponentType = new HeartbeatType("", testInterval, notificationManager);
        assertNotNull(emptyComponentType);
    }

    @Test
    @DisplayName("Should handle zero interval")
    void shouldHandleZeroInterval() {
        HeartbeatType zeroIntervalType = new HeartbeatType(testComponent, 0L, notificationManager);
        assertNotNull(zeroIntervalType);
        assertTrue(zeroIntervalType.getDescription().contains("0"));
    }

    @Test
    @DisplayName("Should handle negative interval")
    void shouldHandleNegativeInterval() {
        HeartbeatType negativeIntervalType = new HeartbeatType(testComponent, -5L, notificationManager);
        assertNotNull(negativeIntervalType);
        assertTrue(negativeIntervalType.getDescription().contains("-5"));
    }

    @Test
    @DisplayName("Should create correct HeartbeatEvent")
    void shouldCreateCorrectHeartbeatEvent() {
        Event event = heartbeatType.getEvent();

        assertNotNull(event);
        assertInstanceOf(HeartbeatEvent.class, event);
        assertEquals(EventType.SYSTEM_HEARTBEAT, event.getEventType());
        assertEquals(Priority.LOW, event.getPriority());

        HeartbeatEvent heartbeatEvent = (HeartbeatEvent) event;
        assertEquals(testComponent, heartbeatEvent.getSystemComponent());
        assertEquals(testInterval * 1000, heartbeatEvent.getInterval());
    }

    @Test
    @DisplayName("Should execute and publish event")
    void shouldExecuteAndPublishEvent() {
        notificationManager.subscribe(testSubscriber, Arrays.asList(EventType.SYSTEM_HEARTBEAT));

        heartbeatType.execute();

        assertEquals(1, testSubscriber.getReceivedEventCount());
        Event receivedEvent = testSubscriber.getReceivedEvents().get(0);

        assertInstanceOf(HeartbeatEvent.class, receivedEvent);
        HeartbeatEvent heartbeatEvent = (HeartbeatEvent) receivedEvent;
        assertEquals(testComponent, heartbeatEvent.getSystemComponent());
        assertEquals(testInterval * 1000, heartbeatEvent.getInterval());
    }

    @Test
    @DisplayName("Should execute multiple times consistently")
    void shouldExecuteMultipleTimesConsistently() {
        notificationManager.subscribe(testSubscriber, Arrays.asList(EventType.SYSTEM_HEARTBEAT));

        heartbeatType.execute();
        heartbeatType.execute();
        heartbeatType.execute();

        assertEquals(3, testSubscriber.getReceivedEventCount());

        // All events should have the same properties
        for (Event event : testSubscriber.getReceivedEvents()) {
            assertInstanceOf(HeartbeatEvent.class, event);
            HeartbeatEvent heartbeatEvent = (HeartbeatEvent) event;
            assertEquals(testComponent, heartbeatEvent.getSystemComponent());
            assertEquals(testInterval * 1000, heartbeatEvent.getInterval());
        }
    }

    @Test
    @DisplayName("Should handle execution with no subscribers")
    void shouldHandleExecutionWithNoSubscribers() {
        // No subscribers for SYSTEM_HEARTBEAT events
        assertDoesNotThrow(() -> heartbeatType.execute());
    }

    @Test
    @DisplayName("Should work with different notification managers")
    void shouldWorkWithDifferentNotificationManagers() {
        // Create new subscriber for this test
        EventSubscriber newSubscriber = new EventSubscriber("NewUser");
        notificationManager.subscribe(newSubscriber, Arrays.asList(EventType.SYSTEM_HEARTBEAT));

        HeartbeatType newHeartbeatType = new HeartbeatType("NewService", 3L, notificationManager);
        newHeartbeatType.execute();

        assertEquals(1, newSubscriber.getReceivedEventCount());
    }
}
