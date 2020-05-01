package client;

import Util.*;
import model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static Util.Genre.MALE;

/**
 * Manages the command line interface to the user
 */
public class CliView implements View {
    private final Scanner scanner;
    private ServerHandler serverHandler;

    /**
     * Constructor: build the CliView
     */
    public CliView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Sets the serverHandler
     *
     * @param serverHandler The serverHandler
     */
    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    /**
     * Interface launcher. Asks the server IP to connect to and notify it to the serverHandler
     */
    public void launch() {
        String defaultServerIP = Configurator.getDefaultIp();

        Frmt.clearScreen();
        System.out.print(Frmt.style('b', " Enter the server IP [press enter for default IP]: "));
        String serverIP = scanner.nextLine();
        if (serverIP.equals("")) {
            serverIP = defaultServerIP;
            System.out.println(Frmt.style('i', "  > Default server IP will be applied, " + defaultServerIP));
        }
        showQueuedMessage();
        serverHandler.setConnection(serverIP);
    }

    /**
     * Shows a message to say to the user that is connected to
     * the server and will be added to the next available game
     */
    public void showQueuedMessage() {
        Frmt.clearScreen();
        System.out.println(Frmt.style('i', "  > You will be added to the first available game..."));
    }

    /**
     * Shows a specified message to the user
     *
     * @param string    The message to be shown
     * @param newScreen True if it's necessary to clean the interface
     */
    public void showMessage(String string, boolean newScreen) {
        if (newScreen)
            Frmt.clearScreen();
        System.out.println(string);
    }

    /**
     * Shows a loading message to the user
     */
    public void showLoading() {
        Frmt.clearScreen();
        System.out.println(Frmt.style('i', "  > Loading..."));
    }

    /**
     * Asks a new nickname to the user and notify the choice to the serverHandler
     */
    public void askNewNickname() {
        Frmt.clearScreen();
        System.out.println(Frmt.color('r', "  > Error: the chosen username is already taken."));
        String newNickname = askNickname();
        Frmt.clearScreen();
        System.out.println(Frmt.style('i', "  > Waiting for the other players to connect..."));
        serverHandler.sendNewNickname(newNickname);
    }

    /**
     * Asks nickname, birth date and if it's a new game the number of players
     * for the game and notify the information to the serverHandler
     *
     * @param newGame True if the it is a new game, otherwise false
     */
    public void setUpGame(boolean newGame) {
        boolean incorrect;
        Frmt.clearScreen();
        String nickname = askNickname();

        Date date = null;
        Frmt.clearScreen();
        do {
            System.out.print(Frmt.style('b', "\n Enter your birth date [dd/mm/yyyy]: "));
            String fullDate = scanner.nextLine();
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(fullDate);
                incorrect = false;
            } catch (ParseException e) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "  > Incorrect date format. Try again."));
                incorrect = true;
            }
        } while (incorrect);

        int numPlayers = 0;
        if (newGame) {
            Frmt.clearScreen();
            do {
                System.out.print(Frmt.style('b', "\n Enter the number of competitors [2..3]: "));
                String numPlayersString = scanner.nextLine();
                try {
                    numPlayers = Integer.parseInt(numPlayersString);
                } catch (Exception e) {
                    numPlayers = 0;
                }
                if (numPlayers < 2 || numPlayers > 3) {
                    Frmt.clearScreen();
                    System.out.println(Frmt.color('r', "  > Invalid choice. Try again."));
                    incorrect = true;
                } else {
                    incorrect = false;
                }
            } while (incorrect);
        }
        Frmt.clearScreen();
        System.out.println(Frmt.style('i', "\n  > Waiting for the other players to connect..."));
        serverHandler.sendSetUpGame(nickname, date, numPlayers);
    }

    /**
     * Asks nickname
     *
     * @return The chosen nickname
     */
    private String askNickname() {
        boolean incorrect;
        Scanner scanner = new Scanner(System.in);
        String nickname;
        do {

            System.out.print(Frmt.style('b', "\n Enter your nickname: "));
            nickname = scanner.nextLine();
            if (nickname.equals("") || nickname.contains(" ")) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "  > Invalid nickname. Try again."));
                incorrect = true;
            } else {
                incorrect = false;
            }
        } while (incorrect);
        return nickname;
    }

    /**
     * Asks the game cards
     *
     * @param numCards Number of cards to be selected
     */
    public void askGameCards(int numCards) {
        List<Card> allCardList = Configurator.getAllCards();
        List<String> allCardNames = new ArrayList<>();
        List<Card> chosenCard = new ArrayList<>();

        boolean incorrect;
        Frmt.clearScreen();
        do {
            System.out.println("\n\n " + Frmt.style('b', "Choose " + (numCards - chosenCard.size()) + " card" + ((numCards - chosenCard.size() > 1) ? "s" : "") + " between these:"));
            for (Card card : allCardList) {
                if (!chosenCard.contains(card)) {
                    System.out.println(Frmt.style('b', "\n   ❖ " + card.getName().toUpperCase()));
                    System.out.println(Frmt.style('i', "      " + card.getDescription()));
                }
                allCardNames.add(card.getName().toLowerCase());
            }
            System.out.println();

            incorrect = false;
            System.out.print("  ↳: ");
            String chosenCardName = scanner.next();
            Frmt.clearScreen();
            if (!allCardNames.contains(chosenCardName.toLowerCase())) {
                System.out.println(Frmt.color('r', "   > Invalid choice. Try again."));
                incorrect = true;
            } else {
                for (Card card : allCardList) {
                    if (card.getName().equalsIgnoreCase(chosenCardName)) {
                        if (chosenCard.contains(card)) {
                            System.out.println(Frmt.color('r', "   > The card has already been selected. Try again."));
                            incorrect = true;
                        } else {
                            chosenCard.add(card);
                        }
                    }
                }
            }
        } while (incorrect || chosenCard.size() < numCards);

        showLoading();
        serverHandler.sendGameCards(chosenCard);
    }

    /**
     * Asks the card the player whats to use during the game
     *
     * @param possibleChoices All the possible cards
     */
    public void askPlayerCard(List<Card> possibleChoices) {
        Card chosenCard = null;
        String chosenCardName;
        List<String> allCardNames = new ArrayList<>();

        boolean incorrect;
        do {
            System.out.println("\n\n " + Frmt.style('b', "Choose your card between these:"));
            for (Card card : possibleChoices) {
                System.out.println(Frmt.style('b', "\n   ❖ " + card.getName().toUpperCase()));
                System.out.println(Frmt.style('i', "      " + card.getDescription()));
                allCardNames.add(card.getName().toLowerCase());
            }
            System.out.println();

            incorrect = false;
            System.out.print("  ↳: ");
            chosenCardName = scanner.next();
            if (!allCardNames.contains(chosenCardName.toLowerCase())) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "   > Invalid choice. Try again."));
                incorrect = true;
            }
        } while (incorrect);

        for (Card card : possibleChoices) {
            if (card.getName().equalsIgnoreCase(chosenCardName)) {
                chosenCard = card;
            }
        }

        showLoading();
        serverHandler.sendPlayerCard(chosenCard);
    }

    /**
     * Asks the nickname of the first player
     *
     * @param playersNicknames All the nicknames
     */
    public void askFirstPlayer(List<String> playersNicknames) {
        List<String> allNicknames = new ArrayList<>();

        String chosenNickname;
        boolean incorrect;
        System.out.println("\n\n");
        do {
            System.out.print(" " + Frmt.style('b', "Choose who will be the first player:") + " ");
            for (int i = 0; i < playersNicknames.size(); i++) {
                String name = playersNicknames.get(i);
                allNicknames.add(name.toLowerCase());
                if (i != playersNicknames.size() - 1) {
                    System.out.print(Frmt.style('b', name + ", "));
                } else {
                    System.out.println(Frmt.style('b', name + "?"));
                }
            }

            incorrect = false;
            System.out.print("  ↳: ");
            chosenNickname = scanner.next();
            if (!allNicknames.contains(chosenNickname.toLowerCase())) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "   > Invalid choice. Try again."));
                incorrect = true;
            }
        } while (incorrect);

        showLoading();
        serverHandler.sendFirstPlayer(chosenNickname);
    }

    /**
     * Show all the cards of the game
     *
     * @param cards All the cards of the game
     */
    public void showGameCards(List<Card> cards) {
        List<Card> allCardList = Configurator.getAllCards();

        Frmt.clearScreen();
        System.out.println(" " + Frmt.style('b', "The cards used in this match will be:"));
        for (Card card : allCardList) {
            if (cards.contains(card)) {
                System.out.println(Frmt.style('b', "\n   ❖ " + card.getName().toUpperCase()));
                System.out.println(Frmt.style('i', "      " + card.getDescription()));
            }
        }
    }

    /**
     * Shows the cards assignment of the game
     *
     * @param playerList The list of players of the game
     */
    public void showCardAssignmentMessage(List<Player> playerList) {
        Frmt.clearScreen();
        System.out.println(" " + Frmt.style('b', "Cards assignment:"));
        for (Player player : playerList) {
            System.out.print(Frmt.style('b', "\n   ▷ " + player.getNickname()));
            System.out.println(Frmt.style('i', " will use " + player.getCard().getName()));
        }
    }

    /**
     * Asks the color the player whats to choose
     *
     * @param availableColors All the available colors
     */
    public void askPlayerColor(List<String> availableColors) {
        String chosenColor;

        Frmt.clearScreen();
        //There's only one color?
        if (availableColors.size() == 1) {
            System.out.print("\n\n " + Frmt.style('b', "Your color will be") + " ");
            System.out.println(Frmt.color(availableColors.get(0).toLowerCase().charAt(0), availableColors.get(0).toLowerCase()));
            chosenColor = availableColors.get(0);

        } else {
            List<String> allColors = new ArrayList<>();
            boolean incorrect;
            do {
                System.out.print("\n\n " + Frmt.style('b', "Choose your color between:") + " ");
                for (int i = 0; i < availableColors.size(); i++) {
                    String color = availableColors.get(i);
                    if (i != availableColors.size() - 1) {
                        System.out.print(Frmt.color(color.toLowerCase().charAt(0), color.toLowerCase() + ", "));
                    } else {
                        System.out.println(Frmt.color(color.toLowerCase().charAt(0), color.toLowerCase() + "?"));
                    }
                    allColors.add(color.toLowerCase());
                }

                incorrect = false;
                System.out.print("  ↳: ");
                chosenColor = scanner.next();
                if (!allColors.contains(chosenColor.toLowerCase())) {
                    Frmt.clearScreen();
                    System.out.println(Frmt.color('r', "   > Invalid choice. Try again."));
                    incorrect = true;
                }
            } while (incorrect);
        }
        serverHandler.sendPlayerColor(chosenColor);
    }

    /**
     * Asks the first position for the male and female worker
     *
     * @param genre          The genre of the worker
     * @param forbiddenCells The forbidden cells
     * @param game           The game
     */
    public void askPlayerPosition(Genre genre, List<Cell> forbiddenCells, Game game) {
        Cell chosenCell;
        String chosenPosition;
        int row = -1, column = -1;
        boolean incorrect;
        Frmt.clearScreen();
        do {
            showMap(game, false);
            incorrect = false;
            System.out.print("\n " + Frmt.style('b', "Choose the position of your " + ((genre == MALE) ? "male" : "female") + " worker [row,column]:") + " ");
            chosenPosition = scanner.next();
            try {
                row = Character.getNumericValue(chosenPosition.charAt(0));
                column = Character.getNumericValue(chosenPosition.charAt(2));
            } catch (Exception e) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "  > Invalid choice. Try again."));
                incorrect = true;
            }

            chosenCell = new Cell(row, column);
            if (!incorrect && (row < 0 || column < 0 || row > 4 || column > 4 || forbiddenCells.contains(chosenCell) || chosenPosition.length() != 3)) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "  > Invalid choice. Try again."));
                incorrect = true;
            }
        } while (incorrect);
        serverHandler.sendPlayerPosition(genre, chosenCell);
    }

    /**
     * Shows the board of the game
     *
     * @param game      The game
     * @param newScreen True if it's necessary to clean the interface
     */
    public void showMap(Game game, boolean newScreen) {
        String[][] cellColorMatrix = new String[5][5];
        Genre[][] cellGenreMatrix = new Genre[5][5];
        Cell[][] cellsInfoMatrix = game.getBoard().getBoard();
        List<String> playerNames = new ArrayList<>();
        List<Character> colors = new ArrayList<>();    // players color in the same order of playerNames

        // Null initialization of the info matrix
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                cellColorMatrix[i][j] = null;
                cellGenreMatrix[i][j] = null;
            }
        }

        // Fill matrix with correct information
        for (Player player : game.getPlayers()) {
            for (Genre genre : Genre.values()) {
                Worker worker = player.getWorker(genre);
                Cell position = worker.getPosition();
                if (position != null) {
                    int row = position.getRow();
                    int column = position.getColumn();
                    cellColorMatrix[row][column] = worker.getColor().toString();
                    cellGenreMatrix[row][column] = genre;
                    if (!colors.contains(cellColorMatrix[row][column].charAt(0))) {
                        playerNames.add(player.getNickname());
                        colors.add(cellColorMatrix[row][column].charAt(0));
                    }
                }
            }
        }

        // Show the map
        if (newScreen)
            Frmt.clearScreen();
        System.out.println("\tNW\t\t                          N                             \tNE");
        System.out.print("\n\t\t\t");
        for (int j = 0; j < 5; j++) {
            System.out.print("    " + j + "    " + "  ");
        }
        System.out.println();
        for (int i = 0; i < 5; i++) {
            System.out.print("\n\t\t\t");
            for (int j = 0; j < 5; j++) {
                char color = (cellColorMatrix[i][j] == null) ? 'w' : cellColorMatrix[i][j].toLowerCase().charAt(0);
                System.out.print(Frmt.color(color, Frmt.style('r', "         ")) + "  ");
            }

            // Legend
            if (i == 1 && colors.size() > 0) {
                System.out.print("\t\t\t  " + Frmt.color(colors.get(0), Frmt.style('r', "  "
                        + playerNames.get(0).toUpperCase() + " - " + game.getPlayerByNickname(playerNames.get(0)).getCard().getName() + "  ")));
            }

            if (i == 2) {
                System.out.print("\n\tW\t" + i + "\t");
            } else {
                System.out.print("\n\t\t" + i + "\t");
            }
            for (int j = 0; j < 5; j++) {
                Frmt domed = (cellsInfoMatrix[i][j].getDome()) ? Frmt.DOME : Frmt.NOT_DOME;
                String genre = (cellGenreMatrix[i][j] == null) ? " " : Frmt.getGenreIcon(cellGenreMatrix[i][j]);
                System.out.print(Frmt.color('w', Frmt.style('r', " " + domed + "    " + genre + "  ")) + "  ");
            }
            if (i == 2) {
                System.out.print("\t\tE");
            }

            // Legend
            if (i == 1 && colors.size() > 1) {
                System.out.print("\t\t\t  " + Frmt.color(colors.get(1), Frmt.style('r', "  " + playerNames.get(1).toUpperCase() + " - " + game.getPlayerByNickname(playerNames.get(1)).getCard().getName() + "  ")));
            }

            System.out.print("\n\t\t\t");
            for (int j = 0; j < 5; j++) {
                int floor = cellsInfoMatrix[i][j].getFloor();
                System.out.print(Frmt.color('w', Frmt.style('r', " " + floor + "       ")) + "  ");
            }

            // Legend
            if (i == 1 && colors.size() > 2) {
                System.out.print("\t\t\t  " + Frmt.color(colors.get(2), Frmt.style('r', "  " + playerNames.get(2).toUpperCase() + " - " + game.getPlayerByNickname(playerNames.get(2)).getCard().getName() + "  ")));
            }
            if (i == 0 && colors.size() > 0) {
                System.out.print("\t\t\t  " + Frmt.style("bi", "KEY:"));
            }
            System.out.print("\n");
        }
        System.out.println("\n\tSW\t\t                          S                             \tSE");
        System.out.print("\n");
    }

    /**
     * Asks the action the player wants to perform
     *
     * @param roundActions  All the possible actions
     * @param game          The game
     * @param loserNickname The nickname of the looser or null value
     */
    public void askAction(RoundActions roundActions, Game game, String loserNickname) {
        String action, genre, direction;
        Action theAction;
        boolean incorrect;

        Frmt.clearScreen();
        do {
            showMap(game, false);
            incorrect = false;

            // There's a loser?
            if (loserNickname != null) {
                System.out.println(Frmt.color('r', "\n\t\t" + Frmt.style('b', "" + loserNickname.toUpperCase() + " has lost " + Frmt.DEATH)));
            }
            System.out.println(Frmt.style('b', "\t\tIt's your turn. "));
            System.out.print("\n\t\t" + Frmt.style('b', "You can:  \n\t\t"));
            int counter = 0;
            for (Action possibleAction : roundActions.getActionList()) {
                if (counter == 11) {
                    System.out.print("\n\t\t");
                    counter = 0;
                }
                if (possibleAction.getActionType().name().equalsIgnoreCase("END")) {
                    System.out.print("END; ");

                } else {
                    System.out.print(possibleAction.getActionType().name() + " " + possibleAction.getGenre().name().charAt(0) + " " + possibleAction.getDirection().name() + "; ");
                }
                counter++;
            }

            System.out.print("\n\n\t\t" + Frmt.style('b', "Insert your action type [MOVE/FLOOR/DOME/END]:") + " ");
            action = scanner.next();
            if (!action.equalsIgnoreCase("end")) {
                System.out.print("\t\t" + Frmt.style('b', "Insert the genre of the worker [M/F]:") + " ");
                genre = scanner.next();
                System.out.print("\t\t" + Frmt.style('b', "Insert the direction [N/NE/E/SE/S/SW/W/NW]:") + " ");
                direction = scanner.next();

                theAction = roundActions.find(action, genre, direction);
            } else {
                theAction = roundActions.canEnd();
            }
            if (theAction == null) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "\t\t  > Invalid action. Try again.\n"));
                incorrect = true;
            }
        } while (incorrect);
        serverHandler.sendAction(theAction);
    }

    /**
     * Notify the players that the game has ended and notify the winner
     *
     * @param winnerNickname The nickname of the winner
     * @param youWin         True if the player has win
     */
    public void showGameEndMessage(String winnerNickname, boolean youWin) {
        if (youWin) {
            System.out.println(Frmt.color('g', "\n\n\t" + Frmt.style('b', "If it seems to be true, probably it is: YOU WIN " + Frmt.CUP)));
        } else {
            System.out.println(Frmt.color('r', "\n\n\t" + Frmt.style('b', "YOU LOSE " + Frmt.DEATH + " : the winner is " + winnerNickname.toUpperCase())));
        }
        askNewGame();
    }

    /**
     * Notify the players that a player has disconnected and the game has ended
     *
     * @param disconnectedNickname The nickname of the disconnected player
     */
    public void showDisconnectionMessage(String disconnectedNickname) {
        Frmt.clearScreen();
        System.out.println(Frmt.color('r', "\n\n\t" + Frmt.style("ui", "GAME OVER: " +
                disconnectedNickname.toUpperCase() + " has disconnected.")));
        askNewGame();
    }

    /**
     * Shows an error message to the user
     *
     * @param errorMessage The message to be shown
     * @param newScreen    True if it's necessary to clean the interface
     */
    public void showErrorMessage(String errorMessage, boolean newScreen) {
        if (newScreen)
            Frmt.clearScreen();
        System.out.println(Frmt.color('r', "> Error: " + errorMessage));
    }

    /**
     * Asks to the player if he wants to start a new game
     */
    private void askNewGame() {
        System.out.print("\n\n\tDo you want to play again? [Yes/No]: ");

        boolean incorrect;
        String choice;
        do {
            incorrect = false;
            choice = scanner.next();

            if (!choice.equalsIgnoreCase("yes") && !choice.equalsIgnoreCase("no")) {
                System.out.println(Frmt.color('r', "> Invalid choice. Try again."));
                incorrect = true;
            }
        } while (incorrect);

        serverHandler.sendNewGame(choice.equalsIgnoreCase("yes"));
    }

}
