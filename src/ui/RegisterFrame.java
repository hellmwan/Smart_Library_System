package ui;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField studentIdField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;
    private Image backgroundImage;

    public RegisterFrame() {
        // Set the window properties
        setTitle("Smart Library - Sign Up");
        setSize(400, 600); // Consistent height with LoginFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- LOAD THE BACKGROUND IMAGE ---
        backgroundImage = new ImageIcon("src/background.png").getImage();

        // --- CREATE A CUSTOM PANEL WITH OPACITY CONTROL ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Create a copy of the graphics object to avoid affecting components
                Graphics2D g2d = (Graphics2D) g.create();

                // ---> MANUAL OPACITY SETTING (0.0f to 1.0f) <---
                // Change 0.4f to adjust the background transparency manually
                float opacity = 0.75f;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }

                // Dispose the copy so buttons and text stay 100% opaque (solid)
                g2d.dispose();
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        buildUI();
    }

    private void buildUI() {
        // --- 1. FULL NAME SECTION ---
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setBounds(50, 80, 300, 20);
        nameLabel.setForeground(Color.BLACK); // Manual Color Setting
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(50, 100, 300, 30);
        nameField.setOpaque(false);
        nameField.setForeground(Color.BLACK);
        // Manual border thickness setting (3 pixels)
        nameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        add(nameField);

        // --- 2. STUDENT ID SECTION ---
        JLabel idLabel = new JLabel("Student ID / Username");
        idLabel.setBounds(50, 160, 300, 20);
        idLabel.setForeground(Color.BLACK);
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(idLabel);

        studentIdField = new JTextField();
        studentIdField.setBounds(50, 180, 300, 30);
        studentIdField.setOpaque(false);
        studentIdField.setForeground(Color.BLACK);
        studentIdField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        add(studentIdField);

        // --- 3. PASSWORD SECTION ---
        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(50, 240, 300, 20);
        passLabel.setForeground(Color.BLACK);
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(50, 260, 300, 30);
        passwordField.setOpaque(false);
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
        add(passwordField);

        // --- 4. SIGN UP BUTTON ---
        registerButton = new JButton("SIGN UP");
        registerButton.setBounds(50, 340, 300, 45);
        registerButton.setBackground(new Color(46, 204, 113)); // Modern Green
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        add(registerButton);

        // --- 5. BACK TO LOGIN BUTTON ---
        backButton = new JButton("Back to Login");
        backButton.setBounds(50, 400, 300, 30);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(new Color(52, 152, 219)); // Professional Blue
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(backButton);

        // --- EVENT LISTENERS ---
        backButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        registerButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Registration Successful! Please login.");
            new LoginFrame().setVisible(true);
            this.dispose();
        });
    }
}