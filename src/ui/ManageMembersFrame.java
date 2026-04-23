package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// 1. CHANGED: Extends JPanel so it can be embedded inside the MainFrame
public class ManageMembersFrame extends JPanel {

    private JTextField txtName, txtPhone, txtAddress;
    private JButton btnAdd, btnUpdate, btnDelete; // Removed btnBack
    private JTable memberTable;
    private DefaultTableModel tableModel;

    public ManageMembersFrame() {
        // Window settings (setTitle, setSize) are REMOVED

        // Use BorderLayout with 10 pixels of space between components
        setLayout(new BorderLayout(10, 10));

        // 2. CHANGED: Use setBackground directly for panels
        setBackground(Color.WHITE);

        // Call methods to create the top and center sections
        buildTopPanel();
        buildCenterPanel();
    }

    private void buildTopPanel() {
        // Create the top panel and set its layout
        JPanel topPanel = new JPanel();
        // Use a grid layout with 4 rows, 2 columns, and 10px gaps
        topPanel.setLayout(new GridLayout(4, 2, 10, 10));

        // Add empty space (padding) around the panel edges
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        topPanel.setBackground(Color.WHITE);

        // Input fields for member information
        topPanel.add(new JLabel("Full Name:"));
        txtName = new JTextField();
        topPanel.add(txtName);

        topPanel.add(new JLabel("Phone Number:"));
        txtPhone = new JTextField();
        topPanel.add(txtPhone);

        topPanel.add(new JLabel("Address / Email:"));
        txtAddress = new JTextField();
        topPanel.add(txtAddress);

        // Panel for action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Create the buttons with modern UI colors
        btnAdd = new JButton("Add Member");
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

        // Add action buttons to their specific panel
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // Add an empty label to keep the grid layout aligned properly
        topPanel.add(new JLabel(""));
        topPanel.add(buttonPanel);

        // Place the top panel at the north side of the frame
        add(topPanel, BorderLayout.NORTH);
    }

    private void buildCenterPanel() {
        // Define the column titles for the member table
        String[] columns = {"Member ID", "Full Name", "Phone", "Address", "Join Date"};

        // Create the table model starting with 0 rows
        tableModel = new DefaultTableModel(columns, 0);
        memberTable = new JTable(tableModel);

        // Add sample member data to the table
        tableModel.addRow(new Object[]{"M-001", "Ayşe Yılmaz", "555-1234", "Antalya, Turkey", "2026-04-20"});
        tableModel.addRow(new Object[]{"M-002", "Ali Veli", "555-9876", "Düzce, Turkey", "2026-04-18"});

        // Add a scrollbar to the table to view all rows easily
        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Place the table in the center area of the frame
        add(scrollPane, BorderLayout.CENTER);

        // NOTE: The Back button logic was removed from here
    }
}