package model;

// Kitap bilgilerini tutan sinif
public class Book {

    private int id;
    private String title;
    private String author;
    private String isbn;
    private String status; // "Available" veya "Loaned"

    public Book(int id, String title, String author, String isbn, String status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
    }

    // Yeni kitap eklerken default status "Available" olsun
    public Book(int id, String title, String author, String isbn) {
        this(id, title, author, isbn, "Available");
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getStatus() { return status; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setStatus(String status) { this.status = status; }

    // Dosyaya yazmak icin satira cevir (CSV format)
    public String toCsvLine() {
        return id + ";" + title + ";" + author + ";" + isbn + ";" + status;
    }

    // Dosyadan okurken satiri Book nesnesine cevir
    public static Book fromCsvLine(String line) {
        String[] parts = line.split(";");
        // 5 parca olmali, eksikse null don
        if (parts.length < 5) return null;
        int id = Integer.parseInt(parts[0]);
        return new Book(id, parts[1], parts[2], parts[3], parts[4]);
    }
}
