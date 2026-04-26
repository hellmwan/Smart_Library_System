package data;

import model.Member;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MemberData - uyeleri diskte text dosyasi olarak okuyup/yazan sinif.
 *
 * BookData ile ayni mantikla calisir. Tek fark: uyeler "data/members.txt" dosyasinda tutulur ve id'ler 100'den baslar
 */
public class MemberData {

    /** Uyelerin saklandigi dosyanin yolu. Test icin setFilePath ile degistirilebilir. */
    private static String FILE = "data/members.txt";

    /** Test sirasinda dosya yolunu degistirmek icin kullanilir. */
    public static void setFilePath(String path) {
        FILE = path;
    }

    /**
     * Tum uyeleri dosyadan okur ve liste olarak doner.
     * Dosya yoksa bos liste doner (programi cokme).
     */
    public static List<Member> loadAll() {
        List<Member> list = new ArrayList<>();
        File f = new File(FILE);
        // Ilk acilis -> dosya yok -> bos liste don
        if (!f.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Bos satirlari atla
                if (line.trim().isEmpty()) continue;
                Member m = Member.fromCsvLine(line);
                if (m != null) list.add(m);
            }
        } catch (IOException e) {
            System.out.println("Uye dosyasi okunamadi: " + e.getMessage());
        }
        return list;
    }

    /**
     * Tum uye listesini dosyaya yazar (uzerine yazar).
     * data/ klasoru yoksa once olusturur.
     */
    public static void saveAll(List<Member> members) {
        new File("data").mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Member m : members) {
                bw.write(m.toCsvLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Uye dosyasi yazilamadi: " + e.getMessage());
        }
    }

    /**
     * Yeni eklenecek uye icin bir sonraki id'yi bulur.
     *
     * NOT: Burada baslangic 100, BookData'da 0. 
     * Boylece uye id'leri 101, 102, 103... seklinde gider; karisiklik olmaz.
     */
    public static int nextId(List<Member> members) {
        int max = 100; // <- baslangic 100, ilk uye 101 olur
        for (Member m : members) {
            if (m.getId() > max) max = m.getId();
        }
        return max + 1;
    }
}
