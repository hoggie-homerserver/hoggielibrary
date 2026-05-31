package net.hoggielibrary.modules.practice.checkpoint;

import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Checkpoint API for tracking and restoring player positions.
 */
public final class CheckpointAPI {

    private final Map<UUID, List<Checkpoint>> playerCheckpoints = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> playerCurrentCheckpoint = new ConcurrentHashMap<>();

    /**
     * Saves a checkpoint for a player.
     *
     * @param playerUuid the player UUID
     * @param position the checkpoint position
     * @param yaw the player yaw
     * @param pitch the player pitch
     */
    public void saveCheckpoint(UUID playerUuid, Vec3d position, float yaw, float pitch) {
        playerCheckpoints.computeIfAbsent(playerUuid, k -> new ArrayList<>())
                .add(new Checkpoint(position, yaw, pitch));
    }

    /**
     * Gets all checkpoints for a player.
     *
     * @param playerUuid the player UUID
     * @return list of checkpoints
     */
    public List<Checkpoint> getCheckpoints(UUID playerUuid) {
        return playerCheckpoints.getOrDefault(playerUuid, List.of());
    }

    /**
     * Gets the latest checkpoint for a player.
     *
     * @param playerUuid the player UUID
     * @return the latest checkpoint, or null
     */
    public Checkpoint getLatestCheckpoint(UUID playerUuid) {
        List<Checkpoint> checkpoints = playerCheckpoints.get(playerUuid);
        if (checkpoints == null || checkpoints.isEmpty()) return null;
        return checkpoints.get(checkpoints.size() - 1);
    }

    /**
     * Clears all checkpoints for a player.
     *
     * @param playerUuid the player UUID
     */
    public void clearCheckpoints(UUID playerUuid) {
        playerCheckpoints.remove(playerUuid);
        playerCurrentCheckpoint.remove(playerUuid);
    }
}
