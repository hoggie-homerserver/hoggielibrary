package net.hoggielibrary.modules.pvp.cps;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Tracks clicks per second (CPS) for combat analysis.
 *
 * <p>Provides a rolling window of click timestamps to calculate
 * current, average, and maximum CPS values.
 */
public final class CpsTracker {

    private final Deque<Long> leftClicks = new ArrayDeque<>();
    private final Deque<Long> rightClicks = new ArrayDeque<>();
    private static final long WINDOW_MS = 1000L;

    /**
     * Records a left click.
     */
    public void recordLeftClick() {
        addClick(leftClicks);
    }

    /**
     * Records a right click.
     */
    public void recordRightClick() {
        addClick(rightClicks);
    }

    /**
     * Returns the current left-click CPS.
     *
     * @return clicks per second
     */
    public int getLeftCps() {
        return getCps(leftClicks);
    }

    /**
     * Returns the current right-click CPS.
     *
     * @return clicks per second
     */
    public int getRightCps() {
        return getCps(rightClicks);
    }

    /**
     * Returns the total CPS (left + right).
     *
     * @return total clicks per second
     */
    public int getTotalCps() {
        return getLeftCps() + getRightCps();
    }

    /**
     * Returns the maximum left CPS in the current window.
     *
     * @return the max left CPS
     */
    public int getMaxLeftCps() {
        return (int) Math.ceil(leftClicks.size() * 1000.0 / WINDOW_MS);
    }

    /**
     * Returns the maximum right CPS in the current window.
     *
     * @return the max right CPS
     */
    public int getMaxRightCps() {
        return (int) Math.ceil(rightClicks.size() * 1000.0 / WINDOW_MS);
    }

    /**
     * Clears all click data.
     */
    public void reset() {
        leftClicks.clear();
        rightClicks.clear();
    }

    private void addClick(Deque<Long> clicks) {
        long now = System.currentTimeMillis();
        clicks.addLast(now);
        while (!clicks.isEmpty() && clicks.peekFirst() < now - WINDOW_MS) {
            clicks.pollFirst();
        }
    }

    private int getCps(Deque<Long> clicks) {
        long now = System.currentTimeMillis();
        while (!clicks.isEmpty() && clicks.peekFirst() < now - WINDOW_MS) {
            clicks.pollFirst();
        }
        return clicks.size();
    }
}
