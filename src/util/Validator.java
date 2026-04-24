package util;

import java.time.LocalDate;
import java.time.Year;
import java.util.regex.Pattern;

public final class Validator {

    // =========================
    // REGEX
    // =========================
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern ISBN =
            Pattern.compile("^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$");

    private static final Pattern PHONE =
            Pattern.compile("^\\+?[0-9]{11}$");

    private static final Pattern USERNAME =
            Pattern.compile("^[A-Za-z0-9_.\\-]{3,30}$");

    private Validator() {}

    // =========================
    // STRING
    // =========================
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static void requireNonBlank(String value, String field) {
        if (isBlank(value)) {
            throw new ValidationException(field + " boş olamaz");
        }
    }

    // =========================
    // EMAIL
    // =========================
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL.matcher(email.trim()).matches();
    }

    public static void requireValidEmail(String email) {
        requireNonBlank(email, "Email");

        if (!isValidEmail(email)) {
            throw new ValidationException("Geçersiz email formatı");
        }
    }

    // =========================
    // PHONE (ZORUNLU)
    // =========================
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;

        String cleaned = phone.replaceAll("\\s+", "");
        return PHONE.matcher(cleaned).matches();
    }

    public static void requireValidPhone(String phone) {
        requireNonBlank(phone, "Telefon");

        if (!isValidPhone(phone)) {
            throw new ValidationException("Geçersiz telefon numarası(0 ile başlayınız)");
        }
    }

    // =========================
    // ISBN
    // =========================
    public static boolean isValidIsbn(String isbn) {
        if (isbn == null) return false;

        String cleaned = isbn.replaceAll("[\\s-]", "");

        return ISBN.matcher(cleaned).matches();

    }

    public static void requireValidIsbn(String isbn) {
        requireNonBlank(isbn, "ISBN");

        if (!isValidIsbn(isbn)) {
            throw new ValidationException("Geçersiz ISBN \n (örnek:978-1-56619-909-4)");
        }
    }

    public static String normalizeIsbn(String isbn) {
        return isbn == null ? null
                : isbn.replaceAll("[\\s-]", "").toUpperCase();
    }

    // =========================
    // USERNAME
    // =========================
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME.matcher(username.trim()).matches();
    }

    public static void requireValidUsername(String username) {
        requireNonBlank(username, "Username");

        if (!isValidUsername(username)) {
            throw new ValidationException("Geçersiz kullanıcı adı");
        }
    }

    // =========================
    // PASSWORD
    // =========================
    public static void requireStrongPassword(String password) {
        requireNonBlank(password, "Şifre");

        if (password.length() < 6) {
            throw new ValidationException("Şifre en az 6 karakter olmalı");
        }
    }

    // =========================
    // NUMBERS
    // =========================
    public static void requirePositive(int value, String field) {
        if (value <= 0) {
            throw new ValidationException(field + " 0'dan büyük olmalı");
        }
    }

    public static void requireNonNegative(int value, String field) {
        if (value < 0) {
            throw new ValidationException(field + " negatif olamaz");
        }
    }

    public static void requireReasonableYear(Integer year) {
        if (year == null) return;

        int current = Year.now().getValue();

        if (year < 1440 || year > current) {
            throw new ValidationException("Yıl geçersiz");
        }
    }

    public static void requireValidCopyCounts(int total, int available) {
        requireNonNegative(total, "Toplam");
        requireNonNegative(available, "Mevcut");

        if (available > total) {
            throw new ValidationException("Mevcut > Toplam olamaz");
        }
    }

    // =========================
    // DATE
    // =========================
    public static void requireDueAfterLoan(LocalDate loan, LocalDate due) {
        if (loan == null || due == null) {
            throw new ValidationException("Tarih gerekli");
        }

        if (!due.isAfter(loan)) {
            throw new ValidationException("Teslim tarihi hatalı");
        }
    }

    public static void requireNotInFuture(LocalDate date, String field) {
        if (date == null) return;

        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException(field + " gelecekte olamaz");
        }
    }
}