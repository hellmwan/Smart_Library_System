package data;

import model.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bu sinif "data katmani"nin bir parcasidir. UI veya is mantigi katmani
 * dosya islemleri icin direkt File/IO kodu yazmaz; bu sinif uzerinden gider. 
 * Boylece dosyadan veritabanina gectigimizde sadece bu sinifi degistirmek yeterli olur.
 * Veriler "data/books.txt" dosyasinda her satir bir kitap olacak sekilde tutulur (Book.toCsvLine ve Book.fromCsvLine metotlariyla).
 */
public class BookData {

    /**
     * Kullanilacak dosya yolu. Static cunku tum sinif paylasiyor.
     * Test sirasinda setFilePath ile degistirilebilir, boylece testler gercek veriyi bozmaz.
     */
    private static String FILE = "data/books.txt";

    /** Test sirasinda dosya yolunu degistirmek icin. */
    public static void setFilePath(String path) {
        FILE = path;
    }


    /**
     * Dosyadaki tum kitaplari okur ve listeyle doner.
     *
     * Adimlar:
     *  1. Dosya yoksa bos liste don
     *  2. Dosyayi satir satir oku
     *  3. Her satiri Book.fromCsvLine ile Book nesnesine cevir
     *  4. Hata olursa konsola yaz, programi durdurma
     */
    public static List<Book> loadAll() {
        List<Book> list = new ArrayList<>();
        File f = new File(FILE);

        // Ilk acilis: dosya yok demektir, bos liste donmek dogru
        if (!f.exists()) {
            return list;
        }

        // try-with-resources -> okuyucu otomatik kapansin
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Bos satirlari atla
                if (line.trim().isEmpty()) continue;
                Book b = Book.fromCsvLine(line);
                if (b != null) list.add(b);
            }
        } catch (IOException e) {
            // Hata olursa programi cokmemek icin sadece yazi yaz
            System.out.println("Kitap dosyasi okunamadi: " + e.getMessage());
        }
        return list;
    }


    /**
     * Tum kitap listesini dosyaya yazar.
     * Mevcut dosya silinir ve yenisi olusur. 
     * Bu yuzden cagirmadan once BUTUN kitaplarin listesinin elinizde olmasi gerekir.
     */
    public static void saveAll(List<Book> books) {
        // data klasoru yoksa olustur
        new File("data").mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Book b : books) {
                bw.write(b.toCsvLine());
                bw.newLine(); // her kitabi ayri satira
            }
        } catch (IOException e) {
            System.out.println("Kitap dosyasi yazilamadi: " + e.getMessage());
        }
    }


    /**
     * Yeni eklenecek kitap icin bir sonraki kullanilabilir id'yi bulur.
     * Mevcut en buyuk id'nin bir fazlasini doner. Liste bossa 1 doner.
     */
    public static int nextId(List<Book> books) {
        int max = 0;
        for (Book b : books) {
            if (b.getId() > max) max = b.getId();
        }
        return max + 1;
    }
}
