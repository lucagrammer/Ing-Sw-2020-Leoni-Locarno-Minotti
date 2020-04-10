package Server.Rules.SimpleGods;

import Model.Cell;
import Model.Game;
import Model.Player;
import Model.Worker;
import Server.Rules.Rules;
import Util.Action;
import Util.RoundActions;

import java.util.List;
import java.util.stream.Collectors;

public class ApolloRules extends Rules {

    @Override
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

        // Remove all the cells that are already occupied by player's workers
        cells.removeAll(game.getPlayerByColor(worker.getColor()).getOccupiedCells());

        roundMoves.addMoves(cells, workerCell, worker.getGenre());
        return roundMoves;
    }

    @Override
    protected boolean doMove(Action action, Player player, Game game) {
        Worker worker = player.getWorker(action.getGenre());
        Cell currentCell = worker.getPosition();
        Cell nexCell = game.getBoard().getNextCell(currentCell, action.getDirection());
        boolean isApolloMove = false;
        Player theEnemy = null;

        for (Player aPlayer : game.getPlayers()) {
            if (aPlayer.getOccupiedCells().contains(nexCell)) {
                isApolloMove = true;
                theEnemy = aPlayer;
                break;
            }
        }

        if (isApolloMove) {
            theEnemy.getWorkerByPosition(nexCell).setPosition(currentCell);
        }
        worker.setPosition(nexCell);

        // Update the actions of the player
        player.registerAction(action);
        return currentCell.getFloor() == 2 && nexCell.getFloor() == 3;
    }

}
