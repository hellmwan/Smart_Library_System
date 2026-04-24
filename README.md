# Smart Library System

Java Swing ile yazilmis kutuphane otomasyon sistemi.
Yazilim Muhendisligi 3. sinif donem projesi.

## Proje Hakkinda

Bu proje bir kutuphanenin temel islemlerini bilgisayar uzerinden yapmaya yariyor.
Admin personeli ekleyebilir, personel (kutuphaneci) ise kitap, uye ve odunc
islemlerini yonetebilir. Veriler text dosyalarinda saklaniyor (data/ klasoru),
yani veritabani kurmaya gerek yok.

## Klasor Yapisi

```
Smart_Library_System/
  src/
    ui/        - Swing pencereleri
    business/  - Is mantigi (BookManager, MemberManager, LoanManager)
    data/      - Dosya okuma/yazma (BookData, MemberData, LoanData)
    model/     - Veri siniflari (Book, Member, Loan, Personnel)
  data/        - CSV veri dosyalari (uygulama yazinca buraya kaydeder)
  docs/        - UML, gereksinim, test dokumanlari
  resources/   - Diger kaynaklar
  test/        - Test dosyalari
```

## Calistirma

IntelliJ IDEA'da projeyi acin, sonra `src/ui/LoginFrame.java` dosyasini calistirin.
Veya komut satirindan:

```
cd Smart_Library_System
javac -d out src/model/*.java src/data/*.java src/business/*.java src/ui/*.java
cd out
java ui.LoginFrame
```

Not: Komut satirindan calistirirken `src/xyz.jpeg` arkaplan resmini gormek icin
proje kok dizininde olmaniz lazim.

## Giris Bilgileri

| Kullanici | Sifre    | Yetki     |
|-----------|----------|-----------|
| admin     | admin123 | Admin     |
| lib       | lib123   | Librarian |

Admin "Manage Personnel" ekranindan yeni kutuphaneci ekleyebilir.
Eklenen kutuphaneciler kendi kullanici adi ve sifresi ile giris yapabilir.

## Ozellikler

- Login + role-based erisim (admin / librarian)
- Kitap ekleme, silme, guncelleme, arama
- Uye ekleme, silme, arama
- Kitap odunc verme ve iade alma
- Gecikmis kitaplar ve ceza raporu (gun basina 5 TL)
- Tum veriler text dosyasinda kalici olarak tutulur

## Demo Veri

Uygulama ilk acilista bos olmasin diye:
- `data/books.txt` - 8 ornek kitap
- `data/members.txt` - 5 ornek uye
- `data/loans.txt` - ilk acilista otomatik 3 ornek odunc kaydi (1 aktif, 1 gecikmis, 1 iade edilmis)

`data/loans.txt` dosyasini silerseniz tekrar ornek odunc kayitlari olusur.
Kitap ve uye dosyalarini silmek istemezsiniz cunku tekrar olusturulmaz.

## Grup Uyeleri

| Isim | Sorumluluk |
|------|------------|
| ... | UI - Login, Main, Dashboard |
| ... | UI - Books ve Members panelleri |
| ... | UI - Loans ve Reports panelleri |
| ... | Business katmani (Manager siniflari) |
| ... | Data katmani + Model siniflari |
