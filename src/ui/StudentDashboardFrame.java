package ui;

import javax.swing.*;
import java.awt.*;

public class StudentDashboardFrame extends JFrame {

    private String currentStudent;

    public StudentDashboardFrame(String studentName) {
        this.currentStudent = studentName; // Store the student's name

        // Set up the main window properties
        setTitle("Smart Library - Student Dashboard (" + studentName + ")");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Use a grid layout for the menu buttons
        setLayout(new GridLayout(2, 2, 20, 20));
        getContentPane().setBackground(Color.WHITE);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Create the buttons
        JButton btnBrowse = createMenuButton("Browse Books", "#3498db");
        JButton btnMyBooks = createMenuButton("My Borrowed Books", "#2ecc71");
        JButton btnProfile = createMenuButton("My Profile & Fines", "#9b59b6");
        JButton btnLogout = createMenuButton("Logout", "#e74c3c");

        // --- EVENT LISTENERS ---

        // 1. Open the Browse Books window
        btnBrowse.addActionListener(e -> {
            BookBrowseFrame browseFrame = new BookBrowseFrame();
            browseFrame.setVisible(true);
        });

        // 2. Open the Borrowed Books window
        btnMyBooks.addActionListener(e -> {
            MyBooksFrame myBooksFrame = new MyBooksFrame();
            myBooksFrame.setVisible(true);
        });

        // 3. Open the Profile and Fines window
        btnProfile.addActionListener(e -> {
            ProfileFrame profileFrame = new ProfileFrame(currentStudent);
            profileFrame.setVisible(true);
        });

        // 4. Log out and return to the Login screen
        btnLogout.addActionListener(e -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
            this.dispose();
        });

        // Add the buttons to the dashboard
        add(btnBrowse);
        add(btnMyBooks);
        add(btnProfile);
        add(btnLogout);
    }

    // Helper method to create modern buttons
    private JButton createMenuButton(String text, String hexColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(Color.decode(hexColor));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}