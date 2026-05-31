package net.hoggielibrary.modules.rpg.dialogue;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dialogue API for creating NPC dialogue trees.
 */
public final class DialogueAPI {

    private final Map<String, Dialogue> dialogues = new ConcurrentHashMap<>();

    /**
     * Creates a new dialogue tree.
     *
     * @param id the dialogue ID
     * @param npcId the NPC ID
     * @return the created dialogue
     */
    public Dialogue createDialogue(String id, String npcId) {
        Dialogue dialogue = new Dialogue(id, npcId);
        dialogues.put(id, dialogue);
        return dialogue;
    }

    /**
     * Gets a dialogue by ID.
     *
     * @param id the dialogue ID
     * @return the dialogue, or null
     */
    public Dialogue getDialogue(String id) {
        return dialogues.get(id);
    }

    /**
     * Starts a dialogue for a player.
     *
     * @param dialogueId the dialogue ID
     * @param playerUuid the player UUID
     */
    public void startDialogue(String dialogueId, UUID playerUuid) {
        Dialogue dialogue = dialogues.get(dialogueId);
        if (dialogue != null) {
            dialogue.start(playerUuid);
            HoggieLogger.debug("Dialogue started: {} for player {}", dialogueId, playerUuid);
        }
    }

    /**
     * Returns all dialogues.
     *
     * @return map of dialogue IDs to dialogues
     */
    public Map<String, Dialogue> getDialogues() {
        return dialogues;
    }

    public static final class Dialogue {
        private final String id;
        private final String npcId;
        private final List<String> nodes = new ArrayList<>();
        private final Map<UUID, Integer> playerProgress = new ConcurrentHashMap<>();

        public Dialogue(String id, String npcId) {
            this.id = id;
            this.npcId = npcId;
        }

        public String getId() { return id; }
        public String getNpcId() { return npcId; }
        public List<String> getNodes() { return nodes; }

        public void addNode(String text) { nodes.add(text); }
        public void start(UUID playerUuid) { playerProgress.put(playerUuid, 0); }

        public String getCurrentText(UUID playerUuid) {
            Integer index = playerProgress.get(playerUuid);
            if (index == null || index >= nodes.size()) return null;
            return nodes.get(index);
        }

        public boolean advance(UUID playerUuid) {
            Integer index = playerProgress.get(playerUuid);
            if (index == null) return false;
            int next = index + 1;
            if (next >= nodes.size()) {
                playerProgress.remove(playerUuid);
                return false;
            }
            playerProgress.put(playerUuid, next);
            return true;
        }

        public boolean isComplete(UUID playerUuid) {
            Integer index = playerProgress.get(playerUuid);
            return index == null || index >= nodes.size();
        }
    }
}
