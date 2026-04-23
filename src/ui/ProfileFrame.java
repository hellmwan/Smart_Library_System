package ui;

import javax.swing.*;
import java.awt.*;

public class ProfileFrame extends JFrame {

    public ProfileFrame(String studentName) {
        // Configure the profile window
        setTitle("Smart Library - My Profile");
        setSize(400, 350); // Reduced height since the payment button is removed
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null);
        setLayout(null); // Absolute positioning for precise placement

        getContentPane().setBackground(Color.WHITE);

        buildUI(studentName);
    }

    private void buildUI(String studentName) {
        // Main Title
        JLabel titleLabel = new JLabel("Student Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBounds(110, 30, 200, 30);
        add(titleLabel);

        // Display Name
        JLabel nameLabel = new JLabel("Name: " + studentName.toUpperCase());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setBounds(50, 100, 300, 25);
        add(nameLabel);

        // Display Membership Status
        JLabel statusLabel = new JLabel("Membership: Active");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setBounds(50, 140, 300, 25);
        statusLabel.setForeground(new Color(46, 204, 113)); // Green color
        add(statusLabel);

        // Display Total Fines (Information only)
        JLabel fineTitleLabel = new JLabel("Current Penalties & Fines:");
        fineTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        fineTitleLabel.setBounds(50, 200, 300, 25);
        fineTitleLabel.setForeground(new Color(231, 76, 60)); // Red color
        add(fineTitleLabel);

        // Display the actual fine amount
        JLabel amountLabel = new JLabel("15.00 TL");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 36));
        amountLabel.setBounds(50, 230, 200, 40);
        add(amountLabel);
    }
}