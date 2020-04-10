package Server.Rules.SimpleGods;

import Model.Cell;
import Model.Game;
import Model.Player;
import Model.Worker;
import Server.Rules.Rules;
import Util.*;

import java.util.List;
import java.util.stream.Collectors;

public class DemeterRules extends Rules {

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

        // Remove the cell occupied by the previous build/dome, if any
        Player player = game.getPlayerByColor(worker.getColor());
        RoundActions roundActions = player.getRoundActions();
        if (roundActions.hasBuildAnything() > 0) {
            Direction previouslyBuildDirection = roundActions.getActionList().get(1).getDirection();
            cells.remove(game.getBoard().getNextCell(workerCell, previouslyBuildDirection));
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

        // Remove the cell occupied by the previous build/dome, if any
        Player player = game.getPlayerByColor(worker.getColor());
        RoundActions roundActions = player.getRoundActions();
        if (roundActions.hasBuildAnything() > 0) {
            Direction previouslyBuildDirection = roundActions.getActionList().get(1).getDirection();
            cells.remove(game.getBoard().getNextCell(workerCell, previouslyBuildDirection));
        }

        roundDomes.addDomes(cells, workerCell, worker.getGenre());
        return roundDomes;
    }
}