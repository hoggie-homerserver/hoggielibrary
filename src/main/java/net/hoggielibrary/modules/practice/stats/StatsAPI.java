package net.hoggielibrary.modules.practice.stats;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Statistics API for tracking practice player stats.
 */
public final class StatsAPI {

    private final Map<UUID, PlayerStats> stats = new ConcurrentHashMap<>();

    /**
     * Gets or creates stats for a player.
     *
     * @param playerUuid the player UUID
     * @return the player stats
     */
    public PlayerStats getStats(UUID playerUuid) {
        return stats.computeIfAbsent(playerUuid, k -> new PlayerStats(playerUuid));
    }

    /**
     * Records a win for a player.
     *
     * @param playerUuid the player UUID
     */
    public void recordWin(UUID playerUuid) {
        getStats(playerUuid).incrementWins();
    }

    /**
     * Records a loss for a player.
     *
     * @param playerUuid the player UUID
     */
    public void recordLoss(UUID playerUuid) {
        getStats(playerUuid).incrementLosses();
    }

    /**
     * Records a kill for a player.
     *
     * @param playerUuid the player UUID
     */
    public void recordKill(UUID playerUuid) {
        getStats(playerUuid).incrementKills();
    }

    /**
     * Records a death for a player.
     *
     * @param playerUuid the player UUID
     */
    public void recordDeath(UUID playerUuid) {
        getStats(playerUuid).incrementDeaths();
    }

    /**
     * Gets the win/loss ratio for a player.
     *
     * @param playerUuid the player UUID
     * @return the W/L ratio
     */
    public double getWinLossRatio(UUID playerUuid) {
        PlayerStats s = getStats(playerUuid);
        return s.getLosses() == 0 ? (double) s.getWins() : (double) s.getWins() / s.getLosses();
    }

    /**
     * Gets the kill/death ratio for a player.
     *
     * @param playerUuid the player UUID
     * @return the K/D ratio
     */
    public double getKillDeathRatio(UUID playerUuid) {
        PlayerStats s = getStats(playerUuid);
        return s.getDeaths() == 0 ? (double) s.getKills() : (double) s.getKills() / s.getDeaths();
    }
}
