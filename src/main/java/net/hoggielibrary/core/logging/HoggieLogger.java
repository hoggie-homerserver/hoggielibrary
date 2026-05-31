package net.hoggielibrary.core.logging;

import net.hoggielibrary.core.HoggieLibraryMod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central logging utility for Hoggie Library.
 *
 * <p>Provides a consistent logging interface across the framework,
 * delegating to SLF4J for the actual logging implementation.
 *
 * <p>Log levels: TRACE, DEBUG, INFO, WARN, ERROR
 */
public final class HoggieLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(HoggieLibraryMod.getModId());

    private HoggieLogger() {
    }

    /**
     * Logs a trace message.
     *
     * @param message the message to log
     * @param args the format arguments
     */
    public static void trace(String message, Object... args) {
        LOGGER.trace(message, args);
    }

    /**
     * Logs a debug message.
     *
     * @param message the message to log
     * @param args the format arguments
     */
    public static void debug(String message, Object... args) {
        LOGGER.debug(message, args);
    }

    /**
     * Logs an info message.
     *
     * @param message the message to log
     * @param args the format arguments
     */
    public static void info(String message, Object... args) {
        LOGGER.info(message, args);
    }

    /**
     * Logs a warning message.
     *
     * @param message the warning message
     * @param args the format arguments
     */
    public static void warn(String message, Object... args) {
        LOGGER.warn(message, args);
    }

    /**
     * Logs an error message.
     *
     * @param message the error message
     * @param args the format arguments
     */
    public static void error(String message, Object... args) {
        LOGGER.error(message, args);
    }

    /**
     * Logs an error message with a throwable.
     *
     * @param message the error message
     * @param throwable the throwable to log
     * @param args the format arguments
     */
    public static void error(String message, Throwable throwable, Object... args) {
        LOGGER.error(message, args, throwable);
    }

    /**
     * Returns whether debug logging is enabled.
     *
     * @return true if debug is enabled
     */
    public static boolean isDebugEnabled() {
        return LOGGER.isDebugEnabled();
    }

    /**
     * Returns the underlying SLF4J logger.
     *
     * @return the SLF4J logger instance
     */
    public static Logger getLogger() {
        return LOGGER;
    }
}
