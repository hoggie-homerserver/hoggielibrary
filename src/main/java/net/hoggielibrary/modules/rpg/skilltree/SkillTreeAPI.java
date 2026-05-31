package net.hoggielibrary.modules.rpg.skilltree;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Skill Tree API for creating RPG skill trees.
 */
public final class SkillTreeAPI {

    private final Map<String, Skill> skills = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> playerSkills = new ConcurrentHashMap<>();

    /**
     * Creates a new skill.
     *
     * @param id the skill ID
     * @param name the skill name
     * @param description the skill description
     * @param maxLevel the maximum level
     * @param costPerLevel the cost per level
     * @return the created skill
     */
    public Skill createSkill(String id, String name, String description, int maxLevel, int costPerLevel) {
        Skill skill = new Skill(id, name, description, maxLevel, costPerLevel);
        skills.put(id, skill);
        return skill;
    }

    /**
     * Adds a prerequisite relationship between skills.
     *
     * @param skillId the skill ID
     * @param prerequisiteId the prerequisite skill ID
     */
    public void addPrerequisite(String skillId, String prerequisiteId) {
        Skill skill = skills.get(skillId);
        Skill prerequisite = skills.get(prerequisiteId);
        if (skill != null && prerequisite != null) {
            skill.addPrerequisite(prerequisiteId);
        }
    }

    /**
     * Unlocks a skill for a player.
     *
     * @param skillId the skill ID
     * @param playerUuid the player UUID
     * @return true if unlocked
     */
    public boolean unlockSkill(String skillId, UUID playerUuid) {
        Skill skill = skills.get(skillId);
        if (skill == null) return false;

        if (!skill.hasPrerequisites()) {
            playerSkills.computeIfAbsent(playerUuid, k -> ConcurrentHashMap.newKeySet()).add(skillId);
            HoggieLogger.info("Skill unlocked: {} for player {}", skillId, playerUuid);
            return true;
        }
        return false;
    }

    /**
     * Checks if a player has a skill.
     *
     * @param skillId the skill ID
     * @param playerUuid the player UUID
     * @return true if the player has the skill
     */
    public boolean hasSkill(String skillId, UUID playerUuid) {
        Set<String> unlocked = playerSkills.get(playerUuid);
        return unlocked != null && unlocked.contains(skillId);
    }

    /**
     * Gets a skill by ID.
     *
     * @param skillId the skill ID
     * @return the skill, or null
     */
    public Skill getSkill(String skillId) {
        return skills.get(skillId);
    }

    /**
     * Gets all unlocked skills for a player.
     *
     * @param playerUuid the player UUID
     * @return set of unlocked skill IDs
     */
    public Set<String> getUnlockedSkills(UUID playerUuid) {
        return playerSkills.getOrDefault(playerUuid, Set.of());
    }

    public static final class Skill {
        private final String id;
        private final String name;
        private final String description;
        private final int maxLevel;
        private final int costPerLevel;
        private final Set<String> prerequisites = new HashSet<>();

        public Skill(String id, String name, String description, int maxLevel, int costPerLevel) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.maxLevel = maxLevel;
            this.costPerLevel = costPerLevel;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getMaxLevel() { return maxLevel; }
        public int getCostPerLevel() { return costPerLevel; }
        public Set<String> getPrerequisites() { return prerequisites; }

        public void addPrerequisite(String skillId) { prerequisites.add(skillId); }
        public boolean hasPrerequisites() { return !prerequisites.isEmpty(); }
    }
}
