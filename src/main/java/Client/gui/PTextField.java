package Client.gui;

import javax.swing.*;
import java.awt.*;

public class PTextField extends JLabel {
    private final JTextField textField;

    public PTextField(String defaultText) {
        Image scaledTextFieldIcon = new ImageIcon("src/main/resources/GuiResources/textfield_bg.png").getImage().getScaledInstance(250, 40, Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(scaledTextFieldIcon));
        setLayout(new BorderLayout());

        textField = new JTextField();
        if (defaultText != null) {
            textField.setText(defaultText);
        }
        add(textField);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBorder(null);
        textField.setOpaque(false);
        textField.setFont(new Font("LeGourmetScript", Font.PLAIN, 30));
        textField.setCaretColor(Color.WHITE);
        textField.setForeground(Color.WHITE);
    }

    public String getTextFieldText() {
        if (textField != null) {
            return textField.getText();
        } else {
            return null;
        }
    }
}
