package game;

// The Observer interface for the game

public interface GameObserver {
    //Should be called whenever a player successfully moves to a new position
    void onPlayerMoved(Player player, int diceRoll, BoardPosition oldPos, BoardPosition newPos);

    void onPlayerHit(Player hitPlayer, BoardPosition hitPosition);

    void onPlayerOvershoot(Player player);

    void onUndo(Player player, BoardPosition restoredPosition);
}