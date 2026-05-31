package net.hoggielibrary.modules.rpg.achievement;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Achievement API for creating RPG achievements.
 */
public final class AchievementAPI {

    private final Map<String, Achievement> achievements = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> playerAchievements = new ConcurrentHashMap<>();

    /**
     * Creates a new achievement.
     *
     * @param id the achievement ID
     * @param name the display name
     * @param description the description
     * @return the created achievement
     */
    public Achievement createAchievement(String id, String name, String description) {
        Achievement achievement = new Achievement(id, name, description);
        achievements.put(id, achievement);
        return achievement;
    }

    /**
     * Awards an achievement to a player.
     *
     * @param achievementId the achievement ID
     * @param playerUuid the player UUID
     */
    public void awardAchievement(String achievementId, UUID playerUuid) {
        playerAchievements.computeIfAbsent(playerUuid, k -> ConcurrentHashMap.newKeySet()).add(achievementId);
        HoggieLogger.info("Achievement awarded: {} to player {}", achievementId, playerUuid);
    }

    /**
     * Checks if a player has an achievement.
     *
     * @param achievementId the achievement ID
     * @param playerUuid the player UUID
     * @return true if the player has the achievement
     */
    public boolean hasAchievement(String achievementId, UUID playerUuid) {
        Set<String> awarded = playerAchievements.get(playerUuid);
        return awarded != null && awarded.contains(achievementId);
    }

    /**
     * Gets an achievement by ID.
     *
     * @param achievementId the achievement ID
     * @return the achievement, or null
     */
    public Achievement getAchievement(String achievementId) {
        return achievements.get(achievementId);
    }

    /**
     * Gets all achievements for a player.
     *
     * @param playerUuid the player UUID
     * @return set of achievement IDs
     */
    public Set<String> getPlayerAchievements(UUID playerUuid) {
        return playerAchievements.getOrDefault(playerUuid, Set.of());
    }

    /**
     * Returns all achievements.
     *
     * @return map of achievement IDs to achievements
     */
    public Map<String, Achievement> getAchievements() {
        return achievements;
    }

    public static final class Achievement {
        private final String id;
        private final String name;
        private final String description;
        private int rewardXP;
        private String iconItem;

        public Achievement(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getRewardXP() { return rewardXP; }
        public String getIconItem() { return iconItem; }

        public void setRewardXP(int xp) { this.rewardXP = xp; }
        public void setIconItem(String item) { this.iconItem = item; }
    }
}
