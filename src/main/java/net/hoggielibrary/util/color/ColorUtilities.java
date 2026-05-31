package net.hoggielibrary.util.color;

/**
 * Color utility functions for ARGB color manipulation.
 */
public final class ColorUtilities {

    private ColorUtilities() {
    }

    /**
     * Creates an ARGB color from individual components.
     *
     * @param alpha the alpha (0-255)
     * @param red the red (0-255)
     * @param green the green (0-255)
     * @param blue the blue (0-255)
     * @return the ARGB color int
     */
    public static int argb(int alpha, int red, int green, int blue) {
        return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF);
    }

    /**
     * Creates an RGB color (fully opaque).
     *
     * @param red the red (0-255)
     * @param green the green (0-255)
     * @param blue the blue (0-255)
     * @return the ARGB color int
     */
    public static int rgb(int red, int green, int blue) {
        return argb(255, red, green, blue);
    }

    /**
     * Extracts the alpha component from an ARGB color.
     *
     * @param argb the ARGB color int
     * @return the alpha value (0-255)
     */
    public static int alpha(int argb) {
        return (argb >> 24) & 0xFF;
    }

    /**
     * Extracts the red component from an ARGB color.
     *
     * @param argb the ARGB color int
     * @return the red value (0-255)
     */
    public static int red(int argb) {
        return (argb >> 16) & 0xFF;
    }

    /**
     * Extracts the green component from an ARGB color.
     *
     * @param argb the ARGB color int
     * @return the green value (0-255)
     */
    public static int green(int argb) {
        return (argb >> 8) & 0xFF;
    }

    /**
     * Extracts the blue component from an ARGB color.
     *
     * @param argb the ARGB color int
     * @return the blue value (0-255)
     */
    public static int blue(int argb) {
        return argb & 0xFF;
    }

    /**
     * Linearly interpolates between two ARGB colors.
     *
     * @param color1 the first color
     * @param color2 the second color
     * @param progress the progress (0.0 to 1.0)
     * @return the interpolated color
     */
    public static int lerp(int color1, int color2, float progress) {
        int a = (int) (alpha(color1) + (alpha(color2) - alpha(color1)) * progress);
        int r = (int) (red(color1) + (red(color2) - red(color1)) * progress);
        int g = (int) (green(color1) + (green(color2) - green(color1)) * progress);
        int b = (int) (blue(color1) + (blue(color2) - blue(color1)) * progress);
        return argb(a, r, g, b);
    }

    /**
     * Sets the alpha of an ARGB color.
     *
     * @param color the ARGB color
     * @param alpha the new alpha (0-255)
     * @return the color with the new alpha
     */
    public static int setAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    /**
     * Returns the rainbow color for a given index.
     *
     * @param index the color index
     * @param speed the animation speed
     * @return the rainbow ARGB color
     */
    public static int rainbow(int index, float speed) {
        float hue = (System.currentTimeMillis() % 10000) / 10000.0f;
        int rgb = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
        return rgb;
    }
}
