package net.hoggielibrary.modules.storage.migration;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data migration system for evolving data schemas.
 */
public final class DataMigration {

    private final Map<Integer, Runnable> migrations = new TreeMap<>();
    private final Set<Integer> appliedMigrations = ConcurrentHashMap.newKeySet();

    /**
     * Registers a migration step.
     *
     * @param version the target version
     * @param migration the migration logic
     */
    public void register(int version, Runnable migration) {
        migrations.put(version, migration);
    }

    /**
     * Marks a version as applied.
     *
     * @param version the version
     */
    public void markApplied(int version) {
        appliedMigrations.add(version);
    }

    /**
     * Runs all pending migrations.
     *
     * @param currentVersion the current data version
     * @return the new version after migrations
     */
    public int runPending(int currentVersion) {
        int version = currentVersion;
        for (Map.Entry<Integer, Runnable> entry : migrations.entrySet()) {
            if (entry.getKey() > version && !appliedMigrations.contains(entry.getKey())) {
                HoggieLogger.info("Running migration v{}", entry.getKey());
                try {
                    entry.getValue().run();
                    appliedMigrations.add(entry.getKey());
                    version = entry.getKey();
                    HoggieLogger.info("Migration v{} completed", entry.getKey());
                } catch (Exception e) {
                    HoggieLogger.error("Migration v{} failed", entry.getKey(), e);
                    break;
                }
            }
        }
        return version;
    }

    /**
     * Resets all migration tracking.
     */
    public void reset() {
        appliedMigrations.clear();
    }
}
