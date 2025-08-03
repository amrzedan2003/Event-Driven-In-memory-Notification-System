package ps.exalt.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ps.exalt.events.Event;
import ps.exalt.events.types.*;
import ps.exalt.events.enums.Priority;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@DisplayName("EventSubscriber Tests")
class EventSubscriberTest {

    private EventSubscriber subscriber;

    @BeforeEach
    void setUp() {
        subscriber = new EventSubscriber("TestUser");
    }

    @Test
    @DisplayName("Should create subscriber with correct properties")
    void shouldCreateSubscriberWithCorrectProperties() {
        assertEquals("TestUser", subscriber.getSubscriberName());
        assertNotNull(subscriber.getSubscriberId());
        assertEquals(0, subscriber.getReceivedEventCount());
        assertTrue(subscriber.getReceivedEvents().isEmpty());
    }

    @Test
    @DisplayName("Should generate unique IDs for different subscribers")
    void shouldGenerateUniqueIdsForDifferentSubscribers() {
        EventSubscriber subscriber1 = new EventSubscriber("User1");
        EventSubscriber subscriber2 = new EventSubscriber("User2");

        assertNotEquals(subscriber1.getSubscriberId(), subscriber2.getSubscriberId());
    }

    @Test
    @DisplayName("Should receive and store events correctly")
    void shouldReceiveAndStoreEventsCorrectly() {
        HeartbeatEvent heartbeatEvent = new HeartbeatEvent("TestService", 1000L);
        NewTaskEvent taskEvent = new NewTaskEvent("Task1", "User1", "Description", Priority.HIGH);

        subscriber.onEvent(heartbeatEvent);
        subscriber.onEvent(taskEvent);

        assertEquals(2, subscriber.getReceivedEventCount());
        List<Event> receivedEvents = subscriber.getReceivedEvents();
        assertEquals(heartbeatEvent, receivedEvents.get(0));
        assertEquals(taskEvent, receivedEvents.get(1));
    }

    @Test
    @DisplayName("Should clear received events correctly")
    void shouldClearReceivedEventsCorrectly() {
        HeartbeatEvent event = new HeartbeatEvent("Service", 1000L);

        subscriber.onEvent(event);
        assertEquals(1, subscriber.getReceivedEventCount());

        subscriber.clearReceivedEvents();

        assertEquals(0, subscriber.getReceivedEventCount());
        assertTrue(subscriber.getReceivedEvents().isEmpty());
    }
}
