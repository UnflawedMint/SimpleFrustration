package game;

import java.util.HashMap;
import java.util.Map;

// Determining the next location according to the dice throw

public class Board {
    private final int mainPositions;
    private final int tailPositions;
    private final Map<PlayerColor, Integer> homeIndices = new HashMap<>();

    public Board(int mainPositions, int tailPositions) {
        this.mainPositions = mainPositions;
        this.tailPositions = tailPositions;
    }

    // Maps a player color to their starting index on the main board
    public void setHomeIndex(PlayerColor color, int index) {
        homeIndices.put(color, index);
    }

    // Determines the new location of a player after the dice roll

    public BoardPosition calculateMove(Player player, int roll, WinRuleStrategy winStrategy, GameManager gameManager) {
        BoardPosition currentPos = player.getCurrentPosition();
        PlayerColor color = player.getColor();
        int homeIndex = homeIndices.get(color);
        int divPoint = (homeIndex == 1) ? mainPositions : homeIndex - 1;

        if (currentPos.getType() == PositionType.HOME || currentPos.getType() == PositionType.MAIN) {
            int currentIndex = (currentPos.getType() == PositionType.HOME) ? homeIndex : currentPos.getIndex();

            int distToDiv;
            if (currentIndex <= divPoint) {
                distToDiv = divPoint - currentIndex;
            } else {
                distToDiv = (mainPositions - currentIndex) + divPoint;
            }

            if (roll <= distToDiv) {
                int nextIndex = currentIndex + roll;
                if (nextIndex > mainPositions) {
                    nextIndex -= mainPositions;
                }
                return new BoardPosition(PositionType.MAIN, nextIndex);
            } else {
                int stepsIntoTail = roll - distToDiv;
                return winStrategy.resolveTailMove(player, stepsIntoTail, tailPositions, divPoint, mainPositions, gameManager); // Use strategy
            }
        } else if (currentPos.getType() == PositionType.TAIL) {
            int stepsIntoTail = currentPos.getIndex() + roll;
            return winStrategy.resolveTailMove(player, stepsIntoTail, tailPositions, divPoint, mainPositions, gameManager);
        }
        return currentPos;
    }
}