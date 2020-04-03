package Controller.Rules;

import Model.Cell;
import Model.Game;
import Model.Player;
import Model.Worker;
import Util.Action;
import Util.ActionType;
import Util.Genre;
import Util.RoundActions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Rules {

    public RoundActions nextPossibleActions(RoundActions roundActions, Player player, Game game) {
        RoundActions possibleRoundActions = new RoundActions();

        if (roundActions.hasMoved() == 0) {
            possibleRoundActions.add(getPossibleMoves(player.getWorker(Genre.MALE), game));
            possibleRoundActions.add(getPossibleMoves(player.getWorker(Genre.FEMALE), game));
            if (possibleRoundActions.isEmpty())
                roundActions.add(new Action(ActionType.LOSE));
        } else {
            if (roundActions.hasBuildAnything() == 0) {
                Genre movedWorkerGenre = roundActions.
                        getActionList().
                        stream().
                        filter(x -> x.getActionType() == ActionType.MOVE).
                        collect(Collectors.toList()).
                        get(0).
                        getGenre();
                possibleRoundActions.add(getPossibleBuilds(player.getWorker(movedWorkerGenre), game));
                possibleRoundActions.add(getPossibleDomes(player.getWorker(movedWorkerGenre), game));
                if (possibleRoundActions.isEmpty())
                    roundActions.add(new Action(ActionType.LOSE));
            } else {
                roundActions.add(new Action(ActionType.END));
            }
        }


        return possibleRoundActions;
    }

    public boolean doAction(Action action) {
        return false;
    }

    private RoundActions getPossibleMoves(Worker worker, Game game) {
        List<Cell> cells = new ArrayList<>();
        RoundActions roundMoves = new RoundActions();
        Cell workerCell = worker.getPosition();

        // Add all the adjacent cells that don't have a dome and are reachable from the current positions of the worker
        cells = game.
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

    private RoundActions getPossibleBuilds(Worker worker, Game game) {
        List<Cell> cells = new ArrayList<>();
        RoundActions roundBuilds = new RoundActions();
        Cell workerCell = worker.getPosition();

        // Add all the adjacent cells that don't have a dome and don't have 3 floors: here you can build a floor
        cells = game.
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

    private RoundActions getPossibleDomes(Worker worker, Game game) {
        List<Cell> cells = new ArrayList<>();
        RoundActions roundDomes = new RoundActions();
        Cell workerCell = worker.getPosition();

        // Add all the adjacent cells that don't have a dome and have 3 floors: here you can build a dome
        cells = game.
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
}
