package client.guiComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Form {
    private PPanelContainer bodyContainer;
    private PLabel errorLabel;
    private ArrayList<PTextField> fieldList;

    public Form(PPanelContainer formContainer){
        bodyContainer=formContainer;
        fieldList=new ArrayList<>();
    }

    /**
     * Adds a Button with text and Action Listener to the bottom of the form container
     * @param text              The text to be shown inside the button
     * @param actionListener    The Action Listener of the button
     */
    public void setActionButton(String text, ActionListener actionListener) {
        /*Image scaledImage = (new ImageIcon(getClass().getResource("/GuiResources/btn_blue_next.png"))).getImage().getScaledInstance(197, 50, Image.SCALE_SMOOTH);
        PButton button = new PButton(scaledImage);
        button.setBounds(322, 300, 197, 50);
        button.addActionListener(actionListener);
        bodyContainer.add(button);*/

        Image scaledImage = (new ImageIcon(getClass().getResource("/GuiResources/btn_blue.png"))).getImage().getScaledInstance(197, 50, Image.SCALE_SMOOTH);
        PButtonSigned button = new PButtonSigned(scaledImage,text);
        button.setBounds(322, 300, 197, 55);
        button.addActionListener(actionListener);
        bodyContainer.add(button);
    }

    /**
     * Adds a form to the form container
     * @param position          The position of the form or a negative number to hide the component
     * @param formField         The name of the field
     * @param defaultValue      The default value
     * @return  The added PTextField element
     */
    public PTextField addField(int position, String formField, String defaultValue) {
        int yBound=30+position*70;
        PLabel label = new PLabelForm(formField);
        label.setBounds(10, yBound, 400, 40);

        PTextField textField = new PTextField(defaultValue);
        textField.setBounds(430, yBound, 250, 40);
        if(position>=0) {
            bodyContainer.add(label);
            bodyContainer.add(textField);
        }

        fieldList.add(textField);
        return textField;
    }

    /**
     * Adds an error Label to the form container
     * @param errorMessage The error message
     * @return  The errorLabel
     */
    public PLabel addErrorLabel(String errorMessage) {
        errorLabel = new PLabelError(errorMessage);
        errorLabel.setBounds(0, 240, 840, 40);
        bodyContainer.add(errorLabel);
        return errorLabel;
    }

    /**
     * Sets the error message
     * @param errorMessage  The error message
     */
    public void setErrorMessage(String errorMessage){
        if(errorLabel==null)
            addErrorLabel(errorMessage);
        else
            errorLabel.setText(errorMessage);
    }

    /**
     * Clear the error message
     */
    public void clearErrorMessage(){
        if(errorLabel!=null)
            errorLabel.setText("");
    }

    /**
     * Gets the value of a field
     * @param position The position of the field
     * @return  The value of the field
     */
    public String getFieldValueAt(int position) {
        PTextField field=fieldList.get(position);
        String value="";
        if(field!=null)
            value=field.getTextFieldText();
        return value;
    }
}
