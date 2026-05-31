package net.hoggielibrary.modules.hud;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * HUD framework for rendering custom in-game overlays.
 *
 * <p>Provides a component-based HUD system where elements can be
 * registered, positioned, and rendered on the screen.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.hud.addElement(new MyHudElement());
 * Hoggie.hud.removeElement("my_element");
 * Hoggie.hud.setVisible(false);
 * }</pre>
 */
public final class HudAPI {

    private final List<HudElement> elements = new CopyOnWriteArrayList<>();
    private boolean visible = true;

    /**
     * Adds a HUD element to the display.
     *
     * @param element the HUD element to add
     */
    public void addElement(HudElement element) {
        if (element.getId() == null || element.getId().isEmpty()) {
            HoggieLogger.warn("HUD element must have a non-empty ID");
            return;
        }
        elements.removeIf(e -> e.getId().equals(element.getId()));
        elements.add(element);
        HoggieLogger.debug("Added HUD element: {}", element.getId());
    }

    /**
     * Removes a HUD element by its ID.
     *
     * @param id the element ID
     */
    public void removeElement(String id) {
        elements.removeIf(e -> e.getId().equals(id));
    }

    /**
     * Returns a HUD element by its ID.
     *
     * @param id the element ID
     * @return the element, or null
     */
    public HudElement getElement(String id) {
        return elements.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns all registered HUD elements.
     *
     * @return list of HUD elements
     */
    public List<HudElement> getElements() {
        return Collections.unmodifiableList(elements);
    }

    /**
     * Clears all HUD elements.
     */
    public void clearElements() {
        elements.clear();
    }

    /**
     * Sets the visibility of the entire HUD.
     *
     * @param visible whether the HUD should be visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns whether the HUD is visible.
     *
     * @return true if visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Returns the number of registered HUD elements.
     *
     * @return element count
     */
    public int getElementCount() {
        return elements.size();
    }
}
