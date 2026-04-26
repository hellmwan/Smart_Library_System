package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Bu sinif tarihlerle ugrasirken her seferinde ayni kodu yazmamak icin
 * yapildi. Tarih bicimleme (display, long, ISO) ve tarih hesaplari (gun farki, gun ekleme vb.) burada toplanir.
 * "final" anahtar kelimesi: Bu sinif kalitilamaz (extends edilmez).
 * Yapici "private" -> bu siniftan nesne uretilemez. Sadece statik metotlar
 * uzerinden kullanilir, mesela: DateUtil.formatDisplay(today)
 */
public final class DateUtil {

    /** Kullaniciya gosterilecek bicim: 24/04/2025 */
    public static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Uzun bicim: 24 April 2025 */
    public static final DateTimeFormatter LONG_FORMAT =
            DateTimeFormatter.ofPattern("dd MMMM yyyy");

    /** ISO standart bicim: 2025-04-24. Dosyaya yazarken bunu tercih ederiz. */
    public static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    /** Bu siniftan nesne uretilmesin diye yapici private. */
    private DateUtil() {  }


    // ------------------------------------------------------------------
    //  TARIH -> METIN cevirme metotlari (formatlama)
    // ------------------------------------------------------------------

    /** Tarihi "dd/MM/yyyy" formatinda String'e cevirir. null gelirse bos string doner. */
    public static String formatDisplay(LocalDate date) {
        return date == null ? "" : date.format(DISPLAY_FORMAT);
    }

    /** Tarihi "24 April 2025" gibi uzun yazi olarak ceviriri. */
    public static String formatLong(LocalDate date) {
        return date == null ? "" : date.format(LONG_FORMAT);
    }

    /** Tarihi "2025-04-24" ISO formatinda metin yapar. */
    public static String formatIso(LocalDate date) {
        return date == null ? "" : date.format(ISO_FORMAT);
    }


    //  METIN -> TARIH cevirme (parsing)
    /**
     * Esnek tarih parser'i.
     * Hem "24/04/2025" hem de "2025-04-24" formatini kabul eder.
     * Hicbiri uymuyorsa ValidationException firlatir.
     */
    public static LocalDate parseFlexible(String input) {
        // Bos input -> null don
        if (input == null || input.trim().isEmpty()) return null;
        String cleaned = input.trim();

        // 1. Once dd/MM/yyyy formatini dene
        try {
            return LocalDate.parse(cleaned, DISPLAY_FORMAT);
        } catch (DateTimeParseException ignored) { /* uymadi, bir sonrakini dene */ }

        // 2. Sonra ISO formatini dene (yyyy-MM-dd)
        try {
            return LocalDate.parse(cleaned, ISO_FORMAT);
        } catch (DateTimeParseException ignored) { /* uymadi, hata firlat */ }

        // Iki format da uymadi -> kullaniciya anlasilir hata mesaji ver
        throw new ValidationException(
                "Date '" + input + "' is not valid. Expected dd/MM/yyyy or yyyy-MM-dd.");
    }



    //  HESAPLAMA metotlari

    /** Iki tarih arasindaki gun farki. Birisi null ise 0 doner. */
    public static long daysBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null) return 0;
        return ChronoUnit.DAYS.between(from, to);
    }

    /** Tarihe belirtilen kadar gun ekler. null gelirse null doner. */
    public static LocalDate addDays(LocalDate date, int days) {
        return date == null ? null : date.plusDays(days);
    }

    /** Tarih bugunden once mi? (Gecmiste mi?) */
    public static boolean isInPast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }
}
