package ps.exalt.events.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EventType Enum Tests")
class EventTypeTest {

    @Test
    @DisplayName("Should have all expected event types")
    void shouldHaveAllExpectedEventTypes() {
        EventType[] eventTypes = EventType.values();
        assertEquals(3, eventTypes.length);

        assertArrayEquals(new EventType[] { EventType.TASK, EventType.SYSTEM_HEARTBEAT, EventType.REMINDER },
                eventTypes);
    }

    @Test
    @DisplayName("Should convert from string correctly")
    void shouldConvertFromStringCorrectly() {
        assertEquals(EventType.TASK, EventType.valueOf("TASK"));
        assertEquals(EventType.SYSTEM_HEARTBEAT, EventType.valueOf("SYSTEM_HEARTBEAT"));
        assertEquals(EventType.REMINDER, EventType.valueOf("REMINDER"));
    }

    @Test
    @DisplayName("Should have consistent naming")
    void shouldHaveConsistentNaming() {
        assertEquals("TASK", EventType.TASK.name());
        assertEquals("SYSTEM_HEARTBEAT", EventType.SYSTEM_HEARTBEAT.name());
        assertEquals("REMINDER", EventType.REMINDER.name());
    }
}
