package Client.gui;

import Client.ServerHandler;
import Client.View;
import Util.Configurator;
import Util.Genre;
import Util.RoundActions;
import model.Card;
import model.Cell;
import model.Game;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GuiView implements View {
    private ServerHandler serverHandler;
    private JFrame mainFrame;
    private JPanel bodyContainer;

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

            JPanel background = new PanelWithBackground(new ImageIcon("src/main/resources/GuiResources/background.png").getImage());
            background.setLayout(null);

            // Prepare the body container
            bodyContainer = new JPanel();
            bodyContainer.setBounds(80, 200, 1000 - 160, 730 - 280);
            bodyContainer.setBackground(new Color(0, 0, 0, 0));
            background.add(bodyContainer);
            bodyContainer.setLayout(null);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/GuiResources/LeGourmetScript.otf")));
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }

            JLabel label = new JLabel();
            label.setText("Enter the server IP: ");
            label.setBounds(160, 30, 250, 40);
            label.setHorizontalAlignment(JLabel.RIGHT);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("LeGourmetScript", Font.PLAIN, 30));
            bodyContainer.add(label);

            PTextField pTextField = new PTextField(Configurator.getDefaultIp());
            pTextField.setBounds(430, 30, 250, 40);
            bodyContainer.add(pTextField);

            Image scaledImage = new ImageIcon("src/main/resources/GuiResources/btn_blue_next.png").getImage().getScaledInstance(197, 50, Image.SCALE_SMOOTH);
            JButton button = new JButton(new ImageIcon(scaledImage));
            button.setBounds(420 - 98, 300, 197, 50);
            button.setBackground(new Color(0, 0, 0, 0));
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    new Thread(() -> {
                        serverHandler.setConnection(pTextField.getTextFieldText());
                    }).start();
                }
            });
            bodyContainer.add(button);

            mainFrame.add(background);
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
            bodyContainer.setBackground(new Color(0, 0, 0, 0));

            JLabel label = new JLabel();
            label.setText("You will be added to the first available game...");
            label.setBounds(0, 0, 840, 450);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("LeGourmetScript", Font.PLAIN, 40));
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
            bodyContainer.setBackground(new Color(0, 0, 0, 0));

            JLabel label = new JLabel();
            label.setText(message);
            label.setBounds(0, 0, 840, 450);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("LeGourmetScript", Font.PLAIN, 40));
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
            JLabel nicknameLabel = new JLabel();
            nicknameLabel.setText("Nickname: ");
            nicknameLabel.setBounds(10, 30, 400, 40);
            nicknameLabel.setHorizontalAlignment(JLabel.RIGHT);
            nicknameLabel.setVerticalAlignment(JLabel.CENTER);
            nicknameLabel.setForeground(Color.WHITE);
            nicknameLabel.setFont(new Font("LeGourmetScript", Font.PLAIN, 30));
            bodyContainer.add(nicknameLabel);

            PTextField nicknameTextField = new PTextField(null);
            nicknameTextField.setBounds(430, 30, 250, 40);
            bodyContainer.add(nicknameTextField);

            // Error label
            JLabel errorLabel = new JLabel();
            errorLabel.setText("The chosen username is already taken.");
            errorLabel.setBounds(0, 240, 840, 40);
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            errorLabel.setVerticalAlignment(JLabel.CENTER);
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("LeGourmetScript", Font.PLAIN, 25));
            bodyContainer.add(errorLabel);

            Image scaledImage = new ImageIcon("src/main/resources/GuiResources/btn_blue_next.png").getImage().getScaledInstance(197, 50, Image.SCALE_SMOOTH);
            JButton button = new JButton(new ImageIcon(scaledImage));
            button.setBounds(420 - 98, 300, 197, 50);
            button.setBackground(new Color(0, 0, 0, 0));
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    new Thread(() -> {
                        String nickname;

                        nickname = nicknameTextField.getTextFieldText();
                        if (nickname.equals("") || nickname.contains(" ")) {
                            errorLabel.setText("Invalid nickname. Try again.");
                            return;
                        }

                        // No errors
                        errorLabel.setText("");

                        serverHandler.sendNewNickname(nickname);
                        showMessage("Waiting for the other players to connect...", true);
                    }).start();
                }
            });
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
            //bodyContainer.setOpaque(true);
            bodyContainer.setBackground(new Color(0, 0, 0, 0));

            // Nickname
            JLabel nicknameLabel = new JLabel();
            nicknameLabel.setText("Nickname: ");
            nicknameLabel.setBounds(10, 30, 400, 40);
            nicknameLabel.setHorizontalAlignment(JLabel.RIGHT);
            nicknameLabel.setVerticalAlignment(JLabel.CENTER);
            nicknameLabel.setForeground(Color.WHITE);
            nicknameLabel.setFont(new Font("LeGourmetScript", Font.PLAIN, 30));
            bodyContainer.add(nicknameLabel);

            PTextField nicknameTextField = new PTextField(null);
            nicknameTextField.setBounds(430, 30, 250, 40);
            bodyContainer.add(nicknameTextField);

            // Date of birth
            JLabel dateLabel = new JLabel();
            dateLabel.setText("Date of birth [dd/mm/yyyy]: ");
            dateLabel.setBounds(10, 100, 400, 40);
            dateLabel.setHorizontalAlignment(JLabel.RIGHT);
            dateLabel.setVerticalAlignment(JLabel.CENTER);
            dateLabel.setForeground(Color.WHITE);
            dateLabel.setFont(new Font("LeGourmetScript", Font.PLAIN, 30));
            bodyContainer.add(dateLabel);

            PTextField dateTextField = new PTextField(null);
            dateTextField.setBounds(430, 100, 250, 40);
            bodyContainer.add(dateTextField);

            // Number of competitors
            JLabel playersNumberLabel = new JLabel();
            playersNumberLabel.setText("Number of competitors [2..3]: ");
            playersNumberLabel.setBounds(10, 170, 400, 40);
            playersNumberLabel.setHorizontalAlignment(JLabel.RIGHT);
            playersNumberLabel.setVerticalAlignment(JLabel.CENTER);
            playersNumberLabel.setForeground(Color.WHITE);
            playersNumberLabel.setFont(new Font("LeGourmetScript", Font.PLAIN, 30));
            if (newGame) {
                bodyContainer.add(playersNumberLabel);
            }

            PTextField playersNumberTextField = new PTextField(null);
            playersNumberTextField.setBounds(430, 170, 250, 40);
            if (newGame) {
                bodyContainer.add(playersNumberTextField);
            }


            // Error label
            JLabel errorLabel = new JLabel();
            errorLabel.setText("");
            errorLabel.setBounds(0, 240, 840, 40);
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            errorLabel.setVerticalAlignment(JLabel.CENTER);
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("LeGourmetScript", Font.PLAIN, 25));
            bodyContainer.add(errorLabel);

            Image scaledImage = new ImageIcon("src/main/resources/GuiResources/btn_blue_next.png").getImage().getScaledInstance(197, 50, Image.SCALE_SMOOTH);
            JButton button = new JButton(new ImageIcon(scaledImage));
            button.setBounds(420 - 98, 300, 197, 50);
            //button.setOpaque(true);
            button.setBackground(new Color(0, 0, 0, 0));
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    new Thread(() -> {
                        Date date;
                        String nickname;
                        int playersNumber;

                        nickname = nicknameTextField.getTextFieldText();
                        if (nickname.equals("") || nickname.contains(" ")) {
                            errorLabel.setText("Invalid nickname. Try again.");
                            return;
                        }

                        try {
                            date = new SimpleDateFormat("dd/MM/yyyy").parse(dateTextField.getTextFieldText());
                        } catch (ParseException e) {
                            errorLabel.setText("Invalid date. Try again.");
                            return;
                        }

                        if (newGame) {
                            playersNumber = Integer.parseInt(playersNumberTextField.getTextFieldText());
                            if (playersNumber < 2 || playersNumber > 3) {
                                errorLabel.setText("Invalid choice. Try again.");
                                return;
                            }
                        } else {
                            playersNumber = 0;
                        }

                        serverHandler.sendSetUpGame(nickname, date, playersNumber);
                    }).start();
                }
            });
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
}

