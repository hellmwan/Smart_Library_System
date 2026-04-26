import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import business.MemberManager;
import data.MemberData;

/**
 * MemberManagerTest - MemberManager is mantigi testleri.
 *
 * Email format kontrolu ve uye listesinin null olmadigi dogrulanir.
 * Test verisi icin "data/test_members.txt" hedeflenir.
 */
class MemberManagerTest {

    /**
     * Test 1: Email dogrulama dogru calisiyor mu?
     * "test@mail.com" -> gecerli olmali
     * "wrong"         -> gecersiz olmali
     */
    @Test
    void testEmailValidation() {
        MemberData.setFilePath("data/test_members.txt");

        MemberManager manager = new MemberManager();

        // Dogru email -> true beklenir
        assertTrue(manager.isValidEmail("test@mail.com"));
        // Yanlis email -> false beklenir
        assertFalse(manager.isValidEmail("wrong"));
    }


    /**
     * Test 2: getAllMembers null donmemeli (en azindan bos liste).
     */
    @Test
    void testGetMembers() {
        MemberData.setFilePath("data/test_members.txt");

        MemberManager manager = new MemberManager();

        assertNotNull(manager.getAllMembers());
    }
}
