package model;

/**
 * Her uyenin id, ad-soyad, e-posta, telefon ve durum bilgisi tutulur.
 * Book sinifina cok benzer mantikla calisir: hem dosyaya yazma (toCsvLine) hem de dosyadan okuma (fromCsvLine) yontemleri vardir.
 */
public class Member {

    // --- Alanlar (uyenin ozellikleri) ---

    private int id;             // Uyenin benzersiz numarasi (101, 102, ... seklinde 100'den baslar)
    private String fullName;    // Uyenin ad-soyadi
    private String email;       // E-posta adresi (uniqe olmali, ayni mail ile iki uye olamaz)
    private String phone;       // Telefon numarasi
    private String status;      // "Active" = aktif uye, "Passive" = pasif uye

    /**
     * Tum alanlari alan ana yapici. Genelde dosyadan okurken kullanilir.
     */
    public Member(int id, String fullName, String email, String phone, String status) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    /**
     * Status almayan kisa yapici.
     * Yeni eklenen uye her zaman "Active" olarak baslar.
     */
    public Member(int id, String fullName, String email, String phone) {
        this(id, fullName, email, phone, "Active");
    }

    // --- Getter metotlari ---
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getStatus() { return status; }

    // --- Setter metotlari ---
    // id setter'i YOK -> id atandiktan sonra degisemez.
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setStatus(String status) { this.status = status; }

    /**
     * Uyeyi tek satirlik CSV metnine cevirir.
     * Ornek: "101;Ali Veli;ali@example.com;05551234567;Active"
     */
    public String toCsvLine() {
        return id + ";" + fullName + ";" + email + ";" + phone + ";" + status;
    }

    /**
     * CSV satirindan Member nesnesi olusturur.
     * @param line dosyadan okunan satir
     * @return olusan Member, satir bozuksa null
     */
    public static Member fromCsvLine(String line) {
        String[] parts = line.split(";");
        // En az 5 alan beklenir, az ise satir bozuk -> null don
        if (parts.length < 5) return null;
        int id = Integer.parseInt(parts[0]);
        return new Member(id, parts[1], parts[2], parts[3], parts[4]);
    }
}
