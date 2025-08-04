package ps.exalt.events.types;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

@DisplayName("HeartbeatEvent Tests")
class HeartbeatEventTest {

    private HeartbeatEvent heartbeatEvent;
    private final String testComponent = "Database";
    private final long testInterval = 5000L;

    @BeforeEach
    void setUp() {
        heartbeatEvent = new HeartbeatEvent(testComponent, testInterval);
    }

    @Test
    @DisplayName("Should create heartbeat event with correct properties")
    void shouldCreateHeartbeatEventWithCorrectProperties() {
        assertEquals(testComponent, heartbeatEvent.getSystemComponent());
        assertEquals(testInterval, heartbeatEvent.getInterval());
        assertEquals(EventType.SYSTEM_HEARTBEAT, heartbeatEvent.getEventType());
        assertEquals(Priority.LOW, heartbeatEvent.getPriority());
        assertNotNull(heartbeatEvent.getTimestamp());
        assertTrue(heartbeatEvent.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Should contain HeartbeatData object")
    void shouldContainHeartbeatDataObject() {
        Object data = heartbeatEvent.getData();
        assertNotNull(data);
        assertInstanceOf(HeartbeatEvent.HeartbeatData.class, data);

        HeartbeatEvent.HeartbeatData heartbeatData = (HeartbeatEvent.HeartbeatData) data;
        assertEquals(testComponent, heartbeatData.getComponent());
        assertEquals(testInterval, heartbeatData.getInterval());
    }

    @Test
    @DisplayName("Should handle null component name")
    void shouldHandleNullComponentName() {
        HeartbeatEvent nullComponentEvent = new HeartbeatEvent(null, testInterval);
        assertNull(nullComponentEvent.getSystemComponent());
        assertNotNull(nullComponentEvent.getDescription());
    }

    @Test
    @DisplayName("Should handle empty component name")
    void shouldHandleEmptyComponentName() {
        HeartbeatEvent emptyComponentEvent = new HeartbeatEvent("", testInterval);
        assertEquals("", emptyComponentEvent.getSystemComponent());
        assertNotNull(emptyComponentEvent.getDescription());
    }

    @Test
    @DisplayName("Should handle zero interval")
    void shouldHandleZeroInterval() {
        HeartbeatEvent zeroIntervalEvent = new HeartbeatEvent(testComponent, 0L);
        assertEquals(0L, zeroIntervalEvent.getInterval());
    }

    @Test
    @DisplayName("Should handle negative interval")
    void shouldHandleNegativeInterval() {
        HeartbeatEvent negativeIntervalEvent = new HeartbeatEvent(testComponent, -1000L);
        assertEquals(-1000L, negativeIntervalEvent.getInterval());
    }

    @Test
    @DisplayName("Multiple heartbeat events should have different timestamps")
    void multipleHeartbeatEventsShouldHaveDifferentTimestamps() throws InterruptedException {
        HeartbeatEvent event1 = new HeartbeatEvent(testComponent, testInterval);
        Thread.sleep(1); // Ensure different timestamps
        HeartbeatEvent event2 = new HeartbeatEvent(testComponent, testInterval);

        assertNotEquals(event1.getTimestamp(), event2.getTimestamp());
        assertTrue(event1.getTimestamp().isBefore(event2.getTimestamp()));
    }
}
