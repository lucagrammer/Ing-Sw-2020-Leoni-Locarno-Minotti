package client.guiComponents;

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

    /**
     * Adds the loading icon to the message
     */
    public void showLoadingIcon(){
        //TODO clessidra
    }
}
