package Server.Rules.SimpleGods;

import Model.Game;
import Model.Player;
import Model.Worker;
import Server.Rules.Rules;
import Util.*;

import java.util.stream.Collectors;

public class HephaestusRules extends Rules {
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
                // Has build. Has build one floor?
                if (roundActions.hasBuildFloor() == 1) {
                    nextPossibleActions.add(getDoubleFloorAction(player.getWorker(movedWorkerGenre), game));
                }
            }
        }
        return nextPossibleActions;
    }

    protected RoundActions getDoubleFloorAction(Worker worker, Game game) {
        // Find the cell-direction of the previous build-floor-action
        Player player = game.getPlayerByColor(worker.getColor());
        RoundActions roundActions = player.getRoundActions();
        RoundActions nextPossibleActions = new RoundActions();

        Action previouslyBuildAction = roundActions.getActionList().get(1);
        Direction previouslyBuildDirection = previouslyBuildAction.getDirection();

        if (game.getBoard().getNextCell(worker.getPosition(), previouslyBuildDirection).getFloor() < 3) {
            int previouslyLevelDifference = previouslyBuildAction.getLevelDifference();
            nextPossibleActions.add(new Action(ActionType.BUILD_FLOOR, worker.getGenre(), previouslyBuildDirection, previouslyLevelDifference + 1));
        }
        return nextPossibleActions;
    }

}
