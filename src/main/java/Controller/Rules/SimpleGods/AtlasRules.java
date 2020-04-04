package Controller.Rules.SimpleGods;

import Controller.Rules.Rules;
import Model.Cell;
import Model.Game;
import Model.Player;
import Model.Worker;
import Util.RoundActions;

import java.util.List;
import java.util.stream.Collectors;

public class AtlasRules extends Rules {

    protected RoundActions getPossibleDomes(Worker worker, Game game) {
        RoundActions roundDomes = new RoundActions();
        Cell workerCell = worker.getPosition();

        // Add all the adjacent cells that don't have a dome and have 3 floors: here you can build a dome
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
