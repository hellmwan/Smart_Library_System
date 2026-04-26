import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import data.BookData;
import model.Book;

import java.util.List;

/**
 * BookDataTest - BookData veri katmani testleri.
 *
 * Bu testler dosyaya/dosyadan okuma yazma davranisini dogrular.
 * Her test once setFilePath ile gercek "data/books.txt" yerine
 * "data/test_books.txt" dosyasini kullanmaya yonlendirir; boylece
 * gercek veriyi bozmamis oluruz.
 *
 * JUnit 5 (Jupiter) kullaniyoruz.
 */
class BookDataTest {

    /**
     * Test 1: nextId metodunun en az 1 dondugunu kontrol et.
     * Liste bossa nextId 1, doluysa max+1 olmali.
     */
    @Test
    void testNextId() {
        // Test dosyasini kullan
        BookData.setFilePath("data/test_books.txt");

        List<Book> books = BookData.loadAll();
        int nextId = BookData.nextId(books);

        // Beklenti: nextId her zaman 1 veya daha buyuk olmali
        assertTrue(nextId >= 1);
    }


    /**
     * Test 2: Dosyadan yukleme islemi en azindan null donmemeli.
     * Dosya yoksa bos liste donmeli (null degil).
     */
    @Test
    void testSaveAndLoad() {
        BookData.setFilePath("data/test_books.txt");

        List<Book> loaded = BookData.loadAll();
        // null OLMAMALI -> bos liste OK
        assertNotNull(loaded);
    }


    /**
     * Test 3: Book -> CSV -> Book tur tur cevrim dogru calisiyor mu?
     * toCsvLine ile string yaz, fromCsvLine ile geri oku, ayni veri olmali.
     */
    @Test
    void testCsvConversion() {
        BookData.setFilePath("data/test_books.txt");

        // Bir test kitabi olustur
        Book b = new Book(1, "A", "X", "111");

        // Once string'e cevir
        String line = b.toCsvLine();
        // Sonra string'den geri okur
        Book parsed = Book.fromCsvLine(line);

        // Beklenti: parsed null olmamali ve title "A" olmali
        assertNotNull(parsed);
        assertEquals("A", parsed.getTitle());
    }


    /**
     * Test 4: Bozuk (eksik alanli) bir CSV satiri null donmeli.
     * "1;A;X" -> sadece 3 alan, en az 5 olmali.
     */
    @Test
    void testFromCsvInvalidLine() {
        BookData.setFilePath("data/test_books.txt");

        String invalid = "1;A;X"; // eksik
        Book parsed = Book.fromCsvLine(invalid);

        // Beklenti: bozuk satir -> null
        assertNull(parsed);
    }
}
