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

public class ReportsFrame extends JPanel {

    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JLabel lblSummary;

    private MemberManager memberManager;
    private BookManager bookManager;
    private LoanManager loanManager;

    public ReportsFrame(MemberManager memberManager, BookManager bookManager, LoanManager loanManager) {
        this.memberManager = memberManager;
        this.bookManager = bookManager;
        this.loanManager = loanManager;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        buildTopPanel();
        buildCenterPanel();

        refreshTable();
    }

    private void buildTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 50));
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Overdue Books & Penalties Report");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#c0392b"));
        topPanel.add(titleLabel, BorderLayout.WEST);

        lblSummary = new JLabel("");
        lblSummary.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSummary.setForeground(Color.DARK_GRAY);
        topPanel.add(lblSummary, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }

    private void buildCenterPanel() {
        String[] columns = {"Member ID", "Member Name", "Book Title", "Due Date", "Days Overdue", "Penalty (₺)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(30);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 14));
        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        reportTable.getTableHeader().setBackground(Color.decode("#ecf0f1"));

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
    }

    // Tablodaki verileri yenile (gercek loan'larden hesapla)
    public void refreshTable() {
        loanManager.reload();
        bookManager.reload();
        memberManager.reload();

        tableModel.setRowCount(0);

        List<Loan> overdueList = loanManager.getOverdueLoans();
        double totalPenalty = 0;

        for (Loan l : overdueList) {
            Member m = memberManager.findById(l.getMemberId());
            Book b = bookManager.findById(l.getBookId());

            String memberName = (m != null) ? m.getFullName() : "?";
            String bookTitle = (b != null) ? b.getTitle() : "?";
            long daysOver = l.getDaysOverdue();
            double penalty = l.calculatePenalty();
            totalPenalty += penalty;

            tableModel.addRow(new Object[]{
                    "M-" + l.getMemberId(),
                    memberName,
                    bookTitle,
                    l.getDueDate().toString(),
                    daysOver,
                    String.format("%.2f ₺", penalty)
            });
        }

        // Ozet bilgi
        lblSummary.setText(String.format(
                "  Toplam gecikmiş: %d  |  Toplam ceza: %.2f ₺",
                overdueList.size(), totalPenalty));
    }
}
