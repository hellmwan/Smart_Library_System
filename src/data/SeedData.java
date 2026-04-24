package data;

import model.Loan;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Ilk acilista bos olan dosyalar icin ornek veriler uretiyor.
// Boylece uygulama bos acilmiyor, demo icin uygun.
public class SeedData {

    // Sadece dosya yoksa olusturalim, mevcut veriyi bozmayalim.
    public static void seedIfEmpty() {
        // books.txt zaten projeyle birlikte geliyor (sabit), dokunmuyoruz.
        // members.txt da oyle.

        // loans.txt bos olabilir cunku tarihler dinamik olmali.
        File loanFile = new File("data/loans.txt");
        if (!loanFile.exists() || loanFile.length() == 0) {
            createSampleLoans();
        }
    }

    private static void createSampleLoans() {
        List<Loan> loans = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Loan 1: Aktif, daha yeni alindi (2 numarali kitap, 101 numarali uye)
        // books.txt'de book id=2 zaten "Loaned" durumda.
        loans.add(new Loan(1, 2, 101,
                today.minusDays(3), today.plusDays(11),
                null, "Active"));

        // Loan 2: GECIKMIS - 16 gun once alinmis, 2 gun once iade edilmesi gerekiyor
        // (book id=7 "Sapiens", uye 102)
        loans.add(new Loan(2, 7, 102,
                today.minusDays(16), today.minusDays(2),
                null, "Active"));

        // Loan 3: Iade edilmis (gecmis kayit) - book id=4 "Algorithms", uye 103
        loans.add(new Loan(3, 4, 103,
                today.minusDays(30), today.minusDays(16),
                today.minusDays(18), "Returned"));

        LoanData.saveAll(loans);
    }
}
