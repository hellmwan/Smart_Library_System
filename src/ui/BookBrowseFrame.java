package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BookBrowseFrame extends JFrame {

    private JTextField searchField;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public BookBrowseFrame() {
        // Basic window configuration
        setTitle("Smart Library - Browse Books");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close this window, not the whole app
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10)); // Using BorderLayout for a more organized structure

        getContentPane().setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        // --- TOP PANEL: Search Section ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(new JLabel("Search Book:"));
        searchField = new JTextField(20);
        topPanel.add(searchField);

        JButton btnSearch = new JButton("Search");
        topPanel.add(btnSearch);

        add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL: Table Section ---
        // Column names for the book table
        String[] columns = {"ID", "Title", "Author", "Category", "Status"};

        // Sample data for visualization (This will come from the database later)
        Object[][] data = {
                {"1", "The Great Gatsby", "F. Scott Fitzgerald", "Classic", "Available"},
                {"2", "Introduction to Java", "Herbert Schildt", "Education", "Borrowed"},
                {"3", "Clean Code", "Robert C. Martin", "Software", "Available"}
        };

        tableModel = new DefaultTableModel(data, columns);
        bookTable = new JTable(tableModel);

        // Adding the table inside a ScrollPane to allow scrolling
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM PANEL: Action Section ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnBorrow = new JButton("Borrow Selected Book");
        btnBorrow.setBackground(new Color(52, 152, 219));
        btnBorrow.setForeground(Color.WHITE);
        bottomPanel.add(btnBorrow);

        add(bottomPanel, BorderLayout.SOUTH);

        // Event: Borrow Button Logic
        btnBorrow.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                String bookTitle = tableModel.getValueAt(selectedRow, 1).toString();
                JOptionPane.showMessageDialog(this, "Request sent for: " + bookTitle);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book first!");
            }
        });
    }
}