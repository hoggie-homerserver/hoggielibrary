package net.hoggielibrary.modules.practice.arena;

import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Arena API for managing practice arenas.
 *
 * <p>Provides arena creation, configuration, and state management.
 */
public final class ArenaAPI {

    private final Map<String, Arena> arenas = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerArenaMap = new ConcurrentHashMap<>();

    /**
     * Creates a new arena.
     *
     * @param name the arena name
     * @param pos1 the first corner
     * @param pos2 the second corner
     * @return the created arena
     */
    public Arena createArena(String name, BlockPos pos1, BlockPos pos2) {
        Arena arena = new Arena(name, pos1, pos2);
        arenas.put(name, arena);
        return arena;
    }

    /**
     * Removes an arena.
     *
     * @param name the arena name
     */
    public void removeArena(String name) {
        arenas.remove(name);
    }

    /**
     * Gets an arena by name.
     *
     * @param name the arena name
     * @return the arena, or null
     */
    public Arena getArena(String name) {
        return arenas.get(name);
    }

    /**
     * Returns all arenas.
     *
     * @return map of arena names to arenas
     */
    public Map<String, Arena> getArenas() {
        return arenas;
    }

    /**
     * Assigns a player to an arena.
     *
     * @param playerUuid the player UUID
     * @param arenaName the arena name
     */
    public void assignPlayer(UUID playerUuid, String arenaName) {
        playerArenaMap.put(playerUuid, arenaName);
    }

    /**
     * Removes a player from their arena.
     *
     * @param playerUuid the player UUID
     */
    public void removePlayer(UUID playerUuid) {
        playerArenaMap.remove(playerUuid);
    }

    /**
     * Gets the arena a player is currently in.
     *
     * @param playerUuid the player UUID
     * @return the arena name, or null
     */
    public String getPlayerArena(UUID playerUuid) {
        return playerArenaMap.get(playerUuid);
    }
}
