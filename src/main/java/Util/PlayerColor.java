package Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * All possible colors of the players/workers
 */
public enum PlayerColor {
    PURPLE("Purple", new Color(226, 150, 169)),
    YELLOW("Yellow", new Color(245, 223, 111)),
    BLUE("Blue", new Color(139, 232, 234));

    private final String name;
    private final Color color;

    /**
     * Constructor: build a PlayerColor
     *
     * @param name The name of the color
     */
    PlayerColor(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Gets a PlayerColor by its name
     *
     * @param color The name of the color
     * @return The appropriate PlayerColor
     */
    public static PlayerColor getColorByName(String color) {
        if (color.equalsIgnoreCase(PURPLE.toString()))
            return PURPLE;
        if (color.equalsIgnoreCase(YELLOW.toString()))
            return YELLOW;
        if (color.equalsIgnoreCase(BLUE.toString()))
            return BLUE;
        return null;
    }


    /**
     * Gets a Color by its name
     *
     * @param color The name of the color
     * @return The appropriate Color
     */
    public static Color getColorCodeByName(String color) {
        if (color.equalsIgnoreCase(PURPLE.toString()))
            return PURPLE.color;
        if (color.equalsIgnoreCase(YELLOW.toString()))
            return YELLOW.color;
        if (color.equalsIgnoreCase(BLUE.toString()))
            return BLUE.color;
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
