package Server.Rules.SimpleGods;

import Server.Rules.Rules;
import Util.Action;
import Util.RoundActions;
import model.Cell;
import model.Game;
import model.Player;
import model.Worker;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Apollo Card
 */
public class ApolloRules extends Rules {

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

        // Remove all the cells that are already occupied by player's workers
        cells.removeAll(game.getPlayerByColor(worker.getPlayerColor()).getOccupiedCells());

        roundMoves.addMoves(cells, workerCell, worker.getGenre());
        return roundMoves;
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
