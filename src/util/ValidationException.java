package util;

/**
 * Validator sinifi yanlis bir veri gordugunde (ornegin gecersiz e-mail,
 * bos isim vs.) bu istisnayi firlatir. UI tarafinda bunu yakalayip
 * kullaniciya guzel bir hata mesaji gosteriyoruz.
 * RuntimeException'dan kalitti, yani "checked" degil -> her metoda
 * "throws" yazmak zorunda degiliz, daha rahat kullanim saglar.
 */
public class ValidationException extends RuntimeException {

    /**
     * Hata mesaji ile yeni bir istisna olusturur.
     * @param message kullaniciya gosterilecek hata aciklamasi
     */
    public ValidationException(String message) {
        super(message);
    }
}
