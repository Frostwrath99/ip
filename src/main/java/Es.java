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
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                break;
            }

            System.out.println(DIVIDER);
            if (command.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(INDENT + (i + 1) + ". " + tasks[i]);
                }
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
