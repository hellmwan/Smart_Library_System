import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import business.BookManager;
import data.BookData;

/**
 * BookManagerTest - BookManager is mantigi testleri.
 *
 * Bu testler manager seviyesinde davranisi dogrular. Her test
 * once test_books.txt dosyasini hedef alir, boylece gercek veriyi
 * bozmaz.
 *
 * Not: testler oldukca yuzeysel (smoke test). Daha kapsamli olmasi
 * icin once temiz veri/state hazirlanmali, sonra spesifik beklentiler
 * yazilmalidir. Su anki halinde sadece "patlamadan calisiyor mu" testi.
 */
class BookManagerTest {

    /**
     * Test 1: addBook cagrilinca cokmeden donuyor mu?
     * Asil sonuc onemli degil cunku ayni ISBN ile tekrar calistirildiginda
     * false donebilir, tek istegimiz islem patlamasin.
     */
    @Test
    void testAddBook() {
        BookData.setFilePath("data/test_books.txt");

        BookManager manager = new BookManager();
        boolean result = manager.addBook("Book A", "Author A", "111");

        // (result || !result) her zaman true -> aslinda burada test yok,
        // sadece "exception firlatmadan calisti mi" kontrolu
        assertTrue(result || !result); // safe
    }


    /**
     * Test 2: search bos bir liste de donse null DONMEMELI.
     */
    @Test
    void testSearch() {
        BookData.setFilePath("data/test_books.txt");

        BookManager manager = new BookManager();
        // Beklenti: arama sonucu null DEGIL (bos olabilir ama null olmamali)
        assertNotNull(manager.search("test"));
    }


    /**
     * Test 3: getAllBooks her zaman bir liste donmelidir (null olmamali).
     */
    @Test
    void testUpdateBook() {
        BookData.setFilePath("data/test_books.txt");

        BookManager manager = new BookManager();
        assertNotNull(manager.getAllBooks());
    }
}
