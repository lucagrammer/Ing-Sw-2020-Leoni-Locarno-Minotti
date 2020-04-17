package Client;

import Model.*;
import Util.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static Util.Genre.FEMALE;
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
        System.out.print(Frmt.style('b', "Enter the server IP: "));
        String serverIP = scanner.nextLine();
        if (serverIP.equals("")) {
            serverIP = defaultServerIP;
            System.out.println(Frmt.style('i', "> Default server IP will be applied, " + defaultServerIP));
        }
        serverHandler.setConnection(serverIP);
    }

    /**
     * Shows a specified message to the user
     *
     * @param string The message to be shown
     */
    public void showMessage(String string) {
        System.out.println(string);
    }

    /**
     * Asks a new nickname to the user and notify the choice to the serverHandler
     */
    public void askNewNickname() {
        System.out.println(Frmt.color('r', "> Error: the chosen username is already taken."));
        String newNickname = askNickname();
        System.out.println(Frmt.style('i', "> Waiting for the other players to connect..."));
        serverHandler.sendNewNickname(newNickname);
    }

    /**
     * Asks nickname, birth date and if it's a new game the number of players
     * for the game and notify the information to the serverHandler
     *
     * @param newGame True if the it is a new game, otherwise false
     */
    public void gameSetUp(boolean newGame) {
        boolean incorrect;
        String nickname = askNickname();

        Date date = null;
        do {
            System.out.print(Frmt.style('b', "Enter your birth date [dd/mm/yyyyy]: "));
            String fullDate = scanner.nextLine();
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(fullDate);
                incorrect = false;
            } catch (ParseException e) {
                System.out.println(Frmt.color('r', "> Incorrect date format. Try again."));
                incorrect = true;
            }
        } while (incorrect);

        int numPlayers = 0;
        if (newGame) {
            do {
                System.out.print(Frmt.style('b', "Enter the number of competitors [2..3]: "));
                numPlayers = scanner.nextInt();
                if (numPlayers < 2 || numPlayers > 3) {
                    System.out.println(Frmt.color('r', "> Invalid choice. Try again."));
                    incorrect = true;
                } else {
                    incorrect = false;
                }
            } while (incorrect);
        }
        System.out.println(Frmt.style('i', "> Waiting for the other players to connect..."));
        serverHandler.sendGameInfo(nickname, date, numPlayers);
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

            System.out.print(Frmt.style('b', "Enter your nickname: "));
            nickname = scanner.nextLine();
            if (nickname.equals("")) {
                System.out.println(Frmt.color('r', "> Invalid nickname. Try again."));
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
    public void chooseCards(int numCards) {
        List<Card> allCardList = Configurator.getAllCards();
        List<String> allCardNames = new ArrayList<>();
        List<Card> chosenCard = new ArrayList<>();

        System.out.println(Frmt.style("bu", "\nChoose " + numCards + " cards between these:"));
        for (Card card : allCardList) {
            System.out.println(Frmt.style('b', "\t❖ " + card.getName()));
            System.out.println(Frmt.style('i', "\t\t" + card.getDescription()));
            allCardNames.add(card.getName().toLowerCase());
        }

        boolean incorrect;
        do {
            incorrect = false;
            System.out.print("↳: ");
            String chosenCardName = scanner.next();
            if (!allCardNames.contains(chosenCardName.toLowerCase())) {
                System.out.println(Frmt.color('r', "> Invalid choice. Try again."));
                incorrect = true;
            } else {
                for (Card card : allCardList) {
                    if (card.getName().equalsIgnoreCase(chosenCardName)) {
                        if (chosenCard.contains(card)) {
                            System.out.println(Frmt.color('r', "> Invalid choice. Try again."));
                            incorrect = true;
                        } else {
                            chosenCard.add(card);
                        }
                    }
                }
            }
        } while (incorrect || chosenCard.size() < numCards);
        serverHandler.sendCards(chosenCard);
    }

    /**
     * Asks the card the player whats to use during the game
     *
     * @param possibleChoices All the possible cards
     */
    public void chooseCard(List<Card> possibleChoices) {
        Card chosenCard = null;
        String chosenCardName;
        List<String> allCardNames = new ArrayList<>();

        System.out.println(Frmt.style("bu", "\nChoose your card between these:"));
        for (Card card : possibleChoices) {
            System.out.println(Frmt.style('b', "\t❖ " + card.getName()));
            System.out.println(Frmt.style('i', "\t\t" + card.getDescription()));
            allCardNames.add(card.getName().toLowerCase());
        }

        boolean incorrect;
        do {
            incorrect = false;
            System.out.print("↳: ");
            chosenCardName = scanner.next();
            if (!allCardNames.contains(chosenCardName.toLowerCase())) {
                System.out.println(Frmt.color('r', "> Invalid choice. Try again."));
                incorrect = true;
            }
        } while (incorrect);

        for (Card card : possibleChoices) {
            if (card.getName().equalsIgnoreCase(chosenCardName)) {
                chosenCard = card;
            }
        }
        serverHandler.sendCard(chosenCard);
    }

    /**
     * Asks the nickname of the first player
     *
     * @param playersNicknames All the nicknames
     */
    public void chooseFirstPlayer(List<String> playersNicknames) {
        List<String> allNicknames = new ArrayList<>();

        System.out.print(Frmt.style("bu", "\nChoose who will be the first player:") + " ");
        for (int i = 0; i < playersNicknames.size(); i++) {
            String name = playersNicknames.get(i);
            allNicknames.add(name.toLowerCase());
            if (i != playersNicknames.size() - 1) {
                System.out.print(Frmt.style('b', name + ", "));
            } else {
                System.out.println(Frmt.style('b', name + "?"));
            }
        }

        String chosenNickname;
        boolean incorrect;
        do {
            incorrect = false;
            System.out.print("↳: ");
            chosenNickname = scanner.next();
            if (!allNicknames.contains(chosenNickname.toLowerCase())) {
                System.out.println(Frmt.color('r', "> Invalid choice. Try again."));
                incorrect = true;
            }
        } while (incorrect);
        serverHandler.sendFirstPlayerNickname(chosenNickname);
    }

    /**
     * Show all the cards of the game
     *
     * @param cards All the cards of the game
     */
    public void showGameCards(List<Card> cards) {
        List<Card> allCardList = Configurator.getAllCards();

        System.out.println(Frmt.style("bu", "\nThe cards used in this match will be:"));
        for (Card card : allCardList) {
            if (cards.contains(card)) {
                System.out.println(Frmt.style('b', "\t⚙︎ " + card.getName()));
                System.out.println(Frmt.style('i', "\t\t" + card.getDescription()));
            }
        }
    }

    /**
     * Shows the cards assignment of the game
     *
     * @param playerList The list of players of the game
     */
    public void showCardAssignment(List<Player> playerList) {
        System.out.println(Frmt.style("bu", "\nCards assignment:"));
        for (Player player : playerList) {
            System.out.print(Frmt.style('b', "\t▷ " + player.getNickname()));
            System.out.println(Frmt.style('i', " will use " + player.getCard().getName()));
        }
    }

    /**
     * Asks the color the player whats to choose and the first position for the male and female worker
     *
     * @param availableColors All the available colors
     * @param game            The game
     */
    public void chooseColorAndPosition(List<String> availableColors, Game game) {
        String myColor = chooseColor(availableColors);

        List<Cell> forbiddenCells = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            forbiddenCells.addAll(player.getOccupiedCells());
        }

        game.getPlayerByNickname(serverHandler.getNickname()).chooseColor(Color.getColorByName(myColor));
        Cell maleCell = setPosition(MALE, forbiddenCells, game);
        forbiddenCells.add(maleCell);
        game.getPlayerByNickname(serverHandler.getNickname()).getWorker(MALE).setPosition(maleCell);
        Cell femaleCell = setPosition(FEMALE, forbiddenCells, game);
        serverHandler.sendColorAndPosition(myColor, maleCell, femaleCell);
    }

    /**
     * Asks the color the player whats to choose
     *
     * @param availableColors All the available colors
     * @return The selected color
     */
    private String chooseColor(List<String> availableColors) {
        if (availableColors.size() == 1) {
            System.out.print(Frmt.style("bu", "\nYour color will be") + " ");
            System.out.println(Frmt.color(availableColors.get(0).toLowerCase().charAt(0), availableColors.get(0).toLowerCase()));
            return availableColors.get(0);
        } else {

            String chosenColor;
            List<String> allColors = new ArrayList<>();

            System.out.print(Frmt.style("bu", "\nChoose your color between:") + " ");
            for (int i = 0; i < availableColors.size(); i++) {
                String color = availableColors.get(i);
                if (i != availableColors.size() - 1) {
                    System.out.print(Frmt.color(color.toLowerCase().charAt(0), color.toLowerCase() + ", "));
                } else {
                    System.out.println(Frmt.color(color.toLowerCase().charAt(0), color.toLowerCase() + "?"));
                }
                allColors.add(color.toLowerCase());
            }

            boolean incorrect;
            do {
                incorrect = false;
                System.out.print("↳: ");
                chosenColor = scanner.next();
                if (!allColors.contains(chosenColor.toLowerCase())) {
                    System.out.println(Frmt.color('r', "> Invalid choice. Try again."));
                    incorrect = true;
                }
            } while (incorrect);

            return chosenColor;
        }
    }

    /**
     * Asks the first position for the male and female worker
     *
     * @param genre          The genre of the worker
     * @param forbiddenCells The forbidden cells
     * @return The chosen position
     */
    private Cell setPosition(Genre genre, List<Cell> forbiddenCells, Game game) {
        showMap(game);
        System.out.println(Frmt.style("bu", "\nChoose the position of your " + ((genre == MALE) ? "male" : "female") + " worker [row,column]:") + " ");

        Cell chosenCell;
        String chosenPosition;
        int row = -1, column = -1;
        boolean incorrect;
        do {
            incorrect = false;
            System.out.print("↳: ");
            chosenPosition = scanner.next();
            try {
                row = Character.getNumericValue(chosenPosition.charAt(0));
                column = Character.getNumericValue(chosenPosition.charAt(2));
            } catch (Exception e) {
                System.out.println(Frmt.color('r', "> Invalid choice. Try again."));
                incorrect = true;
            }

            chosenCell = new Cell(row, column);
            if (!incorrect && (row < 0 || column < 0 || row > 4 || column > 4 || forbiddenCells.contains(chosenCell) || chosenPosition.length() != 3)) {
                System.out.println(Frmt.color('r', "> Invalid choice. Try again."));
                incorrect = true;
            }
        } while (incorrect);
        return chosenCell;
    }

    /**
     * Shows the board of the game
     *
     * @param game The game
     */
    public void showMap(Game game) {
        List<Player> playerList = game.getPlayers();
        String[][] cellColorMatrix = new String[5][5];
        Genre[][] cellGenreMatrix = new Genre[5][5];
        Cell[][] cellsInfoMatrix = game.getBoard().getBoard();

        // Null initialization of the info matrix
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                cellColorMatrix[i][j] = null;
                cellGenreMatrix[i][j] = null;
            }
        }

        // Fill matrix with correct information
        for (Player player : playerList) {
            for (Genre genre : Genre.values()) {
                Worker worker = player.getWorker(genre);
                Cell position = worker.getPosition();
                if (position != null) {
                    int row = position.getRow();
                    int column = position.getColumn();
                    cellColorMatrix[row][column] = worker.getColor().toString();
                    cellGenreMatrix[row][column] = genre;
                }
            }
        }

        // Show the map
        System.out.print("\n\t\t");
        for (int j = 0; j < 5; j++) {
            System.out.print(Frmt.color('w', "    " + j + "    " + "  "));
        }
        System.out.println();
        for (int i = 0; i < 5; i++) {
            System.out.print("\n\t\t");
            for (int j = 0; j < 5; j++) {
                char color = (cellColorMatrix[i][j] == null) ? 'w' : cellColorMatrix[i][j].toLowerCase().charAt(0);
                System.out.print(Frmt.color(color, Frmt.style('r', "         ")) + "  ");
            }
            System.out.print("\n\t" + i + "\t");
            for (int j = 0; j < 5; j++) {
                Frmt domed = (cellsInfoMatrix[i][j].getDome()) ? Frmt.DOME : Frmt.NOT_DOME;
                String genre = (cellGenreMatrix[i][j] == null) ? " " : Frmt.getGengreIcon(cellGenreMatrix[i][j]);
                System.out.print(Frmt.color('w', Frmt.style('r', " " + domed + "    " + genre + "  ")) + "  ");
            }
            System.out.print("\n\t\t");
            for (int j = 0; j < 5; j++) {
                int floor = cellsInfoMatrix[i][j].getFloor();
                System.out.print(Frmt.color('w', Frmt.style('r', " " + floor + "       ")) + "  ");
            }
            System.out.print("\n");
        }

    }

    public void askAction(RoundActions roundActions) {
        String action, genre, direction;
        Action theAction;
        boolean incorrect;
        do {
            incorrect = false;
            System.out.print(Frmt.style("bu", "\nInsert your action type [MOVE/BUILD_FLOOR/BUILD_DOME/END]:") + " ");
            action = scanner.next();
            if (!action.equalsIgnoreCase("end")) {
                System.out.print(Frmt.style("bu", "\nInsert the genre of the worker [M/F]:") + " ");
                genre = scanner.next();
                System.out.print(Frmt.style("bu", "\nInsert the direction [N/NE/E/SE/S/SW/W/NW]:") + " ");
                direction = scanner.next();

                theAction = roundActions.find(action, genre, direction);
            } else {
                theAction = roundActions.canEnd();
            }
            if (theAction == null) {
                System.out.println(Frmt.color('r', "> Invalid action. Try again."));
                incorrect = true;
            }
        } while (incorrect);
        serverHandler.sendAction(theAction);
    }

    public void showGameEnd(String winnerNickname, boolean youWin) {
        if (youWin) {
            System.out.println(Frmt.color('g', Frmt.style("bu", "\n\n\tYOU WIN " + Frmt.CUP)));
        } else {
            System.out.println(Frmt.color('g', Frmt.style("bu", "\n\n\tYOU WIN " + Frmt.DEATH)));
        }
    }

    public void showDisconnection(String nickname) {
        System.out.println(Frmt.color('r', Frmt.style("ui", "GAME OVER: " +
                nickname.toUpperCase() + " has disconnected.\n\n")));
        System.out.println("\nDo you want to play again? [Yes/No]: ");

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

        serverHandler.closeConnection();
        if (choice.equalsIgnoreCase("yes")) {
            ClientLauncher.main(null);
        }
    }

}
