package Client;

import Model.Card;
import Util.Configurator;
import Util.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the command line interface to the user
 */
public class CliView implements View {
    private Scanner scanner;
    private ServerHandler serverHandler;
    private String nickname;

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
        System.out.print(Formatter.fText('b', "Enter the server IP: "));
        String serverIP = scanner.nextLine();
        if (serverIP.equals("")) {
            serverIP = defaultServerIP;
            System.out.println(Formatter.fText('i', "> Default server IP will be applied, " + defaultServerIP));
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
        System.out.println(Formatter.cText('r', "> Error: the chosen username is already taken."));
        String newNickname = askNickname();
        System.out.println(Formatter.fText('i', "> Waiting for the other players to connect..."));
        this.nickname = newNickname;
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
            System.out.print(Formatter.fText('b', "Enter your birth date [dd/mm/yyyyy]: "));
            String fullDate = scanner.nextLine();
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(fullDate);
                incorrect = false;
            } catch (ParseException e) {
                System.out.println(Formatter.cText('r', "> Incorrect date format. Try again."));
                incorrect = true;
            }
        } while (incorrect);

        int numPlayers = 0;
        if (newGame) {
            do {
                System.out.print(Formatter.fText('b', "Enter the number of competitors [2..3]: "));
                numPlayers = scanner.nextInt();
                if (numPlayers < 2 || numPlayers > 3) {
                    System.out.println(Formatter.cText('r', "> Invalid choice. Try again."));
                    incorrect = true;
                } else {
                    incorrect = false;
                }
            } while (incorrect);
        }
        System.out.println(Formatter.fText('i', "> Waiting for the other players to connect..."));
        this.nickname = nickname;
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

            System.out.print(Formatter.fText('b', "Enter your nickname: "));
            nickname = scanner.nextLine();
            if (nickname.equals("")) {
                System.out.println(Formatter.cText('r', "> Invalid nickname. Try again."));
                incorrect = true;
            } else {
                incorrect = false;
            }
        } while (incorrect);
        return nickname;
    }

    public void chooseCards(int numCards) {
        List<Card> allCardList = Configurator.getAllCards();
        List<String> allCardNames = new ArrayList<>();
        List<Card> chosenCard = new ArrayList<>();

        System.out.println(Formatter.fText('b', "\nChoose " + numCards + " cards between these:"));
        for (Card card : allCardList) {
            System.out.println(Formatter.fText('b', "\t-" + card.getName()));
            System.out.println(Formatter.fText('i', "\t\t" + card.getDescription()));
            allCardNames.add(card.getName().toLowerCase());
        }

        boolean incorrect;
        do {
            incorrect = false;
            System.out.print(">: ");
            String chosenCardName = scanner.next();
            if (!allCardNames.contains(chosenCardName.toLowerCase())) {
                System.out.println(Formatter.cText('r', "> Invalid choice. Try again."));
                incorrect = true;
            } else {
                for (Card card : allCardList) {
                    if (card.getName().equalsIgnoreCase(chosenCardName)) {
                        if (chosenCard.contains(card)) {
                            System.out.println(Formatter.cText('r', "> Invalid choice. Try again."));
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

    public void chooseCard(List<Card> possibleChoices) {
        Card chosenCard = null;
        String chosenCardName;
        List<String> allCardNames = new ArrayList<>();

        System.out.println(Formatter.fText('b', "\nChoose your card between these:"));
        for (Card card : possibleChoices) {
            System.out.println(Formatter.fText('b', "\t-" + card.getName()));
            System.out.println(Formatter.fText('i', "\t\t" + card.getDescription()));
            allCardNames.add(card.getName().toLowerCase());
        }

        boolean incorrect;
        do {
            incorrect = false;
            System.out.print(">: ");
            chosenCardName = scanner.next();
            if (!allCardNames.contains(chosenCardName.toLowerCase())) {
                System.out.println(Formatter.cText('r', "> Invalid choice. Try again."));
                incorrect = true;
            }
        } while (incorrect);

        for (Card card : possibleChoices) {
            if (card.getName().equalsIgnoreCase(chosenCardName)) {
                chosenCard = card;
            }
        }
        serverHandler.sendCard(chosenCard, nickname);
    }

}
