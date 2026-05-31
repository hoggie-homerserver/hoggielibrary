package net.hoggielibrary.modules.developer.debug;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Debug tools for runtime inspection and debugging.
 */
public final class DebugTools {

    private final Map<String, Long> timers = new ConcurrentHashMap<>();

    /**
     * Starts a named timer.
     *
     * @param name the timer name
     */
    public void startTimer(String name) {
        timers.put(name, System.nanoTime());
    }

    /**
     * Stops a named timer and returns the elapsed time in milliseconds.
     *
     * @param name the timer name
     * @return elapsed time in ms, or -1 if timer not found
     */
    public long stopTimer(String name) {
        Long start = timers.remove(name);
        if (start == null) return -1;
        return (System.nanoTime() - start) / 1_000_000;
    }

    /**
     * Logs a debug message with the current stack trace.
     *
     * @param message the message to log
     */
    public void logStackTrace(String message) {
        HoggieLogger.debug("=== DEBUG: {} ===", message);
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (int i = 2; i < Math.min(stack.length, 10); i++) {
            HoggieLogger.debug("  at {}", stack[i]);
        }
    }

    /**
     * Prints all system properties for debugging.
     */
    public void printSystemInfo() {
        HoggieLogger.debug("=== System Information ===");
        HoggieLogger.debug("Java: {}", System.getProperty("java.version"));
        HoggieLogger.debug("OS: {}", System.getProperty("os.name"));
        HoggieLogger.debug("Arch: {}", System.getProperty("os.arch"));
        HoggieLogger.debug("Cores: {}", Runtime.getRuntime().availableProcessors());
        HoggieLogger.debug("Max Memory: {}MB",
                Runtime.getRuntime().maxMemory() / (1024 * 1024));
    }

    /**
     * Logs a heap usage snapshot.
     */
    public void logHeapUsage() {
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        long max = runtime.maxMemory();
        HoggieLogger.debug("Heap: {}MB used / {}MB max",
                used / (1024 * 1024), max / (1024 * 1024));
    }
}
