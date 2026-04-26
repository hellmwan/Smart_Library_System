package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Bu projede ana veri saklama text dosyalarinda (data/*.txt) yapiliyor.
 * Bu sinif ileride veritabanina gecis icin hazirlanmis bir altyapidir.
 * Su anki "lib/sqlite-jdbc-3.51.3.0.jar" kutuphanesini kullanir
 * Singleton mantigi: Tek bir Connection nesnesi acilir, butun program onu paylasir.
 * Tekrar tekrar baglanmak performans kaybi olur.
 */
public final class DBConnection {

    // --- Sabitler ---
    private static final String DEFAULT_DB_FILE = "data/library.db";   // Varsayilan db dosyasi
    private static final String PROPERTY_KEY    = "library.db.path";   // Sistem ozelligi anahtari (override icin)

    // --- Statik durum (tum siniflar paylasir) ---
    private static Connection connection;   // Tek baglanti
    private static String      activeUrl;   // Aktif jdbc URL'i (debug icin)

    private DBConnection() {
        // Utility sinifindan nesne uretilmesin
    }


    /**
     * Kullanilacak JDBC URL'sini belirler.
     *
     * Mantik:
     *  1. Eger -Dlibrary.db.path=... ile bir yol verilmisse onu kullan
     *  2. Bu yol ":memory:" ise gecici (RAM'de) veritabani kullan (testler icin)
     *  3. Hicbir sey verilmediyse varsayilan dosyaya bagla
     */
    private static String resolveUrl() {
        String override = System.getProperty(PROPERTY_KEY);
        if (override == null || override.isBlank()) {
            return "jdbc:sqlite:" + DEFAULT_DB_FILE;
        }
        if (":memory:".equalsIgnoreCase(override.trim())) {
            return "jdbc:sqlite::memory:";
        }
        return "jdbc:sqlite:" + override.trim();
    }


    /**
     * Aktif Connection'i doner. Yoksa olusturur.
     * "synchronized" -> ayni anda iki thread cagirsa bile guvenli.
     */
    public static synchronized Connection getConnection() throws SQLException {
        // Baglanti yoksa veya kapanmissa yenisini ac
        if (connection == null || connection.isClosed()) {
            activeUrl = resolveUrl();

            // Eger gercek dosya kullanilacaksa data/ klasorunu olustur (yoksa)
            if (!activeUrl.contains(":memory:")) {
                String path = activeUrl.substring("jdbc:sqlite:".length());
                File dbFile = new File(path);
                File parent = dbFile.getAbsoluteFile().getParentFile();
                if (parent != null && !parent.exists() && !parent.mkdirs()) {
                    throw new SQLException("Could not create directory: " + parent.getAbsolutePath());
                }
            }

            // Asil baglantiyi ac
            connection = DriverManager.getConnection(activeUrl);

            // SQLite varsayilan olarak foreign key zorlamiyor; biz aciyoruz
            try (Statement st = connection.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON;");
            }
        }
        return connection;
    }


    /**
     * Veritabani semasini kurar (tablolari olusturur).
     * "/schema.sql" classpath'inden okunur ve calistirilir.
     */
    public static void initializeDatabase() throws SQLException {
        runScript("/schema.sql");
    }


    /**
     * Eger veritabani bossa "/seed.sql"den ornek veri yukler.
     * @return seed yuklendiyse true, doluysa false
     */
    public static boolean loadSeedDataIfEmpty() throws SQLException {
        if (!isEmptyDatabase()) return false;
        runScript("/seed.sql");
        return true;
    }

    /** books ve members tablolari boş mu? */
    private static boolean isEmptyDatabase() throws SQLException {
        Connection conn = getConnection();
        try (Statement st = conn.createStatement()) {
            int books   = countRows(st, "SELECT COUNT(*) FROM books");
            int members = countRows(st, "SELECT COUNT(*) FROM members");
            return books == 0 && members == 0;
        }
    }

    /** SELECT COUNT(*) sonucunu int olarak doner. */
    private static int countRows(Statement st, String sql) throws SQLException {
        try (var rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }


    /**
     * Bir SQL dosyasini classpath'ten okuyup ; ile ayrilmis komutlari calistirir.
     *
     * Adimlar:
     *  1. Dosyayi metin olarak yukle
     *  2. -- ile baslayan tam satir yorumlarini ele
     *  3. ; ile boluerek her komutu execute et
     */
    private static void runScript(String resourcePath) throws SQLException {
        String script = loadResource(resourcePath);
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement()) {

            // Yorum satirlarini cikar
            StringBuilder cleaned = new StringBuilder();
            for (String line : script.split("\\R")) {
                String trimmed = line.stripLeading();
                if (trimmed.startsWith("--")) continue;   // tam satir yorum -> atla
                cleaned.append(line).append('\n');
            }

            // ; ile parcala ve her komutu calistir
            for (String raw : cleaned.toString().split(";")) {
                String sql = raw.trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }
        }
    }

    /** Classpath uzerinden bir kaynak dosyasini metin olarak yukler. */
    private static String loadResource(String path) {
        try (InputStream is = DBConnection.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalStateException(
                        path + " not found on classpath. " +
                        "It must be in src/main/resources" + path);
            }
            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                // Tum satirlari okuyup \n ile birlestir
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + path, e);
        }
    }


    /**
     * Veritabani baglantisini kapatir.
     * Programdan cikarken cagirmak iyi pratiktir.
     */
    public static synchronized void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
                // Kapatma hatasini yutuyoruz, programi durdurmayalim
            }
            connection = null;
        }
    }
}
