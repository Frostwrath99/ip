/**
 * Represents a task entered by the user and whether it has been completed.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the task description.
     *
     * @return the description entered by the user
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the symbol used to display the completion status.
     *
     * @return {@code X} when complete, otherwise a space
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as complete.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the shared completion status and description for all task types.
     *
     * @return the completion status and task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + getDescription();
    }
}
