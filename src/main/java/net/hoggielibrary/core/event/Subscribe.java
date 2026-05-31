package net.hoggielibrary.core.event;

import java.lang.annotation.*;

/**
 * Marks a method as an event handler for the {@link HoggieEventBus}.
 *
 * <p>The method must accept exactly one parameter: the event type.
 *
 * <p>Usage:
 * <pre>{@code
 * @Subscribe(priority = EventPriority.HIGH)
 * public void onPlayerJoin(PlayerJoinEvent event) {
 *     // handle event
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Subscribe {

    /**
     * The priority of this event handler.
     *
     * @return the priority level
     */
    EventPriority priority() default EventPriority.NORMAL;
}
