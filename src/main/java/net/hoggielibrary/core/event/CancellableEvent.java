package net.hoggielibrary.core.event;

/**
 * Base interface for events that can be cancelled.
 *
 * <p>When a cancellable event is cancelled, the default behaviour
 * associated with the event will be prevented.
 */
public interface CancellableEvent {

    /**
     * Cancels this event.
     */
    void cancel();

    /**
     * Returns whether this event has been cancelled.
     *
     * @return true if cancelled
     */
    boolean isCancelled();
}
