package game;

// Implementation of a double dice shaker

public class RandomDoubleDiceShaker implements DiceShaker {
    // Reuse single dice's logic
    private final DiceShaker singleShaker = new RandomSingleDiceShaker();

    @Override
    public int shake() {
        // Shake the single dice twice
        return singleShaker.shake() + singleShaker.shake();
    }
}