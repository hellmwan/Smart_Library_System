package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Simple password hashing utility.
 * <p>
 * Uses SHA-256 over the UTF-8 bytes of the plaintext password and
 * returns a lowercase hex string. The default admin account in
 * {@code schema.sql} is also stored as a SHA-256 hash, so this class
 * is compatible with the seeded row out of the box.
 * <p>
 * <b>Note for the report:</b> For a production system you would use
 * a password-specific algorithm such as bcrypt or Argon2 with a
 * per-user salt, because SHA-256 is very fast and therefore easy to
 * brute-force with a pre-computed rainbow table. SHA-256 is chosen
 * here as a deliberate simplification that keeps the scope small and
 * avoids pulling in a third-party dependency.
 */
public final class PasswordHasher {

    private static final String ALGORITHM = "SHA-256";

    private PasswordHasher() { /* no instances */ }

    /** Returns the 64-character lowercase hex SHA-256 hash of {@code plain}. */
    public static String hash(String plain) {
        if (plain == null) throw new IllegalArgumentException("password cannot be null");
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] digest = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is in every standard JRE — this should never happen.
            throw new IllegalStateException("SHA-256 not available in this JRE", e);
        }
    }

    /**
     * Compares a plaintext password against a stored hash in constant time.
     * Constant-time comparison prevents timing-based attacks in theory;
     * in practice the difference is negligible for local desktop use, but
     * it is a one-line win so we use it.
     */
    public static boolean verify(String plain, String storedHash) {
        if (plain == null || storedHash == null) return false;
        String candidate = hash(plain);
        return constantTimeEquals(candidate, storedHash);
    }

    // ----------------------------------------------------------------
    // internals
    // ----------------------------------------------------------------

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            diff |= a.charAt(i) ^ b.charAt(i);
        }
        return diff == 0;
    }
}
