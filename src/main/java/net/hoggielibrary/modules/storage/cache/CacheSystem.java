package net.hoggielibrary.modules.storage.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe cache system with TTL support.
 */
public final class CacheSystem {

    private static class CacheEntry {
        final Object value;
        final long expiry;

        CacheEntry(Object value, long expiryMs) {
            this.value = value;
            this.expiry = expiryMs > 0 ? System.currentTimeMillis() + expiryMs : Long.MAX_VALUE;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiry;
        }
    }

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    /**
     * Stores a value with infinite TTL.
     *
     * @param key the cache key
     * @param value the value
     */
    public void put(String key, Object value) {
        cache.put(key, new CacheEntry(value, 0));
    }

    /**
     * Stores a value with a TTL.
     *
     * @param key the cache key
     * @param value the value
     * @param ttlMs the time to live in milliseconds
     */
    public void put(String key, Object value, long ttlMs) {
        cache.put(key, new CacheEntry(value, ttlMs));
    }

    /**
     * Gets a cached value.
     *
     * @param key the cache key
     * @param <T> the expected type
     * @return the cached value, or null if not found or expired
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return (T) entry.value;
    }

    /**
     * Checks if a key exists and is not expired.
     *
     * @param key the cache key
     * @return true if the key exists and is valid
     */
    public boolean contains(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) return false;
        if (entry.isExpired()) {
            cache.remove(key);
            return false;
        }
        return true;
    }

    /**
     * Removes a cached entry.
     *
     * @param key the cache key
     */
    public void remove(String key) {
        cache.remove(key);
    }

    /**
     * Clears all cached entries.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Removes all expired entries.
     *
     * @return the number of entries removed
     */
    public int cleanup() {
        int removed = 0;
        var iterator = cache.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (entry.getValue().isExpired()) {
                iterator.remove();
                removed++;
            }
        }
        return removed;
    }

    /**
     * Returns the number of cached entries.
     *
     * @return the cache size
     */
    public int size() {
        return cache.size();
    }
}
