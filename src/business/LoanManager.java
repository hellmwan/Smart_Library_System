package business;

import data.LoanData;
import model.Book;
import model.Loan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * LoanManager - odunc verme/iade islemlerinin is mantigini yoneten sinif.
 *
 * Bu sinifin diger Manager'lardan farki: BookManager'a bagimlidir,
 * cunku bir kitap odunc verildiginde Book'un durumunu da "Loaned"
 * yapmasi gerekir. Iade alindiginda da tekrar "Available" yapar.
 *
 * Boylece ayni anda iki kisiye ayni kitap odunc verilemez.
 */
public class LoanManager {

    private List<Loan> loans;            // Bellekteki odunc kayit listesi
    private BookManager bookManager;     // Kitap durumlarini guncellemek icin lazim

        /**
     * Yapici. Disari'dan BookManager bekler (dependency injection).
     * MainFrame su sirayla olusturur:
     *   bookManager = new BookManager();
     *   loanManager = new LoanManager(bookManager);
     */
    public LoanManager(BookManager bookManager) {
        this.bookManager = bookManager;
        this.loans = LoanData.loadAll();
    }
    /** Tum odunc kayitlarini doner (aktif + iade edilmis). */
    public List<Loan> getAllLoans() {
        return loans;
    }

    /**
     * Bir uyeye bir kitabi odunc verir.
     *
     * Kontroller ve adimlar:
     *  1. Kitap var mi?
     *  2. Kitap "Available" mi?
     *  3. Yeni Loan kaydi olustur, dosyaya yaz
     *  4. Kitabin durumunu "Loaned" yap
     *
     * @return Hata varsa hata mesaji string'i, basariliysa null
     */
    public String issueBook(int bookId, int memberId) {
        Book book = bookManager.findById(bookId);
        if (book == null) {
            return "Kitap bulunamadi.";
        }
        // Kitabin durumu "Available" degilse odunc verme
        if (!book.getStatus().equals("Available")) {
            return "Bu kitap su anda odunc verilemez (durum: " + book.getStatus() + ").";
        }

        // Yeni id ve tarihleri hazirla
        int newId = LoanData.nextId(loans);
        LocalDate today = LocalDate.now();
        // Iade tarihi: bugun + 14 gun
        LocalDate due = today.plusDays(Loan.DEFAULT_LOAN_DAYS);
        // Loan kaydi olustur (returnDate=null cunku henuz iade edilmedi)
        Loan loan = new Loan(newId, bookId, memberId, today, due, null, "Active");
        loans.add(loan);
        LoanData.saveAll(loans);

        // Kitabin durumunu "Loaned" yap (BookManager kendi dosyasina yazacak)
        bookManager.updateStatus(bookId, "Loaned");

        return null; // basarili
    }

     /**
     * Bir kitabi iade alir.
     * Kontroller:
     *  1. Boyle bir Loan kaydi var mi?
     *  2. Bu Loan zaten iade edilmis mi?
     *  3. Loan'i guncelle: returnDate=bugun, status="Returned"
     *  4. Kitabi tekrar "Available" yap
     *
     * @return Hata varsa mesaj, basariliysa null
     */
    public String returnBook(int loanId) {
        // Onceliklikle ilgili Loan kaydini bul
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
        // isActive() = returnDate==null demek; degilse zaten iade edilmis
        if (!target.isActive()) {
            return "Bu kitap zaten iade edilmis.";
        }
        // Loan'i guncelle
        target.setReturnDate(LocalDate.now());
        target.setStatus("Returned");
        LoanData.saveAll(loans);

        // Kitabi tekrar rafa al
        bookManager.updateStatus(target.getBookId(), "Available");

        return null;
    }

      /**
     * Su anda aktif (henuz iade edilmemis) tum oduncleri doner.
     * Hem normal hem gecikmis olanlar dahildir.
     */
    public List<Loan> getActiveLoans() {
        List<Loan> active = new ArrayList<>();
        for (Loan l : loans) {
            if (l.isActive()) active.add(l);
        }
        return active;
    }

    /**
     * Sadece gecikmis (vadesi gecmis ve hala iade edilmemis) oduncleri doner.
     * ReportsFrame ceza raporu icin bunu kullanir.
     */
    public List<Loan> getOverdueLoans() {
        List<Loan> overdue = new ArrayList<>();
        for (Loan l : loans) {
            if (l.isOverdue()) overdue.add(l);
        }
        return overdue;
    }
   /** Bellekteki listeyi dosyadan tekrar yukler. */
    public void reload() {
        this.loans = LoanData.loadAll();
    }
}
