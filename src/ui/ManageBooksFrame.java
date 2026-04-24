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

public class ManageBooksFrame extends JPanel {

    // UI components for the screen
    private JTextField txtTitle, txtAuthor, txtIsbn;
    private JTextField txtSearch; // arama alani
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    // Backend baglantisi
    private BookManager bookManager;

    // Update icin secili kitabin id'si (yoksa -1)
    private int selectedId = -1;

    public ManageBooksFrame(BookManager bookManager) {
        this.bookManager = bookManager;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        buildTopPanel();
        buildCenterPanel();

        // Tabloyu doldur
        refreshTable();
    }

    private void buildTopPanel() {
        // Top panel form alanlari icin
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(4, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(new JLabel("Book Title:"));
        txtTitle = new JTextField();
        topPanel.add(txtTitle);

        topPanel.add(new JLabel("Author:"));
        txtAuthor = new JTextField();
        topPanel.add(txtAuthor);

        topPanel.add(new JLabel("ISBN Number:"));
        txtIsbn = new JTextField();
        topPanel.add(txtIsbn);

        // Aksiyon butonlari icin alt panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnAdd = new JButton("Add Book");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        btnUpdate = new JButton("Update");
        btnUpdate.setBackground(new Color(52, 152, 219));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);

        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);

        btnClear = new JButton("Clear");
        btnClear.setFocusPainted(false);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        topPanel.add(new JLabel(""));
        topPanel.add(buttonPanel);

        add(topPanel, BorderLayout.NORTH);

        // --- Action listenerlar ---
        btnAdd.addActionListener(e -> handleAdd());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnDelete.addActionListener(e -> handleDelete());
        btnClear.addActionListener(e -> clearForm());
    }

    private void buildCenterPanel() {
        // Tablo + arama icin orta panel
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Arama alani
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        txtSearch = new JTextField();
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        // Yazdikca filtrelesin
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applySearch(); }
            public void removeUpdate(DocumentEvent e) { applySearch(); }
            public void changedUpdate(DocumentEvent e) { applySearch(); }
        });

        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Tablo
        String[] columns = {"ID", "Book Title", "Author", "ISBN", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            // Hucreler duzenlenemez olsun
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(25);
        bookTable.setSelectionBackground(Color.decode("#ffe7ba"));
        bookTable.setSelectionForeground(Color.BLACK);

        // Satira tiklayinca form'a doldur
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = bookTable.getSelectedRow();
            if (row < 0) return;
            // Id'yi al ve form'u doldur
            selectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            txtTitle.setText(tableModel.getValueAt(row, 1).toString());
            txtAuthor.setText(tableModel.getValueAt(row, 2).toString());
            txtIsbn.setText(tableModel.getValueAt(row, 3).toString());
        });

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    // --- Buton islemleri ---

    private void handleAdd() {
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String isbn = txtIsbn.getText().trim();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen tüm alanları doldurun.",
                    "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 🔥 BURAYA EKLE
        try {
            Validator.requireValidIsbn(isbn);
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return;
        }

        boolean ok = bookManager.addBook(title, author, isbn);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Kitap başarıyla eklendi.");
            refreshTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Kitap eklenemedi. Aynı ISBN ile kayıt olabilir veya alanlar boş.",
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

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

    private void handleDelete() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Önce silinecek kitabı seçin.");
            return;
        }
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
            // Genelde odunc verilmis kitabi silmeye calistiysa
            JOptionPane.showMessageDialog(this,
                    "Silinemedi. Kitap ödünç verilmiş olabilir.",
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        selectedId = -1;
        txtTitle.setText("");
        txtAuthor.setText("");
        txtIsbn.setText("");
        bookTable.clearSelection();
    }

    // Aramayi tabloya uygula
    private void applySearch() {
        String key = txtSearch.getText();
        List<Book> filtered = bookManager.search(key);
        fillTable(filtered);
    }

    // Tabloyu sifirdan doldur (manager'dan tum kitaplari al)
    public void refreshTable() {
        bookManager.reload();
        // Eger arama kutusu doluysa filtreyle, degilse hepsini goster
        if (txtSearch != null && !txtSearch.getText().trim().isEmpty()) {
            applySearch();
        } else {
            fillTable(bookManager.getAllBooks());
        }
    }

    private void fillTable(List<Book> list) {
        tableModel.setRowCount(0);
        for (Book b : list) {
            tableModel.addRow(new Object[]{
                    b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.getStatus()
            });
        }
    }
}
