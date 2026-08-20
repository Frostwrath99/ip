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

        Task[] tasks = new Task[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            if (command.equals("bye")) {
                break;
            }

            System.out.println(DIVIDER);
            if (command.equals("list")) {
                System.out.println(INDENT + "Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(INDENT + (i + 1) + "." + tasks[i]);
                }
            } else if (command.equals("mark") || command.startsWith("mark ")) {
                updateTaskStatus(tasks, taskCount, command, true);
            } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                updateTaskStatus(tasks, taskCount, command, false);
            } else if (command.equals("todo") || command.startsWith("todo ")) {
                String description = command.length() == 4 ? "" : command.substring(5).trim();
                if (description.isEmpty()) {
                    System.out.println(INDENT + "OOPS!!! The description of a todo cannot be empty.");
                } else {
                    taskCount = addTask(tasks, taskCount, new Todo(description));
                }
            } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                taskCount = addDeadline(tasks, taskCount, command.substring(8).trim());
            } else if (command.equals("event") || command.startsWith("event ")) {
                taskCount = addEvent(tasks, taskCount, command.substring(5).trim());
            } else if (command.isEmpty()) {
                System.out.println(INDENT + "OOPS!!! Please enter a command.");
            } else {
                System.out.println(INDENT + "OOPS!!! I'm sorry, but I don't know what that means :-(");
            }
            System.out.println(DIVIDER);
        }

        System.out.println(DIVIDER);
        System.out.println(INDENT + "Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }

    /**
     * Adds a task if the fixed-size task list still has capacity.
     *
     * @param tasks the task storage array
     * @param taskCount the current number of tasks
     * @param task the task to add
     * @return the updated task count
     */
    private static int addTask(Task[] tasks, int taskCount, Task task) {
        if (taskCount == tasks.length) {
            System.out.println(INDENT + "OOPS!!! You cannot add more than 100 tasks.");
            return taskCount;
        }

        tasks[taskCount] = task;
        taskCount++;
        printAddedTask(tasks[taskCount - 1], taskCount);
        return taskCount;
    }

    /**
     * Validates and adds a deadline task from its details after the command word.
     *
     * @param tasks the task storage array
     * @param taskCount the current number of tasks
     * @param details the deadline description and /by value
     * @return the updated task count
     */
    private static int addDeadline(Task[] tasks, int taskCount, String details) {
        int byIndex = details.indexOf("/by ");
        if (byIndex < 0 && details.endsWith("/by")) {
            byIndex = details.length() - 3;
        }
        if (byIndex < 0) {
            System.out.println(INDENT + "OOPS!!! A deadline must include /by followed by a date or time.");
            return taskCount;
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + 3).trim();
        if (description.isEmpty()) {
            System.out.println(INDENT + "OOPS!!! The description of a deadline cannot be empty.");
        } else if (by.isEmpty()) {
            System.out.println(INDENT + "OOPS!!! The deadline cannot be empty.");
        } else {
            taskCount = addTask(tasks, taskCount, new Deadline(description, by));
        }
        return taskCount;
    }

    /**
     * Validates and adds an event task from its details after the command word.
     *
     * @param tasks the task storage array
     * @param taskCount the current number of tasks
     * @param details the event description, /from value, and /to value
     * @return the updated task count
     */
    private static int addEvent(Task[] tasks, int taskCount, String details) {
        int fromIndex = details.indexOf("/from ");
        int toIndex = details.indexOf("/to ");
        if (toIndex < 0 && details.endsWith("/to")) {
            toIndex = details.length() - 3;
        }
        if (fromIndex < 0 || toIndex < 0) {
            System.out.println(INDENT + "OOPS!!! An event must include /from and /to times.");
            return taskCount;
        }
        if (toIndex < fromIndex) {
            System.out.println(INDENT + "OOPS!!! The /from time must come before the /to time.");
            return taskCount;
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + 5, toIndex).trim();
        String to = details.substring(toIndex + 3).trim();
        if (description.isEmpty()) {
            System.out.println(INDENT + "OOPS!!! The description of an event cannot be empty.");
        } else if (from.isEmpty()) {
            System.out.println(INDENT + "OOPS!!! The start time of an event cannot be empty.");
        } else if (to.isEmpty()) {
            System.out.println(INDENT + "OOPS!!! The end time of an event cannot be empty.");
        } else {
            taskCount = addTask(tasks, taskCount, new Event(description, from, to));
        }
        return taskCount;
    }

    /**
     * Marks or unmarks a task after validating its one-based task number.
     *
     * @param tasks the task storage array
     * @param taskCount the current number of tasks
     * @param command the complete mark or unmark command
     * @param shouldMark whether the task should be marked done
     */
    private static void updateTaskStatus(Task[] tasks, int taskCount, String command, boolean shouldMark) {
        String action = shouldMark ? "mark" : "unmark";
        String numberText = command.substring(action.length()).trim();
        if (numberText.isEmpty()) {
            System.out.println(INDENT + "OOPS!!! Please provide a task number to " + action + ".");
            return;
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            System.out.println(INDENT + "OOPS!!! The task number to " + action
                    + " must be a positive whole number.");
            return;
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            System.out.println(INDENT + "OOPS!!! There is no task with that number.");
            return;
        }

        Task task = tasks[taskNumber - 1];
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
