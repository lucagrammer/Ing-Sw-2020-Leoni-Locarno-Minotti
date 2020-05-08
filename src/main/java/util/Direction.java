package util;

/**
 * All possible directions of the actions
 */
public enum Direction {
    N, NE, E, SE, S, SW, W, NW;

    /**
     * Calculates the direction from row and column differences
     *
     * @param rowDifference    The row difference (next-current)
     * @param columnDifference The column difference (next-current)
     * @return The direction in which you need to move
     */
    public static Direction fromDiff(int rowDifference, int columnDifference) {
        Direction[][] possibleDirections = {{NW, N, NE}, {W, null, E}, {SW, S, SE}};
        return possibleDirections[rowDifference + 1][columnDifference + 1];
    }
}
