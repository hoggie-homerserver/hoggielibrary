package net.hoggielibrary.modules.developer;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.developer.debug.DebugTools;
import net.hoggielibrary.modules.developer.profiling.ProfilingTools;
import net.hoggielibrary.modules.developer.autoregister.AutoRegister;
import net.hoggielibrary.modules.developer.di.DependencyInjection;

/**
 * Developer Framework API for debugging and development tools.
 *
 * <p>Provides debug tools, profiling, auto-registration,
 * reflection utilities, dependency injection, and more.
 */
public final class DeveloperAPI {

    private static boolean debugMode;
    private final DebugTools debug = new DebugTools();
    private final ProfilingTools profiling = new ProfilingTools();
    private final AutoRegister autoRegister = new AutoRegister();
    private final DependencyInjection di = new DependencyInjection();

    public DeveloperAPI() {
        HoggieLogger.debug("Developer Framework initialized");
    }

    /**
     * Toggles debug mode.
     *
     * @return true if debug is now enabled
     */
    public static boolean toggleDebug() {
        debugMode = !debugMode;
        HoggieLogger.info("Debug mode {}", debugMode ? "enabled" : "disabled");
        return debugMode;
    }

    /**
     * Returns whether debug mode is active.
     *
     * @return true if debug mode is on
     */
    public static boolean isDebugMode() {
        return debugMode;
    }

    public DebugTools debug() { return debug; }
    public ProfilingTools profiling() { return profiling; }
    public AutoRegister autoRegister() { return autoRegister; }
    public DependencyInjection di() { return di; }
}
