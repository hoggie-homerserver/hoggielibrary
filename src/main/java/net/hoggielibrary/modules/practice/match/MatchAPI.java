package net.hoggielibrary.modules.practice.match;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Match API for managing practice matches.
 */
public final class MatchAPI {

    private final Map<String, Match> matches = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerMatchMap = new ConcurrentHashMap<>();

    /**
     * Creates a new match.
     *
     * @param matchId the match ID
     * @param player1 first player UUID
     * @param player2 second player UUID
     * @param arena the arena name
     * @return the created match
     */
    public Match createMatch(String matchId, UUID player1, UUID player2, String arena) {
        Match match = new Match(matchId, player1, player2, arena);
        matches.put(matchId, match);
        playerMatchMap.put(player1, matchId);
        playerMatchMap.put(player2, matchId);
        HoggieLogger.info("Match created: {} in arena {}", matchId, arena);
        return match;
    }

    /**
     * Ends a match.
     *
     * @param matchId the match ID
     * @param winner the winner UUID
     */
    public void endMatch(String matchId, UUID winner) {
        Match match = matches.get(matchId);
        if (match != null) {
            match.setWinner(winner);
            match.setState(MatchState.FINISHED);
            playerMatchMap.remove(match.getPlayer1());
            playerMatchMap.remove(match.getPlayer2());
        }
    }

    /**
     * Gets a match by ID.
     *
     * @param matchId the match ID
     * @return the match, or null
     */
    public Match getMatch(String matchId) {
        return matches.get(matchId);
    }

    /**
     * Gets the match a player is in.
     *
     * @param playerUuid the player UUID
     * @return the match, or null
     */
    public Match getPlayerMatch(UUID playerUuid) {
        String matchId = playerMatchMap.get(playerUuid);
        return matchId != null ? matches.get(matchId) : null;
    }

    /**
     * Returns whether a player is in a match.
     *
     * @param playerUuid the player UUID
     * @return true if in a match
     */
    public boolean isInMatch(UUID playerUuid) {
        return playerMatchMap.containsKey(playerUuid);
    }
}
