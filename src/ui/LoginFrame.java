package ui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        // Set the window title
        setTitle("Smart Library - Login");

        // Define the width and height of the frame
        setSize(400, 500);

        // Close the application when the user clicks the 'X' button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Use absolute positioning for components
        setLayout(null);

        // Change the background color to white
        getContentPane().setBackground(Color.WHITE);

        buildUI();
    }

    private void buildUI() {
        // Create and customize the title text
        JLabel titleLabel = new JLabel("Welcome Back!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(100, 50, 200, 40);
        add(titleLabel);

        // Add a label for the username input
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(50, 150, 100, 20);
        add(userLabel);

        // Create the text field where the user will type their username
        usernameField = new JTextField();
        usernameField.setBounds(50, 170, 300, 40);
        add(usernameField);

        // Add a label for the password input
        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(50, 230, 100, 20);
        add(passLabel);

        // Create a secure text field for the password
        passwordField = new JPasswordField();
        passwordField.setBounds(50, 250, 300, 40);
        add(passwordField);

        // Create the login button and design its appearance
        loginButton = new JButton("LOGIN");
        loginButton.setBounds(50, 330, 300, 45);
        loginButton.setBackground(new Color(52, 152, 219)); // Blue background
        loginButton.setForeground(Color.WHITE); // White text color
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false); // Remove the border when clicked
        add(loginButton);

        // Handle the button click event
        loginButton.addActionListener(e -> {
            // Open the dashboard screen
            MainFrame dashboard = new MainFrame();
            dashboard.setVisible(true);

            // Close the current login window
            this.dispose();
        });
    }

    public static void main(String[] args) {
        // Start the application and show the login frame
        LoginFrame frame = new LoginFrame();
        frame.setVisible(true);
    }
}