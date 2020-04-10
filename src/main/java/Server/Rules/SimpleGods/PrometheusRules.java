package Server.Rules.SimpleGods;

import Model.Cell;
import Model.Game;
import Model.Player;
import Model.Worker;
import Server.Rules.Rules;
import Util.Action;
import Util.ActionType;
import Util.Genre;
import Util.RoundActions;

import java.util.List;
import java.util.stream.Collectors;

public class PrometheusRules extends Rules {

    public RoundActions nextPossibleActions(Player player, Game game) {
        RoundActions roundActions = player.getRoundActions();
        RoundActions nextPossibleActions = new RoundActions();

        // Has not moved and is not Prometheus build?
        if (roundActions.isEmpty()) {
            nextPossibleActions.add(getPossibleMoves(player.getWorker(Genre.MALE), game));
            nextPossibleActions.add(getPossibleBuilds(player.getWorker(Genre.MALE), game));
            nextPossibleActions.add(getPossibleDomes(player.getWorker(Genre.MALE), game));
            nextPossibleActions.add(getPossibleMoves(player.getWorker(Genre.FEMALE), game));
            nextPossibleActions.add(getPossibleBuilds(player.getWorker(Genre.FEMALE), game));
            nextPossibleActions.add(getPossibleDomes(player.getWorker(Genre.FEMALE), game));

            //Can't do anything?
            if (nextPossibleActions.isEmpty()) {
                nextPossibleActions.add(new Action(ActionType.LOSE));
            }
        } else {
            Genre workerGenre = roundActions.
                    getActionList().
                    stream().
                    filter(x -> (x.getActionType() == ActionType.MOVE) || (x.getActionType() == ActionType.BUILD_DOME)
                            || (x.getActionType() == ActionType.BUILD_FLOOR)).
                    collect(Collectors.toList()).
                    get(0).
                    getGenre();

            if (roundActions.hasMoved() == 1 && roundActions.hasBuildAnything() == 0) {
                nextPossibleActions.add(getPossibleBuilds(player.getWorker(workerGenre), game));
                nextPossibleActions.add(getPossibleDomes(player.getWorker(workerGenre), game));

                //Can't do anything?
                if (nextPossibleActions.isEmpty()) {
                    nextPossibleActions.add(new Action(ActionType.LOSE));
                }
            } else {
                ActionType firstAction = player.getRoundActions().getActionList().get(0).getActionType();

                //Prometheus power
                if ((firstAction == ActionType.BUILD_FLOOR || firstAction == ActionType.BUILD_DOME) && roundActions.hasMoved() == 1 && roundActions.hasBuildAnything() == 1) {
                    nextPossibleActions.add(getPossibleBuilds(player.getWorker(workerGenre), game));
                    nextPossibleActions.add(getPossibleDomes(player.getWorker(workerGenre), game));

                    //Can't do anything?
                    if (nextPossibleActions.isEmpty()) {
                        nextPossibleActions.add(new Action(ActionType.LOSE));
                    }
                } else {
                    if (roundActions.hasBuildAnything() == 2 || (firstAction == ActionType.MOVE && roundActions.hasBuildAnything() == 1)) {
                        nextPossibleActions.add(new Action(ActionType.END));
                    } else {
                        nextPossibleActions.add(getPrometheusMoves(player.getWorker(workerGenre), game)); //Moves on the same floor

                        //Can't do anything?
                        if (nextPossibleActions.isEmpty()) {
                            nextPossibleActions.add(new Action(ActionType.LOSE));
                        }
                    }
                }
            }

        }
        return nextPossibleActions;
    }

    protected RoundActions getPrometheusMoves(Worker worker, Game game) {
        RoundActions roundMoves = new RoundActions();
        Cell workerCell = worker.getPosition();
        List<Cell> cells = game.
                getBoard().
                getAdjacents(workerCell).
                stream().
                filter(x -> !x.getDome() && workerCell.getFloorDifference(x) <= 0).
                collect(Collectors.toList());

        // Remove all the cells that are already occupied
        for (Player gamePlayer : game.getPlayers()) {
            cells.removeAll(gamePlayer.getOccupiedCells());
        }

        roundMoves.addMoves(cells, workerCell, worker.getGenre());
        return roundMoves;
    }

}
