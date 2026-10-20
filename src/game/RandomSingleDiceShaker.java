package game;

import java.util.Random;

// Implementation of a single dice shaker

public class RandomSingleDiceShaker implements DiceShaker {
    private final Random random = new Random();

    @Override
    public int shake() {
        // Generate a random number in [1,6]
        return random.nextInt(6) + 1;
    }
}