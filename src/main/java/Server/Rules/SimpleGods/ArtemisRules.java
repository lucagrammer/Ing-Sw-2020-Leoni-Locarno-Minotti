package Server.Rules.SimpleGods;

import Model.Cell;
import Model.Game;
import Model.Player;
import Model.Worker;
import Server.Rules.Rules;
import Util.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Artemis Card
 */
public class ArtemisRules extends Rules {

    /**
     * Gets a RoundActions object containing all the possible actions of the specified player according to the Rules
     *
     * @param player The player whose possible actions are to be analyzed
     * @param game   The game to which the player belongs
     * @return A RoundActions object containing all the possible actions
     */
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

    /**
     * Gets a RoundActions object containing all the move-actions of the specified worker according to the Rules
     *
     * @param worker The worker whose possible moves are to be analyzed
     * @param game   The game to which the player belongs
     * @return A RoundActions object containing all the possible move-actions
     */
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
        Player player = game.getPlayerByColor(worker.getColor());
        RoundActions roundActions = player.getRoundActions();
        if (!roundActions.isEmpty()) {
            Direction previouslyMoveDirection = roundActions.getActionList().get(0).getDirection();
            cells.remove(game.getBoard().getPrevCell(workerCell, previouslyMoveDirection));
        }

        roundMoves.addMoves(cells, workerCell, worker.getGenre());
        return roundMoves;
    }
}
