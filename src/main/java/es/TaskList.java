package es;

import java.util.ArrayList;

/** Owns the in-memory collection of chatbot tasks. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() { this(new ArrayList<>()); }

    /** Wraps tasks loaded from storage. */
    public TaskList(ArrayList<Task> tasks) { this.tasks = tasks; }

    /** Adds a task. */
    public void add(Task task) { tasks.add(task); }

    /** Removes and returns a task at a zero-based index. */
    public Task remove(int index) { return tasks.remove(index); }

    /** Returns a task at a zero-based index. */
    public Task get(int index) { return tasks.get(index); }

    /** Returns the number of tasks. */
    public int size() { return tasks.size(); }

    /** Returns the underlying list for persistence. */
    public ArrayList<Task> asList() { return tasks; }
}
