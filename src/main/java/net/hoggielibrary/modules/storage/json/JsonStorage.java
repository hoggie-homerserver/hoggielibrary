package net.hoggielibrary.modules.storage.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JSON-based storage for persistent data.
 */
public final class JsonStorage {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private Path storagePath;

    /**
     * Initializes the storage with a file path.
     *
     * @param path the storage file path
     */
    public void initialize(Path path) {
        this.storagePath = path;
        load();
    }

    /**
     * Stores a value by key.
     *
     * @param key the key
     * @param value the value
     */
    public void put(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Gets a value by key.
     *
     * @param key the key
     * @param <T> the expected type
     * @return the value, or null
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) data.get(key);
    }

    /**
     * Gets a value with a default.
     *
     * @param key the key
     * @param defaultValue the default
     * @param <T> the type
     * @return the value, or default
     */
    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(String key, T defaultValue) {
        return (T) data.getOrDefault(key, defaultValue);
    }

    /**
     * Checks if a key exists.
     *
     * @param key the key
     * @return true if exists
     */
    public boolean has(String key) {
        return data.containsKey(key);
    }

    /**
     * Saves data to disk.
     */
    public void save() {
        if (storagePath == null) return;
        try {
            Files.createDirectories(storagePath.getParent());
            try (Writer writer = Files.newBufferedWriter(storagePath, StandardCharsets.UTF_8)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save JSON storage", e);
        }
    }

    /**
     * Loads data from disk.
     */
    @SuppressWarnings("unchecked")
    public void load() {
        if (storagePath == null || !Files.exists(storagePath)) return;
        try (Reader reader = Files.newBufferedReader(storagePath, StandardCharsets.UTF_8)) {
            Map<String, Object> loaded = GSON.fromJson(reader, Map.class);
            if (loaded != null) {
                data.putAll(loaded);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load JSON storage", e);
        }
    }

    /**
     * Clears all data.
     */
    public void clear() {
        data.clear();
    }
}
