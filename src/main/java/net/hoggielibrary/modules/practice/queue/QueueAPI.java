package net.hoggielibrary.modules.practice.queue;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Queue API for managing player queues for matchmaking.
 */
public final class QueueAPI {

    private final Map<String, ConcurrentLinkedQueue<UUID>> queues = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerQueueMap = new ConcurrentHashMap<>();

    /**
     * Adds a player to a queue.
     *
     * @param playerUuid the player UUID
     * @param queueName the queue name
     */
    public void addToQueue(UUID playerUuid, String queueName) {
        queues.computeIfAbsent(queueName, k -> new ConcurrentLinkedQueue<>()).add(playerUuid);
        playerQueueMap.put(playerUuid, queueName);
        HoggieLogger.debug("Player {} added to queue {}", playerUuid, queueName);
    }

    /**
     * Removes a player from all queues.
     *
     * @param playerUuid the player UUID
     */
    public void removeFromQueue(UUID playerUuid) {
        String queueName = playerQueueMap.remove(playerUuid);
        if (queueName != null) {
            ConcurrentLinkedQueue<UUID> queue = queues.get(queueName);
            if (queue != null) {
                queue.remove(playerUuid);
            }
        }
    }

    /**
     * Returns whether a player is in a queue.
     *
     * @param playerUuid the player UUID
     * @return true if in any queue
     */
    public boolean isInQueue(UUID playerUuid) {
        return playerQueueMap.containsKey(playerUuid);
    }

    /**
     * Gets the queue a player is in.
     *
     * @param playerUuid the player UUID
     * @return the queue name, or null
     */
    public String getPlayerQueue(UUID playerUuid) {
        return playerQueueMap.get(playerUuid);
    }

    /**
     * Pops the next player from a queue.
     *
     * @param queueName the queue name
     * @return the player UUID, or null if empty
     */
    public UUID popFromQueue(String queueName) {
        ConcurrentLinkedQueue<UUID> queue = queues.get(queueName);
        if (queue == null || queue.isEmpty()) return null;
        UUID player = queue.poll();
        if (player != null) {
            playerQueueMap.remove(player);
        }
        return player;
    }

    /**
     * Returns the number of players in a queue.
     *
     * @param queueName the queue name
     * @return queue size
     */
    public int getQueueSize(String queueName) {
        ConcurrentLinkedQueue<UUID> queue = queues.get(queueName);
        return queue != null ? queue.size() : 0;
    }

    /**
     * Returns all queue names.
     *
     * @return set of queue names
     */
    public Set<String> getQueueNames() {
        return queues.keySet();
    }
}
