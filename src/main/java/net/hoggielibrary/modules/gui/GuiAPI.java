package net.hoggielibrary.modules.gui;

import net.hoggielibrary.api.Hoggie;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

/**
 * GUI management API for opening and managing screens.
 *
 * <p>Provides utilities for opening Minecraft screens from anywhere
 * in the framework.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.gui.open(new MyScreen());
 * Hoggie.gui.close();
 * Hoggie.gui.isScreenOpen();
 * }</pre>
 */
public final class GuiAPI {

    private Screen cachedScreen;

    /**
     * Opens a GUI screen.
     *
     * @param screen the screen to open
     */
    public void open(Screen screen) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.setScreen(screen);
            this.cachedScreen = screen;
            HoggieLogger.debug("Opened screen: {}", screen.getClass().getSimpleName());
        }
    }

    /**
     * Closes the currently open screen.
     */
    public void close() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.setScreen(null);
            this.cachedScreen = null;
        }
    }

    /**
     * Returns whether any screen is currently open.
     *
     * @return true if a screen is open
     */
    public boolean isScreenOpen() {
        return MinecraftClient.getInstance().currentScreen != null;
    }

    /**
     * Returns the currently open screen, if any.
     *
     * @return the current screen, or null
     */
    public Screen getCurrentScreen() {
        return MinecraftClient.getInstance().currentScreen;
    }

    /**
     * Opens a screen and schedules it to close after a delay.
     *
     * @param screen the screen to open
     * @param ticks the delay in ticks before closing
     */
    public void openTimed(Screen screen, long ticks) {
        open(screen);
        Hoggie.scheduler.runLater(() -> {
            if (MinecraftClient.getInstance().currentScreen == screen) {
                close();
            }
        }, ticks);
    }
}
