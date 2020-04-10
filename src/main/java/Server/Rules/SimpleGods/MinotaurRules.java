package Server.Rules.SimpleGods;

import Model.*;
import Server.Rules.Rules;
import Util.Action;
import Util.RoundActions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MinotaurRules extends Rules {

    @Override
    protected RoundActions getPossibleMoves(Worker worker, Game game) {
        RoundActions roundMoves = new RoundActions();
        Cell workerCell = worker.getPosition();
        Board board = game.getBoard();

        // Add all the adjacent cells that don't have a dome and are reachable from the current positions of the worker
        List<Cell> cells = board.
                getAdjacents(workerCell).
                stream().
                filter(x -> !x.getDome() && workerCell.getFloorDifference(x) <= 1).
                collect(Collectors.toList());

        // Remove all the cells that are already occupied by player's workers
        Player myPlayer = game.getPlayerByColor(worker.getColor());
        cells.removeAll(myPlayer.getOccupiedCells());

        // Remove all the cells that are already occupied by other enemy workers if the next cell in the same direction is full
        List<Cell> enemyPlayerAdjacentCells = new ArrayList<>();
        List<Cell> allOccupiedCells = new ArrayList<>();
        for (Player gamePlayer : game.getPlayers()) {
            if (!gamePlayer.equals(myPlayer)) {
                enemyPlayerAdjacentCells.addAll(gamePlayer.getOccupiedCells().stream().filter(x -> x.isAdjacent(workerCell)).collect(Collectors.toList()));
            }
            allOccupiedCells.addAll(gamePlayer.getOccupiedCells());
        }
        if (enemyPlayerAdjacentCells.size() != 0) {
            for (Cell enemyPlayerAdjacentCell : enemyPlayerAdjacentCells) {
                Cell enemyNextCell = board.getNextCell(enemyPlayerAdjacentCell, workerCell.calculateDirection(enemyPlayerAdjacentCell));
                if (enemyNextCell == null || allOccupiedCells.contains(enemyNextCell) || enemyNextCell.getDome()) {
                    cells.remove(enemyPlayerAdjacentCell);
                }
            }
        }

        roundMoves.addMoves(cells, workerCell, worker.getGenre());
        return roundMoves;
    }

    @Override
    protected boolean doMove(Action action, Player player, Game game) {
        Worker worker = player.getWorker(action.getGenre());
        Board board = game.getBoard();
        Cell currentCell = worker.getPosition();
        Cell nexCell = board.getNextCell(currentCell, action.getDirection());
        boolean isMinotaurMove = false;
        Player theEnemy = null;

        for (Player aPlayer : game.getPlayers()) {
            if (aPlayer.getOccupiedCells().contains(nexCell)) {
                isMinotaurMove = true;
                theEnemy = aPlayer;
                break;
            }
        }

        if (isMinotaurMove) {
            Cell enemyNextCell = board.getNextCell(nexCell, action.getDirection());
            theEnemy.getWorkerByPosition(nexCell).setPosition(enemyNextCell);
        }
        worker.setPosition(nexCell);

        // Update the actions of the player
        player.registerAction(action);
        return currentCell.getFloor() == 2 && nexCell.getFloor() == 3;
    }
}
