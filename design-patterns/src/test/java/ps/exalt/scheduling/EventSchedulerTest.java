package ps.exalt.scheduling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ps.exalt.notification.NotificationManager;
import ps.exalt.notification.EventSubscriber;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EventScheduler Tests")
class EventSchedulerTest {

    private EventScheduler eventScheduler;
    private NotificationManager notificationManager;
    private EventSubscriber testSubscriber;

    @BeforeEach
    void setUp() {
        notificationManager = NotificationManager.getInstance();
        eventScheduler = new EventScheduler(notificationManager);
        testSubscriber = new EventSubscriber("TestUser");
    }

    @Test
    @DisplayName("Should create EventScheduler")
    void shouldCreateEventScheduler() {
        assertNotNull(eventScheduler);
        assertDoesNotThrow(() -> eventScheduler.start());
    }

    @Test
    @DisplayName("Should schedule heartbeat events")
    void shouldScheduleHeartbeatEvents() {
        assertDoesNotThrow(() -> eventScheduler.scheduleHeartbeat("TestService", 1));
    }

    @Test
    @DisplayName("Should schedule reminder events")
    void shouldScheduleReminderEvents() {
        assertDoesNotThrow(() -> eventScheduler.scheduleReminder("Test reminder", 1));
    }

    @Test
    @DisplayName("Should stop scheduler")
    void shouldStopScheduler() {
        eventScheduler.start();
        assertDoesNotThrow(() -> eventScheduler.stop());
    }
}
