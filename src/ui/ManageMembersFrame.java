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

/**
 * MainFrame'in "Manage Members" sekmesinde gosterilir.
 * Ust: baslik + arama kutusu.
 * Orta: tum uyeleri listeleyen tablo.
 * Alt: yeni uye ekleme formu + Add/Remove butonlari.
 *
 * Yapilabilecek islemler:
 *  - Uye ekleme (form doldur + Add Member)
 *  - Uye silme (tablodan sec + Remove Member)
 *  - Anlik arama (yazdikca filtreler)
 */
public class ManageMembersFrame extends JPanel {

    // Form alanlari
    private JTextField txtMemberName, txtEmail, txtPhone;
    private JTextField txtSearch;

    // Tablo
    private JTable memberTable;
    private DefaultTableModel tableModel;

    private MemberManager memberManager;


    public ManageMembersFrame(MemberManager memberManager) {
        this.memberManager = memberManager;

        // Ust-duzen: BorderLayout + 15 px iceri kenar bosluk
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        // ==== UST PANEL: baslik + arama ====
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("👥 Member Management (Librarian Access)");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.decode("#556b2f"));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // Arama kutusu
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        txtSearch = new JTextField();
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        // Anlik arama: yazdikca filtreleme
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applySearch(); }
            public void removeUpdate(DocumentEvent e) { applySearch(); }
            public void changedUpdate(DocumentEvent e) { applySearch(); }
        });

        topPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);


        // ==== ORTA PANEL: tablo ====
        String[] columns = {"ID", "Full Name", "E-mail", "Phone Number", "Status"};
        // Hucreler duzenlenemez (read-only tablo)
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        memberTable = new JTable(tableModel);
        memberTable.setRowHeight(30);

        // Tabloyu kaydirma cubukluyla sar
        add(new JScrollPane(memberTable), BorderLayout.CENTER);


        // ==== ALT PANEL: form + butonlar ====
        // 2 satir: 1. satir form, 2. satir butonlar
        JPanel bottomContainer = new JPanel(new GridLayout(2, 1, 10, 10));
        bottomContainer.setBackground(Color.WHITE);

        // --- Form satiri ---
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        inputPanel.setBackground(Color.WHITE);

        inputPanel.add(new JLabel("Full Name:"));
        txtMemberName = new JTextField(12); // 12 karakter genislik
        inputPanel.add(txtMemberName);

        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(12);
        inputPanel.add(txtEmail);

        inputPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField(10);
        inputPanel.add(txtPhone);

        // --- Buton satiri ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Add Member");
        JButton btnDelete = new JButton("Remove Member");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        bottomContainer.add(inputPanel);
        bottomContainer.add(buttonPanel);

        add(bottomContainer, BorderLayout.SOUTH);


        // ==== BUTON OLAYLARI ====

        /*
         * Add Member: formdaki bilgileri al, dogrula, kaydet.
         * Validator hata firlatirsa try/catch ile yakalayip kullaniciya gosteriyoruz.
         */
        btnAdd.addActionListener(e -> {
            String name = txtMemberName.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();

            try {
                // Dogrulamalar (hatali ise ValidationException firlar)
                Validator.requireNonBlank(name, "İsim");
                Validator.requireValidEmail(email);
                Validator.requireValidPhone(phone);

                // MemberManager'a ekleme yaptir
                boolean ok = memberManager.addMember(name, email, phone);

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Üye başarıyla eklendi ");
                    // Formu temizle
                    txtMemberName.setText("");
                    txtEmail.setText("");
                    txtPhone.setText("");
                    refreshTable();
                } else {
                    // Genelde ayni email zaten kayitlidir
                    JOptionPane.showMessageDialog(this,
                            "Email zaten kayıtlı olabilir.",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                }

            } catch (ValidationException ex) {
                // Dogrulama hatasi -> kullaniciya mesaj
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });


        /*
         * Remove Member: tablodan secilen uyeyi sil.
         * Once secim var mi kontrol et, sonra onay sor.
         */
        btnDelete.addActionListener(e -> {
            int selectedRow = memberTable.getSelectedRow();

            // Hicbir satir secili degilse uyari ver
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Lütfen üye seçin");
                return;
            }

            // Onay sor
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Silmek istediğine emin misin?",
                    "Onay", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            // Tablodan id'yi al ve silme islemini yap
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

            if (memberManager.deleteMember(id)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Silinemedi");
            }
        });

        // Acilista tabloyu doldur
        refreshTable();
    }


    /** Arama metnine gore tabloyu filtrele. */
    private void applySearch() {
        fillTable(memberManager.search(txtSearch.getText()));
    }

    /**
     * Tabloyu yenile (disaridan da cagrilir).
     * Tum uyeleri listeler.
     */
    public void refreshTable() {
        fillTable(memberManager.getAllMembers());
    }


    /** Verilen liste ile tabloyu doldurur. */
    private void fillTable(List<Member> list) {
        tableModel.setRowCount(0); // once temizle
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
