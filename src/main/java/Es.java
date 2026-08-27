import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point for the Es chatbot application.
 */
public class Es {
    private static final String INDENT = "    ";

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage();
        ArrayList<Task> tasks = loadTasks(storage);
        String input;
        while ((input = ui.readCommand()) != null) {
            Command command = Command.parse(input);
            if (command == Command.BYE) {
                break;
            }

            ui.showLine();
            try {
                if (command == null) {
                    if (input.isEmpty()) {
                        throw new EsException("Please enter a command.");
                    }
                    throw new EsException("I'm sorry, but I don't know what that means :-(");
                }

                switch (command) {
                case LIST:
                    ui.show("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        ui.show((i + 1) + "." + tasks.get(i));
                    }
                    break;
                case MARK:
                    updateTaskStatus(storage, tasks, input, true);
                    break;
                case UNMARK:
                    updateTaskStatus(storage, tasks, input, false);
                    break;
                case DELETE:
                    deleteTask(storage, tasks, input);
                    break;
                case TODO:
                    String description = input.substring(command.name().length()).trim();
                    if (description.isEmpty()) {
                        throw new EsException("The description of a todo cannot be empty.");
                    }
                    addTask(storage, tasks, new Todo(description));
                    break;
                case DEADLINE:
                    addDeadline(storage, tasks, input.substring(command.name().length()).trim());
                    break;
                case EVENT:
                    addEvent(storage, tasks, input.substring(command.name().length()).trim());
                    break;
                default:
                    throw new EsException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (EsException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }

        ui.showLine();
        ui.show("Bye. Hope to see you again soon!");
        ui.showLine();
    }

    /**
     * Loads tasks from disk, or starts with an empty list if none exist yet.
     * A corrupted or unreadable file is reported and replaced with an empty list.
     *
     * @param storage the file used to persist tasks
     * @return the loaded tasks, or an empty list when loading fails
     */
    private static ArrayList<Task> loadTasks(Storage storage) {
        try {
            return storage.load();
        } catch (EsException e) {
            System.out.println(INDENT + "____________________________________________________________");
            System.out.println(INDENT + "OOPS!!! " + e.getMessage());
            System.out.println(INDENT + "____________________________________________________________");
            return new ArrayList<>();
        }
    }

    /**
     * Adds a task to the list, prints a confirmation, and saves the updated list.
     *
     * @param storage the file used to persist tasks
     * @param tasks the task list
     * @param task the task to add
     */
    private static void addTask(Storage storage, ArrayList<Task> tasks, Task task) throws EsException {
        tasks.add(task);
        printAddedTask(task, tasks.size());
        storage.save(tasks);
    }

    /**
     * Validates and adds a deadline task from its details after the command word.
     *
     * @param storage the file used to persist tasks
     * @param tasks the task list
     * @param details the deadline description and /by value
     */
    private static void addDeadline(Storage storage, ArrayList<Task> tasks, String details) throws EsException {
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
            addTask(storage, tasks, new Deadline(description, by));
        }
    }

    /**
     * Validates and adds an event task from its details after the command word.
     *
     * @param storage the file used to persist tasks
     * @param tasks the task list
     * @param details the event description, /from value, and /to value
     */
    private static void addEvent(Storage storage, ArrayList<Task> tasks, String details) throws EsException {
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
            addTask(storage, tasks, new Event(description, from, to));
        }
    }

    /**
     * Marks or unmarks a task after validating its one-based task number.
     *
     * @param storage the file used to persist tasks
     * @param tasks the task list
     * @param command the complete mark or unmark command
     * @param shouldMark whether the task should be marked done
     */
    private static void updateTaskStatus(Storage storage, ArrayList<Task> tasks, String command, boolean shouldMark)
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
        storage.save(tasks);
    }

    /**
     * Removes a task after validating its one-based task number.
     *
     * @param storage the file used to persist tasks
     * @param tasks the task list
     * @param command the complete delete command
     * @throws EsException if the command has no valid task number
     */
    private static void deleteTask(Storage storage, ArrayList<Task> tasks, String command) throws EsException {
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
        storage.save(tasks);
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
