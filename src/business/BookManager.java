package business;

import data.BookData;
import model.Book;

import java.util.ArrayList;
import java.util.List;

// Kitap islemleri - ekle, sil, ara, guncelle
public class BookManager {

    private List<Book> books;

    public BookManager() {
        // Constructor'da dosyadan oku
        this.books = BookData.loadAll();
    }

    public List<Book> getAllBooks() {
        return books;
    }

    // Yeni kitap ekle (id otomatik verilir)
    public boolean addBook(String title, String author, String isbn) {
        // basit kontrol - bos olmamali
        if (title == null || title.trim().isEmpty()) return false;
        if (author == null || author.trim().isEmpty()) return false;
        if (isbn == null || isbn.trim().isEmpty()) return false;

        // ayni isbn varsa eklemeyelim
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                return false;
            }
        }

        int id = BookData.nextId(books);
        Book b = new Book(id, title.trim(), author.trim(), isbn.trim());
        books.add(b);
        BookData.saveAll(books);
        return true;
    }

    // Id'ye gore guncelle
    public boolean updateBook(int id, String title, String author, String isbn) {
        for (Book b : books) {
            if (b.getId() == id) {
                if (!title.trim().isEmpty()) b.setTitle(title.trim());
                if (!author.trim().isEmpty()) b.setAuthor(author.trim());
                if (!isbn.trim().isEmpty()) b.setIsbn(isbn.trim());
                BookData.saveAll(books);
                return true;
            }
        }
        return false;
    }

    // Id'ye gore sil. Eger kitap odunc verildiyse silmeyelim.
    public boolean deleteBook(int id) {
        Book toRemove = null;
        for (Book b : books) {
            if (b.getId() == id) {
                if (b.getStatus().equals("Loaned")) {
                    return false; // odunc verilmis kitap silinemez
                }
                toRemove = b;
                break;
            }
        }
        if (toRemove != null) {
            books.remove(toRemove);
            BookData.saveAll(books);
            return true;
        }
        return false;
    }

    // Basit arama: baslik veya yazarda kelime gecsin (kucuk-buyuk fark etmez)
    public List<Book> search(String keyword) {
        List<Book> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return books;
        }
        String key = keyword.toLowerCase();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(key)
                    || b.getAuthor().toLowerCase().contains(key)
                    || b.getIsbn().toLowerCase().contains(key)) {
                result.add(b);
            }
        }
        return result;
    }

    // Id'ye gore kitap bul (loan islemleri icin gerekli)
    public Book findById(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    // Status guncelle (issue/return icin)
    public void updateStatus(int id, String newStatus) {
        for (Book b : books) {
            if (b.getId() == id) {
                b.setStatus(newStatus);
                BookData.saveAll(books);
                return;
            }
        }
    }

    // Sadece odunc verilebilir olanlari getir
    public List<Book> getAvailableBooks() {
        List<Book> avail = new ArrayList<>();
        for (Book b : books) {
            if (b.getStatus().equals("Available")) avail.add(b);
        }
        return avail;
    }

    // Listeyi yenile (baska panelden veri degistirildiyse)
    public void reload() {
        this.books = BookData.loadAll();
    }
}
