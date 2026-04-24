package ui;

import javax.swing.*;
import java.awt.*;

public class StudentDashboardFrame extends JFrame {
    public StudentDashboardFrame(String studentName) {
        setTitle("Smart Library - Student Dashboard (" + studentName + ")");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2, 20, 20));
        getContentPane().setBackground(Color.WHITE);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JButton btnBrowse = new JButton("Browse Books");
        JButton btnMyBooks = new JButton("My Borrowed Books");
        JButton btnProfile = new JButton("My Profile & Fines");
        JButton btnLogout = new JButton("Logout");

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        add(btnBrowse);
        add(btnMyBooks);
        add(btnProfile);
        add(btnLogout);
    }
}