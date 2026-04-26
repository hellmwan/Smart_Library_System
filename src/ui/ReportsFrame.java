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
import java.util.List;

/**
 * MainFrame'in "Reports" sekmesinde gosterilir.
 * Sadece teslim tarihi gecmis ama hala iade edilmemis kitaplari listeler.
 *
 * Tablo sutunlari:
 *  - Member ID, Member Name, Book Title, Due Date, Days Overdue, Penalty (TL)
 *
 * Gun basina ceza Loan.DAILY_PENALTY = 5.0 TL.
 * Toplam gecikme sayisi ve toplam ceza ust kosede ozet olarak gosterilir.
 */
public class ReportsFrame extends JPanel {

    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JLabel lblSummary;  // Sag ust ozet etiketi

    private MemberManager memberManager;
    private BookManager bookManager;
    private LoanManager loanManager;


    public ReportsFrame(MemberManager memberManager, BookManager bookManager, LoanManager loanManager) {
        this.memberManager = memberManager;
        this.bookManager = bookManager;
        this.loanManager = loanManager;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        buildTopPanel();    // Baslik + ozet etiket
        buildCenterPanel(); // Tablo

        // Acilista veriyi yukle
        refreshTable();
    }


    /**
     * Ust panel: solda baslik, sagda ozet etiket.
     */
    private void buildTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 50));
        topPanel.setBackground(Color.WHITE);

        // Sol: rapor basligi (kirmizi tonlu, dikkat cekmek icin)
        JLabel titleLabel = new JLabel("Overdue Books & Penalties Report");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#c0392b"));
        topPanel.add(titleLabel, BorderLayout.WEST);

        // Sag: ozet etiket (toplam gecikmis sayisi + toplam ceza)
        lblSummary = new JLabel("");
        lblSummary.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSummary.setForeground(Color.DARK_GRAY);
        topPanel.add(lblSummary, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }


    //Orta panel: gecikmis kitaplari listeleyen tablo.
    private void buildCenterPanel() {
        String[] columns = {"Member ID", "Member Name", "Book Title", "Due Date", "Days Overdue", "Penalty (₺)"};
        // Read-only tablo
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(30);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 14));

        // Tablo basligini farkli stille goster
        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        reportTable.getTableHeader().setBackground(Color.decode("#ecf0f1"));

        // Tabloyu kaydirma cubuklu sar
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
    }


    /**
     * Tabloyu yenile.
     * Adimlar:
     *  1. Manager'lari dosyadan yenile (baska panelde degisiklik olmus olabilir)
     *  2. Tabloyu temizle
     *  3. Tum gecikmis loan'lari gez, her biri icin satir ekle
     *  4. Toplam ceza toplamini hesapla, ozet etikete yaz
     */
    public void refreshTable() {
        loanManager.reload();
        bookManager.reload();
        memberManager.reload();

        // Tabloyu temizle
        tableModel.setRowCount(0);

        // Gecikmis loan'lari al
        List<Loan> overdueList = loanManager.getOverdueLoans();
        double totalPenalty = 0;

        for (Loan l : overdueList) {
            // Loan icindeki id'lerden uye ve kitap nesnelerini bul
            Member m = memberManager.findById(l.getMemberId());
            Book b = bookManager.findById(l.getBookId());

            // Eslesme yoksa "?" yaz, programi durdurma
            String memberName = (m != null) ? m.getFullName() : "?";
            String bookTitle = (b != null) ? b.getTitle() : "?";

            // Loan icinden hesaplananlar
            long daysOver = l.getDaysOverdue();
            double penalty = l.calculatePenalty();
            totalPenalty += penalty; // toplama ekle

            tableModel.addRow(new Object[]{
                    "M-" + l.getMemberId(),    // "M-101" gibi
                    memberName,
                    bookTitle,
                    l.getDueDate().toString(),
                    daysOver,
                    String.format("%.2f ₺", penalty) // 2 ondalik basamakli TL
            });
        }


        // Sag ustte ozet bilgileri yaz
        // Ornek: "  Toplam gecikmiş: 1  |  Toplam ceza: 10.00 ₺"
        lblSummary.setText(String.format(
                "  Toplam gecikmiş: %d  |  Toplam ceza: %.2f ₺",
                overdueList.size(), totalPenalty));
    }
}
