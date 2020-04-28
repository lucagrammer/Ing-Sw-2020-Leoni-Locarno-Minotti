package Server.Rules.SimpleGods;

import model.Cell;
import model.Game;
import model.Player;
import model.Worker;
import Server.Rules.Rules;
import Util.RoundActions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Atlas Card
 */
public class AtlasRules extends Rules {

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

        // Add all the adjacent cells that don't have a dome: here you can build a dome
        List<Cell> cells = game.
                getBoard().
                getAdjacents(workerCell).
                stream().
                filter(x -> !x.getDome()).
                collect(Collectors.toList());

        // Remove all the cells that are already occupied
        for (Player gamePlayer : game.getPlayers()) {
            cells.removeAll(gamePlayer.getOccupiedCells());
        }
        roundDomes.addDomes(cells, workerCell, worker.getGenre());
        return roundDomes;
    }
}
