package net.hoggielibrary.core.event;

/**
 * Priority levels for event handlers.
 *
 * <p>Handlers with higher priority are invoked first.
 * The {@link #MONITOR} priority should be used for observing
 * events without modifying them.
 */
public enum EventPriority {
    /** Highest priority, called first */
    HIGHEST,
    /** High priority */
    HIGH,
    /** Normal priority (default) */
    NORMAL,
    /** Low priority */
    LOW,
    /** Lowest priority, called last */
    LOWEST,
    /** Monitor priority - for observing only */
    MONITOR
}
