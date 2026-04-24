package ui;

import business.MemberManager;
import model.Member;
import util.Validator;
import util.ValidationException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageMembersFrame extends JPanel {

    private JTextField txtMemberName, txtEmail, txtPhone;
    private JTextField txtSearch;
    private JTable memberTable;
    private DefaultTableModel tableModel;

    private MemberManager memberManager;

    public ManageMembersFrame(MemberManager memberManager) {
        this.memberManager = memberManager;

        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("👥 Member Management (Librarian Access)");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.decode("#556b2f"));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        txtSearch = new JTextField();
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applySearch(); }
            public void removeUpdate(DocumentEvent e) { applySearch(); }
            public void changedUpdate(DocumentEvent e) { applySearch(); }
        });

        topPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Full Name", "E-mail", "Phone Number", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        memberTable = new JTable(tableModel);
        memberTable.setRowHeight(30);

        add(new JScrollPane(memberTable), BorderLayout.CENTER);

        JPanel bottomContainer = new JPanel(new GridLayout(2, 1, 10, 10));
        bottomContainer.setBackground(Color.WHITE);

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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Add Member");
        JButton btnDelete = new JButton("Remove Member");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        bottomContainer.add(inputPanel);
        bottomContainer.add(buttonPanel);

        add(bottomContainer, BorderLayout.SOUTH);

        // =========================
        // ADD MEMBER (FULL VALIDATION)
        // =========================
        btnAdd.addActionListener(e -> {
            String name = txtMemberName.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();

            try {
                Validator.requireNonBlank(name, "İsim");
                Validator.requireValidEmail(email);
                Validator.requireValidPhone(phone);

                boolean ok = memberManager.addMember(name, email, phone);

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Üye başarıyla eklendi ✅");
                    txtMemberName.setText("");
                    txtEmail.setText("");
                    txtPhone.setText("");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Email zaten kayıtlı olabilir.",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                }

            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        // =========================
        // DELETE MEMBER
        // =========================
        btnDelete.addActionListener(e -> {
            int selectedRow = memberTable.getSelectedRow();

            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Lütfen üye seçin");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Silmek istediğine emin misin?",
                    "Onay", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

            if (memberManager.deleteMember(id)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Silinemedi");
            }
        });

        refreshTable();
    }

    private void applySearch() {
        fillTable(memberManager.search(txtSearch.getText()));
    }

    public void refreshTable() {
        fillTable(memberManager.getAllMembers());
    }

    private void fillTable(List<Member> list) {
        tableModel.setRowCount(0);
        for (Member m : list) {
            tableModel.addRow(new Object[]{
                    m.getId(),
                    m.getFullName(),
                    m.getEmail(),
                    m.getPhone(),
                    m.getStatus()
            });
        }
    }
}