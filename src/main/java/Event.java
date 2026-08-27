/**
 * Represents a task that begins and ends at specified times.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description the text describing the event
     * @param from the event start time text
     * @param to the event end time text
     */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the encoded event line for saving to disk.
     *
     * @return a line in the form {@code E | 0 | description | from | to}
     */
    @Override
    public String toStorageLine() {
        return toStoragePrefix() + " | " + from + " | " + to;
    }

    /**
     * Returns the formatted event task.
     *
     * @return the task with its start and end times
     */
    @Override
    public String toString() {
        return super.toString()
                + " (from: " + from + " to: " + to + ")";
    }
}
