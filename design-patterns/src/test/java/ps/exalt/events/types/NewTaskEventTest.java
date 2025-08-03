package ps.exalt.events.types;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

@DisplayName("NewTaskEvent Tests")
class NewTaskEventTest {

    private NewTaskEvent taskEvent;
    private final String testTaskName = "Implement User Authentication";
    private final String testAssignee = "Amr Zedan";
    private final String testDescription = "Create login and registration";
    private final Priority testPriority = Priority.HIGH;

    @BeforeEach
    void setUp() {
        taskEvent = new NewTaskEvent(testTaskName, testAssignee, testDescription, testPriority);
    }

    @Test
    @DisplayName("Should create task event with correct properties")
    void shouldCreateTaskEventWithCorrectProperties() {
        assertEquals(testTaskName, taskEvent.getTaskName());
        assertEquals(testAssignee, taskEvent.getAssignee());
        assertEquals(testDescription, taskEvent.getTaskDescription());
        assertEquals(testPriority, taskEvent.getPriority());
        assertEquals(EventType.TASK, taskEvent.getEventType());
        assertNotNull(taskEvent.getTimestamp());
        assertTrue(taskEvent.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Should contain TaskData object")
    void shouldContainTaskDataObject() {
        Object data = taskEvent.getData();
        assertNotNull(data);
        assertInstanceOf(NewTaskEvent.TaskData.class, data);

        NewTaskEvent.TaskData taskData = (NewTaskEvent.TaskData) data;
        assertEquals(testTaskName, taskData.getName());
        assertEquals(testAssignee, taskData.getAssignee());
        assertEquals(testDescription, taskData.getDescription());
    }

    @Test
    @DisplayName("Should work with different priority levels")
    void shouldWorkWithDifferentPriorityLevels() {
        NewTaskEvent lowPriorityTask = new NewTaskEvent("Low Priority Task", "Noor", "Description", Priority.LOW);
        NewTaskEvent mediumPriorityTask = new NewTaskEvent("Medium Priority Task", "Menna", "Description",
                Priority.MEDIUM);
        NewTaskEvent highPriorityTask = new NewTaskEvent("High Priority Task", "Bilal", "Description", Priority.HIGH);

        assertEquals(Priority.LOW, lowPriorityTask.getPriority());
        assertEquals(Priority.MEDIUM, mediumPriorityTask.getPriority());
        assertEquals(Priority.HIGH, highPriorityTask.getPriority());
    }

    @Test
    @DisplayName("Should handle null task name")
    void shouldHandleNullTaskName() {
        NewTaskEvent nullNameEvent = new NewTaskEvent(null, testAssignee, testDescription, testPriority);
        assertNull(nullNameEvent.getTaskName());
        assertNotNull(nullNameEvent.getDescription());
    }

    @Test
    @DisplayName("Should handle empty task name")
    void shouldHandleEmptyTaskName() {
        NewTaskEvent emptyNameEvent = new NewTaskEvent("", testAssignee, testDescription, testPriority);
        assertEquals("", emptyNameEvent.getTaskName());
        assertNotNull(emptyNameEvent.getDescription());
    }

    @Test
    @DisplayName("Should handle null assignee")
    void shouldHandleNullAssignee() {
        NewTaskEvent nullAssigneeEvent = new NewTaskEvent(testTaskName, null, testDescription, testPriority);
        assertNull(nullAssigneeEvent.getAssignee());
        assertNotNull(nullAssigneeEvent.getDescription());
    }

    @Test
    @DisplayName("Should handle empty assignee")
    void shouldHandleEmptyAssignee() {
        NewTaskEvent emptyAssigneeEvent = new NewTaskEvent(testTaskName, "", testDescription, testPriority);
        assertEquals("", emptyAssigneeEvent.getAssignee());
        assertNotNull(emptyAssigneeEvent.getDescription());
    }

    @Test
    @DisplayName("Should handle null description")
    void shouldHandleNullDescription() {
        NewTaskEvent nullDescEvent = new NewTaskEvent(testTaskName, testAssignee, null, testPriority);
        assertNull(nullDescEvent.getTaskDescription());
        assertNotNull(nullDescEvent.getDescription());
    }

    @Test
    @DisplayName("Should handle empty description")
    void shouldHandleEmptyDescription() {
        NewTaskEvent emptyDescEvent = new NewTaskEvent(testTaskName, testAssignee, "", testPriority);
        assertEquals("", emptyDescEvent.getTaskDescription());
        assertNotNull(emptyDescEvent.getDescription());
    }

    // TaskData inner class tests
    @Test
    @DisplayName("TaskData should store all properties correctly")
    void taskDataShouldStoreAllPropertiesCorrectly() {
        NewTaskEvent.TaskData data = new NewTaskEvent.TaskData(testTaskName, testAssignee, testDescription);

        assertEquals(testTaskName, data.getName());
        assertEquals(testAssignee, data.getAssignee());
        assertEquals(testDescription, data.getDescription());
    }

    @Test
    @DisplayName("TaskData should handle null values")
    void taskDataShouldHandleNullValues() {
        NewTaskEvent.TaskData data = new NewTaskEvent.TaskData(null, null, null);

        assertNull(data.getName());
        assertNull(data.getAssignee());
        assertNull(data.getDescription());
    }

    @Test
    @DisplayName("Multiple task events should have different timestamps")
    void multipleTaskEventsShouldHaveDifferentTimestamps() throws InterruptedException {
        NewTaskEvent event1 = new NewTaskEvent("Task1", "Amr", "Desc1", Priority.LOW);
        Thread.sleep(1);
        NewTaskEvent event2 = new NewTaskEvent("Task2", "Menna", "Desc2", Priority.HIGH);

        assertNotEquals(event1.getTimestamp(), event2.getTimestamp());
        assertTrue(event1.getTimestamp().isBefore(event2.getTimestamp()));
    }
}
