package data;

import model.Loan;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * "data/loans.txt" dosyasinda her satir bir Loan kaydi olarak tutulur.
 * BookData ve MemberData ile ayni mantikla calisir, sadece bir farki vardir: 
 * tarih ayristirma hatasi olabilecegi icin her satir icin ekstra try/catch koyarak bozuk kayitlari atlariz, programi durdurmayiz.
 */
public class LoanData {

    /** Odunc kayitlarinin saklandigi dosya. */
    private static String FILE = "data/loans.txt";

    /** Test icin dosya yolunu degistir. */
    public static void setFilePath(String path) {
        FILE = path;
    }

    /**
     * Tum odunc kayitlarini dosyadan okuyup liste olarak doner.
     * Bozuk satirlari (tarih hatali vs.) atlayip diger kayitlari almaya devam eder.
     */
    public static List<Loan> loadAll() {
        List<Loan> list = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Her satir icin ayri try -> bir bozuk satir digerlerini etkilemesin
                try {
                    Loan l = Loan.fromCsvLine(line);
                    if (l != null) list.add(l);
                } catch (Exception ex) {
                    // Bozuk satir tespit edildi, sadece konsola yaz, listeye ekleme
                    System.out.println("Bozuk satir: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Loan dosyasi okunamadi: " + e.getMessage());
        }
        return list;
    }

    /** Tum odunc kayitlarini dosyaya yazar. */
    public static void saveAll(List<Loan> loans) {
        new File("data").mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Loan l : loans) {
                bw.write(l.toCsvLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Loan dosyasi yazilamadi: " + e.getMessage());
        }
    }

    /** Bir sonraki odunc kaydi icin id uretir. */
    public static int nextId(List<Loan> loans) {
        int max = 0;
        for (Loan l : loans) {
            if (l.getId() > max) max = l.getId();
        }
        return max + 1;
    }
}
