package ps.exalt.events.types;

import ps.exalt.events.CommonEvent;
import ps.exalt.events.MyData;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;

public class HeartbeatEvent extends CommonEvent {
    private final String systemComponent;
    private final long interval;

    public HeartbeatEvent(String systemComponent, long interval) {
        super(EventType.SYSTEM_HEARTBEAT, Priority.LOW, new HeartbeatData("1", systemComponent, interval));
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
     * 
     * DTO for heartbeat information
     * ID must be a string
     */
    public static class HeartbeatData extends MyData<String> {
        private final String component;
        private final long interval;

        public HeartbeatData(String id, String component, long interval) {
            super(id);
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
