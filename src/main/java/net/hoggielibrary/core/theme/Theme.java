package net.hoggielibrary.core.theme;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a UI theme with named colors and style properties.
 */
public final class Theme {

    private final String id;
    private final String displayName;
    private final Map<String, Integer> colors = new ConcurrentHashMap<>();
    private final Map<String, Integer> ints = new ConcurrentHashMap<>();
    private final Map<String, Float> floats = new ConcurrentHashMap<>();

    /**
     * Creates a new theme.
     *
     * @param id the theme identifier
     * @param displayName the display name
     */
    public Theme(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    /**
     * Gets the theme ID.
     *
     * @return the theme ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets a color value.
     *
     * @param key the color key
     * @param color the ARGB color
     * @return this theme for chaining
     */
    public Theme setColor(String key, int color) {
        colors.put(key, color);
        return this;
    }

    /**
     * Gets a color value.
     *
     * @param key the color key
     * @return the ARGB color, or 0
     */
    public int getColor(String key) {
        return colors.getOrDefault(key, 0);
    }

    /**
     * Sets an integer property.
     *
     * @param key the property key
     * @param value the value
     * @return this theme for chaining
     */
    public Theme setInt(String key, int value) {
        ints.put(key, value);
        return this;
    }

    /**
     * Gets an integer property.
     *
     * @param key the property key
     * @return the value, or 0
     */
    public int getInt(String key) {
        return ints.getOrDefault(key, 0);
    }

    /**
     * Sets a float property.
     *
     * @param key the property key
     * @param value the value
     * @return this theme for chaining
     */
    public Theme setFloat(String key, float value) {
        floats.put(key, value);
        return this;
    }

    /**
     * Gets a float property.
     *
     * @param key the property key
     * @return the value, or 0.0
     */
    public float getFloat(String key) {
        return floats.getOrDefault(key, 0.0f);
    }
}
