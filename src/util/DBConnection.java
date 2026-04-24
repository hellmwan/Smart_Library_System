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
 * Central database access point.
 * <p>
 * Uses a single shared {@link Connection} (singleton pattern) to a local
 * SQLite file stored at <code>data/library.db</code>. The database file
 * and folder are created automatically on first run.
 * <p>
 * DAO classes should call {@link #getConnection()} for every operation.
 * Call {@link #initializeDatabase()} once at application startup.
 */
public final class DBConnection {

    /**
     * Default on-disk SQLite file. Tests can override it by setting
     * the system property {@code library.db.path} — use the special
     * value {@code ":memory:"} to get a throw-away in-memory database.
     */
    private static final String DEFAULT_DB_FILE = "data/library.db";
    private static final String PROPERTY_KEY    = "library.db.path";

    private static Connection connection;
    private static String      activeUrl;

    private DBConnection() {
        // utility class - no instances
    }

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
     * Returns the shared connection, opening it if necessary.
     * Thread-safe.
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            activeUrl = resolveUrl();

            // For on-disk databases, make sure the parent directory exists.
            // Skip this for in-memory databases (":memory:" has no file).
            if (!activeUrl.contains(":memory:")) {
                // Strip "jdbc:sqlite:" prefix to get the raw path, then find its parent.
                String path = activeUrl.substring("jdbc:sqlite:".length());
                File dbFile = new File(path);
                File parent = dbFile.getAbsoluteFile().getParentFile();
                if (parent != null && !parent.exists() && !parent.mkdirs()) {
                    throw new SQLException("Could not create directory: " + parent.getAbsolutePath());
                }
            }
            connection = DriverManager.getConnection(activeUrl);

            // SQLite does NOT enforce foreign keys by default. Turn them on.
            try (Statement st = connection.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON;");
            }
        }
        return connection;
    }

    /**
     * Executes schema.sql (from resources) to create tables if they
     * do not already exist. Safe to call on every application start.
     */
    public static void initializeDatabase() throws SQLException {
        runScript("/schema.sql");
    }

    /**
     * Populates the database with demo data from seed.sql
     * <b>only if the library is empty</b> (no books and no members).
     * This lets a fresh install come up with something to show off,
     * without wiping real data on subsequent restarts.
     *
     * @return true if seed data was loaded, false if skipped.
     */
    public static boolean loadSeedDataIfEmpty() throws SQLException {
        if (!isEmptyDatabase()) return false;
        runScript("/seed.sql");
        return true;
    }

    private static boolean isEmptyDatabase() throws SQLException {
        Connection conn = getConnection();
        try (Statement st = conn.createStatement()) {
            // If either table has rows already, we consider the DB "populated"
            // and leave it alone.
            int books   = countRows(st, "SELECT COUNT(*) FROM books");
            int members = countRows(st, "SELECT COUNT(*) FROM members");
            return books == 0 && members == 0;
        }
    }

    private static int countRows(Statement st, String sql) throws SQLException {
        try (var rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Reads a SQL script from the classpath and runs each statement in it.
     * Lines beginning with {@code --} are treated as comments and stripped
     * before the script is split on {@code ;}.
     */
    private static void runScript(String resourcePath) throws SQLException {
        String script = loadResource(resourcePath);
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement()) {
            // Strip comment lines so they don't produce phantom empty statements
            // when the script is split on ';'.
            StringBuilder cleaned = new StringBuilder();
            for (String line : script.split("\\R")) {
                String trimmed = line.stripLeading();
                if (trimmed.startsWith("--")) continue;   // full-line comment
                cleaned.append(line).append('\n');
            }

            for (String raw : cleaned.toString().split(";")) {
                String sql = raw.trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }
        }
    }

    private static String loadResource(String path) {
        try (InputStream is = DBConnection.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalStateException(
                        path + " not found on classpath. " +
                        "It must be in src/main/resources" + path);
            }
            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + path, e);
        }
    }

    /** Closes the connection. Call on application shutdown. */
    public static synchronized void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
                /* closing silently */
            }
            connection = null;
        }
    }
}
