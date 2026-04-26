package ui;

import business.BookManager;
import business.LoanManager;
import business.MemberManager;
import model.Book;
import model.Loan;
import model.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * MainFrame'in "Issue / Return" sekmesinde gosterilir.
 * Ust: uye combobox, kitap combobox, Issue/Return butonlari.
 * Orta: aktif odunc kayitlarinin listelendigi tablo.
 *
 * Kullanim:
 *  - Odunc verme: Combobox'tan uye ve kitap sec, "Issue Book"a bas
 *  - Iade alma: Tablodan kaydi sec, "Return Book"a bas
 */
public class IssueReturnFrame extends JPanel {

    // --- Form bileşenleri ---
    private JComboBox<MemberItem> cmbMembers;  // Uye listesi
    private JComboBox<BookItem> cmbBooks;      // Kitap listesi
    private JButton btnIssue, btnReturn;

    // Tablo
    private JTable loanTable;
    private DefaultTableModel tableModel;

    // Manager'lar 
    private BookManager bookManager;
    private MemberManager memberManager;
    private LoanManager loanManager;


    public IssueReturnFrame(BookManager bookManager, MemberManager memberManager, LoanManager loanManager) {
        this.bookManager = bookManager;
        this.memberManager = memberManager;
        this.loanManager = loanManager;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        buildTopPanel();      // Ust form + butonlar
        buildCenterPanel();   // Orta tablo

        refreshAll(); // baslangicta verileri yukle
    }


    /**
     * Ust panel: 3 satir 2 sutun izgara.
     * 1. satir uye, 2. satir kitap, 3. satir butonlar.
     */
    private void buildTopPanel() {
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        topPanel.setBackground(Color.WHITE);

        // Satir 1: uye secimi
        topPanel.add(new JLabel("Select Member:"));
        cmbMembers = new JComboBox<>();
        topPanel.add(cmbMembers);

        // Satir 2: kitap secimi
        topPanel.add(new JLabel("Select Book:"));
        cmbBooks = new JComboBox<>();
        topPanel.add(cmbBooks);

        // Satir 3: 2 buton yan yana
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnIssue = new JButton("Issue Book");
        btnIssue.setBackground(new Color(52, 152, 219));  // mavi
        btnIssue.setForeground(Color.WHITE);

        btnReturn = new JButton("Return Book");
        btnReturn.setBackground(new Color(231, 76, 60));  // kirmizi
        btnReturn.setForeground(Color.WHITE);

        buttonPanel.add(btnIssue);
        buttonPanel.add(btnReturn);

        // Sol bos hucre + butonlar
        topPanel.add(new JLabel(""));
        topPanel.add(buttonPanel);

        add(topPanel, BorderLayout.NORTH);

        // Buton olaylari
        btnIssue.addActionListener(e -> handleIssue());
        btnReturn.addActionListener(e -> handleReturn());
    }


    //Orta panel: aktif odunc kayitlarini gosteren tablo.
    private void buildCenterPanel() {
        String[] columns = {"Loan ID", "Member", "Book", "Issue Date", "Due Date", "Status"};
        // Read-only tablo
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        loanTable = new JTable(tableModel);
        loanTable.setRowHeight(25);

        add(new JScrollPane(loanTable), BorderLayout.CENTER);
    }


    /**
     * "Issue Book" butonu islemi.
     * Uye ve kitap secili mi kontrol et, sonra LoanManager'a issueBook cagir.
     */
    private void handleIssue() {
        // Combobox'lardan secili nesneleri al
        MemberItem m = (MemberItem) cmbMembers.getSelectedItem();
        BookItem b = (BookItem) cmbBooks.getSelectedItem();

        if (m == null || b == null) {
            JOptionPane.showMessageDialog(this, "Seçim yapınız.");
            return;
        }

        // LoanManager.issueBook -> hata varsa hata mesaji string'i, basariliysa null
        String error = loanManager.issueBook(b.id, m.id);

        if (error == null) {
            JOptionPane.showMessageDialog(this, "Kitap ödünç verildi ");
            refreshAll(); // listeyi yenile (kitap artik "Loaned" oldu)
        } else {
            // Hata mesajini direkt kullaniciya goster
            JOptionPane.showMessageDialog(this, error);
        }
    }


    /**
     * "Return Book" butonu islemi.
     * Tablodan satir secili olmali. Onay sor, sonra iade et.
     */
    private void handleReturn() {
        int row = loanTable.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Bir kayıt seçin.");
            return;
        }

        // Tablodaki ilk sutun Loan ID
        int loanId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

        // Onay sor
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Kitap iade edilsin mi?",
                "Onay",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        // LoanManager.returnBook -> hata mesaji veya null
        String error = loanManager.returnBook(loanId);

        if (error == null) {
            JOptionPane.showMessageDialog(this, "Kitap iade edildi ✅");
            refreshAll();
        } else {
            JOptionPane.showMessageDialog(this, error);
        }
    }


    /**
     * Tum bilesenleri yeniden yukler:
     *  - Manager'lari dosyadan yenile
     *  - Uye combobox'ini doldur
     *  - Kitap combobox'ini doldur (sadece "Available" olanlar)
     *  - Aktif odunc tablosunu doldur
     */
    public void refreshAll() {
        // Verileri tazele
        bookManager.reload();
        memberManager.reload();
        loanManager.reload();

        // Uye combobox'ini sifirla ve doldur
        cmbMembers.removeAllItems();
        for (Member m : memberManager.getAllMembers()) {
            cmbMembers.addItem(new MemberItem(m.getId(), m.getFullName()));
        }

        // Kitap combobox'ini sadece "Available" kitaplarla doldur
        cmbBooks.removeAllItems();
        for (Book b : bookManager.getAvailableBooks()) {
            cmbBooks.addItem(new BookItem(b.getId(), b.getTitle()));
        }

        // Aktif loan tablosunu doldur
        tableModel.setRowCount(0);

        for (Loan l : loanManager.getActiveLoans()) {
            // Loan'da sadece id'ler var; uye ve kitap nesnelerini bulmamiz lazim
            Member m = memberManager.findById(l.getMemberId());
            Book b = bookManager.findById(l.getBookId());

            // Eger (silindigi vs.) bulamazsak "?" yaz, programi cokme
            String memberName = (m != null) ? m.getFullName() : "?";
            String bookTitle = (b != null) ? b.getTitle() : "?";

            // Status: gecikmisse uyari ikonu ile goster, normalse "Active"
            String status = l.isOverdue() ? "OVERDUE ⚠" : "Active";

            tableModel.addRow(new Object[]{
                    l.getId(),
                    memberName,
                    bookTitle,
                    l.getIssueDate(),
                    l.getDueDate(),
                    status
            });
        }
    }


    // ==================== YARDIMCI SARMALAYICI SINIFLAR ====================
    /*
     * JComboBox'a direkt Member veya Book koymak yerine bu kucuk sarmalayicilari
     * kullaniyoruz. Boylece toString metoduna sadece gorunecek metni
     * (orn: uye adi, kitap basligi) yazariz, fakat secildikten sonra id'yi de okuyabiliriz.
     */

    /** Uye combobox icin sarmalayici. */
    private static class MemberItem {
        int id;
        String name;

        MemberItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        // Combobox bu metni gosterir
        public String toString() {
            return name;
        }
    }

    /** Kitap combobox icin sarmalayici. */
    private static class BookItem {
        int id;
        String title;

        BookItem(int id, String title) {
            this.id = id;
            this.title = title;
        }

        public String toString() {
            return title;
        }
    }
}
