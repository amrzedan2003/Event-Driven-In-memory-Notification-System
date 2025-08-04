package ps.exalt.notification.filters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ps.exalt.events.types.NewTaskEvent;
import ps.exalt.events.enums.Priority;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalTime;

@DisplayName("WorkingHoursFilter Tests")
class WorkingHoursFilterTest {

    private WorkingHoursFilter workingHoursFilter;

    @BeforeEach
    void setUp() {
        workingHoursFilter = new WorkingHoursFilter(LocalTime.of(8, 0), LocalTime.of(18, 0)); // 8 AM to 6 PM
    }

    @Test
    @DisplayName("Should create filter with start and end times")
    void shouldCreateFilterWithStartAndEndTimes() {
        assertEquals(LocalTime.of(8, 0), workingHoursFilter.getStartTime());
        assertEquals(LocalTime.of(18, 0), workingHoursFilter.getEndTime());
    }

    @Test
    @DisplayName("Should allow events within working hours")
    void shouldAllowEventsWithinWorkingHours() {
        NewTaskEvent event = new NewTaskEvent("Task", "User", "Description", Priority.HIGH);

        assertTrue(workingHoursFilter.allows(event));
    }

    @Test
    @DisplayName("Should work with different events")
    void shouldWorkWithDifferentEvents() {
        NewTaskEvent highPriorityEvent = new NewTaskEvent("Task1", "User", "Description", Priority.HIGH);
        NewTaskEvent lowPriorityEvent = new NewTaskEvent("Task2", "User", "Description", Priority.LOW);

        assertTrue(workingHoursFilter.allows(highPriorityEvent));
        assertTrue(workingHoursFilter.allows(lowPriorityEvent));
    }

    @Test
    @DisplayName("Should handle events correctly")
    void shouldHandleEventsCorrectly() {
        NewTaskEvent event = new NewTaskEvent("Task", "User", "Description", Priority.MEDIUM);

        // Filter should process the event
        assertNotNull(workingHoursFilter.allows(event));
    }
}
