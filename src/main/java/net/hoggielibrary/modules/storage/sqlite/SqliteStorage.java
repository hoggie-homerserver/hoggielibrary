package net.hoggielibrary.modules.storage.sqlite;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SQLite-based storage for structured data persistence.
 */
public final class SqliteStorage {

    private Connection connection;
    private final Map<String, String> tableSchemas = new ConcurrentHashMap<>();

    /**
     * Initializes the SQLite database.
     *
     * @param dbPath the database file path
     */
    public void initialize(Path dbPath) {
        try {
            Files.createDirectories(dbPath.getParent());
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toString());
            HoggieLogger.info("SQLite storage initialized at {}", dbPath);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to initialize SQLite storage", e);
        }
    }

    /**
     * Creates a table if it doesn't exist.
     *
     * @param tableName the table name
     * @param schema the schema definition (e.g., "id INTEGER PRIMARY KEY, name TEXT")
     */
    public void createTable(String tableName, String schema) {
        tableSchemas.put(tableName, schema);
        execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" + schema + ")");
    }

    /**
     * Executes an insert statement.
     *
     * @param table the table name
     * @param columns the column names
     * @param values the values
     */
    public void insert(String table, String[] columns, Object[] values) {
        String placeholders = String.join(",", Collections.nCopies(columns.length, "?"));
        String sql = "INSERT INTO " + table + " ("
                + String.join(",", columns) + ") VALUES (" + placeholders + ")";
        executeUpdate(sql, values);
    }

    /**
     * Executes a query.
     *
     * @param sql the SQL query
     * @param params the query parameters
     * @return list of result rows as maps
     */
    public List<Map<String, Object>> query(String sql, Object... params) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(meta.getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            HoggieLogger.error("SQLite query failed: {}", e.getMessage());
        }
        return results;
    }

    /**
     * Executes an update statement.
     *
     * @param sql the SQL statement
     * @param params the parameters
     * @return the number of affected rows
     */
    public int executeUpdate(String sql, Object... params) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            HoggieLogger.error("SQLite update failed: {}", e.getMessage());
            return 0;
        }
    }

    private void execute(String sql) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            HoggieLogger.error("SQLite execute failed: {}", e.getMessage());
        }
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            HoggieLogger.error("Failed to close SQLite connection", e);
        }
    }
}
