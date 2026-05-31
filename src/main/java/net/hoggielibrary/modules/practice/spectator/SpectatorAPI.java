package net.hoggielibrary.modules.practice.spectator;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spectator API for managing match spectators.
 */
public final class SpectatorAPI {

    private final Map<String, Set<UUID>> matchSpectators = new ConcurrentHashMap<>();

    /**
     * Adds a spectator to a match.
     *
     * @param matchId the match ID
     * @param spectatorUuid the spectator UUID
     */
    public void addSpectator(String matchId, UUID spectatorUuid) {
        matchSpectators.computeIfAbsent(matchId, k -> ConcurrentHashMap.newKeySet()).add(spectatorUuid);
        HoggieLogger.debug("Spectator {} added to match {}", spectatorUuid, matchId);
    }

    /**
     * Removes a spectator from a match.
     *
     * @param matchId the match ID
     * @param spectatorUuid the spectator UUID
     */
    public void removeSpectator(String matchId, UUID spectatorUuid) {
        Set<UUID> spectators = matchSpectators.get(matchId);
        if (spectators != null) {
            spectators.remove(spectatorUuid);
        }
    }

    /**
     * Gets all spectators for a match.
     *
     * @param matchId the match ID
     * @return set of spectator UUIDs
     */
    public Set<UUID> getSpectators(String matchId) {
        return matchSpectators.getOrDefault(matchId, Set.of());
    }

    /**
     * Returns the number of spectators for a match.
     *
     * @param matchId the match ID
     * @return spectator count
     */
    public int getSpectatorCount(String matchId) {
        return getSpectators(matchId).size();
    }

    /**
     * Clears all spectators for a match.
     *
     * @param matchId the match ID
     */
    public void clearSpectators(String matchId) {
        matchSpectators.remove(matchId);
    }
}
