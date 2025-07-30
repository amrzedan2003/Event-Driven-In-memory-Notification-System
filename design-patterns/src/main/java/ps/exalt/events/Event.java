package ps.exalt.events;

import java.time.LocalDateTime;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;

public interface Event {
    LocalDateTime getTimestamp();

    EventType getEventType();

    Priority getPriority();

    Object getData();

    String getDescription();
}
