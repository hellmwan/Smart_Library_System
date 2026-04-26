package model;

/**
 * Admin "Manage Personnel" ekranindan yeni personel ekleyebilir.
 * Eklenen personel kendi kullanici adi ve sifresi ile giris yapabilir.
 *
 * Not: Bu sinif su an LoginFrame icindeki String[] listesi ile birlikte kullaniliyor. 
 * Ileride LoginFrame'i bu nesneye gecirecegiz.
 */
public class Personnel {

    private String fullName;    // Personelin ad-soyadi
    private String username;    // Sisteme giris kullanici adi
    private String password;    // Sifre (su anlik duz metin)

    /**
     * Yeni personel olusturur.
     * @param fullName personelin ad-soyadi
     * @param username giris kullanici adi
     * @param password giris sifresi
     */
    public Personnel(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    // --- Getter metotlari ---
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
