package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// 1. CHANGED: Extends JPanel so it can be shown inside the MainFrame
public class ReportsFrame extends JPanel {

    private JTable reportTable;
    private DefaultTableModel tableModel;
    // btnBack is completely removed

    public ReportsFrame() {
        // Window settings (setTitle, setSize, etc.) are REMOVED

        // Use BorderLayout with 10 pixels of gap between components
        setLayout(new BorderLayout(10, 10));

        // 2. CHANGED: Use setBackground directly for the panel
        setBackground(Color.WHITE);

        // Call methods to build the top and center panels
        buildTopPanel();
        buildCenterPanel();
    }

    private void buildTopPanel() {
        // Create the top panel and use FlowLayout to align the title to the left
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Add empty space (padding) around the panel edges
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 50));
        topPanel.setBackground(Color.WHITE);

        // Create an eye-catching, red title for the report
        JLabel titleLabel = new JLabel("Overdue Books & Penalties Report");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#c0392b")); // Dark red color

        // Add the title to the top panel
        topPanel.add(titleLabel);

        // Add this top panel to the top (NORTH) area of the main screen
        add(topPanel, BorderLayout.NORTH);

        // NOTE: The back button was removed from here.
    }

    private void buildCenterPanel() {
        // Define the column titles for the report table
        String[] columns = {"Member ID", "Member Name", "Book Title", "Due Date", "Days Overdue", "Penalty (₺)"};

        // Create the table model starting with 0 rows
        tableModel = new DefaultTableModel(columns, 0);
        reportTable = new JTable(tableModel);

        // Sample penalty data (These will come from the database later)
        tableModel.addRow(new Object[]{"M-001", "Ayşe YAŞAR", "Data Structures", "2026-04-10", "10", "50.00 ₺"});
        tableModel.addRow(new Object[]{"M-005", "Sema DEMİREL", "Database Systems", "2026-04-15", "5", "25.00 ₺"});

        // Make the table look nicer and easier to read
        reportTable.setRowHeight(30); // Make the rows taller
        reportTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Set the text font

        // Design the table header (titles)
        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        reportTable.getTableHeader().setBackground(Color.decode("#ecf0f1")); // Light gray background for headers

        // Put the table inside a scrollable pane
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50)); // Adjusted padding to look better
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Add the scrollable table to the center of the main screen
        add(scrollPane, BorderLayout.CENTER);
    }
}