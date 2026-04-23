package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManagePersonnelFrame extends JPanel {

    private JTextField txtName, txtUsername, txtPassword;
    private JTable personnelTable;
    private DefaultTableModel tableModel;

    public ManagePersonnelFrame() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("👔 Manage Library Personnel");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));
        add(lblTitle, BorderLayout.NORTH);

        // Tablo Sütunları
        String[] columns = {"ID", "Full Name", "Username", "Role"};
        tableModel = new DefaultTableModel(null, columns);

        // Önceki kayıtlı personelleri tabloda göster
        for (int i = 0; i < LoginFrame.registeredPersonnel.size(); i++) {
            String[] p = LoginFrame.registeredPersonnel.get(i);
            tableModel.addRow(new Object[]{i + 1, p[0], p[1], "Librarian"});
        }

        personnelTable = new JTable(tableModel);
        personnelTable.setRowHeight(25);
        add(new JScrollPane(personnelTable), BorderLayout.CENTER);

        // Form Paneli
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        bottomPanel.setBackground(new Color(245, 246, 250));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        formPanel.setBackground(new Color(245, 246, 250));
        formPanel.add(new JLabel("Full Name:"));
        txtName = new JTextField(10);
        formPanel.add(txtName);
        formPanel.add(new JLabel("User:"));
        txtUsername = new JTextField(8);
        formPanel.add(txtUsername);
        formPanel.add(new JLabel("Pass:"));
        txtPassword = new JTextField(8);
        formPanel.add(txtPassword);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setBackground(new Color(245, 246, 250));

        JButton btnAdd = new JButton("Add Personnel");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        bottomPanel.add(formPanel);
        bottomPanel.add(buttonPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- BUTON AKSİYONLARI ---

        btnAdd.addActionListener(e -> {
            String name = txtName.getText();
            String user = txtUsername.getText();
            String pass = txtPassword.getText();

            if (!name.isEmpty() && !user.isEmpty() && !pass.isEmpty()) {
                // 1. Login listesine ekle (Burası personelin giriş yapabilmesini sağlar)
                LoginFrame.registeredPersonnel.add(new String[]{name, user, pass});

                // 2. Tabloya ekle (Görsel güncelleme)
                int newId = tableModel.getRowCount() + 1;
                tableModel.addRow(new Object[]{newId, name, user, "Librarian"});

                txtName.setText(""); txtUsername.setText(""); txtPassword.setText("");
                JOptionPane.showMessageDialog(this, "Personel başarıyla eklendi! Artık giriş yapabilir.");
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = personnelTable.getSelectedRow();
            if (selectedRow != -1) {
                // Hem listeden hem tablodan sil
                LoginFrame.registeredPersonnel.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            }
        });
    }
}