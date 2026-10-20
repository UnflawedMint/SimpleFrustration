package game;

// Observer that prints formatted game events to the console

public class ConsoleLogger implements GameObserver {

    @Override
    public void onPlayerMoved(Player player, int diceRoll, BoardPosition oldPos, BoardPosition newPos) {
        // Formats and outputs the strings
        System.out.println(player.getName() + " play " + player.getTurnCount() + " rolls " + diceRoll);
        System.out.println(player.getName() + " moves from " + oldPos.toString() + " to " + newPos.toString());
    }

    @Override
    public void onPlayerHit(Player hitPlayer, BoardPosition hitPosition) {
        // Formats the hit output
        System.out.println(hitPlayer.getName() + " " + hitPosition.toString() + " hit");
        System.out.println(hitPlayer.getName() + " moves from " + hitPosition.toString() + " to " + hitPlayer.getHomePosition().toString());
    }

    @Override
    public void onPlayerOvershoot(Player player) {
        System.out.println(player.getName() + " overshoots");
    }

    @Override
    public void onUndo(Player player, BoardPosition restoredPosition) {
        System.out.println("Undo");
        System.out.println(player.getName() + " moves from current position to " + restoredPosition.toString());
    }
}