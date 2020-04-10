package Util;

/**
 * Stores information about a user's actions
 */
public class Action {

    private ActionType actionType;
    private Genre genre;
    private Direction direction;
    private int levelDifference;

    /**
     * Constructor: build the Action
     *
     * @param genre           The genre of the worker that does the action
     * @param actionType      The action type
     * @param direction       The direction of the action
     * @param levelDifference The level difference (positive or negative) from the current position of the worker
     */
    public Action(ActionType actionType, Genre genre, Direction direction, int levelDifference) {
        this.actionType = actionType;
        this.genre = genre;
        this.direction = direction;
        this.levelDifference = levelDifference;
    }

    /**
     * Constructor: build the Action
     *
     * @param actionType the action type
     */
    public Action(ActionType actionType) {
        this.actionType = actionType;
    }

    /**
     * Gets the Genre of the worker that do the action
     *
     * @return The genre of the worker
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * Sets the Genre of the worker that does the action
     *
     * @param genre The genre of the worker
     */
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /**
     * Gets the ActionType of the action
     *
     * @return The action type
     */
    public ActionType getActionType() {
        return actionType;
    }

    /**
     * Sets the ActionType of the action
     *
     * @param action The action type
     */
    public void setActionType(ActionType action) {
        this.actionType = action;
    }

    /**
     * Gets the Direction of the action
     *
     * @return The direction of the action
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the Direction of the action
     *
     * @param direction The direction of the action
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Gets the level difference (positive or negative) between the current cell and the target cell of the action
     *
     * @return The level difference (positive or negative)
     */
    public int getLevelDifference() {
        return levelDifference;
    }

    /**
     * Sets the level difference (positive or negative) between the current cell and the target cell of the action
     *
     * @param levelDifference The level difference (positive or negative)
     */
    public void setLevelDifference(int levelDifference) {
        this.levelDifference = levelDifference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return levelDifference == action.levelDifference &&
                actionType == action.actionType &&
                genre == action.genre &&
                direction == action.direction;
    }

}
