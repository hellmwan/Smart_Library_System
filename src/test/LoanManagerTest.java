import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import business.BookManager;
import business.LoanManager;
import data.BookData;
import data.LoanData;

/**
 * LoanManagerTest - LoanManager is mantigi testleri.
 *
 * Bu testler de gercek veriyi bozmamak icin test_*.txt dosyalarini kullanir.
 * Yine yuzeysel (smoke test) seviyesinde dogrulama yapar.
 */
class LoanManagerTest {

    /**
     * Test 1: LoanManager olusturulup getAllLoans cagirildiginda null donmemeli.
     */
    @Test
    void testLoanFlow() {
        // Hem book hem loan icin test dosyalarini ayarla
        BookData.setFilePath("data/test_books.txt");
        LoanData.setFilePath("data/test_loans.txt");

        // BookManager oncelikle olusturulmali (LoanManager bunu bekler)
        BookManager bm = new BookManager();
        LoanManager lm = new LoanManager(bm);

        // Beklenti: liste null degil (bos olabilir)
        assertNotNull(lm.getAllLoans());
    }


    /**
     * Test 2: getOverdueLoans null donmemeli.
     */
    @Test
    void testOverdue() {
        BookData.setFilePath("data/test_books.txt");
        LoanData.setFilePath("data/test_loans.txt");

        BookManager bm = new BookManager();
        LoanManager lm = new LoanManager(bm);

        assertNotNull(lm.getOverdueLoans());
    }
}
