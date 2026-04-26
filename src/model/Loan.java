package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Bir uye bir kitabi odunc aldiginda burada bir Loan kaydi olusur.
 * Bu kayit; hangi kitap, hangi uye, ne zaman alindi, ne zaman teslim edilmesi gerekir, 
 * ne zaman iade edildi gibi bilgileri tutar.
 * Ayrica gecikme kontrolu ve ceza hesabi da bu sinifta yapilir.
 */
public class Loan {

    // --- Sabitler (tum odunclar icin gecerli kurallar) ---

    /** Odunc verme suresi: 14 gun. (Universite kutuphaneleri genelde 14 gun verir.) */
    public static final int DEFAULT_LOAN_DAYS = 14;

    /** Gecikme cezasi: gun basina 5 TL. */
    public static final double DAILY_PENALTY = 5.0;

    // --- Alanlar (her odunc kaydinin ozellikleri) ---

    private int id;                 // Odunc kaydinin benzersiz numarasi
    private int bookId;             // Hangi kitap odunc verildi (Book.id)
    private int memberId;           // Hangi uyeye verildi (Member.id)
    private LocalDate issueDate;    // Kitap odunc verilis (cikis) tarihi
    private LocalDate dueDate;      // Son iade tarihi (issueDate + 14 gun)
    private LocalDate returnDate;   // Gercekten iade edildigi tarih, henuz iade edilmediyse null
    private String status;          // "Active" = elinde, "Returned" = iade edilmis

    /**
     * Tum alanlari alan ana yapici.
     * Aktif bir odunc icin returnDate = null verilmelidir.
     */
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

    // --- Getter metotlari ---
    public int getId() { return id; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getStatus() { return status; }

    // --- Setter metotlari (sadece iade icin gerekli olanlar) ---
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setStatus(String status) { this.status = status; }

    /**
     * Bu odunc hala devam ediyor mu?
     * returnDate doluysa kitap iade edilmis demek -> aktif degil.
     * returnDate null ise kitap hala uyenin elinde -> aktif.
     */
    public boolean isActive() {
        return returnDate == null;
    }

    /**
     * Bu odunc gecikmis mi?
     *
     * Mantik:
     *  - Eger zaten iade edilmisse (returnDate != null) -> gecikmis sayilmaz, false
     *  - Hala elindeyse ve bugun teslim tarihinden sonraysa -> evet gecikmis
     */
    public boolean isOverdue() {
        if (returnDate != null) return false;
        return LocalDate.now().isAfter(dueDate);
    }

    /**
     * Kac gun gecikmis?
     *
     * Eger gecikme yoksa 0 doner.
     * Varsa ChronoUnit.DAYS.between ile dueDate'ten bugune kac gun oldugunu hesaplar.
     */
    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    /**
     * Toplam ceza miktarini TL cinsinden hesaplar.
     * Formul: gecikme gun sayisi * gunluk ceza (5 TL).
     */
    public double calculatePenalty() {
        return getDaysOverdue() * DAILY_PENALTY;
    }

    /**
     * Bu Loan kaydini dosyaya yazilacak CSV satirina cevirir.
     * Ornek (aktif): "1;3;101;2025-01-10;2025-01-24;-;Active"
     * Ornek (iade):  "2;5;102;2025-01-01;2025-01-15;2025-01-12;Returned"
     *
     * returnDate null ise '-' karakteri yazariz, dosyadan tekrar
     * okurken '-' goren kod onu null'a cevirecek.
     */
    public String toCsvLine() {
        // returnDate yoksa "-" yaz, varsa tarihi metne cevir
        String ret = (returnDate == null) ? "-" : returnDate.toString();
        return id + ";" + bookId + ";" + memberId + ";" + issueDate + ";" + dueDate + ";" + ret + ";" + status;
    }

    /**
     * CSV satirindan Loan nesnesi olusturur.
     * En az 7 alan bekleriz (yukaridaki toCsvLine cikti formati).
     */
    public static Loan fromCsvLine(String line) {
        String[] parts = line.split(";");
        if (parts.length < 7) return null;

        // Sayisal alanlari int'e cevir
        int id = Integer.parseInt(parts[0]);
        int bookId = Integer.parseInt(parts[1]);
        int memberId = Integer.parseInt(parts[2]);

        // Tarih alanlarini LocalDate'e cevir
        LocalDate issue = LocalDate.parse(parts[3]);
        LocalDate due = LocalDate.parse(parts[4]);

        // returnDate "-" ise null demek, degilse tarihe cevir
        LocalDate ret = parts[5].equals("-") ? null : LocalDate.parse(parts[5]);

        return new Loan(id, bookId, memberId, issue, due, ret, parts[6]);
    }
}
