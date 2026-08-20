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

        String[] tasks = new String[100];
        boolean[] isDone = new boolean[100];
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
                    String status = isDone[i] ? "[X]" : "[ ]";
                    System.out.println(INDENT + (i + 1) + "." + status + " " + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                isDone[taskIndex] = true;
                System.out.println(INDENT + "Nice! I've marked this task as done:");
                System.out.println(INDENT + "  [X] " + tasks[taskIndex]);
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println(INDENT + "added: " + command);
            }
            System.out.println(DIVIDER);
        }

        System.out.println(DIVIDER);
        System.out.println(INDENT + "Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }
}
