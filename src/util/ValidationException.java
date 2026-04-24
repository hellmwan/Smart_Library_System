package util;

/**
 * Unchecked exception thrown when user input fails validation.
 * <p>
 * The {@link #getMessage() message} is intentionally user-friendly:
 * the UI catches this exception and shows the message directly in
 * a {@code JOptionPane}, so messages should read like something a
 * user can act on ("E-mail address is not valid", not
 * "regex match failed at index 7").
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
