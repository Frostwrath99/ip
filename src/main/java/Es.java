import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point for the Es chatbot application.
 */
public class Es {
    private static final String INDENT = "    ";
    private static final String DIVIDER = INDENT + "____________________________________________________________";

    public static void main(String[] args) {
        String banner = INDENT + " _____     \n"
                + INDENT + "| ____|___ \n"
                + INDENT + "|  _| / __|\n"
                + INDENT + "| |___\\__ \\\n"
                + INDENT + "|_____|___/\n";

        System.out.println(DIVIDER);
        System.out.print(banner);
        System.out.println(INDENT + "Hello! I'm Es.");
        System.out.println(INDENT + "What can I do for you?");
        System.out.println(DIVIDER);

        ArrayList<Task> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            if (command.equals("bye")) {
                break;
            }

            System.out.println(DIVIDER);
            try {
                if (command.equals("list")) {
                    System.out.println(INDENT + "Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(INDENT + (i + 1) + "." + tasks.get(i));
                    }
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    updateTaskStatus(tasks, command, true);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    updateTaskStatus(tasks, command, false);
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    deleteTask(tasks, command);
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    String description = command.length() == 4 ? "" : command.substring(5).trim();
                    if (description.isEmpty()) {
                        throw new EsException("The description of a todo cannot be empty.");
                    }
                    addTask(tasks, new Todo(description));
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    addDeadline(tasks, command.substring(8).trim());
                } else if (command.equals("event") || command.startsWith("event ")) {
                    addEvent(tasks, command.substring(5).trim());
                } else if (command.isEmpty()) {
                    throw new EsException("Please enter a command.");
                } else {
                    throw new EsException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (EsException e) {
                System.out.println(INDENT + "OOPS!!! " + e.getMessage());
            }
            System.out.println(DIVIDER);
        }

        System.out.println(DIVIDER);
        System.out.println(INDENT + "Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }

    /**
     * Adds a task to the list and prints a confirmation.
     *
     * @param tasks the task list
     * @param task the task to add
     */
    private static void addTask(ArrayList<Task> tasks, Task task) {
        tasks.add(task);
        printAddedTask(task, tasks.size());
    }

    /**
     * Validates and adds a deadline task from its details after the command word.
     *
     * @param tasks the task list
     * @param details the deadline description and /by value
     */
    private static void addDeadline(ArrayList<Task> tasks, String details) throws EsException {
        int byIndex = details.indexOf("/by ");
        if (byIndex < 0 && details.endsWith("/by")) {
            byIndex = details.length() - 3;
        }
        if (byIndex < 0) {
            throw new EsException("A deadline must include /by followed by a date or time.");
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + 3).trim();
        if (description.isEmpty()) {
            throw new EsException("The description of a deadline cannot be empty.");
        } else if (by.isEmpty()) {
            throw new EsException("The deadline cannot be empty.");
        } else {
            addTask(tasks, new Deadline(description, by));
        }
    }

    /**
     * Validates and adds an event task from its details after the command word.
     *
     * @param tasks the task list
     * @param details the event description, /from value, and /to value
     */
    private static void addEvent(ArrayList<Task> tasks, String details) throws EsException {
        int fromIndex = details.indexOf("/from ");
        int toIndex = details.indexOf("/to ");
        if (toIndex < 0 && details.endsWith("/to")) {
            toIndex = details.length() - 3;
        }
        if (fromIndex < 0 || toIndex < 0) {
            throw new EsException("An event must include /from and /to times.");
        }
        if (toIndex < fromIndex) {
            throw new EsException("The /from time must come before the /to time.");
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + 5, toIndex).trim();
        String to = details.substring(toIndex + 3).trim();
        if (description.isEmpty()) {
            throw new EsException("The description of an event cannot be empty.");
        } else if (from.isEmpty()) {
            throw new EsException("The start time of an event cannot be empty.");
        } else if (to.isEmpty()) {
            throw new EsException("The end time of an event cannot be empty.");
        } else {
            addTask(tasks, new Event(description, from, to));
        }
    }

    /**
     * Marks or unmarks a task after validating its one-based task number.
     *
     * @param tasks the task list
     * @param command the complete mark or unmark command
     * @param shouldMark whether the task should be marked done
     */
    private static void updateTaskStatus(ArrayList<Task> tasks, String command, boolean shouldMark)
            throws EsException {
        String action = shouldMark ? "mark" : "unmark";
        String numberText = command.substring(action.length()).trim();
        if (numberText.isEmpty()) {
            throw new EsException("Please provide a task number to " + action + ".");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new EsException("The task number to " + action + " must be a positive whole number.");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new EsException("There is no task with that number.");
        }

        Task task = tasks.get(taskNumber - 1);
        if (shouldMark) {
            task.markAsDone();
            System.out.println(INDENT + "Nice! I've marked this task as done:");
        } else {
            task.markAsNotDone();
            System.out.println(INDENT + "OK, I've marked this task as not done yet:");
        }
        System.out.println(INDENT + "  " + task);
    }

    /**
     * Removes a task after validating its one-based task number.
     *
     * @param tasks the task list
     * @param command the complete delete command
     * @throws EsException if the command has no valid task number
     */
    private static void deleteTask(ArrayList<Task> tasks, String command) throws EsException {
        String numberText = command.substring("delete".length()).trim();
        if (numberText.isEmpty()) {
            throw new EsException("Please provide a task number to delete.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new EsException("The task number to delete must be a positive whole number.");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new EsException("There is no task with that number.");
        }

        Task removedTask = tasks.remove(taskNumber - 1);

        System.out.println(INDENT + "Noted. I've removed this task:");
        System.out.println(INDENT + "  " + removedTask);
        System.out.println(INDENT + "Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Displays the confirmation shown after adding a task.
     *
     * @param task the task that was added
     * @param taskCount the total number of tasks after adding it
     */
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println(INDENT + "Got it. I've added this task:");
        System.out.println(INDENT + "  " + task);
        System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
    }
}
