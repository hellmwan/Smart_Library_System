package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MyBooksFrame extends JFrame {

    private JTable borrowedTable;
    private DefaultTableModel tableModel;

    public MyBooksFrame() {
        // Set up the basic window properties
        setTitle("Smart Library - My Borrowed Books");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        // --- TOP PANEL: Title Section ---
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Books Currently in Your Possession");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel);

        add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL: Borrowed Books Table ---
        // Define column headers for the borrowed books
        String[] columns = {"ID", "Book Title", "Borrow Date", "Due Date", "Status"};

        // Sample data to simulate borrowed books
        Object[][] data = {
                {"2", "Introduction to Java", "2026-04-10", "2026-04-24", "On Time"},
                {"5", "Data Structures", "2026-03-15", "2026-03-29", "Overdue"}
        };

        tableModel = new DefaultTableModel(data, columns);
        borrowedTable = new JTable(tableModel);

        // Wrap the table in a ScrollPane
        JScrollPane scrollPane = new JScrollPane(borrowedTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM PANEL: Action Section ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        // Button to return a selected book
        JButton btnReturn = new JButton("Return Selected Book");
        btnReturn.setBackground(new Color(231, 76, 60)); // Red color for actions
        btnReturn.setForeground(Color.WHITE);
        btnReturn.setFocusPainted(false);
        bottomPanel.add(btnReturn);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- EVENT LISTENER ---
        // Define what happens when the return button is clicked
        btnReturn.addActionListener(e -> {
            int selectedRow = borrowedTable.getSelectedRow();
            if (selectedRow != -1) {
                String bookTitle = tableModel.getValueAt(selectedRow, 1).toString();
                JOptionPane.showMessageDialog(this, "You have successfully returned: " + bookTitle);

                // Remove the returned book from the table visually
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to return!");
            }
        });
    }
}