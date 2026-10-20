package game;

// The Command interface for the command pattern

public interface Command {
    void execute(); // Executes the game turn
    void undo();    // Restores the exact state before the turn was executed
}