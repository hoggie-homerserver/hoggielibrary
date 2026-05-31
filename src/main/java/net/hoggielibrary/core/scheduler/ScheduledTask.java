package net.hoggielibrary.core.scheduler;

import java.util.function.Consumer;

/**
 * Represents a scheduled task within the {@link HoggieScheduler}.
 *
 * <p>Provides methods to cancel, inspect, and manage the lifecycle
 * of a scheduled task.
 */
public final class ScheduledTask {

    final int id;
    final Runnable runnable;
    final Consumer<ScheduledTask> consumer;
    long scheduledTick;
    final long interval;
    final boolean repeating;
    boolean cancelled;
    boolean async;

    ScheduledTask(int id, Runnable runnable, Consumer<ScheduledTask> consumer,
                  long scheduledTick, long interval, boolean repeating) {
        this.id = id;
        this.runnable = runnable;
        this.consumer = consumer;
        this.scheduledTick = scheduledTick;
        this.interval = interval;
        this.repeating = repeating;
    }

    /**
     * Returns the unique ID of this task.
     *
     * @return the task ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns whether this task is repeating.
     *
     * @return true if repeating
     */
    public boolean isRepeating() {
        return repeating;
    }

    /**
     * Returns whether this task is async.
     *
     * @return true if async
     */
    public boolean isAsync() {
        return async;
    }

    /**
     * Returns the tick at which this task is scheduled to run.
     *
     * @return the scheduled tick
     */
    public long getScheduledTick() {
        return scheduledTick;
    }

    /**
     * Returns the interval between executions (only meaningful for repeating tasks).
     *
     * @return the interval in ticks
     */
    public long getInterval() {
        return interval;
    }

    /**
     * Cancels this task.
     */
    public void cancel() {
        this.cancelled = true;
    }

    /**
     * Returns whether this task has been cancelled.
     *
     * @return true if cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }
}
