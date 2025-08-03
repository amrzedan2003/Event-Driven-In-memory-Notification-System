package ps.exalt.notification.filters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ps.exalt.events.Event;
import ps.exalt.events.enums.Priority;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalTime;
import java.time.LocalDateTime;

@DisplayName("WorkingHoursFilter Tests")
class WorkingHoursFilterTest {

    private WorkingHoursFilter standardHoursFilter;
    private WorkingHoursFilter earlyHoursFilter;
    private WorkingHoursFilter lateHoursFilter;

    @BeforeEach
    void setUp() {
        standardHoursFilter = new WorkingHoursFilter(LocalTime.of(9, 0), LocalTime.of(17, 0)); // 9 AM to 5 PM
        earlyHoursFilter = new WorkingHoursFilter(LocalTime.of(6, 0), LocalTime.of(10, 0)); // 6 AM to 10 AM
        lateHoursFilter = new WorkingHoursFilter(LocalTime.of(20, 0), LocalTime.of(23, 59)); // 8 PM to 11:59 PM
    }

    @Test
    @DisplayName("Should create filter with correct start and end times")
    void shouldCreateFilterWithCorrectStartAndEndTimes() {
        assertEquals(LocalTime.of(9, 0), standardHoursFilter.getStartTime());
        assertEquals(LocalTime.of(17, 0), standardHoursFilter.getEndTime());

        assertEquals(LocalTime.of(6, 0), earlyHoursFilter.getStartTime());
        assertEquals(LocalTime.of(10, 0), earlyHoursFilter.getEndTime());
    }

    @Test
    @DisplayName("Should allow events within working hours")
    void shouldAllowEventsWithinWorkingHours() {
        // Create events at different times within working hours (9 AM - 5 PM)
        TestEvent morningEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 10, 30)); // 10:30 AM
        TestEvent noonEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 12, 0)); // 12:00 PM
        TestEvent afternoonEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 16, 45)); // 4:45 PM
        TestEvent startTimeEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 9, 0)); // 9:00 AM exact
        TestEvent endTimeEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 17, 0)); // 5:00 PM exact

        assertTrue(standardHoursFilter.allows(morningEvent));
        assertTrue(standardHoursFilter.allows(noonEvent));
        assertTrue(standardHoursFilter.allows(afternoonEvent));
        assertTrue(standardHoursFilter.allows(startTimeEvent));
        assertTrue(standardHoursFilter.allows(endTimeEvent));
    }

    @Test
    @DisplayName("Should block events outside working hours")
    void shouldBlockEventsOutsideWorkingHours() {
        // Create events outside working hours (9 AM - 5 PM)
        TestEvent earlyMorningEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 8, 59)); // 8:59 AM
        TestEvent lateEveningEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 17, 1)); // 5:01 PM
        TestEvent midnightEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 0, 0)); // 12:00 AM
        TestEvent veryLateEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 23, 30)); // 11:30 PM

        assertFalse(standardHoursFilter.allows(earlyMorningEvent));
        assertFalse(standardHoursFilter.allows(lateEveningEvent));
        assertFalse(standardHoursFilter.allows(midnightEvent));
        assertFalse(standardHoursFilter.allows(veryLateEvent));
    }

    @Test
    @DisplayName("Should handle boundary times correctly")
    void shouldHandleBoundaryTimesCorrectly() {
        TestEvent justBeforeStart = new TestEvent(LocalDateTime.of(2023, 8, 15, 8, 59, 59)); // 8:59:59 AM
        TestEvent exactStart = new TestEvent(LocalDateTime.of(2023, 8, 15, 9, 0, 0)); // 9:00:00 AM
        TestEvent exactEnd = new TestEvent(LocalDateTime.of(2023, 8, 15, 17, 0, 0)); // 5:00:00 PM
        TestEvent justAfterEnd = new TestEvent(LocalDateTime.of(2023, 8, 15, 17, 0, 1)); // 5:00:01 PM

        assertFalse(standardHoursFilter.allows(justBeforeStart));
        assertTrue(standardHoursFilter.allows(exactStart));
        assertTrue(standardHoursFilter.allows(exactEnd));
        assertFalse(standardHoursFilter.allows(justAfterEnd));
    }

    @Test
    @DisplayName("Should work with different time ranges")
    void shouldWorkWithDifferentTimeRanges() {
        TestEvent earlyEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 7, 0)); // 7:00 AM
        TestEvent lateEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 22, 0)); // 10:00 PM

        // Early hours filter (6 AM - 10 AM)
        assertTrue(earlyHoursFilter.allows(earlyEvent));
        assertFalse(earlyHoursFilter.allows(lateEvent));

        // Late hours filter (8 PM - 11:59 PM)
        assertFalse(lateHoursFilter.allows(earlyEvent));
        assertTrue(lateHoursFilter.allows(lateEvent));
    }

    @Test
    @DisplayName("Should handle same start and end time")
    void shouldHandleSameStartAndEndTime() {
        WorkingHoursFilter sameTimeFilter = new WorkingHoursFilter(LocalTime.of(12, 0), LocalTime.of(12, 0));
        TestEvent exactTimeEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 12, 0)); // 12:00 PM
        TestEvent differentTimeEvent = new TestEvent(LocalDateTime.of(2023, 8, 15, 12, 1)); // 12:01 PM

        assertTrue(sameTimeFilter.allows(exactTimeEvent));
        assertFalse(sameTimeFilter.allows(differentTimeEvent));
    }

    @Test
    @DisplayName("Should work with different event types")
    void shouldWorkWithDifferentEventTypes() {
        // All events created at 2:30 PM
        LocalDateTime testTime = LocalDateTime.of(2023, 8, 15, 14, 30);

        TestHeartbeatEvent heartbeatEvent = new TestHeartbeatEvent(testTime);
        TestTaskEvent taskEvent = new TestTaskEvent(testTime);
        TestReminderEvent reminderEvent = new TestReminderEvent(testTime);

        assertTrue(standardHoursFilter.allows(heartbeatEvent));
        assertTrue(standardHoursFilter.allows(taskEvent));
        assertTrue(standardHoursFilter.allows(reminderEvent));
    }

    // Helper test event classes to control timestamps
    private static class TestEvent implements Event {
        private final LocalDateTime timestamp;

        public TestEvent(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        @Override
        public ps.exalt.events.enums.EventType getEventType() {
            return ps.exalt.events.enums.EventType.TASK;
        }

        @Override
        public Priority getPriority() {
            return Priority.MEDIUM;
        }

        @Override
        public Object getData() {
            return "Test data";
        }

        @Override
        public String getDescription() {
            return "Test event";
        }
    }

    private static class TestHeartbeatEvent extends TestEvent {
        public TestHeartbeatEvent(LocalDateTime timestamp) {
            super(timestamp);
        }

        @Override
        public ps.exalt.events.enums.EventType getEventType() {
            return ps.exalt.events.enums.EventType.SYSTEM_HEARTBEAT;
        }
    }

    private static class TestTaskEvent extends TestEvent {
        public TestTaskEvent(LocalDateTime timestamp) {
            super(timestamp);
        }

        @Override
        public ps.exalt.events.enums.EventType getEventType() {
            return ps.exalt.events.enums.EventType.TASK;
        }
    }

    private static class TestReminderEvent extends TestEvent {
        public TestReminderEvent(LocalDateTime timestamp) {
            super(timestamp);
        }

        @Override
        public ps.exalt.events.enums.EventType getEventType() {
            return ps.exalt.events.enums.EventType.REMINDER;
        }
    }
}
