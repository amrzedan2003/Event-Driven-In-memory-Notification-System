package ps.exalt.events;

import java.time.LocalDateTime;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;

public abstract class CommonEvent implements Event {
    private final LocalDateTime timestamp;
    private final EventType eventType;
    private final Priority priority;
    private final Object data;

    protected CommonEvent(EventType eventType, Priority priority, Object data) {
        this.timestamp = LocalDateTime.now();
        this.eventType = eventType;
        this.priority = priority;
        this.data = data;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return String.format("Event:\n type: %s, timestamp=%s, priority: %s \n description: %s}",
                eventType, priority, timestamp, getDescription());
    }
}
