package Server.Rules.SimpleGods;

import model.Cell;
import model.Game;
import model.Player;
import model.Worker;
import Server.Rules.Rules;
import Util.Action;

/**
 * Pan Card
 */
public class PanRules extends Rules {

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
        return (currentCell.getFloor() == 2 && nexCell.getFloor() == 3 || (currentCell.getFloorDifference(nexCell) < -1));
    }

}
