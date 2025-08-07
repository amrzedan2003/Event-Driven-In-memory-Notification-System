package ps.exalt.events.types;

import ps.exalt.events.CommonEvent;
import ps.exalt.events.MyData;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;

public class NewTaskEvent extends CommonEvent {
    private final String taskName;
    private final String assignee;
    private final String description;

    public NewTaskEvent(String taskName, String assignee, String description, Priority priority) {
        super(EventType.TASK, priority, new TaskData(2L, taskName, assignee, description));
        this.taskName = taskName;
        this.assignee = assignee;
        this.description = description;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getTaskDescription() {
        return description;
    }

    @Override
    public String getDescription() {
        return String.format("New task: %s \n Assigned to %s \n Description: \n %s", taskName, assignee, description);
    }

    /**
     * DTO for task information
     * ID must be a Long
     */
    public static class TaskData extends MyData<Long> {
        private final String name;
        private final String assignee;
        private final String description;

        public TaskData(Long id, String name, String assignee, String description) {
            super(id);
            this.name = name;
            this.assignee = assignee;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getAssignee() {
            return assignee;
        }

        public String getDescription() {
            return description;
        }
    }
}
