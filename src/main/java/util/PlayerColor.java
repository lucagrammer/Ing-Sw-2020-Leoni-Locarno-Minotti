package util;

import java.awt.*;
import java.util.ArrayList;

/**
 * All the possible colors of the players/workers
 */
public enum PlayerColor {
    BLUE("Blue", new Color(23, 161, 200)),
    PURPLE("Purple", new Color(226, 150, 169)),
    YELLOW("Yellow", new Color(245, 223, 111));

    private final String name;
    private final Color color;

    /**
     * Constructor: build a PlayerColor
     *
     * @param name  The name of the color
     * @param color The related Color object
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
        return PlayerColor.valueOf(color.toUpperCase());
    }

    /**
     * Gets the Color by its name
     *
     * @param color The name of the color
     * @return The appropriate Color
     */
    public static Color getColorCodeByName(String color) {
        return PlayerColor.valueOf(color.toUpperCase()).color;
    }

    /**
     * Returns all the possible colors
     *
     * @return A list containing all the possible colors
     */
    public static ArrayList<String> allColorsToString() {
        ArrayList<String> allColors = new ArrayList<>();
        for (PlayerColor color : PlayerColor.values()) {
            allColors.add(color.name);
        }
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
