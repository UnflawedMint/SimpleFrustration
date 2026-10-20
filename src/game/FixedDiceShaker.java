package game;

// A special dice shaker used for testing and debugging

public class FixedDiceShaker implements DiceShaker {
    private final int[] fixedShakes;
    private int index = 0;

    public FixedDiceShaker(int[] fixedShakes) {
        this.fixedShakes = fixedShakes;
    }

    @Override
    public int shake() {
        int result = fixedShakes[index];
        index++; // Move to the next value

        // Loop back to the start if reach the end of the array
        if (index >= fixedShakes.length) {
            index = 0;
        }
        return result;
    }
}