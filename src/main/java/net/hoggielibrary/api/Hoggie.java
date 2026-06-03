package net.hoggielibrary.api;

import net.hoggielibrary.core.HoggieLibraryMod;
import net.hoggielibrary.core.config.HoggieConfig;
import net.hoggielibrary.core.scheduler.HoggieScheduler;
import net.hoggielibrary.core.event.HoggieEventBus;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.bridge.BridgeAPI;
import net.hoggielibrary.modules.combat.CombatAPI;
import net.hoggielibrary.modules.gui.GuiAPI;
import net.hoggielibrary.modules.hud.HudAPI;
import net.hoggielibrary.modules.inventory.InventoryAPI;
import net.hoggielibrary.modules.notification.NotificationAPI;
import net.hoggielibrary.modules.pathfinder.PathfinderAPI;
import net.hoggielibrary.modules.player.PlayerAPI;
import net.hoggielibrary.modules.render.RenderAPI;
import net.hoggielibrary.modules.rotation.RotationAPI;
import net.hoggielibrary.modules.world.WorldAPI;
import net.hoggielibrary.modules.pvp.PvPAPI;
import net.hoggielibrary.modules.practice.PracticeAPI;
import net.hoggielibrary.modules.bedwars.BedwarsAPI;
import net.hoggielibrary.modules.rpg.RpgAPI;
import net.hoggielibrary.modules.bot.BotAPI;
import net.hoggielibrary.modules.storage.StorageAPI;
import net.hoggielibrary.modules.developer.DeveloperAPI;
import net.hoggielibrary.modules.modtoggle.ModToggleAPI;

/**
 * Central API entry point for Hoggie Library.
 *
 * <p>All framework subsystems are accessible through this class,
 * providing a unified, discoverable API surface.
 *
 * <p>Usage example:
 * <pre>{@code
 * Hoggie.gui.open(screen);
 * Hoggie.notifications.info("Enabled");
 * Hoggie.combat.attack(target);
 * Hoggie.inventory.findSword();
 * Hoggie.world.placeBlock(pos);
 * Hoggie.rotation.lookAt(entity);
 * Hoggie.pathfinder.walkTo(pos);
 * Hoggie.config.save();
 * Hoggie.scheduler.runLater(() -> {}, 20);
 * }</pre>
 */
public final class Hoggie {

    private static Hoggie instance;
    private static boolean initialized;

    // API Subsystems
    /** GUI management API */
    public static final GuiAPI gui = new GuiAPI();
    /** Combat-related utilities */
    public static final CombatAPI combat = new CombatAPI();
    /** Inventory management */
    public static final InventoryAPI inventory = new InventoryAPI();
    /** Player-related operations */
    public static final PlayerAPI player = new PlayerAPI();
    /** World interaction utilities */
    public static final WorldAPI world = new WorldAPI();
    /** Rendering utilities */
    public static final RenderAPI render = new RenderAPI();
    /** Notification display system */
    public static final NotificationAPI notifications = new NotificationAPI();
    /** Entity rotation utilities */
    public static final RotationAPI rotation = new RotationAPI();
    /** Pathfinding and navigation */
    public static final PathfinderAPI pathfinder = new PathfinderAPI();
    /** Bridging utilities */
    public static final BridgeAPI bridge = new BridgeAPI();
    /** PvP framework */
    public static final PvPAPI pvp = new PvPAPI();
    /** HUD rendering framework */
    public static final HudAPI hud = new HudAPI();
    /** Practice/PvP practice framework */
    public static final PracticeAPI practice = new PracticeAPI();
    /** Bedwars game framework */
    public static final BedwarsAPI bedwars = new BedwarsAPI();
    /** RPG game framework */
    public static final RpgAPI rpg = new RpgAPI();
    /** Bot/AI framework */
    public static final BotAPI bot = new BotAPI();
    /** Storage and persistence */
    public static final StorageAPI storage = new StorageAPI();
    /** Developer tools and debugging */
    public static final DeveloperAPI developer = new DeveloperAPI();
    /** Mod toggle management */
    public static final ModToggleAPI modToggle = new ModToggleAPI();
    // Internal core systems (lazy-loaded - see initialize())
    /** Task scheduling */
    public static HoggieScheduler scheduler;
    /** Configuration management */
    public static HoggieConfig config;
    /** Event bus for inter-component communication */
    public static HoggieEventBus events;

    private Hoggie() {
    }

    /**
     * Initializes the Hoggie API.
     *
     * @param mod the mod initializer instance
     */
    public static void initialize(HoggieLibraryMod mod) {
        if (initialized) {
            HoggieLogger.warn("Hoggie API already initialized");
            return;
        }
        instance = new Hoggie();
        scheduler = mod.getScheduler();
        config = mod.getConfig();
        events = mod.getEventBus();
        initialized = true;
        HoggieLogger.debug("Hoggie API initialized");
    }

    /**
     * Returns whether the Hoggie API has been initialized.
     *
     * @return true if initialized
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Returns the singleton instance of the Hoggie API.
     *
     * @return the Hoggie instance
     */
    public static Hoggie getInstance() {
        return instance;
    }
}
