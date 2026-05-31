package net.hoggielibrary.modules.practice.timer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Timer API for tracking elapsed time in practice sessions.
 */
public final class TimerAPI {

    private final Map<UUID, Long> startTimes = new ConcurrentHashMap<>();
    private final Map<UUID, Long> pausedTimes = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> paused = new ConcurrentHashMap<>();

    /**
     * Starts a timer for a player.
     *
     * @param playerUuid the player UUID
     */
    public void start(UUID playerUuid) {
        startTimes.put(playerUuid, System.currentTimeMillis());
        paused.put(playerUuid, false);
    }

    /**
     * Stops the timer and returns elapsed time.
     *
     * @param playerUuid the player UUID
     * @return elapsed time in milliseconds
     */
    public long stop(UUID playerUuid) {
        Long start = startTimes.remove(playerUuid);
        paused.remove(playerUuid);
        Long pauseTime = pausedTimes.remove(playerUuid);
        if (start == null) return 0;
        long elapsed = System.currentTimeMillis() - start;
        return pauseTime != null ? elapsed - pauseTime : elapsed;
    }

    /**
     * Pauses the timer.
     *
     * @param playerUuid the player UUID
     */
    public void pause(UUID playerUuid) {
        if (paused.put(playerUuid, true) == Boolean.FALSE) {
            pausedTimes.put(playerUuid, System.currentTimeMillis());
        }
    }

    /**
     * Resumes the timer from pause.
     *
     * @param playerUuid the player UUID
     */
    public void resume(UUID playerUuid) {
        if (paused.put(playerUuid, false) == Boolean.TRUE) {
            Long pauseStart = pausedTimes.remove(playerUuid);
            if (pauseStart != null && startTimes.containsKey(playerUuid)) {
                startTimes.put(playerUuid,
                        startTimes.get(playerUuid) + (System.currentTimeMillis() - pauseStart));
            }
        }
    }

    /**
     * Gets the current elapsed time without stopping.
     *
     * @param playerUuid the player UUID
     * @return elapsed time in milliseconds
     */
    public long getElapsed(UUID playerUuid) {
        Long start = startTimes.get(playerUuid);
        if (start == null) return 0;
        if (paused.getOrDefault(playerUuid, false)) {
            Long pauseStart = pausedTimes.get(playerUuid);
            return pauseStart != null ? pauseStart - start : 0;
        }
        return System.currentTimeMillis() - start;
    }

    /**
     * Formats elapsed time as a string (MM:SS.ms).
     *
     * @param playerUuid the player UUID
     * @return formatted time string
     */
    public String getFormattedTime(UUID playerUuid) {
        long elapsed = getElapsed(playerUuid);
        long minutes = (elapsed / 1000) / 60;
        long seconds = (elapsed / 1000) % 60;
        long millis = elapsed % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }

    /**
     * Returns whether a player's timer is running.
     *
     * @param playerUuid the player UUID
     * @return true if timer is running
     */
    public boolean isRunning(UUID playerUuid) {
        return startTimes.containsKey(playerUuid) && !paused.getOrDefault(playerUuid, false);
    }
}
