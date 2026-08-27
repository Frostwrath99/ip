package es;

/** Command action that terminates the chatbot loop. */
public class ExitCommand extends CommandAction {
    @Override
    public void execute() { }

    @Override
    public boolean isExit() { return true; }
}
