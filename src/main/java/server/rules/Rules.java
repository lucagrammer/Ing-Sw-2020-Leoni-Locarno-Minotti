package server.rules;

import model.Cell;
import model.Game;
import model.Player;
import model.Worker;
import util.Action;
import util.ActionType;
import util.Genre;
import util.RoundActions;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The regular rules
 */
public class Rules implements Serializable {
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

        // Has already moved?
        if (roundActions.hasMoved() == 0) {
            nextPossibleActions.add(getPossibleMoves(player.getWorker(Genre.MALE), game));
            nextPossibleActions.add(getPossibleMoves(player.getWorker(Genre.FEMALE), game));
            //Can't move?
            if (nextPossibleActions.isEmpty())
                nextPossibleActions.add(new Action(ActionType.LOSE));
        } else {
            // Has already build?
            if (roundActions.hasBuildAnything() == 0) {
                Genre movedWorkerGenre = roundActions.
                        getActionList().
                        stream().
                        filter(x -> x.getActionType() == ActionType.MOVE).
                        collect(Collectors.toList()).
                        get(0).
                        getGenre();
                nextPossibleActions.add(getPossibleBuilds(player.getWorker(movedWorkerGenre), game));
                nextPossibleActions.add(getPossibleDomes(player.getWorker(movedWorkerGenre), game));
                //Can't build anything?
                if (nextPossibleActions.isEmpty())
                    nextPossibleActions.add(new Action(ActionType.LOSE));
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
        List<Cell> cells = calculateStandardMoves(workerCell, game);

        roundMoves.addMoves(cells, workerCell, worker.getGenre());
        return roundMoves;
    }

    /**
     * Gets a list of the cells you can move to
     *
     * @param workerCell The cell of the worker
     * @param game       The game
     * @return The list of the cells you can move to
     */
    protected List<Cell> calculateStandardMoves(Cell workerCell, Game game) {
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
        return cells;
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

        roundBuilds.addBuilds(cells, workerCell, worker.getGenre());
        return roundBuilds;
    }

    /**
     * Gets a list of the cells you can build a floor on
     *
     * @param workerCell The cell of the worker
     * @param game       The game
     * @return The list of the cells you can build a floor on
     */
    protected List<Cell> calculateStandardBuilds(Cell workerCell, Game game) {
        // Add all the adjacent cells that don't have a dome and don't have 3 floors: here you can build a floor
        List<Cell> cells = game.
                getBoard().
                getAdjacents(workerCell).
                stream().
                filter(x -> !x.getDome() && x.getFloor() <= 2).
                collect(Collectors.toList());

        // Remove all the cells that are already occupied
        for (Player gamePlayer : game.getPlayers()) {
            cells.removeAll(gamePlayer.getOccupiedCells());
        }
        return cells;
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

        roundDomes.addDomes(cells, workerCell, worker.getGenre());
        return roundDomes;
    }

    /**
     * Gets a list of the cells you can build a dome on
     *
     * @param workerCell The cell of the worker
     * @param game       The game
     * @return The list of the cells you can build a dome on
     */
    protected List<Cell> calculateStandardDomes(Cell workerCell, Game game) {
        // Add all the adjacent cells that don't have a dome and have 3 floors: here you can build a dome
        List<Cell> cells = game.
                getBoard().
                getAdjacents(workerCell).
                stream().
                filter(x -> !x.getDome() && x.getFloor() == 3).
                collect(Collectors.toList());

        // Remove all the cells that are already occupied
        for (Player gamePlayer : game.getPlayers()) {
            cells.removeAll(gamePlayer.getOccupiedCells());
        }
        return cells;
    }

    /**
     * Performs a certain allowed action
     *
     * @param action The action to be performed
     * @param player The player who performs the action
     * @param game   The game to which the player belongs
     * @return True if the action is a winning action, otherwise false
     */
    public boolean doAction(Action action, Player player, Game game) {
        switch (action.getActionType()) {
            case MOVE:
                return doMove(action, player, game);
            case FLOOR:
                return doBuild(action, player, game);
            case DOME:
                return doDome(action, player, game);
            case END:
                player.registerAction(action);
                return false;
            default:
                return false;
        }
    }

    /**
     * Performs a certain build-dome-action
     *
     * @param action The build-dome-action to be performed
     * @param player The player who performs the build-dome-action
     * @param game   The game to which the player belongs
     * @return True if the build-dome-action is a winning action, otherwise false
     */
    protected boolean doDome(Action action, Player player, Game game) {
        Worker worker = player.getWorker(action.getGenre());
        Cell currentCell = worker.getPosition();
        Cell position = game.getBoard().getNextCell(currentCell, action.getDirection());
        position.setDome(true);

        // Update the actions of the player
        player.registerAction(action);
        return false;
    }

    /**
     * Performs a certain build-floor-action
     *
     * @param action The build-floor-action to be performed
     * @param player The player who performs the build-floor-action
     * @param game   The game to which the player belongs
     * @return True if the build-floor-action is a winning action, otherwise false
     */
    protected boolean doBuild(Action action, Player player, Game game) {
        Worker worker = player.getWorker(action.getGenre());
        Cell currentCell = worker.getPosition();
        Cell position = game.getBoard().getNextCell(currentCell, action.getDirection());
        position.addFloor();

        // Update the actions of the player
        player.registerAction(action);
        return false;
    }

    /**
     * Performs a certain move-action
     *
     * @param action The move-action to be performed
     * @param player The player who performs the move-action
     * @param game   The game to which the player belongs
     * @return True if the build-floor-action is a winning action, otherwise false
     */
    protected boolean doMove(Action action, Player player, Game game) {
        Worker worker = player.getWorker(action.getGenre());
        Cell currentCell = worker.getPosition();
        Cell nexCell = game.getBoard().getNextCell(currentCell, action.getDirection());
        worker.setPosition(nexCell);

        // Update the actions of the player
        player.registerAction(action);
        return currentCell.getFloor() == 2 && nexCell.getFloor() == 3;
    }

}