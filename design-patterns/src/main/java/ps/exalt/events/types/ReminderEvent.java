package ps.exalt.events.types;

import ps.exalt.events.CommonEvent;
import ps.exalt.events.enums.EventType;
import ps.exalt.events.enums.Priority;

public class ReminderEvent extends CommonEvent {
    private final String reminderText;
    private final String recipient;

    public ReminderEvent(String reminderText, String recipient, Priority priority) {
        super(EventType.REMINDER, priority, new ReminderData(reminderText, recipient));
        this.reminderText = reminderText;
        this.recipient = recipient;
    }

    public String getReminderText() {
        return reminderText;
    }

    public String getRecipient() {
        return recipient;
    }

    @Override
    public String getDescription() {
        return String.format("Reminder for (%s): %s", recipient, reminderText);
    }

    /**
     * DTO for reminder information
     */
    public static class ReminderData {
        private final String text;
        private final String recipient;

        public ReminderData(String text, String recipient) {
            this.text = text;
            this.recipient = recipient;
        }

        public String getText() {
            return text;
        }

        public String getRecipient() {
            return recipient;
        }
    }
}
