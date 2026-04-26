import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import util.Validator;
import util.ValidationException;

/**
 * ValidatorTest - Validator yardimci sinifin testleri.
 *
 * Bu testler en olgun olan testler. Validator metotlari girdi
 * dogrulamasi yapar; hem dogru girdi (assertTrue/doesNotThrow)
 * hem hatali girdi (assertFalse/Throws) durumlari kontrol edilir.
 */
class ValidatorTest {

    /**
     * Email dogrulama: dogru email -> true, hatali -> false.
     */
    @Test
    void testValidEmail() {
        assertTrue(Validator.isValidEmail("test@mail.com"));
        assertFalse(Validator.isValidEmail("wrong-email")); // @ yok
    }


    /**
     * Telefon dogrulama: tam 11 rakam olmali (Turkiye formati).
     */
    @Test
    void testValidPhone() {
        assertTrue(Validator.isValidPhone("05551234567")); // 11 rakam, OK
        assertFalse(Validator.isValidPhone("123"));        // 3 rakam, kisa
    }


    /**
     * ISBN dogrulama: 10 veya 13 rakam icermeli.
     */
    @Test
    void testValidIsbn() {
        assertTrue(Validator.isValidIsbn("9781566199094")); // 13 rakam, OK
        assertFalse(Validator.isValidIsbn("abc"));          // rakam degil
    }


    /**
     * Kullanici adi dogrulama: harf/sayi/_/./- ile 3-30 karakter.
     */
    @Test
    void testUsername() {
        assertTrue(Validator.isValidUsername("user_123")); // OK
        assertFalse(Validator.isValidUsername("!@#"));     // ozel karakter
    }


    /**
     * Sifre kontrolu: 6+ karakter ise OK, kisa ise istisna firlat.
     *
     * assertDoesNotThrow -> code blogu HIC istisna firlatMAMALI
     * assertThrows       -> code blogu BU TIPTE istisna firlatMALI
     */
    @Test
    void testStrongPassword() {
        // 6 karakter -> kabul
        assertDoesNotThrow(() -> Validator.requireStrongPassword("123456"));

        // 3 karakter -> ValidationException firlatmali
        assertThrows(ValidationException.class, () ->
                Validator.requireStrongPassword("123"));
    }


    /**
     * Bos string verilirse requireNonBlank istisna firlatmali.
     */
    @Test
    void testRequireNonBlank() {
        assertThrows(ValidationException.class, () ->
                Validator.requireNonBlank("", "Test"));
    }


    /**
     * requirePositive: pozitif sayi OK, 0 veya negatif istisna.
     */
    @Test
    void testPositiveNumber() {
        // 5 pozitif -> OK
        assertDoesNotThrow(() -> Validator.requirePositive(5, "Value"));

        // 0 pozitif degil -> istisna
        assertThrows(ValidationException.class, () ->
                Validator.requirePositive(0, "Value"));
    }
}
