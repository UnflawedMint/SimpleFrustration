package game;

// Representing a player

public class Player {
    private final PlayerColor color;
    private final BoardPosition homePosition; // The starting position for this player
    private BoardPosition currentPosition;    // The player's current position on the board
    private int turnCount;                    // Tracks how many rounds are executed

    public Player(PlayerColor color, BoardPosition homePosition) {
        this.color = color;
        this.homePosition = homePosition;
        this.currentPosition = homePosition; // Players start at their home position
        this.turnCount = 0;
    }

    // Query

    public PlayerColor getColor() {
        return color;
    }

    public BoardPosition getHomePosition() {
        return homePosition;
    }

    public BoardPosition getCurrentPosition() {
        return currentPosition;
    }

    public int getTurnCount() {
        return turnCount;
    }

    // Returns a capitalized string of the player's color
    public String getName() {
        String name = color.toString();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    // Command operations

    // Moves the player to a new position
    public void moveTo(BoardPosition newPosition) {
        this.currentPosition = newPosition;
    }

    // Player's turn count++
    public void incrementTurn() {
        this.turnCount++;
    }

    // Undo, player's turn count--
    public void decrementTurn() {
        this.turnCount--;
    }
}