package ps.exalt.scheduling;

import ps.exalt.events.Event;

/**
 * Interface for scheduled event publishing
 */
public interface ScheduledEvent {
    void execute();

    Event getEvent();

    String getDescription();
}
