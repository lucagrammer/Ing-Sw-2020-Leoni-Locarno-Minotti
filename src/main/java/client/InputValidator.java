package client;

import model.Card;
import util.Configurator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An input validator that verifies the syntactic correctness of the user input
 */
public class InputValidator {
    private final List<Card> allCardList;

    /**
     * Constructor: build an input validator
     */
    public InputValidator() {
        allCardList= Configurator.getAllCards();
    }


    /**
     * Tests if the entered string is an ip correct
     * @param serverIP  The entered text
     * @return  True if the ip is empty, otherwise false
     */
    public boolean isIp(String serverIP) {
        for(int i=0; i<serverIP.length(); i++){
            if(!((serverIP.charAt(i)<='9' && serverIP.charAt(i)>='0') || serverIP.charAt(i)=='.')){
                return false;
            }
        }
        return true;
    }

    /**
     * Tests if the entered ip is empty
     * @param serverIP  The entered server ip
     * @return  True if the ip is empty, otherwise false
     */
    public boolean isEmptyIp(String serverIP) {
        return serverIP.equals("");
    }

    /**
     * Tests if the entered string is a correct num player value
     * @param numPlayersString  The entered text
     * @return  The null value if the string is not a correct value, otherwise its integer value
     */
    public Integer isNumPlayer(String numPlayersString) {
        int numPlayers;
        try {
            numPlayers = Integer.parseInt(numPlayersString);
        } catch (Exception e) {
            numPlayers = 0;
        }
        return (numPlayers ==2 || numPlayers ==3)? numPlayers : null;
    }

    /**
     * Tests if the entered string is a correct date
     * @param fullDate  The entered text
     * @return  The null value if the string is not a correct date, otherwise its Date value
     */
    public Date isDate(String fullDate) {
        Date date;
        for(int i=0; i<fullDate.length(); i++){
            if(!((fullDate.charAt(i)<='9' && fullDate.charAt(i)>='0')|| fullDate.charAt(i)=='/')){
                return null;
            }
        }
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(fullDate);
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }

    /**
     * Tests if the entered string is a valid nickname
     * @param nickname  The entered string
     * @return  True if the nickname is valid, otherwise false
     */
    public boolean isNickname(String nickname) {
        return !(nickname.equals("") || nickname.contains(" "));
    }

    /**
     * Test if the entered string is a correct name of card
     * @param chosenCardName    The entered string
     * @param availableNames    The set of available cards (lower cases)
     * @return  The selected card
     */
    public Card isCard(String chosenCardName, List<String> availableNames) {
        if (availableNames.contains(chosenCardName.toLowerCase())) {
            for (Card card : allCardList) {
                if (card.getName().equalsIgnoreCase(chosenCardName)) {
                    return card;
                }
            }
        }
        return null;
    }

    /**
     * Tests if the entered string is a nickname contained in a set of string
     * @param choice                The entered string
     * @param availableNicknames    The set of allowed nicknames
     * @return  True if the nickname is valid, otherwise false
     */
    public boolean isNicknameBetween(String choice, List<String> availableNicknames) {
        for(String nickname: availableNicknames){
            if(choice.equalsIgnoreCase(nickname)){
                return true;
            }
        }
        return false;
    }

    /**
     * Tests if the entered string is a color contained in a set of colors names
     * @param choice                The entered string
     * @param availableColors    The set of allowed colors
     * @return  True if the color is valid, otherwise false
     */
    public boolean isColorBetween(String choice, ArrayList<String> availableColors) {
        for(String color: availableColors){
            if(choice.equalsIgnoreCase(color)){
                return true;
            }
        }
        return false;
    }
}
