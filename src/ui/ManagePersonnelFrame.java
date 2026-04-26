package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * AdminDashboardFrame'in "Manage Personnel" sekmesinde gosterilir.
 * Sadece admin'in goreceigi panel.
 * Yapilabilecek islemler:
 *  - Yeni personel ekleme (LoginFrame.registeredPersonnel listesine ekler)
 *  - Tablodan personel silme
 *
 * Not: Burada bilgiler bellekte tutuluyor, dosyaya kaydedilmiyor.
 * Yani program kapaninca eklenen personeller silinir. Ileride bu
 * sinif bir PersonnelData katmaniyla degistirilecektir.
 */
public class ManagePersonnelFrame extends JPanel {

    // Form alanlari
    private JTextField txtName, txtUsername, txtPassword;

    // Tablo
    private JTable personnelTable;
    private DefaultTableModel tableModel;


    public ManagePersonnelFrame() {
        // Ust-duzen ayarlari
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Baslik
        JLabel lblTitle = new JLabel("👔 Manage Library Personnel");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));
        add(lblTitle, BorderLayout.NORTH);


        // ---- Tablo modelini hazirla ----
        String[] columns = {"ID", "Full Name", "Username", "Role"};
        tableModel = new DefaultTableModel(null, columns);


        // Kayitli personelleri tabloya yukle
        // LoginFrame.registeredPersonnel'i okuyoruz (ortak liste)
        for (int i = 0; i < LoginFrame.registeredPersonnel.size(); i++) {
            String[] p = LoginFrame.registeredPersonnel.get(i);
            // p[0]=ad, p[1]=username, p[2]=password
            // Tabloya: ID(=index+1), ad, username, sabit "Librarian"
            tableModel.addRow(new Object[]{i + 1, p[0], p[1], "Librarian"});
        }

        personnelTable = new JTable(tableModel);
        personnelTable.setRowHeight(25);
        add(new JScrollPane(personnelTable), BorderLayout.CENTER);


        // ---- ALT PANEL: form + butonlar ----
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        bottomPanel.setBackground(new Color(245, 246, 250));

        // Form satiri
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

        // Buton satiri
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setBackground(new Color(245, 246, 250));

        JButton btnAdd = new JButton("Add Personnel");
        btnAdd.setBackground(new Color(46, 204, 113)); // yesil
        btnAdd.setForeground(Color.WHITE);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setBackground(new Color(231, 76, 60)); // kirmizi
        btnDelete.setForeground(Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        bottomPanel.add(formPanel);
        bottomPanel.add(buttonPanel);
        add(bottomPanel, BorderLayout.SOUTH);


        // ==== BUTON OLAYLARI ====

        /*
         * Add Personnel:
         *  1. Form alanlarini al
         *  2. Hicbiri bos olmamali
         *  3. LoginFrame.registeredPersonnel listesine ekle
         *  4. Tabloya yeni satir ekle
         *  5. Formu temizle ve haber ver
         *
         * Onemli: Bu eklenen personel hemen Login ekranindan giris yapabilir
         * cunku LoginFrame ayni listeyi kontrol ediyor.
         */
        btnAdd.addActionListener(e -> {
            String name = txtName.getText();
            String user = txtUsername.getText();
            String pass = txtPassword.getText();

            if (!name.isEmpty() && !user.isEmpty() && !pass.isEmpty()) {

                // 1. Login listesine ekle (LoginFrame zaten kontrol edecek)
                LoginFrame.registeredPersonnel.add(new String[]{name, user, pass});

                // 2. Tabloya yeni satir ekle (id = mevcut satir sayisi + 1)
                int newId = tableModel.getRowCount() + 1;
                tableModel.addRow(new Object[]{newId, name, user, "Librarian"});

                // Formu temizle
                txtName.setText(""); txtUsername.setText(""); txtPassword.setText("");
                JOptionPane.showMessageDialog(this, "Personel başarıyla eklendi! Artık giriş yapabilir.");
            }
            // Bos alan varsa sessizce hicbir sey yapma (basit bir UX)
        });


        /*
         * Delete Selected:
         *  Tablodan secilen satira karsilik gelen personeli hem
         *  LoginFrame listesinden hem de tablodan siler.
         */
        btnDelete.addActionListener(e -> {
            int selectedRow = personnelTable.getSelectedRow();
            if (selectedRow != -1) { // bir satir secili
                // Onemli: tablo satir indeksi ile listenin indeksinin AYNI olmasi gerekir (sirayla ekledik).
                // Bu yuzden ayni index'i kullaniyoruz.
                LoginFrame.registeredPersonnel.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            }
        });
    }
}
