package net.hoggielibrary.modules.developer.profiling;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Profiling tools for performance analysis.
 */
public final class ProfilingTools {

    private final Map<String, ProfileEntry> profiles = new ConcurrentHashMap<>();
    private boolean enabled;

    /**
     * Starts a profiling session.
     */
    public void start() {
        enabled = true;
        profiles.clear();
        HoggieLogger.info("Profiling started");
    }

    /**
     * Stops profiling and logs results.
     */
    public void stop() {
        enabled = false;
        HoggieLogger.info("=== Profiling Results ===");
        profiles.forEach((name, entry) ->
                HoggieLogger.info("{}: {} calls, avg {}ms, total {}ms",
                        name, entry.count, entry.getAverageMs(), entry.getTotalMs())
        );
    }

    /**
     * Records a sample point.
     *
     * @param name the sample name
     */
    public void sample(String name) {
        if (!enabled) return;
        profiles.computeIfAbsent(name, k -> new ProfileEntry()).addSample(System.nanoTime());
    }

    /**
     * Records the time taken for an operation.
     *
     * @param name the operation name
     * @param startTimeNanos the start time in nanoseconds
     */
    public void recordTiming(String name, long startTimeNanos) {
        if (!enabled) return;
        long elapsed = System.nanoTime() - startTimeNanos;
        profiles.computeIfAbsent(name, k -> new ProfileEntry()).addTiming(elapsed);
    }

    /**
     * Returns whether profiling is active.
     *
     * @return true if profiling
     */
    public boolean isEnabled() {
        return enabled;
    }

    private static class ProfileEntry {
        long totalTimeNanos;
        int count;

        void addSample(long timeNanos) {
            count++;
        }

        void addTiming(long nanos) {
            totalTimeNanos += nanos;
            count++;
        }

        double getAverageMs() {
            return count > 0 ? (totalTimeNanos / 1_000_000.0) / count : 0;
        }

        double getTotalMs() {
            return totalTimeNanos / 1_000_000.0;
        }
    }
}
