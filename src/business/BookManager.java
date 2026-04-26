package business;

import data.BookData;
import model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * BookManager - kitaplarin is mantigini yoneten sinif.
 *
 * Bu sinif "business katmani"nin parcasidir. UI dogrudan dosyaya
 * yazmaz veya okumaz; bunun yerine BookManager'a "kitabi ekle, sil,
 * ara, guncelle" gibi komutlar verir. Buradaki metotlar:
 *  - Veri katmanindan (BookData) listeyi alir
 *  - Liste uzerinde islem yapar
 *  - Yine BookData'ya kaydetmesini soyler
 *
 * Boylece UI temiz kalir, dosya islemleri tek bir yerde toplanir.
 */
public class BookManager {
    /** Bellekteki kitap listesi. Dosyadan okunur, islem sonrasi geri yazilir. */
    private List<Book> books;

    public BookManager() {
        // Constructor'da dosyadan tum kitaplari yukler.
        this.books = BookData.loadAll();
    }
    /** Bellekteki tum kitaplari doner. */
    public List<Book> getAllBooks() {
        return books;
    }

    /**
     * Yeni kitap ekler.
     *
     * Kontroller:
     *  1. title, author, isbn alanlarindan hicbiri bos olamaz
     *  2. Ayni ISBN'le baska kitap varsa ekleme (cunku ISBN benzersiz olmali)
     *
     * @return ekleme basarili olduysa true, aksi halde false
     */
    public boolean addBook(String title, String author, String isbn) {
        // Bos alan kontrolu
        if (title == null || title.trim().isEmpty()) return false;
        if (author == null || author.trim().isEmpty()) return false;
        if (isbn == null || isbn.trim().isEmpty()) return false;

        // Ayni ISBN ile baska kayit var mi?
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                return false;// varsa ekleme
            }
        }

        int id = BookData.nextId(books);
        Book b = new Book(id, title.trim(), author.trim(), isbn.trim());
        books.add(b);
        BookData.saveAll(books);
        return true;
    }

    /**
     * Var olan bir kitabi gunceller.
     *
     * Bos gelen alanlar guncellenmez (kullanici sadece bir alani degistirmek
     * isteyebilir, digerleri olduğu gibi kalir).
     *
     * @return kitap bulunup guncellendiyse true
     */
    public boolean updateBook(int id, String title, String author, String isbn) {
        for (Book b : books) {
            if (b.getId() == id) {
                // Sadece dolu olan alanlari guncelle
                if (!title.trim().isEmpty()) b.setTitle(title.trim());
                if (!author.trim().isEmpty()) b.setAuthor(author.trim());
                if (!isbn.trim().isEmpty()) b.setIsbn(isbn.trim());
                BookData.saveAll(books); // dosyaya yaz
                return true;
            }
        }
        return false; // o id'de kitap yok
    }

    /**
     * Bir kitabi siler.
     *
     * KURAL: Su anda odunc verilmis ("Loaned") bir kitap silinemez.
     * Once iade alinmasi gerekir.
     *
     * @return silindiyse true
     */
    public boolean deleteBook(int id) {
        Book toRemove = null;
        for (Book b : books) {
            if (b.getId() == id) {
                // Odunc verilmisse silmeyi reddet
                if (b.getStatus().equals("Loaned")) {
                    return false; 
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
        return false; // kitap bulunamadi
    }

    /**
     * Kitap arama.
     *
     * Verilen anahtar kelime kitap basliginda, yazarinda veya ISBN'inde
     * geciyorsa o kitap sonuca eklenir. Buyuk/kucuk harf duyarli degildir.
     * Arama bos verilirse tum kitaplar doner.
     */
    public List<Book> search(String keyword) {
        List<Book> result = new ArrayList<>();
        // Bos arama -> tum kitaplari don
        if (keyword == null || keyword.trim().isEmpty()) {
            return books;
        }
        // Karsilastirma icin kucuk harfe cevir
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

    /** Verilen id'ye sahip kitabi bulur. Yoksa null doner. */
    public Book findById(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    /**
     * Kitabin durumunu (status) gunceller.
     * LoanManager kitap odunc verildiginde "Loaned",
     * iade edildiginde "Available" yapmak icin kullanir.
     */
    public void updateStatus(int id, String newStatus) {
        for (Book b : books) {
            if (b.getId() == id) {
                b.setStatus(newStatus);
                BookData.saveAll(books);
                return;
            }
        }
    }

    /**
     * Sadece "Available" durumdaki (raflarda olan) kitaplari doner.
     * Issue/Return ekraninda combobox'i doldurmak icin kullanilir
     * (cunku zaten odunc verilmis kitabi tekrar odunc veremezsiniz).
     */
    public List<Book> getAvailableBooks() {
        List<Book> avail = new ArrayList<>();
        for (Book b : books) {
            if (b.getStatus().equals("Available")) avail.add(b);
        }
        return avail;
    }

    /**
     * Bellekteki listeyi dosyadan tekrar yukler.
     * Baska bir ekran kitabi guncellediyse (orn: Loan ekrani status
     * degistirdi) kullanicinin guncel veriyi gormesi icin cagrilir.
     */
    public void reload() {
        this.books = BookData.loadAll();
    }
}
