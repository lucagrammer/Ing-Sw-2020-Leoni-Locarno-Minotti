package Util;

import Model.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a set of player actions
 */
public class RoundActions {

    private List<Action> actionList;

    /**
     * Constructor: build an empty RoundActions
     */
    public RoundActions() {
        actionList = new ArrayList<>();
    }

    /**
     * Gets the actions list of the current round
     *
     * @return The actions list of the current round
     */
    public List<Action> getActionList() {
        return new ArrayList<>(actionList);
    }

    /**
     * Gets the number of times the player has moved a worker during the current round
     * @return The number of times that the worker has moved
     */
    public int hasMoved() {
        int counter = 0;
        for (Action action : actionList) {
            if (action.getActionType() == ActionType.MOVE)
                counter++;
        }
        return counter;
    }

    /**
     * Gets the number of times the player has build (a floor or a couple) during the current round
     *
     * @return The number of times that player has build
     */
    public int hasBuildAnything() {
        int counter = 0;
        for (Action action : actionList) {
            if (action.getActionType() == ActionType.BUILD_DOME || action.getActionType() == ActionType.BUILD_FLOOR)
                counter++;
        }
        return counter;
    }

    /**
     * Checks if it contains at least one move
     *
     * @return True if it contains at least one move, otherwise false
     */
    public boolean isEmpty() {
        return actionList.size() == 0;
    }

    /**
     * Adds a set of moves to the actions container
     *
     * @param cells  Set of reachable cells
     * @param origin Starting cell
     * @param genre  Genre of the worker
     */
    public void addMoves(List<Cell> cells, Cell origin, Genre genre) {
        for (Cell cell : cells) {
            actionList.add(new Action(ActionType.MOVE, genre, origin.calculateDirection(cell), origin.getFloorDifference(cell)));
        }
    }

    /**
     * Adds a set of build-actions to the actions container
     *
     * @param cells  Set of reachable cells
     * @param origin Starting cell
     * @param genre  Genre of the worker
     */
    public void addBuilds(List<Cell> cells, Cell origin, Genre genre) {
        for (Cell cell : cells) {
            actionList.add(new Action(ActionType.BUILD_FLOOR, genre, origin.calculateDirection(cell), origin.getFloorDifference(cell)));
        }
    }

    /**
     * Adds a set of dome-actions to the action container
     *
     * @param cells  Set of reachable cells
     * @param origin Starting cell
     * @param genre  Genre of the worker
     */
    public void addDomes(List<Cell> cells, Cell origin, Genre genre) {
        for (Cell cell : cells) {
            actionList.add(new Action(ActionType.BUILD_DOME, genre, origin.calculateDirection(cell), origin.getFloorDifference(cell)));
        }
    }

    /**
     * Adds all the actions stored in another Round Actions object
     *
     * @param actions The Round Action object that contains the actions to be added
     */
    public void add(RoundActions actions) {
        actionList.addAll(actions.getActionList());
    }

    /**
     * Adds a list of actions
     *
     * @param actions The list of actions to be added
     */
    public void add(List<Action> actions) {
        actionList.addAll(actions);
    }

    /**
     * Adds a single action
     *
     * @param action The action to be added
     */
    public void add(Action action) {
        actionList.add(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoundActions that = (RoundActions) o;
        return actionList.containsAll(that.getActionList()) && that.getActionList().containsAll(actionList);
    }

    /**
     * Checks if it contains at least one move up
     *
     * @return True if it contains at least one move up, otherwise false
     */
    public boolean hasMovedUp() {
        for (Action action : actionList) {
            if (action.getActionType() == ActionType.MOVE && action.getLevelDifference() > 0)
                return true;
        }
        return false;
    }
}
