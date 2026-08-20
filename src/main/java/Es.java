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
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                break;
            }

            System.out.println(DIVIDER);
            if (command.equals("list")) {
                System.out.println(INDENT + "Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(INDENT + (i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(INDENT + "Nice! I've marked this task as done:");
                System.out.println(INDENT + "  " + tasks[taskIndex]);
            } else if (command.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(command.substring(7)) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println(INDENT + "OK, I've marked this task as not done yet:");
                System.out.println(INDENT + "  " + tasks[taskIndex]);
            } else if (command.startsWith("todo ")) {
                tasks[taskCount] = new Todo(command.substring(5));
                taskCount++;
                printAddedTask(tasks[taskCount - 1], taskCount);
            } else if (command.startsWith("deadline ")) {
                int byIndex = command.indexOf(" /by ");
                String description = command.substring(9, byIndex);
                String by = command.substring(byIndex + 5);
                tasks[taskCount] = new Deadline(description, by);
                taskCount++;
                printAddedTask(tasks[taskCount - 1], taskCount);
            } else if (command.startsWith("event ")) {
                int fromIndex = command.indexOf(" /from ");
                int toIndex = command.indexOf(" /to ");
                String description = command.substring(6, fromIndex);
                String from = command.substring(fromIndex + 7, toIndex);
                String to = command.substring(toIndex + 5);
                tasks[taskCount] = new Event(description, from, to);
                taskCount++;
                printAddedTask(tasks[taskCount - 1], taskCount);
            } else {
                tasks[taskCount] = new Todo(command);
                taskCount++;
                printAddedTask(tasks[taskCount - 1], taskCount);
            }
            System.out.println(DIVIDER);
        }

        System.out.println(DIVIDER);
        System.out.println(INDENT + "Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
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
