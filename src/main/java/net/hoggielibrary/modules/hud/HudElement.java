package net.hoggielibrary.modules.hud;

/**
 * Base class for HUD elements.
 *
 * <p>Implement this interface to create custom HUD components that
 * can be registered with the {@link HudAPI}.
 */
public interface HudElement {

    /**
     * Returns the unique identifier for this HUD element.
     *
     * @return the element ID
     */
    String getId();

    /**
     * Returns the display name of this HUD element.
     *
     * @return the display name
     */
    String getDisplayName();

    /**
     * Returns the current X position of this element on screen.
     *
     * @return the x position
     */
    float getX();

    /**
     * Returns the current Y position of this element on screen.
     *
     * @return the y position
     */
    float getY();

    /**
     * Sets the X position of this element.
     *
     * @param x the x position
     */
    void setX(float x);

    /**
     * Sets the Y position of this element.
     *
     * @param y the y position
     */
    void setY(float y);

    /**
     * Returns the width of this element.
     *
     * @return the width in pixels
     */
    float getWidth();

    /**
     * Returns the height of this element.
     *
     * @return the height in pixels
     */
    float getHeight();

    /**
     * Returns whether this element is visible.
     *
     * @return true if visible
     */
    boolean isVisible();

    /**
     * Sets the visibility of this element.
     *
     * @param visible whether visible
     */
    void setVisible(boolean visible);

    /**
     * Renders this HUD element.
     *
     * @param delta the partial tick time
     */
    void render(float delta);
}
