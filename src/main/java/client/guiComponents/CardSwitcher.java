package client.guiComponents;

import client.GuiView;
import model.Card;
import util.Configurator;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * A cards switcher with an action listener that enable multiple or single selection
 */
public class CardSwitcher {
    private final GuiView guiView;
    private final PPanelContainer bodyContainer;

    private final PPanelContainer cardDetailsContainer;
    private final PPanelContainer cardContainer;
    private final PLabel nameLabel;
    private final PLabel descriptionLabel;
    private PPanelContainer labelContainer;
    private boolean switcherVisibility;

    /**
     * Constructor: build a card switcher
     * @param bodyContainer The container of the card switcher
     * @param guiView       The guiView that controls the bodyContainer
     */
    public CardSwitcher(PPanelContainer bodyContainer, GuiView guiView){
        this.bodyContainer=bodyContainer;
        this.guiView=guiView;
        switcherVisibility=false;

        // Prepares the external container
        cardContainer = new PPanelContainer();
        cardContainer.setBounds(0, 80, bodyContainer.getWidth(), 138);
        cardContainer.setLayout(new GridLayout(1, 9, 10, 0));

        // Prepare the card details container
        cardDetailsContainer = new PPanelContainer();
        cardDetailsContainer.setBounds(0, 280, bodyContainer.getWidth(), 150);
        cardDetailsContainer.setLayout(null);

        // Prepare the card name label
        nameLabel = new PLabel("");
        nameLabel.setFontSize(30);
        nameLabel.setForeground(new Color(186, 164, 154));
        nameLabel.setBounds(0, 20, cardDetailsContainer.getWidth(), 35);
        cardDetailsContainer.add(nameLabel);

        // Prepare the card description label
        descriptionLabel = new PLabel("");
        descriptionLabel.setFont(new Font("Helvetica", Font.PLAIN, 17));
        descriptionLabel.setBounds(90, 50, cardDetailsContainer.getWidth() - 180, 80);
        cardDetailsContainer.add(descriptionLabel);
    }

    /**
     * Sets the text of the card switcher heading
     * @param heading   The text to be shown
     */
    public void setHeading(String heading){
        PLabel label = new PLabel(heading);
        label.setFontSize(30);
        label.setBounds(0, 0, bodyContainer.getWidth(), 40);
        bodyContainer.add(label);
    }

    /**
     * Sets the card details to be shown below the switcher
     * @param card  The card or null value to clear the details
     */
    void setDetails(Card card){
        nameLabel.setText(card==null? "": card.getName().toUpperCase());
        descriptionLabel.setText(card==null? "": ("<HTML><CENTER>" + card.getDescription() + "</CENTER></HTML>"));
    }

    /**
     * Sets the card label to be shown below each card
     * @param owners  The label values or null value to clear the labels
     */
    public void setSingleCardLabel(List<String> owners){
        if(labelContainer==null) {
            labelContainer = new PPanelContainer();
            labelContainer.setBounds(0, 225, bodyContainer.getWidth(), 30);
            labelContainer.setLayout(new GridLayout(1, 3, 10, 0));
            bodyContainer.add(labelContainer);

            for(String owner: owners){
                PLabel cardLabel = new PLabel(owner);
                cardLabel.setFontSize(25);
                cardLabel.setForeground(new Color(186, 164, 154));
                labelContainer.add(cardLabel);
            }
        }
    }


    /**
     * Adds the switcher to the container in a default position with a single selection action listener
     * @param visibleCards      All the cards to be added to the switcher
     * @param selectable        True if a single card can be selected, otherwise false
     */
    public void showSwitcher(List<Card> visibleCards,boolean selectable) {
        switcherVisibility=true;
        bodyContainer.add(cardContainer);

        for (Card card : visibleCards) {
            Image scaledImage = Configurator.getCardImage(card.getName()).getScaledInstance(82, 138, Image.SCALE_SMOOTH);
            PButton cardButton = new PButton(scaledImage);
            cardContainer.add(cardButton);
            cardButton.addMouseListener(new CardMouseListener(card, this, selectable));
        }
    }

    /**
     * Adds the switcher to the container in a default position with a multiple selection action listener
     * The switcher contains all the cards except a specified set of cards
     *
     * @param chosenCards   The cards not to be shown
     * @param numCards      The number of cards to be selected
     */
    public void showSwitcher(List<Card> chosenCards,int numCards) {
        switcherVisibility=true;
        bodyContainer.add(cardContainer);

        List<Card> allCardList = Configurator.getAllCards();
        allCardList.removeAll(chosenCards);
        for (Card card : allCardList) {
            Image scaledImage = Configurator.getCardImage(card.getName()).getScaledInstance(82, 138, Image.SCALE_SMOOTH);
            PButton cardButton = new PButton(scaledImage);
            cardContainer.add(cardButton);
            cardButton.addMouseListener(new CardMouseListener(card, this, chosenCards, numCards));
        }
    }

    /**
     * Adds the card details container to the default container in a default position
     * if the switcher has already been added.
     */
    public void showCardDetails() {
        if(!switcherVisibility)
            return;
        bodyContainer.add(cardDetailsContainer);
    }


    /**
     * The mouse event manager for the card switcher
     */
    public class CardMouseListener implements MouseListener {
        private final Card card;
        private final CardSwitcher cardSwitcher;
        private final boolean multipleSelection;
        private final boolean singleSelection;
        private int numCards;
        private List<Card> chosenCards;

        /**
         * Constructor: build a CardMouseListener with multiple selection enabled
         * @param card                  The managed card
         * @param cardSwitcher          The card switcher to be controlled
         * @param numCards              The number of cards to be selected
         */
        public CardMouseListener(Card card, CardSwitcher cardSwitcher, List<Card> chosenCards, int numCards) {
            this.multipleSelection = true;
            this.singleSelection = true;
            this.card = card;
            this.cardSwitcher=cardSwitcher;
            this.chosenCards = chosenCards;
            this.numCards = numCards;
        }

        /**
         * Constructor: build a CardMouseListener with single selection
         * @param card                  The managed card
         * @param cardSwitcher          The card switcher to be controlled
         * @param selectable            True to enable single selection, otherwise false
         */
        public CardMouseListener(Card card, CardSwitcher cardSwitcher, boolean selectable) {
            this.multipleSelection = false;
            this.singleSelection = selectable;
            this.card = card;
            this.cardSwitcher=cardSwitcher;
        }

        /**
         * Mouse on-card-click manager: if multiple selection is enabled update the guiView, otherwise
         * if single selection is enabled update the guiView, otherwise do nothing
         * @param e The mouse event
         */
        public void mouseClicked(MouseEvent e) {
            if (multipleSelection) {
                chosenCards.add(card);
                guiView.printCards(chosenCards, numCards);
            } else {
                if (singleSelection) {
                    guiView.showLoading();
                    (new Thread(() -> guiView.getServerHandler().sendPlayerCard(card))).start();
                }

            }
        }

        /**
         * Mouse on-card-press manager: same as on-card-click
         * @param e The mouse event
         */
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }

        /**
         * Do nothing
         * @param e The mouse event
         */
        public void mouseReleased(MouseEvent e) {}

        /**
         * Mouse-entered-card manager: adds the details to the card switcher
         * @param e The mouse event
         */
        public void mouseEntered(MouseEvent e) {
            cardSwitcher.setDetails(card);
        }

        /**
         * Mouse-exited-card manager: clear the details
         * @param e The mouse event
         */
        public void mouseExited(MouseEvent e) {
            cardSwitcher.setDetails(null);
        }
    }
}
