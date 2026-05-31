package net.hoggielibrary.modules.rpg.stats;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RPG Stats API for player attribute management.
 */
public final class RpgStatsAPI {

    private final Map<UUID, RpgPlayerStats> playerStats = new ConcurrentHashMap<>();

    /**
     * Gets or creates stats for a player.
     *
     * @param playerUuid the player UUID
     * @return the player stats
     */
    public RpgPlayerStats getStats(UUID playerUuid) {
        return playerStats.computeIfAbsent(playerUuid, k -> new RpgPlayerStats());
    }

    /**
     * Adds experience points to a player.
     *
     * @param playerUuid the player UUID
     * @param amount the XP amount
     */
    public void addExperience(UUID playerUuid, int amount) {
        RpgPlayerStats stats = getStats(playerUuid);
        stats.addExperience(amount);
    }

    /**
     * Gets a player's level.
     *
     * @param playerUuid the player UUID
     * @return the player level
     */
    public int getLevel(UUID playerUuid) {
        return getStats(playerUuid).getLevel();
    }

    public static final class RpgPlayerStats {
        private int level = 1;
        private int experience;
        private int strength;
        private int agility;
        private int intelligence;
        private int vitality;

        public int getLevel() { return level; }
        public int getExperience() { return experience; }
        public int getStrength() { return strength; }
        public int getAgility() { return agility; }
        public int getIntelligence() { return intelligence; }
        public int getVitality() { return vitality; }

        public void setLevel(int level) { this.level = level; }
        public void setStrength(int value) { this.strength = value; }
        public void setAgility(int value) { this.agility = value; }
        public void setIntelligence(int value) { this.intelligence = value; }
        public void setVitality(int value) { this.vitality = value; }

        public void addExperience(int amount) {
            this.experience += amount;
            int xpForNext = level * 100;
            while (this.experience >= xpForNext) {
                this.experience -= xpForNext;
                this.level++;
                xpForNext = level * 100;
            }
        }
    }
}
