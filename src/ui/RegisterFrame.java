package ui;

import javax.swing.*;

public class RegisterFrame extends JFrame {
    public RegisterFrame() {
        setTitle("Smart Library - Sign Up");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Sadece bu pencereyi kapat
        setLocationRelativeTo(null);
        add(new JLabel("Registration Screen will be here.", SwingConstants.CENTER));
    }
}