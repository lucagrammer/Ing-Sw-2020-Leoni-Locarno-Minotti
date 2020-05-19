package client.gui.elements;

import client.gui.components.PLabel;
import client.gui.components.PPanelContainer;

/**
 * A message element with personalized icons
 */
public class MessageElement {
    private final PLabel label;

    /**
     * Constructor: build a MessageElement
     * @param bodyContainer The container of the message element
     */
    public MessageElement(PPanelContainer bodyContainer){
        label = new PLabel("");
        label.setBounds(0, 0, bodyContainer.getWidth(), bodyContainer.getHeight());
        bodyContainer.add(label);
    }

    /**
     * Sets the message to be shown
     * @param message   The message to be shown
     */
    public void setMessage(String message){
        label.setText(message);
    }
}
