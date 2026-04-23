package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// 1. CHANGED: Now it extends JPanel instead of JFrame!
public class ManageBooksFrame extends JPanel {

    // UI components for the screen
    private JTextField txtTitle, txtAuthor, txtIsbn;
    private JButton btnAdd, btnUpdate, btnDelete; // Removed the btnBack
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public ManageBooksFrame() {
        // Window settings like setSize() and setTitle() are REMOVED
        // because this is now a panel inside the MainFrame.

        // Divide the screen into North, South, and Center areas with 10px gaps
        setLayout(new BorderLayout(10, 10));

        // 2. CHANGED: Use setBackground directly instead of getContentPane()
        setBackground(Color.WHITE);

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

        // Create buttons for managing books with modern colors
        btnAdd = new JButton("Add Book");
        btnAdd.setBackground(new Color(46, 204, 113)); // Green
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        btnUpdate = new JButton("Update");
        btnUpdate.setBackground(new Color(52, 152, 219)); // Blue
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);

        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60)); // Red
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);

        // Add action buttons to the sub-panel
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // Add an empty label to keep the grid layout aligned
        topPanel.add(new JLabel(""));
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

        // NOTE: The "Back" button logic is removed because the MainFrame sidebar handles navigation!
    }
}