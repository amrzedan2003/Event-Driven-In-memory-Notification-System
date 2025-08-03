package ps.exalt.notification.filters;

import ps.exalt.events.Event;
import ps.exalt.events.enums.Priority;

/**
 * Concrete Strategy for filtering events by priority
 * Only delivers events with priority >= minimum priority level
 */
public class PriorityFilter implements EventFilter {
    private final Priority minimumPriority;

    public PriorityFilter(Priority minimumPriority) {
        this.minimumPriority = minimumPriority;
    }

    public Priority getMinimumPriority() {
        return minimumPriority;
    }

    @Override
    public boolean allows(Event event) {
        return event.getPriority().getLevel() >= minimumPriority.getLevel();
    }

    @Override
    public String getInfo() {
        return "Priority filter: minimum (" + minimumPriority + ")";
    }
}
