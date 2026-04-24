package data;

import model.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Kitap dosyasini okuma/yazma islemleri
public class BookData {

    // Dosya yolu
    private static final String FILE = "data/books.txt";

    // Tum kitaplari dosyadan oku
    public static List<Book> loadAll() {
        List<Book> list = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) {
            // Dosya yoksa bos liste don
            return list;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Book b = Book.fromCsvLine(line);
                if (b != null) list.add(b);
            }
        } catch (IOException e) {
            System.out.println("Kitap dosyasi okunamadi: " + e.getMessage());
        }
        return list;
    }

    // Tum kitaplari dosyaya yaz (uzerine yaz)
    public static void saveAll(List<Book> books) {
        // data klasoru yoksa olustur
        new File("data").mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Book b : books) {
                bw.write(b.toCsvLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Kitap dosyasi yazilamadi: " + e.getMessage());
        }
    }

    // Yeni id uretmek icin son id + 1
    public static int nextId(List<Book> books) {
        int max = 0;
        for (Book b : books) {
            if (b.getId() > max) max = b.getId();
        }
        return max + 1;
    }
}
