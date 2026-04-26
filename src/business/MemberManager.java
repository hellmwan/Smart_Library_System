package business;

import data.MemberData;
import model.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/*
* BookManager'a cok benzer mantikla calisir: dosyadan uye listesini
 * yukler, ekleme/silme/arama islemlerini yapar ve degisikligi tekrar
 * dosyaya yazar.
 *
 * Ek olarak email format kontrolu icin bir regex desenini de tasir.
 */
public class MemberManager {
    /**
     * E-mail formatini dogrulayan regex deseni.
     *
     * Acilim:
     *  ^                       -> stringin basi
     *  [A-Za-z0-9._%+-]+       -> @ oncesi: harf/sayi/. _ % + -
     *  @                       -> @ isareti
     *  [A-Za-z0-9.-]+          -> alan adi
     *  \\.                     -> nokta
     *  [A-Za-z]{2,}            -> uzanti (en az 2 harf, orn: com, net)
     *  $                       -> stringin sonu
     */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    /** Bellekteki uye listesi. */
    private List<Member> members;
    /** Constructer: tum uyeleri dosyadan yukle. */
    public MemberManager() {
        this.members = MemberData.loadAll();
    }
   /** Bellekteki tum uyeleri doner. */
    public List<Member> getAllMembers() {
        return members;
    }
    /**
     * Yeni uye ekler.
     *
     * Kontroller:
     *  1. Ad-soyad bos olamaz
     *  2. Email bos olamaz ve formati gecerli olmali
     *  3. Ayni email ile baska uye olmamali (benzersiz)
     *
     * @return ekleme basarili olduysa true
     */
    public boolean addMember(String fullName, String email, String phone) {
        if (fullName == null || fullName.trim().isEmpty()) return false;
        if (email == null || email.trim().isEmpty()) return false;
        if (!isValidEmail(email)) return false;

       // Ayni email ile baska uye var mi?
        for (Member m : members) {
           // equalsIgnoreCase -> Ali@A.com ile ali@a.com ayni sayilir
            if (m.getEmail().equalsIgnoreCase(email.trim())) {
                return false;
            }
        }
        // Kontroller gectiyse yeni id al ve uyeyi ekle
        int id = MemberData.nextId(members);
        // Telefon null gelirse bos string ver, yoksa trim et
        Member m = new Member(id, fullName.trim(), email.trim(), phone == null ? "" : phone.trim());
        members.add(m);
        MemberData.saveAll(members);
        return true;
    }
    /** E-mail format kontrolu (yukaridaki regex deseniyle). */
    public boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Uyeyi siler.
     * @return silindiyse true, bulunamadiysa false
     */
    public boolean deleteMember(int id) {
        Member toRemove = null;
        for (Member m : members) {
            // Once silinecek uyeyi bul
            if (m.getId() == id) {
                toRemove = m;
                break;
            }
        }
        // Bulduysa listeden cikar ve dosyayi yenile
        if (toRemove != null) {
            members.remove(toRemove);
            MemberData.saveAll(members);
            return true;
        }
        return false;
    }
    
    /**
     * Uye arama.
     * Anahtar kelime ad veya email icinde geciyorsa eslesme sayilir.
     * Bos arama tum uyeleri doner.
     */
    public List<Member> search(String keyword) {
        List<Member> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return members;
        }
        String key = keyword.toLowerCase();
        for (Member m : members) {
            if (m.getFullName().toLowerCase().contains(key)
                    || m.getEmail().toLowerCase().contains(key)) {
                result.add(m);
            }
        }
        return result;
    }

    /** Verilen id'ye sahip uyeyi bulur, yoksa null. */
    public Member findById(int id) {
        for (Member m : members) {
            if (m.getId() == id) return m;
        }
        return null;
    }
    
    /** Bellekteki listeyi dosyadan tekrar yukler. */
    public void reload() {
        this.members = MemberData.loadAll();
    }
}
