package Util;

import Model.Card;
import Server.Rules.EnemyRules;
import Server.Rules.Rules;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Menage the configuration of the application
 */
public class Configurator {
    private static final SAXBuilder builder = new SAXBuilder();

    /**
     * Gets the default IP of the server
     *
     * @return The default IP of the server
     */
    public static String getDefaultIp() {
        String ip = null;
        try {
            Document document = builder.build(new File("src/main/resources/ConnectionConfig.xml"));
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
            Document document = builder.build(new File("src/main/resources/ConnectionConfig.xml"));
            Element rootElement = document.getRootElement();

            port = Integer.parseInt(rootElement.getChildText("server-port-default"));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return port;
    }

    /**
     * Gets the fast start flag for the debug mode
     *
     * @return The boolean value of the flag
     */
    public static boolean getFastStartFlag() {
        String flag = "false";
        try {
            Document document = builder.build(new File("src/main/resources/ConnectionConfig.xml"));
            Element rootElement = document.getRootElement();

            flag = rootElement.getChildText("fast-start");
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return flag.equalsIgnoreCase("true");
    }

    /**
     * Gets the list of all the cards
     *
     * @return A list containing all the game cards
     */
    public static List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        try {
            Document document = builder.build(new File("src/main/resources/SimpleGodsConfig.xml"));
            Element rootElement = document.getRootElement();

            List children = rootElement.getChildren();
            for (Object child : children) {
                Element element = (Element) child;
                String name = element.getChildText("name");
                String description = element.getChildText("description");
                boolean threePlayerCompatible = Boolean.parseBoolean(element.getChildText("three-players-compatible"));
                Rules rules = (Rules) Class.forName(element.getChildText("rules")).getDeclaredConstructor().newInstance();
                EnemyRules enemyRules = (EnemyRules) Class.forName(element.getChildText("enemy-rules")).getDeclaredConstructor().newInstance();
                cards.add(new Card(name, threePlayerCompatible, description, rules, enemyRules));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }
}
