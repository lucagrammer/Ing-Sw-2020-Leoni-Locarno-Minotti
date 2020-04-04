package Controller.Rules;

import Model.Cell;
import Model.Game;
import Model.Player;
import Model.Worker;
import Util.Action;
import Util.ActionType;
import Util.Genre;
import Util.RoundActions;

import java.util.List;
import java.util.stream.Collectors;

public class Rules {

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
        roundMoves.addMoves(cells, workerCell, worker.getGenre());
        return roundMoves;
    }

    protected RoundActions getPossibleBuilds(Worker worker, Game game) {
        RoundActions roundBuilds = new RoundActions();
        Cell workerCell = worker.getPosition();

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
        roundBuilds.addBuilds(cells, workerCell, worker.getGenre());
        return roundBuilds;
    }

    protected RoundActions getPossibleDomes(Worker worker, Game game) {
        RoundActions roundDomes = new RoundActions();
        Cell workerCell = worker.getPosition();

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
        roundDomes.addDomes(cells, workerCell, worker.getGenre());
        return roundDomes;
    }

    public boolean doAction(Action action, Player player, Game game) {
        switch (action.getActionType()) {
            case MOVE:
                return doMove(action, player, game);
            case BUILD_FLOOR:
                return doBuild(action, player, game);
            case BUILD_DOME:
                return doDome(action, player, game);
            default:
                return false;
        }
    }

    protected boolean doDome(Action action, Player player, Game game) {
        Worker worker = player.getWorker(action.getGenre());
        Cell currentCell = worker.getPosition();
        Cell position = game.getBoard().getNextCell(currentCell, action.getDirection());
        position.setDome(true);

        // Update the actions of the player
        player.registerAction(action);
        return false;
    }

    protected boolean doBuild(Action action, Player player, Game game) {
        Worker worker = player.getWorker(action.getGenre());
        Cell currentCell = worker.getPosition();
        Cell position = game.getBoard().getNextCell(currentCell, action.getDirection());
        position.addFloor();

        // Update the actions of the player
        player.registerAction(action);
        return false;
    }

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