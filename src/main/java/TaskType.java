/**
 * The kinds of tasks Es can store, each identified by a one-letter icon.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String icon;

    TaskType(String icon) {
        this.icon = icon;
    }

    /**
     * Returns the letter shown in square brackets for this task type.
     *
     * @return the type icon, such as {@code T} for a to-do
     */
    public String getIcon() {
        return icon;
    }
}
