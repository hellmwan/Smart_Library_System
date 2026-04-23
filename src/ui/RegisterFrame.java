package ui;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField studentIdField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterFrame() {
        // Set up the main window properties
        setTitle("Smart Library - Sign Up");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(null); // Use absolute positioning for custom layout

        // Apply a white background for a modern look
        getContentPane().setBackground(Color.WHITE);

        // Initialize and place all UI components
        buildUI();
    }

    private void buildUI() {
        // Main Title Label
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(100, 30, 200, 40);
        add(titleLabel);

        // 1. Full Name Input Section
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setBounds(50, 100, 100, 20);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(50, 120, 300, 40);
        add(nameField);

        // 2. Student ID Section (Functions as the username)
        JLabel idLabel = new JLabel("Student ID / Username");
        idLabel.setBounds(50, 180, 150, 20);
        add(idLabel);

        studentIdField = new JTextField();
        studentIdField.setBounds(50, 200, 300, 40);
        add(studentIdField);

        // 3. Password Input Section
        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(50, 260, 100, 20);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(50, 280, 300, 40);
        add(passwordField);

        // 4. Registration Button Configuration
        registerButton = new JButton("SIGN UP");
        registerButton.setBounds(50, 360, 300, 45);
        registerButton.setBackground(new Color(46, 204, 113)); // Modern green color
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        add(registerButton);

        // 5. Back to Login Button (Styled as a hyperlink)
        backButton = new JButton("Back to Login");
        backButton.setBounds(100, 420, 200, 30);
        backButton.setContentAreaFilled(false); // Make the background transparent
        backButton.setBorderPainted(false); // Remove borders to simulate a link
        backButton.setForeground(new Color(52, 152, 219));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(backButton);

        // --- EVENT LISTENERS ---

        // Handle navigation back to the login screen
        backButton.addActionListener(e -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
            this.dispose(); // Close the current registration frame
        });

        // Handle the sign-up process
        registerButton.addActionListener(e -> {
            // TODO: Database insertion logic will be implemented here (Data Layer)
            JOptionPane.showMessageDialog(this, "Registration Successful! Please log in.");

            LoginFrame login = new LoginFrame();
            login.setVisible(true);
            this.dispose();
        });
    }
}