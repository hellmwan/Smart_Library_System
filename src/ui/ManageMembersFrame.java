package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageMembersFrame extends JFrame {

    private JTextField txtName, txtPhone, txtAddress;
    private JButton btnAdd, btnUpdate, btnDelete, btnBack;
    private JTable memberTable;
    private DefaultTableModel tableModel;

    public ManageMembersFrame() {
        // Set the title of the window
        setTitle("Smart Library - Manage Members");

        // Define the width and height of the frame
        setSize(850, 650);

        // Close the application when the exit button is clicked
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Use BorderLayout with 10 pixels of space between components
        setLayout(new BorderLayout(10, 10));

        // Change the main background color to white
        getContentPane().setBackground(Color.WHITE);

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

        // Create the buttons for member operations
        btnAdd = new JButton("Add Member");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnBack = new JButton("← Back to Dashboard");

        // Add action buttons to their specific panel
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // Add the back button and the button panel to the top panel
        topPanel.add(btnBack);
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

        // Handle the back button click event
        btnBack.addActionListener(e -> {
            // Open the main dashboard screen
            MainFrame dashboard = new MainFrame();
            dashboard.setVisible(true);

            // Close the current window
            this.dispose();
        });
    }
}