package net.hoggielibrary.core.theme;

import net.hoggielibrary.util.color.ColorUtilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Theme management system for consistent UI theming.
 *
 * <p>Provides color scheme management, theme definitions, and
 * hot-swappable theme support for all GUI/HUD components.
 */
public final class ThemeManager {

    private static ThemeManager instance;
    private Theme currentTheme;
    private final Map<String, Theme> themes = new ConcurrentHashMap<>();

    /**
     * Gets the singleton ThemeManager instance.
     *
     * @return the theme manager
     */
    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    private ThemeManager() {
        registerDefaultThemes();
        this.currentTheme = themes.get("dark");
    }

    private void registerDefaultThemes() {
        Theme dark = new Theme("dark", "Dark Theme")
                .setColor("background", ColorUtilities.argb(255, 25, 25, 30))
                .setColor("primary", ColorUtilities.argb(255, 50, 120, 255))
                .setColor("secondary", ColorUtilities.argb(255, 100, 180, 255))
                .setColor("text", ColorUtilities.argb(255, 220, 220, 230))
                .setColor("textSecondary", ColorUtilities.argb(255, 140, 140, 150))
                .setColor("success", ColorUtilities.argb(255, 60, 220, 80))
                .setColor("warning", ColorUtilities.argb(255, 255, 200, 50))
                .setColor("error", ColorUtilities.argb(255, 255, 70, 60))
                .setColor("accent", ColorUtilities.argb(255, 180, 60, 255))
                .setInt("borderRadius", 4)
                .setFloat("opacity", 0.85f);

        Theme light = new Theme("light", "Light Theme")
                .setColor("background", ColorUtilities.argb(255, 240, 240, 245))
                .setColor("primary", ColorUtilities.argb(255, 30, 100, 230))
                .setColor("secondary", ColorUtilities.argb(255, 80, 160, 240))
                .setColor("text", ColorUtilities.argb(255, 30, 30, 40))
                .setColor("textSecondary", ColorUtilities.argb(255, 100, 100, 110))
                .setColor("success", ColorUtilities.argb(255, 40, 200, 60))
                .setColor("warning", ColorUtilities.argb(255, 230, 180, 30))
                .setColor("error", ColorUtilities.argb(255, 230, 50, 40))
                .setColor("accent", ColorUtilities.argb(255, 160, 40, 240))
                .setInt("borderRadius", 4)
                .setFloat("opacity", 0.9f);

        themes.put("dark", dark);
        themes.put("light", light);
    }

    /**
     * Registers a custom theme.
     *
     * @param theme the theme to register
     */
    public void registerTheme(Theme theme) {
        themes.put(theme.getId(), theme);
    }

    /**
     * Sets the current theme by ID.
     *
     * @param themeId the theme ID
     */
    public void setTheme(String themeId) {
        Theme theme = themes.get(themeId);
        if (theme != null) {
            this.currentTheme = theme;
        }
    }

    /**
     * Gets the current theme.
     *
     * @return the current theme
     */
    public Theme getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Gets a registered theme by ID.
     *
     * @param themeId the theme ID
     * @return the theme, or null
     */
    public Theme getTheme(String themeId) {
        return themes.get(themeId);
    }

    /**
     * Gets a color value from the current theme.
     *
     * @param key the color key
     * @return the ARGB color, or 0 if not found
     */
    public int getColor(String key) {
        return currentTheme != null ? currentTheme.getColor(key) : 0;
    }
}
