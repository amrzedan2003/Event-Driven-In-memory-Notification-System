package ps.exalt.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ps.exalt.events.Event;
import ps.exalt.events.types.HeartbeatEvent;
import ps.exalt.events.types.NewTaskEvent;
import ps.exalt.events.enums.Priority;
import ps.exalt.notification.filters.EventFilter;
import ps.exalt.notification.filters.PriorityFilter;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SubscriberInfo Tests")
class SubscriberInfoTest {

    private EventSubscriber testSubscriber;
    private EventFilter testFilter;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        testSubscriber = new EventSubscriber("TestUser");
        testFilter = new PriorityFilter(Priority.MEDIUM);
        testEvent = new HeartbeatEvent("TestService", 1000L);
    }

    @Test
    @DisplayName("Should create SubscriberInfo with subscriber only")
    void shouldCreateSubscriberInfoWithSubscriberOnly() {
        SubscriberInfo subscriberInfo = new SubscriberInfo(testSubscriber);

        assertEquals(testSubscriber, subscriberInfo.getSubscriber());
        assertNull(subscriberInfo.getFilter());
    }

    @Test
    @DisplayName("Should create SubscriberInfo with subscriber and filter")
    void shouldCreateSubscriberInfoWithSubscriberAndFilter() {
        SubscriberInfo subscriberInfo = new SubscriberInfo(testSubscriber, testFilter);

        assertEquals(testSubscriber, subscriberInfo.getSubscriber());
        assertEquals(testFilter, subscriberInfo.getFilter());
    }

    @Test
    @DisplayName("Should handle null subscriber")
    void shouldHandleNullSubscriber() {
        SubscriberInfo subscriberInfo = new SubscriberInfo(null);

        assertNull(subscriberInfo.getSubscriber());
        assertNull(subscriberInfo.getFilter());
    }

    @Test
    @DisplayName("Should handle null subscriber with filter")
    void shouldHandleNullSubscriberWithFilter() {
        SubscriberInfo subscriberInfo = new SubscriberInfo(null, testFilter);

        assertNull(subscriberInfo.getSubscriber());
        assertEquals(testFilter, subscriberInfo.getFilter());
    }

    @Test
    @DisplayName("Should handle null filter")
    void shouldHandleNullFilter() {
        SubscriberInfo subscriberInfo = new SubscriberInfo(testSubscriber, null);

        assertEquals(testSubscriber, subscriberInfo.getSubscriber());
        assertNull(subscriberInfo.getFilter());
    }

    @Test
    @DisplayName("Should allow event when no filter exists")
    void shouldAllowEventWhenNoFilterIsPresent() {
        SubscriberInfo subscriberInfo = new SubscriberInfo(testSubscriber);

        assertTrue(subscriberInfo.allows(testEvent));
    }

    @Test
    @DisplayName("Should use filter when exist and filter allows event")
    void shouldUseFilterWhenPresentAndFilterAllowsEvent() {
        // Create a high priority event that should pass MEDIUM filter
        Event highPriorityEvent = new NewTaskEvent("Task", "User", "Description", Priority.HIGH);

        SubscriberInfo subscriberInfo = new SubscriberInfo(testSubscriber, testFilter);

        assertTrue(subscriberInfo.allows(highPriorityEvent));
    }

    @Test
    @DisplayName("Should use filter when present and filter blocks event")
    void shouldUseFilterWhenPresentAndFilterBlocksEvent() {
        // Create a low priority event that should NOT pass MEDIUM filter
        Event lowPriorityEvent = new NewTaskEvent("Task", "User", "Description", Priority.LOW);

        SubscriberInfo subscriberInfo = new SubscriberInfo(testSubscriber, testFilter);

        assertFalse(subscriberInfo.allows(lowPriorityEvent));
    }

    @Test
    @DisplayName("Should handle null event with no filter")
    void shouldHandleNullEventWithNoFilter() {
        SubscriberInfo subscriberInfo = new SubscriberInfo(testSubscriber);

        assertTrue(subscriberInfo.allows(null));
    }

    @Test
    @DisplayName("Should work with different filter types")
    void shouldWorkWithDifferentFilterTypes() {
        PriorityFilter priorityFilter = new PriorityFilter(Priority.HIGH);
        SubscriberInfo subscriberInfo = new SubscriberInfo(testSubscriber, priorityFilter);

        Event highPriorityEvent = new NewTaskEvent("Task", "User", "Description", Priority.HIGH);
        Event lowPriorityEvent = new NewTaskEvent("Task", "User", "Description", Priority.LOW);

        assertTrue(subscriberInfo.allows(highPriorityEvent));
        assertFalse(subscriberInfo.allows(lowPriorityEvent));
    }
}
