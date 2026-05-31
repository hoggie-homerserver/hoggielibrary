package net.hoggielibrary.modules.practice.duel;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Duel API for managing 1v1 duels.
 */
public final class DuelAPI {

    private final Map<UUID, Duel> activeDuels = new ConcurrentHashMap<>();

    /**
     * Creates a new duel between two players.
     *
     * @param player1 the first player UUID
     * @param player2 the second player UUID
     * @param arena the arena name
     * @return the created duel
     */
    public Duel createDuel(UUID player1, UUID player2, String arena) {
        Duel duel = new Duel(player1, player2, arena);
        activeDuels.put(player1, duel);
        activeDuels.put(player2, duel);
        HoggieLogger.info("Duel created: {} vs {} in {}", player1, player2, arena);
        return duel;
    }

    /**
     * Ends a duel and declares a winner.
     *
     * @param winner the winner UUID
     * @param loser the loser UUID
     */
    public void endDuel(UUID winner, UUID loser) {
        Duel duel = activeDuels.get(winner);
        if (duel != null && duel.containsPlayer(loser)) {
            duel.setWinner(winner);
            duel.setState(DuelState.FINISHED);
            activeDuels.remove(winner);
            activeDuels.remove(loser);
            HoggieLogger.info("Duel ended: {} defeated {}", winner, loser);
        }
    }

    /**
     * Gets the active duel for a player.
     *
     * @param playerUuid the player UUID
     * @return the duel, or null
     */
    public Duel getDuel(UUID playerUuid) {
        return activeDuels.get(playerUuid);
    }

    /**
     * Returns whether a player is in a duel.
     *
     * @param playerUuid the player UUID
     * @return true if in a duel
     */
    public boolean isInDuel(UUID playerUuid) {
        return activeDuels.containsKey(playerUuid);
    }

    /**
     * Cancels a duel.
     *
     * @param playerUuid one of the duel participants
     */
    public void cancelDuel(UUID playerUuid) {
        Duel duel = activeDuels.remove(playerUuid);
        if (duel != null) {
            activeDuels.remove(duel.getPlayer1());
            activeDuels.remove(duel.getPlayer2());
            duel.setState(DuelState.CANCELLED);
        }
    }
}
