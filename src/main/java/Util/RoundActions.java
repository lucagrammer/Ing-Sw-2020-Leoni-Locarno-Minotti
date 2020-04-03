package Util;

import Model.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoundActions {
    private List<Action> actionList;

    public RoundActions() {
        actionList = new ArrayList<>();
    }

    /**
     * Gets the action list of the current round
     *
     * @return
     */
    public List<Action> getActionList() {
        return new ArrayList<>(actionList);
    }

    /**
     * Gets the number of times the player has moved a worker during the current round
     *
     * @return the number of times that the worker has moved
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
     * @return the number of times that player has build
     */
    public int hasBuildAnything() {
        int counter = 0;
        for (Action action : actionList) {
            if (action.getActionType() == ActionType.BUILD_DOME || action.getActionType() == ActionType.BUILD_FLOOR)
                counter++;
        }
        return counter;
    }

    public boolean isEmpty() {
        return actionList.size() == 0;
    }


    public void addMoves(List<Cell> cells, Cell origin, Genre genre) {
        for (Cell cell : cells) {
            actionList.add(new Action(ActionType.MOVE, genre, origin.calculateDirection(cell), origin.getFloorDifference(cell)));
        }
    }

    public void addBuilds(List<Cell> cells, Cell origin, Genre genre) {
        for (Cell cell : cells) {
            actionList.add(new Action(ActionType.BUILD_FLOOR, genre, origin.calculateDirection(cell), origin.getFloorDifference(cell)));
        }
    }

    public void addDomes(List<Cell> cells, Cell origin, Genre genre) {
        for (Cell cell : cells) {
            actionList.add(new Action(ActionType.BUILD_DOME, genre, origin.calculateDirection(cell), origin.getFloorDifference(cell)));
        }
    }

    public void add(RoundActions actions) {
        actionList.addAll(actions.getActionList());

    }

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
}
