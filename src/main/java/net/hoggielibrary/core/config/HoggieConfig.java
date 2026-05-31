package net.hoggielibrary.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.hoggielibrary.core.logging.HoggieLogger;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Central configuration manager for Hoggie Library.
 *
 * <p>Provides a thread-safe, JSON-backed configuration system with
 * automatic saving and loading. All configuration values are
 * namespaced for modular access.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.config.get("module.setting");
 * Hoggie.config.set("module.setting", value);
 * Hoggie.config.save();
 * }</pre>
 */
public final class HoggieConfig {

    private static final String CONFIG_FILE_NAME = "hoggielibrary.json";
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private final Map<String, Object> values = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Path configPath;
    private boolean dirty;

    /**
     * Creates a new config manager using the default config path.
     */
    public HoggieConfig() {
        this.configPath = Path.of(".", "config", CONFIG_FILE_NAME);
        load();
    }

    /**
     * Creates a new config manager with a custom config path.
     *
     * @param configPath the path to the config file
     */
    public HoggieConfig(Path configPath) {
        this.configPath = configPath;
        load();
    }

    /**
     * Gets a configuration value by key.
     *
     * @param key the configuration key (e.g., "combat.reach")
     * @param defaultValue the default value if key is not set
     * @param <T> the expected type of the value
     * @return the configuration value, or defaultValue if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        lock.readLock().lock();
        try {
            return (T) values.getOrDefault(key, defaultValue);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets a configuration value by key, returning null if not set.
     *
     * @param key the configuration key
     * @param <T> the expected type of the value
     * @return the configuration value, or null if not found
     */
    public <T> T get(String key) {
        return get(key, null);
    }

    /**
     * Sets a configuration value by key.
     *
     * @param key the configuration key
     * @param value the value to set
     * @param <T> the type of the value
     */
    public <T> void set(String key, T value) {
        lock.writeLock().lock();
        try {
            values.put(key, value);
            dirty = true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Returns whether a configuration key exists.
     *
     * @param key the configuration key
     * @return true if the key exists
     */
    public boolean has(String key) {
        lock.readLock().lock();
        try {
            return values.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Removes a configuration value by key.
     *
     * @param key the configuration key to remove
     */
    public void remove(String key) {
        lock.writeLock().lock();
        try {
            values.remove(key);
            dirty = true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Saves the configuration to disk.
     */
    public void save() {
        lock.readLock().lock();
        try {
            if (!dirty) return;
        } finally {
            lock.readLock().unlock();
        }
        lock.writeLock().lock();
        try {
            Files.createDirectories(configPath.getParent());
            try (Writer writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)) {
                GSON.toJson(values, writer);
            }
            dirty = false;
            HoggieLogger.debug("Configuration saved to {}", configPath);
        } catch (IOException e) {
            HoggieLogger.error("Failed to save configuration", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Loads the configuration from disk.
     */
    @SuppressWarnings("unchecked")
    public void load() {
        lock.writeLock().lock();
        try {
            if (Files.exists(configPath)) {
                try (Reader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
                    Type type = new TypeToken<Map<String, Object>>() {}.getType();
                    Map<String, Object> loaded = GSON.fromJson(reader, type);
                    if (loaded != null) {
                        values.putAll(loaded);
                    }
                    HoggieLogger.debug("Configuration loaded from {}", configPath);
                } catch (IOException e) {
                    HoggieLogger.error("Failed to load configuration", e);
                }
            }
            dirty = false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Reloads the configuration from disk, discarding in-memory changes.
     */
    public void reload() {
        lock.writeLock().lock();
        try {
            values.clear();
            load();
            HoggieLogger.info("Configuration reloaded");
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Returns the config file path.
     *
     * @return the config file path
     */
    public Path getConfigPath() {
        return configPath;
    }

    /**
     * Returns a copy of all configuration values.
     *
     * @return a map of all configuration entries
     */
    public Map<String, Object> getAll() {
        lock.readLock().lock();
        try {
            return new ConcurrentHashMap<>(values);
        } finally {
            lock.readLock().unlock();
        }
    }
}
