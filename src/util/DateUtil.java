package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Date formatting / parsing helpers used by the UI.
 * <p>
 * All LocalDate values stored in the database are in ISO format
 * (yyyy-MM-dd), because that is what {@code LocalDate.toString()}
 * produces and what {@code LocalDate.parse()} expects. For user-facing
 * display we prefer day-month-year ordering, which is what everyone
 * outside North America reads.
 */
public final class DateUtil {

    /** How dates are shown in tables and labels: "18/04/2026". */
    public static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Longer form used on reports and receipts: "18 April 2026". */
    public static final DateTimeFormatter LONG_FORMAT =
            DateTimeFormatter.ofPattern("dd MMMM yyyy");

    /** ISO form used for storage and round-tripping: "2026-04-18". */
    public static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private DateUtil() { /* no instances */ }

    // ----------------------------------------------------------------
    // formatting
    // ----------------------------------------------------------------

    /** Null-safe; returns an empty string for null. */
    public static String formatDisplay(LocalDate date) {
        return date == null ? "" : date.format(DISPLAY_FORMAT);
    }

    public static String formatLong(LocalDate date) {
        return date == null ? "" : date.format(LONG_FORMAT);
    }

    public static String formatIso(LocalDate date) {
        return date == null ? "" : date.format(ISO_FORMAT);
    }

    // ----------------------------------------------------------------
    // parsing — lenient about which of the common formats the user typed
    // ----------------------------------------------------------------

    /**
     * Parses a date from user input, accepting both "dd/MM/yyyy" and
     * "yyyy-MM-dd". Returns {@code null} for blank input. Throws
     * {@link ValidationException} with a friendly message if the input
     * is present but unparseable.
     */
    public static LocalDate parseFlexible(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        String cleaned = input.trim();

        // 1) day-first display format
        try {
            return LocalDate.parse(cleaned, DISPLAY_FORMAT);
        } catch (DateTimeParseException ignored) { /* fall through */ }

        // 2) ISO format (what the DB stores and what JDatePicker-style
        //    components typically produce)
        try {
            return LocalDate.parse(cleaned, ISO_FORMAT);
        } catch (DateTimeParseException ignored) { /* fall through */ }

        throw new ValidationException(
                "Date '" + input + "' is not valid. Expected dd/MM/yyyy or yyyy-MM-dd.");
    }

    // ----------------------------------------------------------------
    // arithmetic
    // ----------------------------------------------------------------

    /** Positive if {@code to} is after {@code from}. */
    public static long daysBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null) return 0;
        return ChronoUnit.DAYS.between(from, to);
    }

    public static LocalDate addDays(LocalDate date, int days) {
        return date == null ? null : date.plusDays(days);
    }

    public static boolean isInPast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }
}
