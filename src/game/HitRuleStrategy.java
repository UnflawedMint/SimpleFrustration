package game;

// Strategy interface for handling hit

public interface HitRuleStrategy {
    void applyHit(Player movingPlayer, Player targetPlayer, GameManager gameManager);
}