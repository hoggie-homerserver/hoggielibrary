package net.hoggielibrary.modules.rpg.perk;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Perk API for RPG perk/ability system.
 */
public final class PerkAPI {

    private final Map<String, Perk> perks = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> playerPerks = new ConcurrentHashMap<>();

    /**
     * Registers a new perk.
     *
     * @param id the perk ID
     * @param name the display name
     * @param description the perk description
     * @return the created perk
     */
    public Perk registerPerk(String id, String name, String description) {
        Perk perk = new Perk(id, name, description);
        perks.put(id, perk);
        return perk;
    }

    /**
     * Adds a perk to a player.
     *
     * @param perkId the perk ID
     * @param playerUuid the player UUID
     */
    public void addPerk(String perkId, UUID playerUuid) {
        if (perks.containsKey(perkId)) {
            playerPerks.computeIfAbsent(playerUuid, k -> ConcurrentHashMap.newKeySet()).add(perkId);
            HoggieLogger.info("Perk added: {} to player {}", perkId, playerUuid);
        }
    }

    /**
     * Removes a perk from a player.
     *
     * @param perkId the perk ID
     * @param playerUuid the player UUID
     */
    public void removePerk(String perkId, UUID playerUuid) {
        Set<String> playerPerkSet = playerPerks.get(playerUuid);
        if (playerPerkSet != null) {
            playerPerkSet.remove(perkId);
        }
    }

    /**
     * Checks if a player has a perk.
     *
     * @param perkId the perk ID
     * @param playerUuid the player UUID
     * @return true if the player has the perk
     */
    public boolean hasPerk(String perkId, UUID playerUuid) {
        Set<String> playerPerkSet = playerPerks.get(playerUuid);
        return playerPerkSet != null && playerPerkSet.contains(perkId);
    }

    /**
     * Gets all perks for a player.
     *
     * @param playerUuid the player UUID
     * @return set of perk IDs
     */
    public Set<String> getPlayerPerks(UUID playerUuid) {
        return playerPerks.getOrDefault(playerUuid, Set.of());
    }

    /**
     * Gets a perk by ID.
     *
     * @param perkId the perk ID
     * @return the perk, or null
     */
    public Perk getPerk(String perkId) {
        return perks.get(perkId);
    }

    /**
     * Returns all perks.
     *
     * @return map of perk IDs to perks
     */
    public Map<String, Perk> getPerks() {
        return perks;
    }

    public static final class Perk {
        private final String id;
        private final String name;
        private final String description;
        private int requiredLevel;
        private PerkType type = PerkType.PASSIVE;

        public Perk(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getRequiredLevel() { return requiredLevel; }
        public PerkType getType() { return type; }

        public void setRequiredLevel(int level) { this.requiredLevel = level; }
        public void setType(PerkType type) { this.type = type; }
    }

    public enum PerkType {
        PASSIVE,
        ACTIVE,
        ULTIMATE
    }
}
