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
     * Interface launcher. Asks the server IP to connect to and notify it to the
     serverHandler
     */
    public void launch() {
        SwingUtilities.invokeLater(() -> {// Prepare the main frame
            mainFrame = new JFrame("Santorini");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1000, 730);
            mainFrame.setResizable(false);
            JPanel background = new PanelWithBackground(new ImageIcon("src/
                    main/resources/GuiResources/background.png").getImage());
                    background.setLayout(null);
// Prepare the body container
            bodyContainer = new JPanel();
            bodyContainer.setBounds(80, 200, 1000 - 160, 730 - 280);
            bodyContainer.setBackground(new Color(0, 0, 0, 0));
            background.add(bodyContainer);
            bodyContainer.setLayout(null);
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new
                        File("src/main/resources/GuiResources/LeGourmetScript.otf")));
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
            Image scaledImage = new ImageIcon("src/main/resources/
                    GuiResources/btn_blue_next.png").getImage().getScaledInstance(197, 50,
                    Image.SCALE_SMOOTH);
            JButton button = new JButton(new ImageIcon(scaledImage));
            button.setBounds(420 - 98, 300, 197, 50);button.setBackground(new Color(0, 0, 0, 0));
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
     * @param message The message to be shown* @param newScreen True if it's necessary to clean the interface
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
// Prepare the main frame
        mainFrame = new JFrame("Santorini");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 730);
        mainFrame.setResizable(false);
        JPanel background = new PanelWithBackground(new ImageIcon("src/
                main/resources/GuiResources/background.png").getImage());
                background.setLayout(null);
// Prepare the body container
        bodyContainer = new JPanel();
        bodyContainer.setBounds(80, 200, 1000 - 160, 730 - 280);
        bodyContainer.setBackground(new Color(0, 0, 0, 0));
        background.add(bodyContainer);
        bodyContainer.setLayout(null);
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new
                    File("src/main/resources/GuiResources/LeGourmetScript.otf")));
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
        Image scaledImage = new ImageIcon("src/main/resources/
                GuiResources/btn_blue_next.png").getImage().getScaledInstance(197, 50,
                Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(scaledImage));
        button.setBounds(420 - 98, 300, 197, 50);button.setBackground(new Color(0, 0, 0, 0));
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
     * @param message The message to be shown* @param newScreen True if it's necessary to clean the interface
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