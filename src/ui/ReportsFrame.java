package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportsFrame extends JFrame {

    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JButton btnBack;

    public ReportsFrame() {
        // Set the window title
        setTitle("Smart Library - Penalties & Reports");

        // Define the width and height of the window
        setSize(850, 650);

        // Close the application when the exit button is clicked
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Use BorderLayout with 10 pixels of gap between components
        setLayout(new BorderLayout(10, 10));

        // Change the main background color to white
        getContentPane().setBackground(Color.WHITE);

        // Call methods to build the top and center panels
        buildTopPanel();
        buildCenterPanel();
    }

    private void buildTopPanel() {
        // Create the top panel and use BorderLayout for it
        JPanel topPanel = new JPanel(new BorderLayout());

        // Add empty space (padding) around the panel edges
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        topPanel.setBackground(Color.WHITE);

        // Create an eye-catching, red title for the report
        JLabel titleLabel = new JLabel("Overdue Books & Penalties Report");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.decode("#c0392b")); // Dark red color

        // Create the back button and add its action
        btnBack = new JButton("← Back to Dashboard");
        btnBack.addActionListener(e -> {
            // Open the main dashboard screen
            MainFrame dashboard = new MainFrame();
            dashboard.setVisible(true);

            // Close the current reports window
            this.dispose();
        });

        // Place the title on the left (WEST) and the back button on the right (EAST)
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(btnBack, BorderLayout.EAST);

        // Add this top panel to the top (NORTH) area of the main screen
        add(topPanel, BorderLayout.NORTH);
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
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Add the scrollable table to the center of the main screen
        add(scrollPane, BorderLayout.CENTER);
    }
}