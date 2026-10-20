package game;

// Strategy interface for handling the win rules and tail movements

public interface WinRuleStrategy {
    BoardPosition resolveTailMove(Player player, int stepsIntoTail, int tailPositions, int divPoint, int mainPositions, GameManager gameManager);
}