package net.hoggielibrary.modules.notification;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.UUID;

/**
 * Notification API for displaying in-game messages and alerts.
 *
 * <p>Provides methods for showing information, warning, error,
 * and success notifications to the player.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.notifications.info("Mod enabled");
 * Hoggie.notifications.warning("Low health");
 * Hoggie.notifications.error("Failed to load");
 * Hoggie.notifications.success("Task complete");
 * }</pre>
 */
public final class NotificationAPI {

    private static final String PREFIX = "§8[§bHoggie§8] §7";

    /**
     * Displays an info notification.
     *
     * @param message the message to display
     */
    public void info(String message) {
        send(PREFIX + "§f" + message);
    }

    /**
     * Displays a warning notification.
     *
     * @param message the warning message
     */
    public void warning(String message) {
        send(PREFIX + "§e⚠ " + message);
    }

    /**
     * Displays an error notification.
     *
     * @param message the error message
     */
    public void error(String message) {
        send(PREFIX + "§c✘ " + message);
    }

    /**
     * Displays a success notification.
     *
     * @param message the success message
     */
    public void success(String message) {
        send(PREFIX + "§a✔ " + message);
    }

    /**
     * Displays a debug notification.
     *
     * @param message the debug message
     */
    public void debug(String message) {
        if (HoggieLogger.isDebugEnabled()) {
            send(PREFIX + "§7[DEBUG] " + message);
        }
    }

    /**
     * Displays a notification in the action bar.
     *
     * @param message the message to display
     */
    public void actionBar(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message), true);
        }
    }

    /**
     * Displays a title notification.
     *
     * @param title the title text
     * @param subtitle the subtitle text
     */
    public void title(String title, String subtitle) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.inGameHud.setTitle(Text.literal(title));
            client.inGameHud.setSubtitle(Text.literal(subtitle));
        }
    }

    private void send(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message), false);
        }
    }
}
