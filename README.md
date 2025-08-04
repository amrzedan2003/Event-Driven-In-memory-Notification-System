# Event-Driven Notification System

A Java application that demonstrates several design patterns through an event-driven notification system. The system handles various types of events, manages subscribers, and provides filtering capabilities.

## System Design

This project showcases multiple design patterns working together:

### Core Components

**Events Package (`ps.exalt.events`)**

- **Event Interface**: Defines the contract for all events in the system
- **CommonEvent**: Base implementation with common event properties
- **Event Types**: Specific event implementations (NewTaskEvent, ReminderEvent, HeartbeatEvent)
- **Enums**: EventType and Priority for categorization

**Notification Package (`ps.exalt.notification`)**

- **NotificationManager**: Central hub for managing subscribers and publishing events
- **EventSubscriber**: Handles incoming events and processes them
- **EventPublisher**: Interface for publishing events to subscribers
- **SubscriberInfo**: Contains subscriber details and filtering preferences
- **Filters**: Event filtering system (PriorityFilter, WorkingHoursFilter)

**Scheduling Package (`ps.exalt.scheduling`)**

- **EventScheduler**: Manages time-based event scheduling and execution
- **ScheduledEvent**: Represents events that should be triggered at specific times
- **PublishEvent**: Command for publishing scheduled events
- **Event Types**: Specific scheduling implementations (HeartbeatType, ReminderType)

### Design Patterns Used

1. **Singleton Pattern**

   - `NotificationManager` ensures only one instance manages all notifications

2. **Observer Pattern**

   - Subscribers register with NotificationManager to receive event notifications
   - Loose coupling between event publishers and subscribers

3. **Factory Pattern**

   - Event creation and type management through enums and factories

4. **Command Pattern**

   - `PublishEvent` encapsulates event publishing operations
   - Scheduling commands for delayed execution

5. **Strategy Pattern**

   - Event filters (`PriorityFilter`, `WorkingHoursFilter`) implement different filtering strategies
   - Allows dynamic filtering behavior based on subscriber preferences

6. **Template Method Pattern**
   - Common event structure with specific implementations in event types

## How to Run the App

1. **Compile the project:**

   ```powershell
   mvn compile
   ```

2. **Run the main application:**

   ```powershell
   mvn exec:java -Dexec.mainClass="ps.exalt.App"
   ```

## How to Run the Tests

### Running All Tests

```powershell
mvn test
```

### Running Specific Test Classes

```powershell
# Test the notification manager
mvn test -Dtest=NotificationManagerTest

# Test event scheduling
mvn test -Dtest=EventSchedulerTest

# Test event filters
mvn test -Dtest=PriorityFilterTest
mvn test -Dtest=WorkingHoursFilterTest

# Test specific event types
mvn test -Dtest=HeartbeatEventTest
mvn test -Dtest=NewTaskEventTest
mvn test -Dtest=ReminderEventTest
```

## Project Structure

```
src/
├── main/java/ps/exalt/
│   ├── App.java                 # Main application
│   ├── events/                  # Event definitions and types
│   ├── notification/            # Observer pattern implementation
│   └── scheduling/              # Time-based event scheduling
└── test/java/ps/exalt/          # Unit tests for all components
```
