package net.hoggielibrary.core.scheduler;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Task scheduler for delayed and repeating tasks.
 *
 * <p>Provides a Minecraft-tick-aware scheduling system with support for
 * delayed tasks, repeating tasks, and async tasks. All scheduled tasks
 * receive a {@link ScheduledTask} handle that can be used to cancel or
 * inspect the task.
 *
 * <p>Usage:
 * <pre>{@code
 * // Run once after 20 ticks (1 second)
 * Hoggie.scheduler.runLater(() -> {}, 20);
 *
 * // Run every 5 ticks indefinitely
 * Hoggie.scheduler.runRepeating(() -> {}, 5);
 *
 * // Run async after 10 ticks
 * Hoggie.scheduler.runLaterAsync(() -> {}, 10);
 * }</pre>
 */
public final class HoggieScheduler {

    private final PriorityQueue<ScheduledTask> taskQueue = new PriorityQueue<>(
            (a, b) -> Long.compare(a.scheduledTick, b.scheduledTick)
    );
    private final Map<Integer, ScheduledTask> taskMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors(),
            r -> {
                Thread t = new Thread(r, "Hoggie-Scheduler");
                t.setDaemon(true);
                return t;
            }
    );
    private final AtomicInteger taskIdCounter = new AtomicInteger(0);
    private long currentTick = 0;
    private boolean running = true;

    /**
     * Creates a new scheduler instance.
     */
    public HoggieScheduler() {
    }

    /**
     * Advances the scheduler by one tick and executes due tasks.
     * This should be called once per Minecraft tick (20 times per second).
     */
    public void tick() {
        if (!running) return;
        currentTick++;
        while (!taskQueue.isEmpty() && taskQueue.peek().scheduledTick <= currentTick) {
            ScheduledTask task = taskQueue.poll();
            if (task.cancelled) continue;
            taskMap.remove(task.id);
            try {
                if (task.runnable != null) {
                    task.runnable.run();
                } else if (task.consumer != null) {
                    task.consumer.accept(task);
                }
            } catch (Exception e) {
                HoggieLogger.error("Error executing scheduled task {}", task.id, e);
            }
            if (task.repeating && !task.cancelled) {
                task.scheduledTick = currentTick + task.interval;
                taskMap.put(task.id, task);
                taskQueue.offer(task);
            }
        }
    }

    /**
     * Schedules a task to run after a specified number of ticks.
     *
     * @param runnable the task to run
     * @param delayTicks the delay in ticks (20 ticks = 1 second)
     * @return the scheduled task handle
     */
    public ScheduledTask runLater(Runnable runnable, long delayTicks) {
        ScheduledTask task = new ScheduledTask(
                taskIdCounter.incrementAndGet(),
                runnable,
                null,
                currentTick + delayTicks,
                0,
                false
        );
        taskMap.put(task.id, task);
        taskQueue.offer(task);
        return task;
    }

    /**
     * Schedules a task to run after a specified delay with a consumer callback.
     *
     * @param consumer the consumer that receives the task handle
     * @param delayTicks the delay in ticks
     * @return the scheduled task handle
     */
    public ScheduledTask runLater(Consumer<ScheduledTask> consumer, long delayTicks) {
        ScheduledTask task = new ScheduledTask(
                taskIdCounter.incrementAndGet(),
                null,
                consumer,
                currentTick + delayTicks,
                0,
                false
        );
        taskMap.put(task.id, task);
        taskQueue.offer(task);
        return task;
    }

    /**
     * Schedules a repeating task.
     *
     * @param runnable the task to run repeatedly
     * @param intervalTicks the interval in ticks between executions
     * @return the scheduled task handle
     */
    public ScheduledTask runRepeating(Runnable runnable, long intervalTicks) {
        return runRepeating(runnable, intervalTicks, intervalTicks);
    }

    /**
     * Schedules a repeating task with an initial delay.
     *
     * @param runnable the task to run repeatedly
     * @param delayTicks the initial delay in ticks
     * @param intervalTicks the interval in ticks between executions
     * @return the scheduled task handle
     */
    public ScheduledTask runRepeating(Runnable runnable, long delayTicks, long intervalTicks) {
        ScheduledTask task = new ScheduledTask(
                taskIdCounter.incrementAndGet(),
                runnable,
                null,
                currentTick + delayTicks,
                intervalTicks,
                true
        );
        taskMap.put(task.id, task);
        taskQueue.offer(task);
        return task;
    }

    /**
     * Schedules a task to run asynchronously after a delay.
     *
     * @param runnable the task to run asynchronously
     * @param delayTicks the delay in ticks
     * @return the scheduled task handle
     */
    public ScheduledTask runLaterAsync(Runnable runnable, long delayTicks) {
        ScheduledTask task = new ScheduledTask(
                taskIdCounter.incrementAndGet(),
                runnable,
                null,
                currentTick + delayTicks,
                0,
                false
        );
        task.async = true;
        taskMap.put(task.id, task);
        if (delayTicks <= 0) {
            executor.submit(() -> {
                try {
                    runnable.run();
                } catch (Exception e) {
                    HoggieLogger.error("Error in async task {}", task.id, e);
                }
            });
        } else {
            executor.schedule(() -> {
                try {
                    runnable.run();
                } catch (Exception e) {
                    HoggieLogger.error("Error in async task {}", task.id, e);
                }
            }, delayTicks * 50, TimeUnit.MILLISECONDS);
        }
        return task;
    }

    /**
     * Schedules a repeating task to run asynchronously.
     *
     * @param runnable the task to run asynchronously
     * @param intervalTicks the interval in ticks
     * @return the scheduled task handle
     */
    public ScheduledTask runRepeatingAsync(Runnable runnable, long intervalTicks) {
        ScheduledTask task = new ScheduledTask(
                taskIdCounter.incrementAndGet(),
                runnable,
                null,
                currentTick + intervalTicks,
                intervalTicks,
                true
        );
        task.async = true;
        taskMap.put(task.id, task);
        executor.scheduleAtFixedRate(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                HoggieLogger.error("Error in async repeating task {}", task.id, e);
            }
        }, intervalTicks * 50, intervalTicks * 50, TimeUnit.MILLISECONDS);
        return task;
    }

    /**
     * Cancels a task by its ID.
     *
     * @param taskId the task ID to cancel
     * @return true if a task was cancelled
     */
    public boolean cancel(int taskId) {
        ScheduledTask task = taskMap.get(taskId);
        if (task != null) {
            task.cancel();
            return true;
        }
        return false;
    }

    /**
     * Returns whether the scheduler has pending tasks.
     *
     * @return true if there are pending tasks
     */
    public boolean hasPendingTasks() {
        return !taskQueue.isEmpty();
    }

    /**
     * Returns the number of pending tasks.
     *
     * @return the count of pending tasks
     */
    public int getPendingTaskCount() {
        return taskQueue.size();
    }

    /**
     * Returns the current tick count.
     *
     * @return the current tick
     */
    public long getCurrentTick() {
        return currentTick;
    }

    /**
     * Shuts down the scheduler, cancelling all tasks.
     */
    public void shutdown() {
        running = false;
        taskQueue.clear();
        taskMap.clear();
        executor.shutdownNow();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                HoggieLogger.warn("Scheduler executor did not terminate in time");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
