package net.hoggielibrary.examples.gui;

import net.fabricmc.api.ModInitializer;
import net.hoggielibrary.api.Hoggie;
import net.hoggielibrary.core.animation.AnimationSystem;
import net.hoggielibrary.core.animation.Easing;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.core.theme.ThemeManager;

/**
 * Example mod demonstrating the Hoggie Library GUI/HUD/Theme/Animation Framework.
 */
public final class GuiExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {
        HoggieLogger.info("GUI Example Mod initialized");

        // Example: Theme system
        ThemeManager themes = ThemeManager.getInstance();
        HoggieLogger.info("Current theme: {}", themes.getCurrentTheme().getDisplayName());

        // Example: Switch theme
        themes.setTheme("light");
        HoggieLogger.info("Theme switched to: {}", themes.getCurrentTheme().getDisplayName());
        int primaryColor = themes.getColor("primary");
        HoggieLogger.info("Primary color: {}", primaryColor);

        // Example: Animation system
        AnimationSystem animSystem = new AnimationSystem();
        animSystem.animate("slide", 0.0, 100.0, 20, Easing.EASE_OUT_BOUNCE)
                .onUpdate(value -> HoggieLogger.debug("Animation value: {}", value))
                .onFinish(() -> HoggieLogger.info("Animation complete!"));

        // Example: Notifications
        Hoggie.notifications.info("Hoggie GUI Framework active");
        Hoggie.notifications.actionBar("GUI Example Loaded");

        // Example: HUD Elements
        Hoggie.hud.setVisible(true);
        HoggieLogger.info("HUD elements: {}", Hoggie.hud.getElementCount());

        // Example: Render API
        int width = Hoggie.render.getWindowWidth();
        int height = Hoggie.render.getWindowHeight();
        HoggieLogger.info("Screen: {}x{}", width, height);

        Hoggie.notifications.success("GUI Example Mod loaded");
    }
}
