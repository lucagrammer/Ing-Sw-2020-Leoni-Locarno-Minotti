package server.rules.simpleGods;

import model.Cell;
import model.Game;
import model.Player;
import model.Worker;
import server.rules.Rules;
import util.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Demeter Card
 */
public class DemeterRules extends Rules {

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
                nextPossibleActions.add(getPossibleBuilds(player.getWorker(movedWorkerGenre), game));
                nextPossibleActions.add(getPossibleDomes(player.getWorker(movedWorkerGenre), game));
                //Can't build anything?
                if (nextPossibleActions.isEmpty())
                    nextPossibleActions.add(new Action(ActionType.LOSE));
            } else {

                nextPossibleActions.add(new Action(ActionType.END));
                // Has build once?
                if (roundActions.hasBuildAnything() == 1) {
                    nextPossibleActions.add(getPossibleBuilds(player.getWorker(movedWorkerGenre), game));
                    nextPossibleActions.add(getPossibleDomes(player.getWorker(movedWorkerGenre), game));
                }
            }
        }
        return nextPossibleActions;
    }

    /**
     * Gets a RoundActions object containing all the build-floor-actions of the specified worker according to the Rules
     *
     * @param worker The worker whose possible actions are to be analyzed
     * @param game   The game to which the player belongs
     * @return A RoundActions object containing all the possible build-floor-actions
     */
    protected RoundActions getPossibleBuilds(Worker worker, Game game) {
        RoundActions roundBuilds = new RoundActions();
        Cell workerCell = worker.getPosition();
        List<Cell> cells = calculateStandardBuilds(workerCell, game);

        // Remove the cell occupied by the previous build/dome, if any
        Player player = game.getPlayerByColor(worker.getPlayerColor());
        RoundActions roundActions = player.getRoundActions();
        if (roundActions.hasBuildAnything() > 0) {
            Direction prevBuildDirection = roundActions.getActionList().get(1).getDirection();
            cells.remove(game.getBoard().getNextCell(workerCell, prevBuildDirection));
        }

        roundBuilds.addBuilds(cells, workerCell, worker.getGenre());
        return roundBuilds;
    }

    /**
     * Gets a RoundActions object containing all the build-dome-actions of the specified worker according to the Rules
     *
     * @param worker The worker whose possible actions are to be analyzed
     * @param game   The game to which the player belongs
     * @return A RoundActions object containing all the possible build-dome-actions
     */
    protected RoundActions getPossibleDomes(Worker worker, Game game) {
        RoundActions roundDomes = new RoundActions();
        Cell workerCell = worker.getPosition();
        List<Cell> cells = calculateStandardDomes(workerCell, game);

        // Remove the cell occupied by the previous build/dome, if any
        Player player = game.getPlayerByColor(worker.getPlayerColor());
        RoundActions roundActions = player.getRoundActions();
        if (roundActions.hasBuildAnything() > 0) {
            Direction prevBuildDirection = roundActions.getActionList().get(1).getDirection();
            cells.remove(game.getBoard().getNextCell(workerCell, prevBuildDirection));
        }

        roundDomes.addDomes(cells, workerCell, worker.getGenre());
        return roundDomes;
    }
}