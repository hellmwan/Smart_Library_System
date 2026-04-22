package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageBooksFrame extends JFrame {

    // UI components for the screen
    private JTextField txtTitle, txtAuthor, txtIsbn;
    private JButton btnAdd, btnUpdate, btnDelete, btnBack;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public ManageBooksFrame() {
        // Set the window title
        setTitle("Smart Library - Manage Books");

        // Define the width and height of the frame
        setSize(850, 650);

        // Close the application when clicking the exit button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Divide the screen into North, South, and Center areas with 10px gaps
        setLayout(new BorderLayout(10, 10));

        // Set the main background color to white
        getContentPane().setBackground(Color.WHITE);

        // Call methods to build the interface
        buildTopPanel();    // Top panel for input fields
        buildCenterPanel(); // Center panel for the data table
    }

    private void buildTopPanel() {
        // Create a panel for the top section
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(4, 2, 10, 10));

        // Add padding around the panel
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        topPanel.setBackground(Color.WHITE);

        // Input labels and text fields for book details
        topPanel.add(new JLabel("Book Title:"));
        txtTitle = new JTextField();
        topPanel.add(txtTitle);

        topPanel.add(new JLabel("Author:"));
        txtAuthor = new JTextField();
        topPanel.add(txtAuthor);

        topPanel.add(new JLabel("ISBN Number:"));
        txtIsbn = new JTextField();
        topPanel.add(txtIsbn);

        // A small sub-panel to align action buttons horizontally
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Create buttons for managing books
        btnAdd = new JButton("Add Book");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnBack = new JButton("← Back to Dashboard");

        // Add action buttons to the sub-panel
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // Add the back button and the sub-panel to the top panel
        topPanel.add(btnBack);
        topPanel.add(buttonPanel);

        // Add this top panel to the North (top) area of the screen
        add(topPanel, BorderLayout.NORTH);
    }

    private void buildCenterPanel() {
        // Column headers for the table
        String[] columns = {"ID", "Book Title", "Author", "ISBN", "Status"};

        // Table model to hold the data
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);

        // Add 2 sample rows (These will come from the database later)
        tableModel.addRow(new Object[]{"1", "Java Programming", "John Doe", "978-1234", "Available"});
        tableModel.addRow(new Object[]{"2", "Data Structures", "Jane Smith", "978-5678", "Loaned"});

        // Put the table inside a scrollable pane to handle many rows
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Add the scrollable pane to the Center area of the screen
        add(scrollPane, BorderLayout.CENTER);

        // Return to the Main Menu when the back button is clicked
        btnBack.addActionListener(e -> {
            MainFrame dashboard = new MainFrame();
            dashboard.setVisible(true);

            // Close the current window
            this.dispose();
        });
    }

    public static void main(String[] args) {
        // Start the application and show the frame
        new ManageBooksFrame().setVisible(true);
    }
}