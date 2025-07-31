package ps.exalt.notification.filters;

import ps.exalt.events.Event;
import java.time.LocalTime;

/**
 * Concrete Strategy for filtering events by time window
 * Only delivers events within specified working hours
 */
public class WorkingHoursFilter implements EventFilter {
    private final LocalTime startTime;
    private final LocalTime endTime;

    public WorkingHoursFilter(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean allows(Event event) {
        LocalTime eventTime = event.getTimestamp().toLocalTime();
        return !eventTime.isBefore(startTime) && !eventTime.isAfter(endTime);
    }

    @Override
    public String getInfo() {
        return "Working hours filter: " + startTime + " - " + endTime;
    }
}
