package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Sifreleri dosyada duz metin olarak tutmak guvenlik acigi olusturur.
 * Bu yuzden sifreleri SHA-256 algoritmasi ile hashleyip oyle saklariz.
 * Hashleme tek yonludur: hash'ten geri sifreyi cikaramazsiniz.
 * Ana kullanım:
 *   String hash = PasswordHasher.hash("benimSifrem");      // sifrele
 *   boolean dogruMu = PasswordHasher.verify("benimSifrem", hash);  // dogrula
 */
public final class PasswordHasher {

    // Kullanilacak hash algoritmasi
    private static final String ALGORITHM = "SHA-256";

    private PasswordHasher() {  } // nesne uretilmesin

    /**
     * Verilen duz metin sifreyi SHA-256 ile hashler ve hex (16'lik tabanda) String doner.
     * @param plain duz metin sifre (kullanicinin yazdigi)
     * @return 64 karakter uzunlugunda hex hash
     */
    public static String hash(String plain) {
        if (plain == null) throw new IllegalArgumentException("password cannot be null");
        try {
            // SHA-256 algoritmasini hazirla
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);

            // Sifreyi UTF-8 byte dizisine cevirip hashle
            byte[] digest = md.digest(plain.getBytes(StandardCharsets.UTF_8));

            // Byte dizisini insan okuyabilen hex'e cevir
            return toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            // Bu cok nadir olur ama Java surumu SHA-256 destekemiyorsa kapan
            throw new IllegalStateException("SHA-256 not available in this JRE", e);
        }
    }

    /**
     * Bir duz metin sifrenin daha onceden saklanmis hash ile eslesip eslesmedigini kontrol eder.
     *
     * @param plain kullanicinin yeni girdigi sifre
     * @param storedHash dosyada/db'de saklanan hash
     * @return eslesirse true
     */
    public static boolean verify(String plain, String storedHash) {
        if (plain == null || storedHash == null) return false;
        // Yeni sifreyi de ayni sekilde hashle
        String candidate = hash(plain);
        // Sabit zamanli karsilastirma (timing attack onlemi)
        return constantTimeEquals(candidate, storedHash);
    }


    /**
     * Byte dizisini iki haneli hex (16'lik tabanda) String'e cevirir.
     * Ornek: byte 0xAB -> "ab"
     */
    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // %02x = en az 2 karakter, hex kucuk harf, b'yi 0-255 arasi olarak yaz
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    /**
     * Iki String'i sabit zamanda karsilastirir.
     * Neden boyle: Normal equals erken cikar (ilk farkli karakterde durur),
     * bu da bir saldirganin sifre hash'ini karakter karakter tahmin etmesini kolaylastirabilir. 
     * Biz tum karakterleri donup XOR farkini biriktiririz, sonunda fark 0 ise eslesir.
     */
    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            // XOR -> karakterler aynisa 0, farkliysa 0'dan farkli
            diff |= a.charAt(i) ^ b.charAt(i);
        }
        return diff == 0;
    }
}
