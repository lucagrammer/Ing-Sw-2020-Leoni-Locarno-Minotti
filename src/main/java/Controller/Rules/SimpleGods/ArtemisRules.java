package Controller.Rules.SimpleGods;

import Controller.Rules.Rules;
import Model.Cell;
import Model.Game;
import Model.Player;
import Model.Worker;
import Util.*;

import java.util.List;
import java.util.stream.Collectors;

public class ArtemisRules extends Rules {

    public RoundActions nextPossibleActions(Player player, Game game) {
        RoundActions roundActions = player.getRoundActions();
        RoundActions nextPossibleActions = new RoundActions();

        // Has not moved?
        if (roundActions.hasMoved() == 0) {
            nextPossibleActions.add(getPossibleMoves(player.getWorker(Genre.MALE), game));
            nextPossibleActions.add(getPossibleMoves(player.getWorker(Genre.FEMALE), game));
            //Can't move?
            if (nextPossibleActions.isEmpty())
                nextPossibleActions.add(new Action(ActionType.LOSE));
        } else {
            Genre movedWorkerGenre = roundActions.
                    getActionList().
                    stream().
                    filter(x -> x.getActionType() == ActionType.MOVE).
                    collect(Collectors.toList()).
                    get(0).
                    getGenre();

            // Has not build?
            if (roundActions.hasBuildAnything() == 0) {
                // Has moved once?
                if (roundActions.hasMoved() == 1) {
                    nextPossibleActions.add(getPossibleMoves(player.getWorker(movedWorkerGenre), game));
                    nextPossibleActions.add(getPossibleBuilds(player.getWorker(movedWorkerGenre), game));
                    nextPossibleActions.add(getPossibleDomes(player.getWorker(movedWorkerGenre), game));
                    //Can't move/build anything?
                    if (nextPossibleActions.isEmpty())
                        nextPossibleActions.add(new Action(ActionType.LOSE));
                } else {
                    nextPossibleActions.add(getPossibleBuilds(player.getWorker(movedWorkerGenre), game));
                    nextPossibleActions.add(getPossibleDomes(player.getWorker(movedWorkerGenre), game));
                    //Can't build anything?
                    if (nextPossibleActions.isEmpty())
                        nextPossibleActions.add(new Action(ActionType.LOSE));
                }
            } else {
                nextPossibleActions.add(new Action(ActionType.END));
            }
        }
        return nextPossibleActions;
    }

    protected RoundActions getPossibleMoves(Worker worker, Game game) {
        RoundActions roundMoves = new RoundActions();
        Cell workerCell = worker.getPosition();

        // Add all the adjacent cells that don't have a dome and are reachable from the current positions of the worker
        List<Cell> cells = game.
                getBoard().
                getAdjacents(workerCell).
                stream().
                filter(x -> !x.getDome() && workerCell.getFloorDifference(x) <= 1).
                collect(Collectors.toList());

        // Remove all the cells that are already occupied
        for (Player gamePlayer : game.getPlayers()) {
            cells.removeAll(gamePlayer.getOccupiedCells());
        }

        // Remove the cell occupied by the previous move, if any
        Player player = game.getPlayersByColor(worker.getColor());
        RoundActions roundActions = player.getRoundActions();
        if (!roundActions.isEmpty()) {
            Direction previouslyMoveDirection = roundActions.getActionList().get(0).getDirection();
            cells.remove(game.getBoard().getPrevCell(workerCell, previouslyMoveDirection));
        }

        roundMoves.addMoves(cells, workerCell, worker.getGenre());
        return roundMoves;
    }
}
