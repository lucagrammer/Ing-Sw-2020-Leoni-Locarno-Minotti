package Server.Rules.SimpleGods;

import Model.Cell;
import Model.Game;
import Model.Player;
import Model.Worker;
import Server.Rules.Rules;
import Util.Action;

public class PanRules extends Rules {

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
