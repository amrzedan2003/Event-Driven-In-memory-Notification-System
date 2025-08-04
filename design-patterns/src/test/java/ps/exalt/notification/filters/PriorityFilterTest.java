package ps.exalt.notification.filters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ps.exalt.events.Event;
import ps.exalt.events.types.*;
import ps.exalt.events.enums.Priority;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PriorityFilter Tests")
class PriorityFilterTest {

    private PriorityFilter lowFilter;
    private PriorityFilter mediumFilter;
    private PriorityFilter highFilter;

    @BeforeEach
    void setUp() {
        lowFilter = new PriorityFilter(Priority.LOW);
        mediumFilter = new PriorityFilter(Priority.MEDIUM);
        highFilter = new PriorityFilter(Priority.HIGH);
    }

    @Test
    @DisplayName("Should create filter with correct minimum priority")
    void shouldCreateFilterWithCorrectMinimumPriority() {
        assertEquals(Priority.LOW, lowFilter.getMinimumPriority());
        assertEquals(Priority.MEDIUM, mediumFilter.getMinimumPriority());
        assertEquals(Priority.HIGH, highFilter.getMinimumPriority());
    }

    @Test
    @DisplayName("LOW filter should allow all priority events")
    void lowFilterShouldAllowAllPriorityEvents() {
        Event lowEvent = new NewTaskEvent("Task", "User", "Description", Priority.LOW);
        Event mediumEvent = new NewTaskEvent("Task", "User", "Description", Priority.MEDIUM);
        Event highEvent = new NewTaskEvent("Task", "User", "Description", Priority.HIGH);

        assertTrue(lowFilter.allows(lowEvent));
        assertTrue(lowFilter.allows(mediumEvent));
        assertTrue(lowFilter.allows(highEvent));
    }

    @Test
    @DisplayName("MEDIUM filter should allow MEDIUM and HIGH priority events")
    void mediumFilterShouldAllowMediumAndHighPriorityEvents() {
        Event lowEvent = new NewTaskEvent("Task", "User", "Description", Priority.LOW);
        Event mediumEvent = new NewTaskEvent("Task", "User", "Description", Priority.MEDIUM);
        Event highEvent = new NewTaskEvent("Task", "User", "Description", Priority.HIGH);

        assertFalse(mediumFilter.allows(lowEvent));
        assertTrue(mediumFilter.allows(mediumEvent));
        assertTrue(mediumFilter.allows(highEvent));
    }

    @Test
    @DisplayName("HIGH filter should only allow HIGH priority events")
    void highFilterShouldOnlyAllowHighPriorityEvents() {
        Event lowEvent = new NewTaskEvent("Task", "User", "Description", Priority.LOW);
        Event mediumEvent = new NewTaskEvent("Task", "User", "Description", Priority.MEDIUM);
        Event highEvent = new NewTaskEvent("Task", "User", "Description", Priority.HIGH);

        assertFalse(highFilter.allows(lowEvent));
        assertFalse(highFilter.allows(mediumEvent));
        assertTrue(highFilter.allows(highEvent));
    }

    @Test
    @DisplayName("Should work with different event types")
    void shouldWorkWithDifferentEventTypes() {
        NewTaskEvent taskEvent = new NewTaskEvent("Task", "User", "Description", Priority.HIGH);
        ReminderEvent reminderEvent = new ReminderEvent("Reminder", "User", Priority.HIGH);
        HeartbeatEvent heartbeatEvent = new HeartbeatEvent("Service", 1000L); // Always LOW priority

        assertTrue(highFilter.allows(taskEvent));
        assertTrue(highFilter.allows(reminderEvent));
        assertFalse(highFilter.allows(heartbeatEvent)); // HeartbeatEvent is always LOW priority
    }

    @Test
    @DisplayName("Should work correctly with edge case priorities")
    void shouldWorkCorrectlyWithEdgeCasePriorities() {
        Event mediumEvent = new NewTaskEvent("Task", "User", "Description", Priority.MEDIUM);

        // Event with MEDIUM priority should pass MEDIUM filter
        assertTrue(mediumFilter.allows(mediumEvent));

        // Event with MEDIUM priority should NOT pass HIGH filter
        assertFalse(highFilter.allows(mediumEvent));

        // Event with MEDIUM priority should pass LOW filter
        assertTrue(lowFilter.allows(mediumEvent));
    }

    @Test
    @DisplayName("Should handle events with same priority level correctly")
    void shouldHandleEventsWithSamePriorityLevelCorrectly() {
        Event event1 = new NewTaskEvent("Task1", "User1", "Description1", Priority.MEDIUM);
        Event event2 = new NewTaskEvent("Task2", "User2", "Description2", Priority.MEDIUM);
        Event event3 = new ReminderEvent("Reminder", "User3", Priority.MEDIUM);

        assertTrue(mediumFilter.allows(event1));
        assertTrue(mediumFilter.allows(event2));
        assertTrue(mediumFilter.allows(event3));
    }
}
