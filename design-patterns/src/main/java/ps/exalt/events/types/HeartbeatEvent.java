package ps.exalt.events.types;

import ps.exalt.events.CommonEvent;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;

public class HeartbeatEvent extends CommonEvent {
    private final String systemComponent;
    private final long interval;

    public HeartbeatEvent(String systemComponent, long interval) {
        super(EventType.SYSTEM_HEARTBEAT, Priority.LOW, new HeartbeatData(systemComponent, interval));
        this.systemComponent = systemComponent;
        this.interval = interval;
    }

    public String getSystemComponent() {
        return systemComponent;
    }

    public long getInterval() {
        return interval;
    }

    @Override
    public String getDescription() {
        return String.format("Heartbeat from %s ==> interval: %d ms", systemComponent, interval);
    }

    /**
     * DTO for heartbeat information
     */
    public static class HeartbeatData {
        private final String component;
        private final long interval;

        public HeartbeatData(String component, long interval) {
            this.component = component;
            this.interval = interval;
        }

        public String getComponent() {
            return component;
        }

        public long getInterval() {
            return interval;
        }
    }
}
