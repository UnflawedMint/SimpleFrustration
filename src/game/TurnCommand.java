package game;

// Command encapsulating a single player's turn

public class TurnCommand implements Command {
    private final GameManager gameManager;
    private final Player movingPlayer;
    private final DiceShaker dice;

    // State required for undoing the move
    private BoardPosition previousPosition;
    private int rolledValue;

    // State required for undoing a hit
    private Player hitPlayer = null;
    private BoardPosition hitPlayerPreviousPosition = null;

    public TurnCommand(GameManager gameManager, Player movingPlayer, DiceShaker dice) {
        this.gameManager = gameManager;
        this.movingPlayer = movingPlayer;
        this.dice = dice;
    }

    @Override
    public void execute() {
        this.previousPosition = movingPlayer.getCurrentPosition();
        this.rolledValue = dice.shake();
        movingPlayer.incrementTurn();

        // Calculate and apply the new position
        BoardPosition newPos = gameManager.getBoard().calculateMove(movingPlayer, rolledValue, gameManager.getWinRuleStrategy(), gameManager);
        movingPlayer.moveTo(newPos);
        gameManager.notifyPlayerMoved(movingPlayer, rolledValue, previousPosition, newPos);

        // Check if hit
        if (newPos.getType() != PositionType.HOME && newPos.getType() != PositionType.END) {
            for (Player target : gameManager.getPlayers()) {
                if (target != movingPlayer && target.getCurrentPosition().equals(newPos)) {
                    // Before apply the hit, record the state of the player who get hit
                    this.hitPlayer = target;
                    this.hitPlayerPreviousPosition = target.getCurrentPosition();

                    // Hit happens
                    gameManager.getHitRuleStrategy().applyHit(movingPlayer, target, gameManager);
                    break;
                }
            }
        }
    }

    @Override
    public void undo() {
        // Restore the moving player's position and turn count
        movingPlayer.moveTo(previousPosition);
        movingPlayer.decrementTurn();

        // If someone was hit during this turn, they will back to the previous position
        if (hitPlayer != null && hitPlayerPreviousPosition != null) {
            hitPlayer.moveTo(hitPlayerPreviousPosition);
            gameManager.notifyUndo(hitPlayer, hitPlayerPreviousPosition);
        }

        // Notify observers
        gameManager.notifyUndo(movingPlayer, previousPosition);
    }
}