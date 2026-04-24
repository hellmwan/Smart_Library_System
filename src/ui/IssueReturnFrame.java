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

public class IssueReturnFrame extends JPanel {

    private JComboBox<MemberItem> cmbMembers;
    private JComboBox<BookItem> cmbBooks;
    private JButton btnIssue, btnReturn;
    private JTable loanTable;
    private DefaultTableModel tableModel;

    private BookManager bookManager;
    private MemberManager memberManager;
    private LoanManager loanManager;

    public IssueReturnFrame(BookManager bookManager, MemberManager memberManager, LoanManager loanManager) {
        this.bookManager = bookManager;
        this.memberManager = memberManager;
        this.loanManager = loanManager;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        buildTopPanel();
        buildCenterPanel();

        refreshAll();
    }

    private void buildTopPanel() {
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(new JLabel("Select Member:"));
        cmbMembers = new JComboBox<>();
        topPanel.add(cmbMembers);

        topPanel.add(new JLabel("Select Book:"));
        cmbBooks = new JComboBox<>();
        topPanel.add(cmbBooks);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnIssue = new JButton("Issue Book");
        btnIssue.setBackground(new Color(52, 152, 219));
        btnIssue.setForeground(Color.WHITE);

        btnReturn = new JButton("Return Book");
        btnReturn.setBackground(new Color(231, 76, 60));
        btnReturn.setForeground(Color.WHITE);

        buttonPanel.add(btnIssue);
        buttonPanel.add(btnReturn);

        topPanel.add(new JLabel(""));
        topPanel.add(buttonPanel);

        add(topPanel, BorderLayout.NORTH);

        btnIssue.addActionListener(e -> handleIssue());
        btnReturn.addActionListener(e -> handleReturn());
    }

    private void buildCenterPanel() {
        String[] columns = {"Loan ID", "Member", "Book", "Issue Date", "Due Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        loanTable = new JTable(tableModel);
        loanTable.setRowHeight(25);

        add(new JScrollPane(loanTable), BorderLayout.CENTER);
    }

    // =========================
    // ISSUE
    // =========================
    private void handleIssue() {
        MemberItem m = (MemberItem) cmbMembers.getSelectedItem();
        BookItem b = (BookItem) cmbBooks.getSelectedItem();

        if (m == null || b == null) {
            JOptionPane.showMessageDialog(this, "Seçim yapınız.");
            return;
        }

        String error = loanManager.issueBook(b.id, m.id);

        if (error == null) {
            JOptionPane.showMessageDialog(this, "Kitap ödünç verildi ✅");
            refreshAll();
        } else {
            JOptionPane.showMessageDialog(this, error);
        }
    }

    // =========================
    // RETURN
    // =========================
    private void handleReturn() {
        int row = loanTable.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Bir kayıt seçin.");
            return;
        }

        int loanId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Kitap iade edilsin mi?",
                "Onay",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String error = loanManager.returnBook(loanId);

        if (error == null) {
            JOptionPane.showMessageDialog(this, "Kitap iade edildi ✅");
            refreshAll();
        } else {
            JOptionPane.showMessageDialog(this, error);
        }
    }

    // =========================
    // REFRESH
    // =========================
    public void refreshAll() {
        bookManager.reload();
        memberManager.reload();
        loanManager.reload();

        cmbMembers.removeAllItems();
        for (Member m : memberManager.getAllMembers()) {
            cmbMembers.addItem(new MemberItem(m.getId(), m.getFullName()));
        }

        cmbBooks.removeAllItems();
        for (Book b : bookManager.getAvailableBooks()) {
            cmbBooks.addItem(new BookItem(b.getId(), b.getTitle()));
        }

        tableModel.setRowCount(0);

        for (Loan l : loanManager.getActiveLoans()) {

            Member m = memberManager.findById(l.getMemberId());
            Book b = bookManager.findById(l.getBookId());

            String memberName = (m != null) ? m.getFullName() : "?";
            String bookTitle = (b != null) ? b.getTitle() : "?";

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

    // =========================
    // HELPER CLASSES
    // =========================
    private static class MemberItem {
        int id;
        String name;

        MemberItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

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