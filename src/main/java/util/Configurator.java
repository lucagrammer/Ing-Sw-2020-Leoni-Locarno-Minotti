package util;

import model.Card;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import server.rules.EnemyRules;
import server.rules.Rules;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Menage the configuration of the application
 */
public class Configurator {

    private static final SAXBuilder builder = new SAXBuilder();
    private static final Object connectionConfigLock = new Object();
    private static final Object cardsConfigLock = new Object();
    private static final boolean showErrorDetailsFlag = true;
    private static final boolean showPingFlag = false;

    /**
     * Gets the default IP of the server
     *
     * @return The default IP of the server
     */
    public static String getDefaultIp() {
        String ip = null;

        try {
            Document document;
            synchronized (connectionConfigLock) {
                document = builder.build(new File(Configurator.class.getResource("/ConnectionConfig.xml").getFile()));
            }
            Element rootElement = document.getRootElement();

            ip = rootElement.getChildText("server-ip-default");
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * Gets the default port of the server
     *
     * @return The default port of the server
     */
    public static int getDefaultPort() {
        int port = 0;
        try {
            Document document;
            synchronized (connectionConfigLock) {
                document = builder.build(new File(Configurator.class.getResource("/ConnectionConfig.xml").getFile()));
            }
            Element rootElement = document.getRootElement();

            port = Integer.parseInt(rootElement.getChildText("server-port-default"));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return port;
    }

    /**
     * Gets the list of all the cards
     *
     * @return A list containing all the game cards
     */
    public static List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        try {
            Document document;
            synchronized (cardsConfigLock) {
                document = builder.build(new File(Configurator.class.getResource("/SimpleGodsConfig.xml").getFile()));
            }
            Element rootElement = document.getRootElement();

            List<Element> children = rootElement.getChildren();
            for (Object child : children) {
                Element element = (Element) child;
                String name = element.getChildText("name");
                String description = element.getChildText("description");
                boolean threePlayerFlag = Boolean.parseBoolean(element.getChildText("three-players-compatible"));
                Rules rules = (Rules) Class.forName(element.getChildText("rules")).getDeclaredConstructor().newInstance();
                EnemyRules enemyRules = (EnemyRules) Class.forName(element.getChildText("enemy-rules")).getDeclaredConstructor().newInstance();
                cards.add(new Card(name, threePlayerFlag, description, rules, enemyRules));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

    /**
     * Gets a card image by the name of the card
     *
     * @param cardName The name of the card
     * @return The related Image object
     */
    public static Image getCardImage(String cardName) {
        Image image = null;
        try {
            image = (new ImageIcon(Configurator.class.getResource("/GuiResources/" + cardName.toLowerCase() + "Card.png"))).getImage();
        } catch (Exception ignored) {
        }
        return image;
    }

    /**
     * Gets the show-ping-messages flag
     *
     * @return The boolean value of the show-ping-messages flag
     */
    public static boolean getPingFlag() {
        return showPingFlag;
    }

    /**
     * Gets the show-error-details flag
     *
     * @return The boolean value of the show-error-details flag
     */
    public static boolean getErrorDetailsFlag() {
        return showErrorDetailsFlag;
    }
}

