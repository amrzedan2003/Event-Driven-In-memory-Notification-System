package ps.exalt.events.types;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

@DisplayName("ReminderEvent Tests")
class ReminderEventTest {

    private ReminderEvent reminderEvent;
    private final String testReminderText = "Do not forget the exalt team lunch at next sunday";
    private final String testRecipient = "Amr Zedan";
    private final Priority testPriority = Priority.MEDIUM;

    @BeforeEach
    void setUp() {
        reminderEvent = new ReminderEvent(testReminderText, testRecipient, testPriority);
    }

    @Test
    @DisplayName("Should create reminder event with correct properties")
    void shouldCreateReminderEventWithCorrectProperties() {
        assertEquals(testReminderText, reminderEvent.getReminderText());
        assertEquals(testRecipient, reminderEvent.getRecipient());
        assertEquals(testPriority, reminderEvent.getPriority());
        assertEquals(EventType.REMINDER, reminderEvent.getEventType());
        assertNotNull(reminderEvent.getTimestamp());
        assertTrue(reminderEvent.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Should contain ReminderData object")
    void shouldContainReminderDataObject() {
        Object data = reminderEvent.getData();
        assertNotNull(data);
        assertInstanceOf(ReminderEvent.ReminderData.class, data);

        ReminderEvent.ReminderData reminderData = (ReminderEvent.ReminderData) data;
        assertEquals(testReminderText, reminderData.getText());
        assertEquals(testRecipient, reminderData.getRecipient());
    }

    @Test
    @DisplayName("Should work with different priority levels")
    void shouldWorkWithDifferentPriorityLevels() {
        ReminderEvent lowPriorityReminder = new ReminderEvent("Low priority reminder", "User1", Priority.LOW);
        ReminderEvent mediumPriorityReminder = new ReminderEvent("Medium priority reminder", "User2", Priority.MEDIUM);
        ReminderEvent highPriorityReminder = new ReminderEvent("High priority reminder", "User3", Priority.HIGH);

        assertEquals(Priority.LOW, lowPriorityReminder.getPriority());
        assertEquals(Priority.MEDIUM, mediumPriorityReminder.getPriority());
        assertEquals(Priority.HIGH, highPriorityReminder.getPriority());
    }

    @Test
    @DisplayName("Should handle null reminder text")
    void shouldHandleNullReminderText() {
        ReminderEvent nullTextEvent = new ReminderEvent(null, testRecipient, testPriority);
        assertNull(nullTextEvent.getReminderText());
        assertNotNull(nullTextEvent.getDescription());
    }

    @Test
    @DisplayName("Should handle empty reminder text")
    void shouldHandleEmptyReminderText() {
        ReminderEvent emptyTextEvent = new ReminderEvent("", testRecipient, testPriority);
        assertEquals("", emptyTextEvent.getReminderText());
        assertNotNull(emptyTextEvent.getDescription());
    }

    @Test
    @DisplayName("Should handle null recipient")
    void shouldHandleNullRecipient() {
        ReminderEvent nullRecipientEvent = new ReminderEvent(testReminderText, null, testPriority);
        assertNull(nullRecipientEvent.getRecipient());
        assertNotNull(nullRecipientEvent.getDescription());
    }

    @Test
    @DisplayName("Should handle empty recipient")
    void shouldHandleEmptyRecipient() {
        ReminderEvent emptyRecipientEvent = new ReminderEvent(testReminderText, "", testPriority);
        assertEquals("", emptyRecipientEvent.getRecipient());
        assertNotNull(emptyRecipientEvent.getDescription());
    }

    @Test
    @DisplayName("Should handle very long reminder text")
    void shouldHandleVeryLongReminderText() {
        String longText = "A".repeat(500);
        ReminderEvent longTextEvent = new ReminderEvent(longText, testRecipient, testPriority);
        assertEquals(longText, longTextEvent.getReminderText());
        assertNotNull(longTextEvent.getDescription());
    }

    // ReminderData inner class tests
    @Test
    @DisplayName("ReminderData should store text and recipient correctly")
    void reminderDataShouldStoreTextAndRecipientCorrectly() {
        ReminderEvent.ReminderData data = new ReminderEvent.ReminderData(testReminderText, testRecipient);

        assertEquals(testReminderText, data.getText());
        assertEquals(testRecipient, data.getRecipient());
    }

    @Test
    @DisplayName("ReminderData should handle null values")
    void reminderDataShouldHandleNullValues() {
        ReminderEvent.ReminderData data = new ReminderEvent.ReminderData(null, null);

        assertNull(data.getText());
        assertNull(data.getRecipient());
    }

    @Test
    @DisplayName("Multiple reminder events should have different timestamps")
    void multipleReminderEventsShouldHaveDifferentTimestamps() throws InterruptedException {
        ReminderEvent event1 = new ReminderEvent("Reminder1", "User1", Priority.LOW);
        Thread.sleep(1);
        ReminderEvent event2 = new ReminderEvent("Reminder2", "User2", Priority.HIGH);

        assertNotEquals(event1.getTimestamp(), event2.getTimestamp());
        assertTrue(event1.getTimestamp().isBefore(event2.getTimestamp()));
    }
}
