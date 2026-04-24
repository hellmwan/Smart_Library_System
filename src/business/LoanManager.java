package business;

import data.LoanData;
import model.Book;
import model.Loan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Odunc verme / iade alma islemleri
public class LoanManager {

    private List<Loan> loans;
    private BookManager bookManager;

    public LoanManager(BookManager bookManager) {
        this.bookManager = bookManager;
        this.loans = LoanData.loadAll();
    }

    public List<Loan> getAllLoans() {
        return loans;
    }

    // Odunc ver: kitap varsa ve uygunsa kayit ac, kitabin durumunu Loaned yap
    // Hata mesajini geri donduruyor (null ise islem basarili)
    public String issueBook(int bookId, int memberId) {
        Book book = bookManager.findById(bookId);
        if (book == null) {
            return "Kitap bulunamadi.";
        }
        if (!book.getStatus().equals("Available")) {
            return "Bu kitap su anda odunc verilemez (durum: " + book.getStatus() + ").";
        }

        // Yeni loan kaydi olustur
        int newId = LoanData.nextId(loans);
        LocalDate today = LocalDate.now();
        LocalDate due = today.plusDays(Loan.DEFAULT_LOAN_DAYS);

        Loan loan = new Loan(newId, bookId, memberId, today, due, null, "Active");
        loans.add(loan);
        LoanData.saveAll(loans);

        // kitabin status'unu guncelle
        bookManager.updateStatus(bookId, "Loaned");

        return null; // basarili
    }

    // Iade al: aktif loan'i bul, return date'i bugune al, kitabi Available yap
    public String returnBook(int loanId) {
        Loan target = null;
        for (Loan l : loans) {
            if (l.getId() == loanId) {
                target = l;
                break;
            }
        }
        if (target == null) {
            return "Odunc kaydi bulunamadi.";
        }
        if (!target.isActive()) {
            return "Bu kitap zaten iade edilmis.";
        }

        target.setReturnDate(LocalDate.now());
        target.setStatus("Returned");
        LoanData.saveAll(loans);

        // Kitabi tekrar Available yap
        bookManager.updateStatus(target.getBookId(), "Available");

        return null;
    }

    // Sadece aktif (henuz iade edilmemis) loan'lari getir
    public List<Loan> getActiveLoans() {
        List<Loan> active = new ArrayList<>();
        for (Loan l : loans) {
            if (l.isActive()) active.add(l);
        }
        return active;
    }

    // Sadece gecikmis olanlari getir (rapor icin)
    public List<Loan> getOverdueLoans() {
        List<Loan> overdue = new ArrayList<>();
        for (Loan l : loans) {
            if (l.isOverdue()) overdue.add(l);
        }
        return overdue;
    }

    public void reload() {
        this.loans = LoanData.loadAll();
    }
}
