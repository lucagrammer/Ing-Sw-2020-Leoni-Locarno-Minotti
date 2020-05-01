package client;

import Util.Configurator;
import Util.Genre;
import Util.RoundActions;
import client.gui.*;
import model.Card;
import model.Cell;
import model.Game;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuiView implements View {
    private ServerHandler serverHandler;
    private JFrame mainFrame;
    private PPanelContainer bodyContainer;

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
        SwingUtilities.invokeLater(() -> {

            // Prepare the main frame
            mainFrame = new JFrame("Santorini");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1000, 730);
            mainFrame.setResizable(false);

            Image backgroundImage = (new ImageIcon(getClass().getResource("/GuiResources/background.png"))).getImage();
            JPanel background = new PPanelBackground(backgroundImage);
            mainFrame.repaint();
            background.setLayout(null);
            mainFrame.add(background);

            // Prepare the body container
            bodyContainer = new PPanelContainer();
            bodyContainer.setBounds(80, 200, 840, 450);
            background.add(bodyContainer);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {
                ge.registerFont(Font.createFont(0, new File(getClass().getResource("/GuiResources/LeGourmetScript.otf").getFile())));
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }

            PLabel label = new PLabelForm("Enter the server IP: ");
            label.setBounds(160, 30, 250, 40);
            bodyContainer.add(label);

            PTextField textField = new PTextField(Configurator.getDefaultIp());
            textField.setBounds(430, 30, 250, 40);
            bodyContainer.add(textField);

            Image scaledImage = (new ImageIcon(getClass().getResource("/GuiResources/btn_blue_next.png"))).getImage().getScaledInstance(197, 50, Image.SCALE_SMOOTH);
            PButton button = new PButton(scaledImage);
            button.setBounds(322, 300, 197, 50);
            button.addActionListener((ev) -> (new Thread(() -> {
                showQueuedMessage();
                serverHandler.setConnection(textField.getTextFieldText());
            })).start());
            bodyContainer.add(button);
            mainFrame.setVisible(true);
        });
    }

    /**
     * Shows a message to say to the user that is connected to
     * the server and will be added to the next available game
     */
    public void showQueuedMessage() {
        SwingUtilities.invokeLater(() -> {
            // Flush body components
            bodyContainer.removeAll();

            PLabel label = new PLabel("You will be added to the first available game...");
            label.setBounds(0, 0, 840, 450);
            bodyContainer.add(label);

            // Apply
            bodyContainer.revalidate();
            bodyContainer.repaint();
        });
    }

    /**
     * Shows a specified message to the user
     *
     * @param message   The message to be shown
     * @param newScreen True if it's necessary to clean the interface
     */
    public void showMessage(String message, boolean newScreen) {
        SwingUtilities.invokeLater(() -> {
            // Flush body components
            bodyContainer.removeAll();

            PLabel label = new PLabel(message);
            label.setBounds(0, 0, 840, 450);
            bodyContainer.add(label);

            // Apply
            bodyContainer.revalidate();
            bodyContainer.repaint();
        });
    }


    public void showLoading() {
        SwingUtilities.invokeLater(() -> {
            // Flush body components
            bodyContainer.removeAll();

            PLabel label = new PLabel("The game will start shortly, get ready!");
            label.setBounds(0, 0, 840, 450);
            bodyContainer.add(label);

            // Apply
            bodyContainer.revalidate();
            bodyContainer.repaint();
        });
    }

    /**
     * Asks a new nickname to the user and notify the choice to the serverHandler
     */
    public void askNewNickname() {
        SwingUtilities.invokeLater(() -> {
            // Flush body components
            bodyContainer.removeAll();

            // New Nickname
            PLabel nicknameLabel = new PLabelForm("Nickname: ");
            nicknameLabel.setBounds(10, 31, 400, 40);
            bodyContainer.add(nicknameLabel);

            PTextField nicknameTextField = new PTextField(null);
            nicknameTextField.setBounds(430, 30, 250, 40);
            bodyContainer.add(nicknameTextField);

            // Error label
            PLabel errorLabel = new PLabelError("The chosen username is already taken.");
            errorLabel.setBounds(0, 240, 840, 40);
            bodyContainer.add(errorLabel);

            Image scaledImage = (new ImageIcon(this.getClass().getResource("/GuiResources/btn_blue_next.png"))).getImage().getScaledInstance(197, 50, Image.SCALE_SMOOTH);
            PButton button = new PButton(scaledImage);
            button.setBounds(322, 300, 197, 50);
            button.addActionListener((ev) -> (new Thread(() -> {
                String nickname = nicknameTextField.getTextFieldText();
                if (!nickname.equals("") && !nickname.contains(" ")) {
                    // No errors
                    errorLabel.setText("");

                    showMessage("Waiting for the other players to connect...", true);
                    serverHandler.sendNewNickname(nickname);
                } else {
                    errorLabel.setText("Invalid nickname. Try again.");
                }
            })).start());
            bodyContainer.add(button);

            // Apply
            bodyContainer.revalidate();
            bodyContainer.repaint();
        });
    }

    /**
     * Asks nickname, birth date and if it's a new game the number of players
     * for the game and notify the information to the serverHandler
     *
     * @param newGame True if the it is a new game, otherwise false
     */
    public void setUpGame(boolean newGame) {
        SwingUtilities.invokeLater(() -> {
            // Flush body components
            bodyContainer.removeAll();

            // Nickname
            PLabel nicknameLabel = new PLabelForm("Nickname: ");
            nicknameLabel.setBounds(10, 30, 400, 40);
            bodyContainer.add(nicknameLabel);

            PTextField nicknameTextField = new PTextField(null);
            nicknameTextField.setBounds(430, 30, 250, 40);
            bodyContainer.add(nicknameTextField);

            // Date of birth
            PLabel dateLabel = new PLabelForm("Date of birth [dd/mm/yyyy]: ");
            dateLabel.setBounds(10, 100, 400, 40);
            bodyContainer.add(dateLabel);

            PTextField dateTextField = new PTextField(null);
            dateTextField.setBounds(430, 100, 250, 40);
            bodyContainer.add(dateTextField);

            // Number of competitors
            PLabel playersNumberLabel = new PLabelForm("Number of competitors [2..3]: ");
            playersNumberLabel.setBounds(10, 170, 400, 40);
            if (newGame) {
                bodyContainer.add(playersNumberLabel);
            }

            PTextField playersNumberTextField = new PTextField(null);
            playersNumberTextField.setBounds(430, 170, 250, 40);
            if (newGame) {
                bodyContainer.add(playersNumberTextField);
            }


            // Error label
            PLabel errorLabel = new PLabelError("");
            errorLabel.setBounds(0, 240, 840, 40);
            bodyContainer.add(errorLabel);
            Image scaledImage = (new ImageIcon(getClass().getResource("/GuiResources/btn_blue_next.png"))).getImage().getScaledInstance(197, 50, Image.SCALE_SMOOTH);
            PButton button = new PButton(scaledImage);
            button.setBounds(322, 300, 197, 50);
            button.addActionListener((ev) -> (new Thread(() -> {
                String nickname = nicknameTextField.getTextFieldText();
                if (!nickname.equals("") && !nickname.contains(" ")) {
                    Date date;
                    try {
                        date = (new SimpleDateFormat("dd/MM/yyyy")).parse(dateTextField.getTextFieldText());
                    } catch (ParseException e) {
                        errorLabel.setText("Invalid date. Try again.");
                        return;
                    }

                    int playersNumber;
                    if (newGame) {
                        try {
                            playersNumber = Integer.parseInt(playersNumberTextField.getTextFieldText());
                        } catch (Exception e) {
                            errorLabel.setText("Invalid choice. Try again.");
                            return;
                        }

                        if (playersNumber < 2 || playersNumber > 3) {
                            errorLabel.setText("Invalid choice. Try again.");
                            return;
                        }
                    } else {
                        playersNumber = 0;
                    }

                    showMessage("Waiting for the other players to connect...", true);
                    serverHandler.sendSetUpGame(nickname, date, playersNumber);
                } else {
                    errorLabel.setText("Invalid nickname. Try again.");
                }
            })).start());
            bodyContainer.add(button);

            // Apply
            bodyContainer.revalidate();
            bodyContainer.repaint();
        });
    }

    /**
     * Asks the game cards
     *
     * @param numCards Number of cards to be selected
     */
    public void askGameCards(int numCards) {
        List<Card> chosenCard = new ArrayList<>();
        printCards(chosenCard, numCards);
    }

    private void printCards(List<Card> chosenCards, int numCards) {
        if (numCards == chosenCards.size()) {
            (new Thread(() -> {
                showLoading();
                serverHandler.sendGameCards(chosenCards);
            })).start();
        } else {
            SwingUtilities.invokeLater(() -> {
                // Flush body components
                this.bodyContainer.removeAll();

                List<Card> allCardList = Configurator.getAllCards();

                // Remove cards
                allCardList.removeAll(chosenCards);

                PLabel label = new PLabel("Choose " + (numCards - chosenCards.size()) + " card" + (numCards - chosenCards.size() > 1 ? "s" : "") + " between these:");
                label.setFontSize(30);
                label.setBounds(0, 0, 840, 40);
                bodyContainer.add(label);

                PPanelContainer cardContainer = new PPanelContainer();
                cardContainer.setBounds(0, 80, bodyContainer.getWidth(), 138);
                cardContainer.setLayout(new GridLayout(1, 9, 10, 0));
                bodyContainer.add(cardContainer);

                PPanelContainer descriptionBackground = new PPanelContainer();
                descriptionBackground.setBounds(0, 280, bodyContainer.getWidth(), 150);
                descriptionBackground.setLayout(null);
                bodyContainer.add(descriptionBackground);

                PLabel nameLabel = new PLabel("");
                nameLabel.setFontSize(30);
                nameLabel.setForeground(new Color(186, 164, 154));
                nameLabel.setBounds(0, 20, descriptionBackground.getWidth(), 35);
                descriptionBackground.add(nameLabel);

                PLabel descriptionLabel = new PLabel("");
                descriptionLabel.setFont(new Font("Helvetica", Font.PLAIN, 17));
                descriptionLabel.setBounds(90, 50, descriptionBackground.getWidth() - 180, 80);
                descriptionBackground.add(descriptionLabel);

                for (Card card : allCardList) {
                    Image scaledImage = Configurator.getCardImage(card.getName()).getScaledInstance(82, 138, Image.SCALE_SMOOTH);
                    PButton cardButton = new PButton(scaledImage);
                    cardContainer.add(cardButton);
                    cardButton.addMouseListener(new GuiView.CardMouseListener(card, nameLabel, descriptionLabel, chosenCards, numCards));
                }

                // Apply
                bodyContainer.revalidate();
                bodyContainer.repaint();
            });
        }

    }

    /**
     * Asks the card the player whats to use during the game
     *
     * @param possibleChoices All the possible cards
     */
    public void askPlayerCard(List<Card> possibleChoices) {

    }

    /**
     * Asks the nickname of the first player
     *
     * @param playersNicknames All the nicknames
     */
    public void askFirstPlayer(List<String> playersNicknames) {

    }

    /**
     * Show all the cards of the game
     *
     * @param cards All the cards of the game
     */
    public void showGameCards(List<Card> cards) {

    }

    /**
     * Shows th cards assignment of the game
     *
     * @param playerList The list of players of the game
     */
    public void showCardAssignmentMessage(List<Player> playerList) {

    }

    /**
     * Asks the color the player whats to choose
     *
     * @param availableColors All the available colors
     */
    public void askPlayerColor(List<String> availableColors) {

    }

    /**
     * Asks the first position for the male and female worker
     *
     * @param genre          The genre of the worker
     * @param forbiddenCells The forbidden cells
     * @param game           The game
     */
    public void askPlayerPosition(Genre genre, List<Cell> forbiddenCells, Game game) {

    }

    /**
     * Shows the board of the game
     *
     * @param game      The game
     * @param newScreen True if it's necessary to clean the interface
     */
    public void showMap(Game game, boolean newScreen) {

    }

    /**
     * Asks the action the player wants to perform
     *
     * @param roundActions  All the possible actions
     * @param game          The game
     * @param loserNickname The nickname of the looser or null value
     */
    public void askAction(RoundActions roundActions, Game game, String loserNickname) {

    }

    /**
     * Notify the players that the game has ended and notify the winner
     *
     * @param winnerNickname The nickname of the winner
     * @param youWin         True if the player has win
     */
    public void showGameEndMessage(String winnerNickname, boolean youWin) {

    }

    /**
     * Notify the players that a player has disconnected and the game has ended
     *
     * @param disconnectedNickname The nickname of the disconnected player
     */
    public void showDisconnectionMessage(String disconnectedNickname) {

    }

    /**
     * Shows an error message to the user
     *
     * @param errorMessage The message to be shown
     * @param newScreen    True if it's necessary to clean the interface
     */
    public void showErrorMessage(String errorMessage, boolean newScreen) {
        SwingUtilities.invokeLater(() -> {
            // Flush body components
            bodyContainer.removeAll();

            PLabel label = new PLabel(errorMessage);
            label.setBounds(0, 0, 840, 450);
            label.setForeground(Color.RED);
            bodyContainer.add(label);

            // Apply
            bodyContainer.revalidate();
            bodyContainer.repaint();
        });
    }

    class CardMouseListener implements MouseListener {
        private final Card card;
        private final PLabel nameLabel;
        private final PLabel descriptionLabel;
        private final int numCards;
        private final List<Card> chosenCards;

        public CardMouseListener(Card card, PLabel nameLabel, PLabel descriptionLabel, List<Card> chosenCards, int numCards) {
            this.card = card;
            this.nameLabel = nameLabel;
            this.descriptionLabel = descriptionLabel;
            this.chosenCards = chosenCards;
            this.numCards = numCards;
        }

        public void mouseClicked(MouseEvent e) {
            chosenCards.add(card);
            printCards(chosenCards, numCards);
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
            nameLabel.setText(card.getName().toUpperCase());
            String description = card.getDescription();
            descriptionLabel.setText("<HTML><CENTER>" + description + "</CENTER></HTML>");
        }

        public void mouseExited(MouseEvent e) {
            nameLabel.setText("");
            descriptionLabel.setText("");
        }
    }
}
