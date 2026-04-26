package util;

import java.time.LocalDate;
import java.time.Year;
import java.util.regex.Pattern;

/**
 * Bu sinif e-mail, telefon, ISBN, kullanici adi gibi alanlarin formatini
 * kontrol eder. Her alan icin iki tip metot vardir:
 *  - isValidXxx(...)    -> sadece true/false doner (sessiz kontrol)
 *  - requireValidXxx(...) -> hatalisa ValidationException firlatir (zorunlu kontrol)
 * UI tarafinda formdan gelen veriyi kaydetmeden once requireValidXxx
 * metotlari ile kontrol ediyoruz. Hata olursa try/catch ile yakalayip kullaniciya mesaj gosteriyoruz.
 */
public final class Validator {

    // ------------------  Regex Pattern ------------------


    /** E-mail formatini dogrular: harf/sayi + @ + alan + .com gibi uzanti */
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /** ISBN: toplam 10 ya da 13 rakam icermeli, arada tire olabilir */
    private static final Pattern ISBN =
            Pattern.compile("^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$");

    /** Telefon: opsiyonel + ile baslar, sonra tam 11 rakam (Turkiye formati 05551234567) */
    private static final Pattern PHONE =
            Pattern.compile("^\\+?[0-9]{11}$");

    /** Kullanici adi: 3-30 karakter, sadece harf/sayi/_/./- */
    private static final Pattern USERNAME =
            Pattern.compile("^[A-Za-z0-9_.\\-]{3,30}$");

    private Validator() {} // nesne uretilmesin


    // ----------------- GENEL kontroller ------------------

    /** String null mu, bos mu, sadece bosluk mu? */
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Bir alan bossa hata firlatir.
     * @param value kontrol edilecek deger
     * @param field hata mesajinda yazilacak alan adi (orn: "Isim")
     */
    public static void requireNonBlank(String value, String field) {
        if (isBlank(value)) {
            throw new ValidationException(field + " boş olamaz");
        }
    }


    // -------------- E-MAIL kontrolu -----------------


    public static boolean isValidEmail(String email) {
        return email != null && EMAIL.matcher(email.trim()).matches();
    }

    public static void requireValidEmail(String email) {
        // Once bos olmamali
        requireNonBlank(email, "Email");
        // Sonra format dogru olmali
        if (!isValidEmail(email)) {
            throw new ValidationException("Geçersiz email formatı");
        }
    }



    //  --------------- TELEFON kontrolu -----------
 
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        // Bosluklari sil, sonra regex ile kontrol et
        String cleaned = phone.replaceAll("\\s+", "");
        return PHONE.matcher(cleaned).matches();
    }

    public static void requireValidPhone(String phone) {
        requireNonBlank(phone, "Telefon");
        if (!isValidPhone(phone)) {
            throw new ValidationException("Geçersiz telefon numarası(0 ile başlayınız)");
        }
    }



    //  --------------- ISBN kontrolu ----------------


    public static boolean isValidIsbn(String isbn) {
        if (isbn == null) return false;
        // Bosluk ve tireleri sil, sonra regex ile dogrula
        String cleaned = isbn.replaceAll("[\\s-]", "");
        return ISBN.matcher(cleaned).matches();
    }

    public static void requireValidIsbn(String isbn) {
        requireNonBlank(isbn, "ISBN");
        if (!isValidIsbn(isbn)) {
            throw new ValidationException("Geçersiz ISBN \n (örnek:978-1-56619-909-4)");
        }
    }

    /**
     * ISBN'i kanonik forma cevir (boslu/tire silinmis, buyuk harfli).
     * Karsilastirma icin kullanisli (ayni ISBN'in farkli yazimlarini
     * tek bir bicime indirgemek icin).
     */
    public static String normalizeIsbn(String isbn) {
        return isbn == null ? null
                : isbn.replaceAll("[\\s-]", "").toUpperCase();
    }



    // ------------- KULLANICI ADI kontrolu --------------

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME.matcher(username.trim()).matches();
    }

    public static void requireValidUsername(String username) {
        requireNonBlank(username, "Username");
        if (!isValidUsername(username)) {
            throw new ValidationException("Geçersiz kullanıcı adı");
        }
    }


    
    // --------- SIFRE kontrolu ------------
    
    /** Sifre en az 6 karakter olmali. */
    public static void requireStrongPassword(String password) {
        requireNonBlank(password, "Şifre");
        if (password.length() < 6) {
            throw new ValidationException("Şifre en az 6 karakter olmalı");
        }
    }


    //  --------------- SAYI ve TARIH kontrolleri ----------------


    /** Sayi 0'dan buyuk olmali. */
    public static void requirePositive(int value, String field) {
        if (value <= 0) {
            throw new ValidationException(field + " 0'dan büyük olmalı");
        }
    }

    /** Sayi negatif olmamali. */
    public static void requireNonNegative(int value, String field) {
        if (value < 0) {
            throw new ValidationException(field + " negatif olamaz");
        }
    }

    /**
     * Yil makul araliklarda olmali.
     * En eski yil 1440 (Gutenberg matbaasi yili).
     * En yeni yil bu yil olabilir, gelecekteki yillar gecersiz.
     */
    public static void requireReasonableYear(Integer year) {
        if (year == null) return; // null ise gecerli kabul et (opsiyonel alan)
        int current = Year.now().getValue();
        if (year < 1440 || year > current) {
            throw new ValidationException("Yıl geçersiz");
        }
    }

    /**
     * Mevcut kopya sayisi toplamdan fazla olamaz.
     * Iki sayi negatif olmamali.
     */
    public static void requireValidCopyCounts(int total, int available) {
        requireNonNegative(total, "Toplam");
        requireNonNegative(available, "Mevcut");
        if (available > total) {
            throw new ValidationException("Mevcut > Toplam olamaz");
        }
    }

    /**
     * Teslim tarihi odunc tarihinden sonra olmali.
     * Iki tarih de bos olmamali.
     */
    public static void requireDueAfterLoan(LocalDate loan, LocalDate due) {
        if (loan == null || due == null) {
            throw new ValidationException("Tarih gerekli");
        }
        if (!due.isAfter(loan)) {
            throw new ValidationException("Teslim tarihi hatalı");
        }
    }

    /** Tarih bugunden sonra olmamali (gelecekte degil). */
    public static void requireNotInFuture(LocalDate date, String field) {
        if (date == null) return; // null ise atla
        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException(field + " gelecekte olamaz");
        }
    }
}
