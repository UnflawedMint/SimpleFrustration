package game;

// Strategy that a player must land exactly on the end position

public class ExactWinStrategy implements WinRuleStrategy {
    @Override
    public BoardPosition resolveTailMove(Player player, int stepsIntoTail, int tailPositions, int divPoint, int mainPositions, GameManager gameManager) {
        if (stepsIntoTail == tailPositions) {
            return new BoardPosition(PositionType.END, 0); // Exact win
        } else if (stepsIntoTail > tailPositions) {
            // Overshoot and print the log
            gameManager.notifyPlayerOvershoot(player);

            // Calculate the distance player bounces back from end
            int overshoot = stepsIntoTail - tailPositions;
            int backoffPos = tailPositions - overshoot;

            if (backoffPos > 0) {
                // The player bounced back but is still inside the tail
                return new BoardPosition(PositionType.TAIL, backoffPos);
            } else {
                // The player bounced completely out of the tail and back onto the main board
                int mainIndex = divPoint + backoffPos;
                if (mainIndex <= 0) {
                    mainIndex += mainPositions; // Go back to the circular board
                }
                return new BoardPosition(PositionType.MAIN, mainIndex);
            }
        } else {
            return new BoardPosition(PositionType.TAIL, stepsIntoTail); // Normal tail move
        }
    }
}