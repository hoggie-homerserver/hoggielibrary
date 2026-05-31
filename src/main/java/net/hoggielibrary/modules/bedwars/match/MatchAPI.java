package net.hoggielibrary.modules.bedwars.match;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bedwars Match API for game lifecycle management.
 */
public final class MatchAPI {

    private final Map<String, BedwarsMatch> matches = new ConcurrentHashMap<>();

    /**
     * Creates a new Bedwars match.
     *
     * @param matchId the match ID
     * @return the created match
     */
    public BedwarsMatch createMatch(String matchId) {
        BedwarsMatch match = new BedwarsMatch(matchId);
        matches.put(matchId, match);
        HoggieLogger.info("Bedwars match created: {}", matchId);
        return match;
    }

    /**
     * Starts a match.
     *
     * @param matchId the match ID
     */
    public void startMatch(String matchId) {
        BedwarsMatch match = matches.get(matchId);
        if (match != null) {
            match.setState(MatchState.IN_PROGRESS);
            HoggieLogger.info("Bedwars match started: {}", matchId);
        }
    }

    /**
     * Ends a match with a winning team.
     *
     * @param matchId the match ID
     * @param winningTeam the winning team name
     */
    public void endMatch(String matchId, String winningTeam) {
        BedwarsMatch match = matches.get(matchId);
        if (match != null) {
            match.setWinner(winningTeam);
            match.setState(MatchState.FINISHED);
            HoggieLogger.info("Bedwars match ended: {} won by {}", matchId, winningTeam);
        }
    }

    /**
     * Gets a match by ID.
     *
     * @param matchId the match ID
     * @return the match, or null
     */
    public BedwarsMatch getMatch(String matchId) {
        return matches.get(matchId);
    }

    /**
     * Gets the match a player is in.
     *
     * @param playerUuid the player UUID
     * @return the match, or null
     */
    public BedwarsMatch getPlayerMatch(UUID playerUuid) {
        for (BedwarsMatch match : matches.values()) {
            if (match.hasPlayer(playerUuid)) return match;
        }
        return null;
    }

    /**
     * Removes a match.
     *
     * @param matchId the match ID
     */
    public void removeMatch(String matchId) {
        matches.remove(matchId);
    }

    public static final class BedwarsMatch {
        private final String matchId;
        private MatchState state = MatchState.WAITING;
        private String winner;
        private final Map<String, Integer> teamScores = new ConcurrentHashMap<>();

        public BedwarsMatch(String matchId) { this.matchId = matchId; }

        public String getMatchId() { return matchId; }
        public MatchState getState() { return state; }
        public String getWinner() { return winner; }
        public Map<String, Integer> getTeamScores() { return teamScores; }

        public void setState(MatchState state) { this.state = state; }
        public void setWinner(String winner) { this.winner = winner; }

        public boolean hasPlayer(UUID playerUuid) { return false; } // Simplified
        public void addTeamScore(String team, int score) {
            teamScores.merge(team, score, Integer::sum);
        }
    }

    public enum MatchState {
        WAITING,
        STARTING,
        IN_PROGRESS,
        FINISHED,
        CANCELLED
    }
}
