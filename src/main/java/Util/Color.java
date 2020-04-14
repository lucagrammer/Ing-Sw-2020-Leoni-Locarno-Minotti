package Util;

import java.util.ArrayList;
import java.util.List;

/**
 * All possible colors of the players/workers
 */
public enum Color {
    PURPLE("Purple"), YELLOW("Yellow"), BLUE("Blue");

    String name;

    /**
     * Constructor: build a Color
     *
     * @param name The name of the color
     */
    Color(String name) {
        this.name = name;
    }

    /**
     * Gets a Color by its name
     *
     * @param color The name of the color
     * @return The appropriate Color
     */
    public static Color getColorByName(String color) {
        if (color.equalsIgnoreCase(PURPLE.toString()))
            return PURPLE;
        if (color.equalsIgnoreCase(YELLOW.toString()))
            return YELLOW;
        if (color.equalsIgnoreCase(BLUE.toString()))
            return BLUE;
        return null;
    }

    /**
     * Returns all the possible colors
     *
     * @return A list containing all the possible colors
     */
    public static List<String> allColorsToString() {
        List<String> allColors = new ArrayList<>();
        allColors.add(PURPLE.toString());
        allColors.add(YELLOW.toString());
        allColors.add(BLUE.toString());
        return allColors;
    }

    /**
     * Gets the name of the color
     *
     * @return The name of the color
     */
    public String toString() {
        return name;
    }

}
