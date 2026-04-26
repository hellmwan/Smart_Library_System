package model;

/**
 * Bu sinif kutuphanedeki bir kitabin bilgilerini tutar:
 * id, baslik, yazar, ISBN ve durum (Available / Loaned).
 * Ayrica kitabi CSV satiri haline cevirip dosyaya yazma ve
 * dosyadan okurken CSV satirindan tekrar Book nesnesi
 * olusturma islemleri de bu sinif icinde yapilir.
 */
public class Book {

    // --- Alanlar (kitabin ozellikleri) ---

    private int id;             // Kitabin benzersiz numarasi (1, 2, 3, ...)
    private String title;       // Kitabin adi (orn: "Suc ve Ceza")
    private String author;      // Yazarin adi (orn: "Dostoyevski")
    private String isbn;        // ISBN kodu (orn: "978-1-56619-909-4")
    private String status;      // "Available" = raflarda, "Loaned" = odunc verilmis

    /**
     * Tum alanlari alan ana yapici metot.
     * Genelde dosyadan kitap okurken bu kullanilir cunku status'u da biliyoruz.
     */
    public Book(int id, String title, String author, String isbn, String status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
    }

    /**
     * Status almayan kisa yapici.
     * Yeni eklenen kitap her zaman "Available" (raflarda) olarak baslar.
     * Bu yuzden status'u biz manuel vermiyoruz, varsayilan atiyoruz.
     */
    public Book(int id, String title, String author, String isbn) {
        // this(...) -> ayni siniftaki diger yapiciyi cagirir
        this(id, title, author, isbn, "Available");
    }

    // --- Getter metotlari (alanlari okumak icin) ---
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getStatus() { return status; }

    // --- Setter metotlari (alanlari degistirmek icin) ---
    // id setter'i YOK cunku id verildikten sonra degismemeli.
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setStatus(String status) { this.status = status; }


    /**
     * Bu kitabi tek satirlik CSV metnine cevirir.
     * Ornek cikti: "1;Suc ve Ceza;Dostoyevski;9789750718533;Available"
     * Alanlari noktali virgul (;) ile ayiriyoruz cunku kitap adi veya
     * yazarda virgul (,) gecebilir, bu yuzden virgul yerine ; tercih ettik.
     */
    public String toCsvLine() {
        return id + ";" + title + ";" + author + ";" + isbn + ";" + status;
    }


    /**
     * Dosyadan okunan bir CSV satirini Book nesnesine ceviren statik metot.
     * @param line dosyadan okunan satir, orn: "1;Suc ve Ceza;Dostoyevski;9789750718533;Available"
     * @return olusturulan Book nesnesi, satir bozuksa null doner
     */
    public static Book fromCsvLine(String line) {
        // Satiri ; karakterine gore parcalara ayir
        String[] parts = line.split(";");

        // En az 5 parca olmali (id, title, author, isbn, status)
        // Daha az ise satir bozuk demektir, null doneriz.
        if (parts.length < 5) return null;

        // Ilk parca id, onu sayiya cevirmemiz lazim
        int id = Integer.parseInt(parts[0]);

        // Diger parcalari sirayla yapiciya gondererek yeni Book olustur
        return new Book(id, parts[1], parts[2], parts[3], parts[4]);
    }
}
