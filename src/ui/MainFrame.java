package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        // Set the title of the dashboard window
        setTitle("Smart Library - Dashboard");

        // Set the width and height of the window
        setSize(800, 600);

        // Close the application when clicking the exit button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center the dashboard on the screen
        setLocationRelativeTo(null);

        // Use a 2x2 grid layout with 20 pixels of space between the buttons
        setLayout(new GridLayout(2, 2, 20, 20));

        // Set the background color to white
        getContentPane().setBackground(Color.WHITE);

        // Add 50 pixels of padding around the main content area
        getRootPane().setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // 1. Red Button: Manage Books section
        JButton btnBooks = createMenuButton("Manage Books", "#e74c3c");
        btnBooks.addActionListener(e -> {
            // Open the Manage Books screen
            ManageBooksFrame booksFrame = new ManageBooksFrame();
            booksFrame.setVisible(true);

            // Close the main dashboard
            this.dispose();
        });

        // 2. Green Button: Manage Members section
        JButton btnMembers = createMenuButton("Manage Members", "#2ecc71");
        btnMembers.addActionListener(e -> {
            // Open the Manage Members screen
            ManageMembersFrame membersFrame = new ManageMembersFrame();
            membersFrame.setVisible(true);
            this.dispose();
        });

        // 3. Yellow Button: Issue / Return operations
        JButton btnLoans = createMenuButton("Issue / Return", "#f1c40f");
        btnLoans.addActionListener(e -> {
            // Open the Issue and Return screen
            IssueReturnFrame loansFrame = new IssueReturnFrame();
            loansFrame.setVisible(true);
            this.dispose();
        });

        // 4. Purple Button: Penalties & Reports section (NEW LINK)
        JButton btnReports = createMenuButton("Penalties & Reports", "#9b59b6");
        btnReports.addActionListener(e -> {
            // Open the Reports screen
            ReportsFrame reportsFrame = new ReportsFrame();
            reportsFrame.setVisible(true);
            this.dispose();
        });

        // Add all four buttons to the dashboard grid
        add(btnBooks);
        add(btnMembers);
        add(btnLoans);
        add(btnReports);
    }

    /**
     * A helper method to create and design menu buttons easily.
     * This prevents code duplication.
     */
    private JButton createMenuButton(String text, String hexColor) {
        JButton btn = new JButton(text);

        // Set the text font, style, and size
        btn.setFont(new Font("Arial", Font.BOLD, 20));

        // Set the button background color using a hex code
        btn.setBackground(Color.decode(hexColor));

        // Set the text color to white
        btn.setForeground(Color.WHITE);

        // Remove the default focus border when the button is clicked
        btn.setFocusPainted(false);

        // Change the mouse icon to a hand pointer when hovering over the button
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }
}