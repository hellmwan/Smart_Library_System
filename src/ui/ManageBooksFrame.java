package ui;

import business.BookManager;
import model.Book;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import util.Validator;
import util.ValidationException;

/**
 * MainFrame'in "Manage Books" sekmesinde gosterilir.
 * Ustte form (kitap bilgileri) + butonlar, ortada arama kutusu ve
 * tum kitaplari listeleyen tablo bulunur.
 *
 * Yapilabilecek islemler:
 *  - Yeni kitap ekleme (Add)
 *  - Tablodan secilen kitabi guncelleme (Update)
 *  - Tablodan secilen kitabi silme (Delete)
 *  - Form temizleme (Clear)
 *  - Anlik arama (yazdikca filtreler)
 */
public class ManageBooksFrame extends JPanel {

    // --- Form alanlari ---
    private JTextField txtTitle, txtAuthor, txtIsbn;
    private JTextField txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    // --- Tablo bileşenleri ---
    private JTable bookTable;
    private DefaultTableModel tableModel;

    // Is mantigi nesnesi (MainFrame'den gelir)
    private BookManager bookManager;

    /**
     * Tablodan secilen kitabin id'si.
     * -1 = hicbir kitap secilmedi.
     * Update ve Delete butonlari calisirken bu id kullanilir.
     */
    private int selectedId = -1;


    /**
     * Yapici. MainFrame ortak BookManager'i buraya yollar.
     */
    public ManageBooksFrame(BookManager bookManager) {
        this.bookManager = bookManager;

        // BorderLayout: ustte form, ortada arama+tablo
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        buildTopPanel();    // Ust form panelini olustur
        buildCenterPanel(); // Orta tablo panelini olustur

        // Ilk acilista tabloyu doldur
        refreshTable();
    }


    /**
     * Ust panel: kitap bilgileri formu + Add/Update/Delete/Clear butonlari.
     * 4 satir, 2 sutunlu izgara duzeni.
     */
    private void buildTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(4, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        topPanel.setBackground(Color.WHITE);

        // Satir 1: kitap basligi
        topPanel.add(new JLabel("Book Title:"));
        txtTitle = new JTextField();
        topPanel.add(txtTitle);

        // Satir 2: yazar
        topPanel.add(new JLabel("Author:"));
        txtAuthor = new JTextField();
        topPanel.add(txtAuthor);

        // Satir 3: ISBN
        topPanel.add(new JLabel("ISBN Number:"));
        txtIsbn = new JTextField();
        topPanel.add(txtIsbn);


        // Satir 4: 4 buton yan yana
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnAdd = new JButton("Add Book");
        btnAdd.setBackground(new Color(46, 204, 113));   // yesil
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        btnUpdate = new JButton("Update");
        btnUpdate.setBackground(new Color(52, 152, 219)); // mavi
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);

        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60));  // kirmizi
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);

        btnClear = new JButton("Clear");
        btnClear.setFocusPainted(false);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // Bos hucre + butonlar (4. satir)
        topPanel.add(new JLabel(""));
        topPanel.add(buttonPanel);

        add(topPanel, BorderLayout.NORTH);


        // Buton olaylari -> handler metotlara baglan
        btnAdd.addActionListener(e -> handleAdd());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnDelete.addActionListener(e -> handleDelete());
        btnClear.addActionListener(e -> clearForm());
    }


    /**
     * Orta panel: arama kutusu + kitap tablosu.
     */
    private void buildCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));


        // ---- Arama kutusu ----
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        txtSearch = new JTextField();
        searchPanel.add(txtSearch, BorderLayout.CENTER);


        // Arama kutusunda her degisiklikte tabloyu filtrele
        // (kullanici yazdikca canli filtreleme yapiyoruz)
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applySearch(); }
            public void removeUpdate(DocumentEvent e) { applySearch(); }
            public void changedUpdate(DocumentEvent e) { applySearch(); }
        });

        centerPanel.add(searchPanel, BorderLayout.NORTH);


        // ---- Tablo ----
        String[] columns = {"ID", "Book Title", "Author", "ISBN", "Status"};
        // DefaultTableModel'i isCellEditable false donecek sekilde override et
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(25);
        bookTable.setSelectionBackground(Color.decode("#ffe7ba"));
        bookTable.setSelectionForeground(Color.BLACK);


        // Tablo satir secimi -> form alanlarini doldur
        // Boylece kullanici tablodan kitap secip Update/Delete yapabilir
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            // valueIsAdjusting -> mouse hala tikladigi sirada gelir,
            // false olunca tikitamamlanmis olur.
            if (e.getValueIsAdjusting()) return;
            int row = bookTable.getSelectedRow();
            if (row < 0) return; // hicbir satir secili degil

            // Secilen satirdaki alanlari forma yaz
            selectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            txtTitle.setText(tableModel.getValueAt(row, 1).toString());
            txtAuthor.setText(tableModel.getValueAt(row, 2).toString());
            txtIsbn.setText(tableModel.getValueAt(row, 3).toString());
        });

        // Tabloyu kaydirma cubukluyla sar ve panele ekle
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }


    // ==================== BUTON ISLEMLERI ====================

    /**
     * "Add Book" butonu.
     * Form alanlarini kontrol et, ISBN dogrulamasi yap, BookManager'a ekleme yaptir.
     */
    private void handleAdd() {
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String isbn = txtIsbn.getText().trim();

        // Bos alan kontrolu
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen tüm alanları doldurun.",
                    "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ISBN format kontrolu 
        try {
            Validator.requireValidIsbn(isbn);
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return;
        }

        // BookManager'a sor; ekleme basarili mi?
        boolean ok = bookManager.addBook(title, author, isbn);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Kitap başarıyla eklendi.");
            refreshTable();
            clearForm();
        } else {
            // Genelde ayni ISBN'le baska kitap vardir
            JOptionPane.showMessageDialog(this,
                    "Kitap eklenemedi. Aynı ISBN ile kayıt olabilir veya alanlar boş.",
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * "Update" butonu.
     * Tablodan kitap secili olmali. Secili kitabi formdaki bilgilerle gunceller.
     */
    private void handleUpdate() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Önce tablodan bir kitap seçin.");
            return;
        }
        boolean ok = bookManager.updateBook(selectedId,
                txtTitle.getText(), txtAuthor.getText(), txtIsbn.getText());
        if (ok) {
            JOptionPane.showMessageDialog(this, "Kitap güncellendi.");
            refreshTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Güncelleme başarısız.",
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * "Delete" butonu.
     * Kullanicidan onay al, sonra sil. Odunc verilen kitap silinemez.
     */
    private void handleDelete() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Önce silinecek kitabı seçin.");
            return;
        }
        // Onay dialog'u
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bu kitabı silmek istediğinize emin misiniz?",
                "Onay", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean ok = bookManager.deleteBook(selectedId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Kitap silindi.");
            refreshTable();
            clearForm();
        } else {
            // BookManager.deleteBook "Loaned" durumdaki kitaba false donuyor
            JOptionPane.showMessageDialog(this,
                    "Silinemedi. Kitap ödünç verilmiş olabilir.",
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    /** Formu ve tablo secimi temizler. */
    private void clearForm() {
        selectedId = -1;
        txtTitle.setText("");
        txtAuthor.setText("");
        txtIsbn.setText("");
        bookTable.clearSelection();
    }


    /** Arama kutusundaki metne gore tabloyu filtreler. */
    private void applySearch() {
        String key = txtSearch.getText();
        List<Book> filtered = bookManager.search(key);
        fillTable(filtered);
    }


    /**
     * Tabloyu yenile (disaridan da cagrilir, MainFrame'den).
     * Once dosyadan tekrar oku, sonra arama varsa filtre ile, yoksa
     * tum listeyle doldur.
     */
    public void refreshTable() {
        bookManager.reload();

        if (txtSearch != null && !txtSearch.getText().trim().isEmpty()) {
            applySearch();
        } else {
            fillTable(bookManager.getAllBooks());
        }
    }


    /**
     * Verilen kitap listesini tabloya yazar.
     * Once tabloyu temizler (setRowCount(0)), sonra her kitap icin satir ekler.
     */
    private void fillTable(List<Book> list) {
        tableModel.setRowCount(0);
        for (Book b : list) {
            tableModel.addRow(new Object[]{
                    b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.getStatus()
            });
        }
    }
}
