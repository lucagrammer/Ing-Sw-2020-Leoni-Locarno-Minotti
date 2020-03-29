package Util;

public class Action {

    private ActionType actionType;
    private Genre genre;
    private Direction direction;
    private int levelDifference;
    private boolean winning;
    private boolean influenceRivals;

    /**
     * Build the Action
     *
     * @param genre           the genre of the worker that can do that action
     * @param actionType      the action that the worker can do
     * @param direction       the direction of the action
     * @param levelDifference the level difference (positive or negative) from the current position of the worker
     * @param winning         true if the action is a winning action
     * @param influenceRivals true if the action may influence the actions of the enemy workers
     */
    public Action(ActionType actionType, Genre genre, Direction direction, int levelDifference, boolean winning, boolean influenceRivals) {
        this.actionType = actionType;
        this.genre = genre;
        this.direction = direction;
        this.levelDifference = levelDifference;
        this.winning = winning;
        this.influenceRivals = influenceRivals;
    }

    /**
     * Gets the Genre of the worker that can do the action
     *
     * @return the genre of the worker
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * Sets the Genre of the worker that can do the action
     *
     * @param genre the genre of the worker
     */
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /**
     * Gets the ActionType of the action
     *
     * @return the action type
     */
    public ActionType getActionType() {
        return actionType;
    }

    /**
     * Sets the ActionTyper of the action
     *
     * @param action the action type
     */
    public void setActionType(ActionType action) {
        this.actionType = action;
    }

    /**
     * Gets the Direction of the action
     *
     * @return the direction of the action
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the Direction of the action
     *
     * @param direction the direction of the action
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Gets the level difference (positive or negative) from the current position of the worker
     *
     * @return the level difference (positive or negative)
     */
    public int getLevelDifference() {
        return levelDifference;
    }

    /**
     * Sets the level difference (positive or negative) from the current position of the worker
     *
     * @param levelDifference the level difference (positive or negative)
     */
    public void setLevelDifference(int levelDifference) {
        this.levelDifference = levelDifference;
    }

    /**
     * Tests if the action may influence the actions of the enemy workers
     *
     * @return true if the action may influence the actions of the enemy workers, otherwise false
     */
    public boolean isInfluenceRivals() {
        return influenceRivals;
    }

    /**
     * Sets if the action may influence the actions of the enemy workers
     *
     * @param influenceRivals true if the the action may influence the actions of the enemy workers , otherwise false
     */
    public void setInfluenceRivals(boolean influenceRivals) {
        this.influenceRivals = influenceRivals;
    }

    /**
     * Tests if the action is a winning action
     *
     * @return true if the action is a winning action, otherwise false;
     */
    public boolean isWinning() {
        return winning;
    }

    /**
     * Sets if the action is a winning action
     *
     * @param winning true if the action is a winning action, otherwise false;
     */
    public void setWinning(boolean winning) {
        this.winning = winning;
    }
}
