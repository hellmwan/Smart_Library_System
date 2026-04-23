package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// 1. CHANGED: Extends JPanel to fit inside the MainFrame CardLayout
public class IssueReturnFrame extends JPanel {

    private JComboBox<String> cmbMembers;
    private JComboBox<String> cmbBooks;
    private JButton btnIssue, btnReturn; // btnBack is removed
    private JTable loanTable;
    private DefaultTableModel tableModel;

    public IssueReturnFrame() {
        // Window settings (setSize, setTitle, etc.) are REMOVED

        // Use BorderLayout with 10px horizontal and vertical gaps
        setLayout(new BorderLayout(10, 10));

        // 2. CHANGED: Use setBackground directly for the panel
        setBackground(Color.WHITE);

        // Call methods to build different parts of the UI
        buildTopPanel();
        buildCenterPanel();
    }

    private void buildTopPanel() {
        // Create the top panel and configure its layout
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // Add padding around the panel edges
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        topPanel.setBackground(Color.WHITE);

        // 1. Dropdown menu for member selection
        topPanel.add(new JLabel("Select Member:"));

        // These will be fetched from the SQL database later. We use test data for now.
        String[] members = {"M-001 - Ayşe YAŞAR", "M-002 - Ali Veli"};
        cmbMembers = new JComboBox<>(members);
        cmbMembers.setBackground(Color.WHITE);
        topPanel.add(cmbMembers);

        // 2. Dropdown menu for book selection
        topPanel.add(new JLabel("Select Book:"));
        String[] books = {"1 - Java Programming", "2 - Data Structures"};
        cmbBooks = new JComboBox<>(books);
        cmbBooks.setBackground(Color.WHITE);
        topPanel.add(cmbBooks);

        // Panel for action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Create functional buttons with modern colors
        btnIssue = new JButton("Issue Book (Ödünç Ver)");
        btnIssue.setBackground(new Color(52, 152, 219)); // Blue color for issuing
        btnIssue.setForeground(Color.WHITE);
        btnIssue.setFocusPainted(false);

        btnReturn = new JButton("Return Book (İade Al)");
        btnReturn.setBackground(new Color(230, 126, 34)); // Orange color for returning
        btnReturn.setForeground(Color.WHITE);
        btnReturn.setFocusPainted(false);

        buttonPanel.add(btnIssue);
        buttonPanel.add(btnReturn);

        // Add an empty label to keep the grid aligned properly
        topPanel.add(new JLabel(""));
        topPanel.add(buttonPanel);

        // Place the top panel at the north side of the main frame
        add(topPanel, BorderLayout.NORTH);
    }

    private void buildCenterPanel() {
        // Define column titles for the loan data table
        String[] columns = {"Transaction ID", "Member", "Book", "Issue Date", "Status"};

        // Initialize the table model with 0 starting rows
        tableModel = new DefaultTableModel(columns, 0);
        loanTable = new JTable(tableModel);

        // Add a sample row to show an active transaction
        tableModel.addRow(new Object[]{"TRX-100", "Ayşe YAŞAR", "Java Programming", "2026-04-10", "Active (Not Returned)"});

        // Add a scrollbar to the table to handle multiple rows
        JScrollPane scrollPane = new JScrollPane(loanTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Place the table in the center of the main frame
        add(scrollPane, BorderLayout.CENTER);

        // NOTE: The Back button logic was completely removed from here.
    }
}