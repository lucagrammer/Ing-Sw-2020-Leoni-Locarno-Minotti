package Util;

/**
 * Stores useful text-formatting ansi codes
 */
public enum Formatter {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    BOLD("\u001b[1m"),
    ITALIC("\u001B[3m"),
    UNDERLINE("\u001b[4m"),
    REVERSED("\u001b[7m"),
    RESET("\u001B[0m");
    private String escape;

    Formatter(String escape) {
        this.escape = escape;
    }

    /**
     * Gets a string containing colored text
     *
     * @param color The color
     * @param text  The text to be colored
     * @return The colored string
     */
    public static String cText(char color, String text) {
        color = Character.toLowerCase(color);
        Formatter myColor;
        switch (color) {
            case 'r':
                myColor = RED;
                break;
            case 'b':
                myColor = BLUE;
                break;
            case 'g':
                myColor = GREEN;
                break;
            case 'y':
                myColor = YELLOW;
                break;
            default:
                myColor = RESET;
        }
        return myColor + text + RESET;
    }

    /**
     * Gets a string containing formatted text
     *
     * @param style The style
     * @param text  The text to be formatted
     * @return The colored string
     */
    public static String fText(char style, String text) {
        style = Character.toLowerCase(style);
        Formatter myStyle;
        switch (style) {
            case 'i':
                myStyle = ITALIC;
                break;
            case 'b':
                myStyle = BOLD;
                break;
            case 'r':
                myStyle = REVERSED;
                break;
            case 'u':
                myStyle = UNDERLINE;
                break;
            default:
                myStyle = RESET;
        }
        return myStyle + text + RESET;
    }

    /**
     * Gets an heading text
     *
     * @param s The string to be formatted
     * @return The formatted string
     */
    public static String headingText(String s) {
        int max = 70;
        String padding = "";
        for (int i = 0; i < (max - s.length()) / 2; i++) {
            padding += " ";
        }
        return BOLD + "" + REVERSED + padding + s + padding + RESET + "\n";
    }

    @Override
    public String toString() {
        return this.escape;
    }
}
