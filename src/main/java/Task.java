/**
 * Represents a task entered by the user and whether it has been completed.
 */
public abstract class Task {
    private final String description;
    private final TaskType type;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description and type.
     *
     * @param description the text describing the task
     * @param type the kind of task being created
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
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
     * Returns the encoded line used to save this task to disk.
     *
     * @return the storage line for this task
     */
    public abstract String toStorageLine();

    /**
     * Returns the shared type, completion flag, and description used in storage lines.
     *
     * @return the prefix {@code T | 0 | description} (with the matching type icon and flag)
     */
    protected String toStoragePrefix() {
        return type.getIcon() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the type icon, completion status, and description for all task types.
     *
     * @return the formatted task without type-specific extra details
     */
    @Override
    public String toString() {
        return "[" + type.getIcon() + "][" + getStatusIcon() + "] " + getDescription();
    }
}
