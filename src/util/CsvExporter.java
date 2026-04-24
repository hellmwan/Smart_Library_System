package util;

import javax.swing.table.TableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Minimal, dependency-free CSV writer.
 * <p>
 * Writes any {@link TableModel} to a file using the common Excel-friendly
 * dialect: comma separator, CRLF line endings, fields quoted only when
 * they contain a comma, quote, CR, or LF; embedded quotes are doubled.
 * <p>
 * A UTF-8 BOM is written as the first three bytes so that Microsoft
 * Excel opens non-ASCII characters correctly (otherwise Turkish letters
 * appear mangled).
 */
public final class CsvExporter {

    private static final String  SEPARATOR = ",";
    private static final String  LINE_END  = "\r\n";
    private static final char[]  BOM       = {'\uFEFF'};

    private CsvExporter() { /* no instances */ }

    /** Writes {@code model} to {@code file}, including the header row. */
    public static void export(TableModel model, File file) throws IOException {
        try (BufferedWriter out =
                     Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            out.write(BOM);  // helps Excel detect UTF-8

            // Header
            int cols = model.getColumnCount();
            for (int c = 0; c < cols; c++) {
                if (c > 0) out.write(SEPARATOR);
                out.write(escape(model.getColumnName(c)));
            }
            out.write(LINE_END);

            // Rows
            int rows = model.getRowCount();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (c > 0) out.write(SEPARATOR);
                    Object v = model.getValueAt(r, c);
                    out.write(escape(v == null ? "" : v.toString()));
                }
                out.write(LINE_END);
            }
        }
    }

    private static String escape(String field) {
        boolean mustQuote = field.indexOf(',') >= 0
                || field.indexOf('"') >= 0
                || field.indexOf('\n') >= 0
                || field.indexOf('\r') >= 0;
        if (!mustQuote) return field;
        return '"' + field.replace("\"", "\"\"") + '"';
    }
}
