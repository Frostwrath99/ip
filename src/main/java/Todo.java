/**
 * Represents a task with no associated date or time.
 */
public class Todo extends Task {
    /**
     * Creates a to-do task.
     *
     * @param description the text describing the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the formatted to-do task.
     *
     * @return the task prefixed with its type and completion status
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
