package game;

import java.util.Objects;

// Value object representing a position on the game board

public final class BoardPosition {
    private final PositionType type;
    private final int index; // The numerical index

    public BoardPosition(PositionType type, int index) {
        this.type = type;
        this.index = index;
    }

    public PositionType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPosition that = (BoardPosition) o;
        return index == that.index && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, index);
    }

    // Formats the position into the string
    @Override
    public String toString() {
        switch (type) {
            case HOME:
                return "HOME (Position " + index + ")";
            case END:
                return "END";
            case TAIL:
                return "TAIL (Tail Position " + index + ")";
            case MAIN:
            default:
                return "Position " + index;
        }
    }
}