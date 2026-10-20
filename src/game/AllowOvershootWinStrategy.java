package game;

// Basic function that allows player overshoot to win

public class AllowOvershootWinStrategy implements WinRuleStrategy {
    @Override
    public BoardPosition resolveTailMove(Player player, int stepsIntoTail, int tailPositions, int divPoint, int mainPositions, GameManager gameManager) {
        if (stepsIntoTail >= tailPositions) {
            return new BoardPosition(PositionType.END, 0); // Player wins
        } else {
            return new BoardPosition(PositionType.TAIL, stepsIntoTail); // Stays in the tail
        }
    }
}