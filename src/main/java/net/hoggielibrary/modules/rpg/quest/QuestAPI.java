package net.hoggielibrary.modules.rpg.quest;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quest API for creating and managing RPG quests.
 */
public final class QuestAPI {

    private final Map<String, Quest> quests = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> playerQuests = new ConcurrentHashMap<>();

    /**
     * Creates a new quest.
     *
     * @param id the quest ID
     * @param name the quest name
     * @param description the quest description
     * @return the created quest
     */
    public Quest createQuest(String id, String name, String description) {
        Quest quest = new Quest(id, name, description);
        quests.put(id, quest);
        return quest;
    }

    /**
     * Assigns a quest to a player.
     *
     * @param questId the quest ID
     * @param playerUuid the player UUID
     */
    public void assignQuest(String questId, UUID playerUuid) {
        playerQuests.computeIfAbsent(playerUuid, k -> ConcurrentHashMap.newKeySet()).add(questId);
        HoggieLogger.info("Quest assigned: {} to player {}", questId, playerUuid);
    }

    /**
     * Completes a quest for a player.
     *
     * @param questId the quest ID
     * @param playerUuid the player UUID
     */
    public void completeQuest(String questId, UUID playerUuid) {
        Set<String> active = playerQuests.get(playerUuid);
        if (active != null) {
            active.remove(questId);
            HoggieLogger.info("Quest completed: {} by player {}", questId, playerUuid);
        }
    }

    /**
     * Gets a quest by ID.
     *
     * @param questId the quest ID
     * @return the quest, or null
     */
    public Quest getQuest(String questId) {
        return quests.get(questId);
    }

    /**
     * Gets active quests for a player.
     *
     * @param playerUuid the player UUID
     * @return set of active quest IDs
     */
    public Set<String> getActiveQuests(UUID playerUuid) {
        return playerQuests.getOrDefault(playerUuid, Set.of());
    }

    /**
     * Returns all quests.
     *
     * @return map of quest IDs to quests
     */
    public Map<String, Quest> getQuests() {
        return quests;
    }

    public static final class Quest {
        private final String id;
        private final String name;
        private final String description;
        private final List<String> objectives = new ArrayList<>();
        private final Map<String, Integer> rewards = new ConcurrentHashMap<>();
        private int requiredLevel;

        public Quest(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public List<String> getObjectives() { return objectives; }
        public Map<String, Integer> getRewards() { return rewards; }
        public int getRequiredLevel() { return requiredLevel; }

        public void addObjective(String objective) { objectives.add(objective); }
        public void addReward(String type, int amount) { rewards.put(type, amount); }
        public void setRequiredLevel(int level) { this.requiredLevel = level; }
    }
}
