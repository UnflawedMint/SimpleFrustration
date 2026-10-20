package game;

// Strategy to send player to home when hit

public class SendHomeHitStrategy implements HitRuleStrategy {
    @Override
    public void applyHit(Player movingPlayer, Player targetPlayer, GameManager gameManager) {
        // Send player back to home
        targetPlayer.moveTo(targetPlayer.getHomePosition());

        // Notify observers there is a hit event
        gameManager.notifyPlayerHit(targetPlayer, movingPlayer.getCurrentPosition());
    }
}