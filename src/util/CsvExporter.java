package util;

import javax.swing.table.TableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * UI'daki herhangi bir tabloyu (kitap listesi, uye listesi, gecikme raporu vb.)
 * Excel'de acilabilir CSV dosyasi olarak disa aktarmak icin kullanilir.
 *
 * Ozellikler:
 *  - UTF-8 ile yazar (Turkce karakter sorunu yasanmaz)
 *  - Basa BOM ekler (Excel "Save as UTF-8" olarak dogru tanisin diye)
 *  - Hucrede virgul, tirnak, satir sonu varsa otomatik olarak escape eder
 */
public final class CsvExporter {

    // --- CSV bicim sabitleri ---
    private static final String  SEPARATOR = ",";          // Hucreler arasi virgul
    private static final String  LINE_END  = "\r\n";       // Windows tarzi satir sonu
    private static final char[]  BOM       = {'\uFEFF'};   // Excel UTF-8 imzasi

    private CsvExporter() {  } // nesne uretilmesin


    /**
     * Verilen JTable modelini hedef dosyaya CSV olarak yazar.
     * @param model Swing tablosunun modeli (basliklar + satirlar)
     * @param file  yazilacak dosya
     */
    public static void export(TableModel model, File file) throws IOException {
        // try-with-resources -> dosya en sonunda otomatik kapanir
        try (BufferedWriter out =
                     Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {

            // 1. Once BOM imzasini yaz (Excel iyi tanisin diye)
            out.write(BOM);


            // 2. Baslik satirini yaz: kolon adlarini virgulle ayirarak
            int cols = model.getColumnCount();
            for (int c = 0; c < cols; c++) {
                if (c > 0) out.write(SEPARATOR);   // ilk basa virgul koyma
                out.write(escape(model.getColumnName(c)));
            }
            out.write(LINE_END);


            // 3. Veri satirlarini yaz
            int rows = model.getRowCount();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (c > 0) out.write(SEPARATOR);
                    Object v = model.getValueAt(r, c);
                    // null hucreler bos string olarak yazilsin
                    out.write(escape(v == null ? "" : v.toString()));
                }
                out.write(LINE_END);
            }
        }
    }

    /**
     * Bir CSV hucresini kacis (escape) kurallarina gore hazirlar.
     * Kural: Hucrede virgul, tirnak veya satir sonu varsa hucreyi
     * cift tirnak ile cevreleyip, icerideki tirnaklari "" yaparak kacisla.
     * Ornek:  Hello, "World" -> "Hello, ""World"""
     */
    private static String escape(String field) {
        boolean mustQuote = field.indexOf(',') >= 0
                || field.indexOf('"') >= 0
                || field.indexOf('\n') >= 0
                || field.indexOf('\r') >= 0;
        if (!mustQuote) return field; // ozel karakter yoksa oldugu gibi don
        // " karakterlerini iki kat yap, sonra basina/sonuna " ekle
        return '"' + field.replace("\"", "\"\"") + '"';
    }
}
