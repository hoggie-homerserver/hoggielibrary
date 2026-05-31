package net.hoggielibrary.core;

import net.fabricmc.api.ModInitializer;
import net.hoggielibrary.core.command.HoggieCommandRegistry;
import net.hoggielibrary.core.config.HoggieConfig;
import net.hoggielibrary.core.event.HoggieEventBus;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.core.scheduler.HoggieScheduler;
import net.hoggielibrary.api.Hoggie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HoggieLibraryMod implements ModInitializer {

    private static final String MOD_ID = "hoggielibrary";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static HoggieLibraryMod instance;
    private HoggieConfig config;
    private HoggieScheduler scheduler;
    private HoggieEventBus eventBus;
    private boolean initialized;

    public static HoggieLibraryMod getInstance() {
        return instance;
    }

    public static String getModId() {
        return MOD_ID;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void onInitialize() {
        instance = this;
        HoggieLogger.info("Initializing Hoggie Library v{}", getClass().getPackage().getImplementationVersion());

        this.eventBus = new HoggieEventBus();
        this.config = new HoggieConfig();
        this.scheduler = new HoggieScheduler();

        Hoggie.initialize(this);

        HoggieCommandRegistry.registerCommands();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        this.initialized = true;
        HoggieLogger.info("Hoggie Library initialized successfully");
    }

    public void shutdown() {
        if (!initialized) return;
        HoggieLogger.info("Shutting down Hoggie Library");
        this.scheduler.shutdown();
        this.config.save();
        this.initialized = false;
    }

    public HoggieConfig getConfig() {
        return config;
    }

    public HoggieScheduler getScheduler() {
        return scheduler;
    }

    public HoggieEventBus getEventBus() {
        return eventBus;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
