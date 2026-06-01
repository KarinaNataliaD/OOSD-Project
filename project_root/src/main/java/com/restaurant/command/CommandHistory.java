package com.restaurant.command;

import java.util.ArrayDeque; 
import java.util.Deque;

public class CommandHistory {
 
    private final Deque<Command> history = new ArrayDeque<>();
 
    
    public void execute(Command command) {
        System.out.printf("%n  >> Executing: %s%n", command.getDescription());
        command.execute();
        history.push(command);
    }
 
    public void undo() {
        if (history.isEmpty()) {
            System.out.println("  [UNDO] Nothing to undo.");
            return;
        }
        Command last = history.pop();
        System.out.printf("%n  >> Undoing: %s%n", last.getDescription());
        last.undo();
    }
 
    public boolean hasHistory() {
        return !history.isEmpty();
    }
 
    public int historySize() {
        return history.size();
    }
}
