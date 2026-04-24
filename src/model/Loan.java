package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Odunc alma kaydi
public class Loan {

    // Default odunc suresi (gun) ve gun basina ceza (TL)
    public static final int DEFAULT_LOAN_DAYS = 14;
    public static final double DAILY_PENALTY = 5.0;

    private int id;
    private int bookId;
    private int memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // null ise henuz iade edilmemis
    private String status; // "Active" veya "Returned"

    public Loan(int id, int bookId, int memberId, LocalDate issueDate, LocalDate dueDate,
                LocalDate returnDate, String status) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getId() { return id; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getStatus() { return status; }

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setStatus(String status) { this.status = status; }

    // Henuz iade edilmedi mi?
    public boolean isActive() {
        return returnDate == null;
    }

    // Suresi gecti mi (henuz iade edilmedi ve due date gecti)
    public boolean isOverdue() {
        if (returnDate != null) return false;
        return LocalDate.now().isAfter(dueDate);
    }

    // Kac gun gecikmis (gecmemis ise 0)
    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    // Ceza tutari = gecikme gunu * gun basina ceza
    public double calculatePenalty() {
        return getDaysOverdue() * DAILY_PENALTY;
    }

    public String toCsvLine() {
        // returnDate null olabilir, "-" yazalim
        String ret = (returnDate == null) ? "-" : returnDate.toString();
        return id + ";" + bookId + ";" + memberId + ";" + issueDate + ";" + dueDate + ";" + ret + ";" + status;
    }

    public static Loan fromCsvLine(String line) {
        String[] parts = line.split(";");
        if (parts.length < 7) return null;
        int id = Integer.parseInt(parts[0]);
        int bookId = Integer.parseInt(parts[1]);
        int memberId = Integer.parseInt(parts[2]);
        LocalDate issue = LocalDate.parse(parts[3]);
        LocalDate due = LocalDate.parse(parts[4]);
        LocalDate ret = parts[5].equals("-") ? null : LocalDate.parse(parts[5]);
        return new Loan(id, bookId, memberId, issue, due, ret, parts[6]);
    }
}
