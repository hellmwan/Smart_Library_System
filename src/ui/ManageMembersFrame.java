package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageMembersFrame extends JPanel {

    private JTextField txtMemberName, txtEmail, txtPhone;
    private JTable memberTable;
    private DefaultTableModel tableModel;

    public ManageMembersFrame() {
        // Panel yapısını ayarla
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE); // İçerik alanı ferah görünmesi için beyaz
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ÜST KISIM: Başlık ---
        JLabel lblTitle = new JLabel("👥 Member Management (Librarian Access)");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.decode("#556b2f")); // Menü rengiyle uyumlu başlık
        add(lblTitle, BorderLayout.NORTH);

        // --- ORTA KISIM: Üye Tablosu ---
        String[] columns = {"ID", "Full Name", "E-mail", "Phone Number", "Status"};
        Object[][] data = {
                {"101", "Ahmet Yılmaz", "ahmet@email.com", "0555 111 22 33", "Active"},
                {"102", "Zeynep Kaya", "zeynep@email.com", "0555 222 44 55", "Active"}
        };

        tableModel = new DefaultTableModel(data, columns);
        memberTable = new JTable(tableModel);
        memberTable.setRowHeight(30);
        memberTable.setSelectionBackground(Color.decode("#ffe7ba")); // Seçili satır krem rengi olsun
        memberTable.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(memberTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- ALT KISIM: Üye Ekleme Formu ---
        JPanel bottomContainer = new JPanel(new GridLayout(2, 1, 10, 10));
        bottomContainer.setBackground(Color.WHITE);

        // 1. Giriş Alanları
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        inputPanel.setBackground(Color.WHITE);

        inputPanel.add(new JLabel("Full Name:"));
        txtMemberName = new JTextField(12);
        inputPanel.add(txtMemberName);

        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(12);
        inputPanel.add(txtEmail);

        inputPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField(10);
        inputPanel.add(txtPhone);

        // 2. Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Add Member");
        styleButton(btnAdd, "#556b2f", Color.WHITE); // Zeytin yeşili buton

        JButton btnDelete = new JButton("Remove Member");
        styleButton(btnDelete, "#e74c3c", Color.WHITE); // Silme için kırmızı buton

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        bottomContainer.add(inputPanel);
        bottomContainer.add(buttonPanel);

        add(bottomContainer, BorderLayout.SOUTH);

        // --- EVENT LISTENERS ---

        // Üye Ekleme Mantığı
        btnAdd.addActionListener(e -> {
            String name = txtMemberName.getText();
            String email = txtEmail.getText();
            String phone = txtPhone.getText();

            if (!name.isEmpty() && !email.isEmpty()) {
                String id = String.valueOf(100 + tableModel.getRowCount() + 1);
                tableModel.addRow(new Object[]{id, name, email, phone, "Active"});

                // Alanları temizle
                txtMemberName.setText("");
                txtEmail.setText("");
                txtPhone.setText("");
                JOptionPane.showMessageDialog(this, "New member registered successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in Name and Email!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Üye Silme Mantığı
        btnDelete.addActionListener(e -> {
            int selectedRow = memberTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this member?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member to remove!");
            }
        });
    }

    // Butonları dashboard stiline uygun hale getiren yardımcı metod
    private void styleButton(JButton btn, String bgColor, Color fgColor) {
        btn.setBackground(Color.decode(bgColor));
        btn.setForeground(fgColor);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}