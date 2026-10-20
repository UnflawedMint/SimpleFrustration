package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameManager {
    private final List<GameObserver> observers = new ArrayList<>();
    private final Board board;
    private final List<Player> players = new ArrayList<>();

    // A stack to keep track of command history for Undo
    private final Stack<Command> commandHistory = new Stack<>();

    private HitRuleStrategy hitRuleStrategy = new IgnoreHitStrategy();
    private WinRuleStrategy winRuleStrategy = new AllowOvershootWinStrategy();

    public GameManager(Board board) {
        this.board = board;
    }

    public Board getBoard() { return board; }
    public List<Player> getPlayers() { return players; }
    public HitRuleStrategy getHitRuleStrategy() { return hitRuleStrategy; }
    public WinRuleStrategy getWinRuleStrategy() { return winRuleStrategy; }

    public void setHitRuleStrategy(HitRuleStrategy hitRuleStrategy) { this.hitRuleStrategy = hitRuleStrategy; }
    public void setWinRuleStrategy(WinRuleStrategy winRuleStrategy) { this.winRuleStrategy = winRuleStrategy; }
    public void addPlayer(Player player) { players.add(player); }
    public void addObserver(GameObserver observer) { observers.add(observer); }

    public void notifyPlayerMoved(Player player, int diceRoll, BoardPosition oldPos, BoardPosition newPos) {
        for (GameObserver observer : observers) { observer.onPlayerMoved(player, diceRoll, oldPos, newPos); }
    }

    public void notifyPlayerHit(Player hitPlayer, BoardPosition hitPosition) {
        for (GameObserver observer : observers) { observer.onPlayerHit(hitPlayer, hitPosition); }
    }

    public void notifyPlayerOvershoot(Player player) {
        for (GameObserver observer : observers) { observer.onPlayerOvershoot(player); }
    }

    public void notifyUndo(Player player, BoardPosition restoredPosition) {
        for (GameObserver observer : observers) { observer.onUndo(player, restoredPosition); }
    }

    // Start a turn using the command pattern
    public void simulateTurn(Player player, DiceShaker dice) {
        Command turnCommand = new TurnCommand(this, player, dice);
        turnCommand.execute();
        commandHistory.push(turnCommand); // save the command for possible undo
    }

    // Pops the last command from the history stack and undo
    public void undoLastTurn() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.pop();
            lastCommand.undo();
        } else {
            System.out.println("Nothing to undo!");
        }
    }
}