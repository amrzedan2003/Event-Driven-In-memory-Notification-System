package ps.exalt.events.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Priority Enum Tests")
class PriorityTest {

    @Test
    @DisplayName("Should have correct priority levels")
    void shouldHaveCorrectPriorityLevels() {
        assertEquals(1, Priority.LOW.getLevel());
        assertEquals(2, Priority.MEDIUM.getLevel());
        assertEquals(3, Priority.HIGH.getLevel());
    }

    @Test
    @DisplayName("Should have all expected priority values")
    void shouldHaveAllExpectedPriorityValues() {
        Priority[] priorities = Priority.values();
        assertEquals(3, priorities.length);

        assertArrayEquals(new Priority[] { Priority.LOW, Priority.MEDIUM, Priority.HIGH }, priorities);
    }

    @Test
    @DisplayName("Should have correct order by level")
    void shouldHaveCorrectOrderByLevel() {
        assertTrue(Priority.LOW.getLevel() < Priority.MEDIUM.getLevel());
        assertTrue(Priority.MEDIUM.getLevel() < Priority.HIGH.getLevel());
    }

    @Test
    @DisplayName("Should convert from string correctly")
    void shouldConvertFromStringCorrectly() {
        assertEquals(Priority.LOW, Priority.valueOf("LOW"));
        assertEquals(Priority.MEDIUM, Priority.valueOf("MEDIUM"));
        assertEquals(Priority.HIGH, Priority.valueOf("HIGH"));
    }
}
