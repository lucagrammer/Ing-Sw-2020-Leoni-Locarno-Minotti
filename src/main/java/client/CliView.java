package client;

import model.Card;
import model.Cell;
import model.Player;
import util.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static util.Genre.MALE;

/**
 * Manages the command line interface to the user
 */
public class CliView implements View {
    private final Scanner scanner;
    private final InputValidator inputValidator;
    private ServerHandler serverHandler;

    /**
     * Constructor: build the CliView
     */
    public CliView() {
        this.inputValidator = new InputValidator();
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
        String serverIP;
        boolean correct;

        Frmt.clearScreen();
        do {
            System.out.print(Frmt.style('b', " Enter the server IP [press enter for default IP]: "));
            serverIP = scanner.nextLine();
            if (inputValidator.isEmptyIp(serverIP)) {
                correct=true;
                serverIP = defaultServerIP;
                System.out.println(Frmt.style('i', "  > Default server IP will be applied, " + defaultServerIP));
            }else{
                correct=inputValidator.isIp(serverIP);
                if (!correct){
                    Frmt.clearScreen();
                    System.out.println(Frmt.color('r', "  > Invalid ip. Try again."));
                }
            }
        }while(!correct);
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
     * @param message   The message to be shown
     * @param newScreen True if it's necessary to clean the interface
     */
    public void showMessage(String message, boolean newScreen) {
        if (newScreen)
            Frmt.clearScreen();
        System.out.println(message);
    }

    /**
     * Shows a loading message to the user
     */
    public void showLoading() {
        Frmt.clearScreen();
        System.out.println(Frmt.style('i', "  > The game will start shortly, get ready!"));
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
        Frmt.clearScreen();
        String nickname = askNickname();

        Date date = askDate();

        boolean correct;
        Integer numPlayers = 0;
        if (newGame) {
            Frmt.clearScreen();
            do {
                System.out.print(Frmt.style('b', "\n Enter the number of competitors [2..3]: "));
                String numPlayersString = scanner.nextLine();

                numPlayers=inputValidator.isNumPlayer(numPlayersString);
                correct= numPlayers!=null;
                if (!correct) {
                    Frmt.clearScreen();
                    System.out.println(Frmt.color('r', "  > Invalid choice. Try again."));
                }
            } while (!correct);
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
    private Date askDate() {
        boolean correct;
        Date date;
        Frmt.clearScreen();
        do {
            System.out.print(Frmt.style('b', "\n Enter your birth date [dd/mm/yyyy]: "));
            String fullDate = scanner.nextLine();
            date=inputValidator.isDate(fullDate);
            correct= date!=null;
            if (!correct) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "  > Incorrect date format. Try again."));
            }
        } while (!correct);
        return date;
    }

    /**
     * Asks nickname
     *
     * @return The chosen nickname
     */
    private String askNickname() {
        boolean correct;
        String nickname;
        do {

            System.out.print(Frmt.style('b', "\n Enter your nickname: "));
            nickname = scanner.nextLine();
            correct = inputValidator.isNickname(nickname);
            if (!correct) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "  > Invalid nickname. Try again."));
            }
        } while (!correct);
        return nickname;
    }

    /**
     * Asks the game cards
     *
     * @param numCards Number of cards to be selected
     */
    public void askGameCards(int numCards) {
        List<Card> chosenCards = new ArrayList<>();

        boolean correct;
        Frmt.clearScreen();
        do {
            System.out.println("\n\n " + Frmt.style('b', "Choose " + (numCards - chosenCards.size()) + " card" + ((numCards - chosenCards.size() > 1) ? "s" : "") + " between these:"));
            List<String> availableCards = printAllCardsExcept(chosenCards);

            System.out.print("  ↳: ");
            String chosenCardName = scanner.next();
            Card enteredCard = inputValidator.isCard(chosenCardName, availableCards);
            correct = enteredCard!=null;
            if(!correct){
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "   > Invalid choice. Try again."));
            }else{
                chosenCards.add(enteredCard);
            }

        } while (!correct || chosenCards.size() < numCards);

        showLoading();
        serverHandler.sendGameCards(chosenCards);
    }

    /**
     * Print all the available cards except the specified ones
     *
     * @param unavailableCards  The unavailable cards
     * @return A list containing all the names of the printed available cards
     */
    private List<String> printAllCardsExcept(List<Card> unavailableCards) {
        List<Card> allCardList = Configurator.getAllCards();
        List<String> availableCards = new ArrayList<>();
        for (Card card : allCardList) {
            if (!unavailableCards.contains(card)) {
                System.out.println(Frmt.style('b', "\n   ❖ " + card.getName().toUpperCase()));
                System.out.println(Frmt.style('i', "      " + card.getDescription()));
                availableCards.add(card.getName().toLowerCase());
            }
        }
        System.out.println();
        return availableCards;
    }

    /**
     * Asks the card the player whats to use during the game
     *
     * @param possibleChoices All the possible cards
     */
    public void askPlayerCard(List<Card> possibleChoices) {
        Card chosenCard;
        List<String> availableNames = new ArrayList<>();

        boolean correct;
        do {
            System.out.println("\n\n " + Frmt.style('b', "Choose your card between these:"));
            for (Card card : possibleChoices) {
                System.out.println(Frmt.style('b', "\n   ❖ " + card.getName().toUpperCase()));
                System.out.println(Frmt.style('i', "      " + card.getDescription()));
                availableNames.add(card.getName().toLowerCase());
            }
            System.out.println();

            System.out.print("  ↳: ");
            String chosenCardName = scanner.next();
            chosenCard = inputValidator.isCard(chosenCardName, availableNames);
            correct = chosenCard!=null;
            if(!correct){
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "   > Invalid choice. Try again."));
            }
        } while (!correct);

        showLoading();
        serverHandler.sendPlayerCard(chosenCard);
    }

    /**
     * Asks the nickname of the first player
     *
     * @param playersNicknames All the nicknames
     */
    public void askFirstPlayer(List<String> playersNicknames) {

        String chosenNickname;
        boolean correct;
        System.out.println("\n\n");
        do {
            System.out.print(" " + Frmt.style('b', "Choose who will be the first player:") + " ");
            for (int i = 0; i < playersNicknames.size(); i++) {
                String name = playersNicknames.get(i);
                if (i != playersNicknames.size() - 1) {
                    System.out.print(Frmt.style('b', name + ", "));
                } else {
                    System.out.println(Frmt.style('b', name + "?"));
                }
            }

            System.out.print("  ↳: ");
            chosenNickname = scanner.next();
            correct= inputValidator.isNicknameBetween(chosenNickname,playersNicknames);
            if (!correct) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "   > Invalid choice. Try again."));
            }
        } while (!correct);

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
    public void askPlayerColor(ArrayList<String> availableColors) {
        String chosenColor;

        Frmt.clearScreen();
        //There's only one color?
        if (availableColors.size() == 1) {
            System.out.print("\n\n " + Frmt.style('b', "Your color will be") + " ");
            System.out.println(Frmt.color(availableColors.get(0).toLowerCase().charAt(0), availableColors.get(0).toLowerCase()));
            chosenColor = availableColors.get(0);

        } else {
            boolean correct;
            do {
                System.out.print("\n\n " + Frmt.style('b', "Choose your color between:") + " ");
                for (int i = 0; i < availableColors.size(); i++) {
                    String color = availableColors.get(i);
                    if (i != availableColors.size() - 1) {
                        System.out.print(Frmt.color(color.toLowerCase().charAt(0), color.toLowerCase() + ", "));
                    } else {
                        System.out.println(Frmt.color(color.toLowerCase().charAt(0), color.toLowerCase() + "?"));
                    }
                }

                System.out.print("  ↳: ");
                chosenColor = scanner.next();
                correct= inputValidator.isColorBetween(chosenColor,availableColors);
                if (!correct) {
                    Frmt.clearScreen();
                    System.out.println(Frmt.color('r', "   > Invalid choice. Try again."));
                }
            } while (!correct);
        }
        serverHandler.sendPlayerColor(chosenColor);
    }

    /**
     * Asks the first position for the male and female worker
     * @param genre          The genre of the worker
     * @param forbiddenCells The forbidden cells
     * @param mapInfo        The map info
     */
    public void askPlayerPosition(Genre genre, List<Cell> forbiddenCells, MapInfo mapInfo) {
        Cell chosenCell;
        String chosenPosition;
        int row = -1, column = -1;
        boolean incorrect;
        Frmt.clearScreen();
        do {
            showMap(mapInfo, false);
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
     * @param mapInfo   The map info
     * @param newScreen True if it's necessary to clean the interface
     */
    public void showMap(MapInfo mapInfo, boolean newScreen) {
        List<String> playerNames=mapInfo.getNicknames();
        List<String> colors=mapInfo.getColors();
        List<String> cards=mapInfo.getCards();

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
                char color = (mapInfo.getColorAt(i,j) == null) ? 'w' : mapInfo.getColorAt(i,j).charAt(0);
                System.out.print(Frmt.color(color, Frmt.style('r', "         ")) + "  ");
            }

            // Legend
            if (i == 1 && colors.size() > 0) {
                System.out.print("\t\t\t  " + Frmt.color(colors.get(0).charAt(0), Frmt.style('r', "  "
                        + playerNames.get(0).toUpperCase() + " - " + cards.get(0) + "  ")));
            }

            if (i == 2) {
                System.out.print("\n\tW\t" + i + "\t");
            } else {
                System.out.print("\n\t\t" + i + "\t");
            }
            for (int j = 0; j < 5; j++) {
                Frmt domed = (mapInfo.getDomeAt(i,j)) ? Frmt.DOME : Frmt.NOT_DOME;
                String genre = (mapInfo.getGenreAt(i,j) == null) ? " " : Frmt.getGenreIcon(mapInfo.getGenreAt(i,j));
                System.out.print(Frmt.color('w', Frmt.style('r', " " + domed + "    " + genre + "  ")) + "  ");
            }
            if (i == 2) {
                System.out.print("\t\tE");
            }

            // Legend
            if (i == 1 && colors.size() > 1) {
                System.out.print("\t\t\t  " + Frmt.color(colors.get(1).charAt(0), Frmt.style('r', "  "
                        + playerNames.get(1).toUpperCase() + " - " + cards.get(1) + "  ")));
            }

            System.out.print("\n\t\t\t");
            for (int j = 0; j < 5; j++) {
                int floor = mapInfo.getFloorAt(i,j);
                System.out.print(Frmt.color('w', Frmt.style('r', " " + floor + "       ")) + "  ");
            }

            // Legend
            if (i == 1 && colors.size() > 2) {
                System.out.print("\t\t\t  " + Frmt.color(colors.get(2).charAt(0), Frmt.style('r', "  "
                        + playerNames.get(2).toUpperCase() + " - " + cards.get(2) + "  ")));
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
     *  @param roundActions     All the possible actions
     * @param mapInfo           The map info
     * @param loserNickname     The nickname of the looser or null value
     */
    public void askAction(RoundActions roundActions, MapInfo mapInfo, String loserNickname) {
        String action, genre, direction;
        Action theAction;
        boolean incorrect;

        Frmt.clearScreen();
        do {
            showMap(mapInfo, false);
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
                disconnectedNickname + " has disconnected.")));
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
