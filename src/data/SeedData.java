package data;

import model.Loan;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * SeedData - ilk acilista programa ornek odunc verileri ekleyen sinif.
 * Amac: Program ilk kez calistirildiginda Reports ve Issue/Return
 * ekranlari bom-bos gorunmesin, ogretmen veya kullanici daha ilk
 * acista nasil calistigini gorsun diye 3 ornek odunc kaydi ekleriz:
 *  1. Aktif (suresi gecmemis) odunc
 *  2. Aktif AMA gecikmis odunc -> ceza hesabi gorunsun diye
 *  3. Iade edilmis odunc -> tarihce icin
 * Loan dosyasi mevcut ve dolu ise hicbir sey yapmaz.
 */
public class SeedData {

    /**
     * Eger data/loans.txt yoksa veya bossa ornek kayitlari olusturur.
     * Aksi halde hicbir sey yapmaz (kullanicinin verisini bozmaz).
     */
    public static void seedIfEmpty() {
        File loanFile = new File("data/loans.txt");

        // Dosya yok ya da iceriksiz -> ornek veri yukleme zamani
        if (!loanFile.exists() || loanFile.length() == 0) {
            createSampleLoans();
        }
    }

    /**
     * 3 farkli senaryoyu temsil eden ornek Loan kayitlarini olusturur ve dosyaya yazar.
     */
    private static void createSampleLoans() {
        List<Loan> loans = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // 1. AKTIF: 3 gun once verildi, 11 gun sonra iade edilmesi gerek
        loans.add(new Loan(1, 2, 101,
                today.minusDays(3), today.plusDays(11),
                null, "Active"));

        // 2. GECIKMIS: 16 gun once verildi, 2 gun once teslim edilmesi gerekirdi
        //    -> hala iade edilmemis (returnDate=null) -> Reports'ta ceza gorunecek
        loans.add(new Loan(2, 7, 102,
                today.minusDays(16), today.minusDays(2),
                null, "Active"));

        // 3. IADE EDILMIS: 30 gun once verildi, 16 gun once teslim edilecekti,
        //    18 gun once iade edildi -> tarihce icin guzel bir ornek
        loans.add(new Loan(3, 4, 103,
                today.minusDays(30), today.minusDays(16),
                today.minusDays(18), "Returned"));

        // Olusturulan listeyi dosyaya kaydet
        LoanData.saveAll(loans);
    }
}
