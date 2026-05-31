package net.hoggielibrary.modules.storage;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.storage.json.JsonStorage;
import net.hoggielibrary.modules.storage.sqlite.SqliteStorage;
import net.hoggielibrary.modules.storage.cache.CacheSystem;
import net.hoggielibrary.modules.storage.migration.DataMigration;
import net.hoggielibrary.modules.storage.backup.BackupSystem;

/**
 * Storage Framework API for data persistence.
 *
 * <p>Provides JSON storage, SQLite storage, caching, data migration,
 * and backup systems.
 */
public final class StorageAPI {

    private final JsonStorage jsonStorage = new JsonStorage();
    private final SqliteStorage sqliteStorage = new SqliteStorage();
    private final CacheSystem cache = new CacheSystem();
    private final DataMigration migration = new DataMigration();
    private final BackupSystem backup = new BackupSystem();

    public StorageAPI() {
        HoggieLogger.debug("Storage Framework initialized");
    }

    public JsonStorage json() { return jsonStorage; }
    public SqliteStorage sqlite() { return sqliteStorage; }
    public CacheSystem cache() { return cache; }
    public DataMigration migration() { return migration; }
    public BackupSystem backup() { return backup; }
}
