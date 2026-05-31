package net.hoggielibrary.modules.rpg.npc;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NPC API for creating and managing RPG non-player characters.
 */
public final class NpcAPI {

    private final Map<String, Npc> npcs = new ConcurrentHashMap<>();

    /**
     * Creates a new NPC.
     *
     * @param id the NPC identifier
     * @param name the display name
     * @param position the spawn position
     * @return the created NPC
     */
    public Npc createNpc(String id, String name, BlockPos position) {
        Npc npc = new Npc(id, name, position);
        npcs.put(id, npc);
        HoggieLogger.debug("NPC created: {} ({})", name, id);
        return npc;
    }

    /**
     * Gets an NPC by ID.
     *
     * @param id the NPC ID
     * @return the NPC, or null
     */
    public Npc getNpc(String id) {
        return npcs.get(id);
    }

    /**
     * Removes an NPC.
     *
     * @param id the NPC ID
     */
    public void removeNpc(String id) {
        npcs.remove(id);
    }

    /**
     * Returns all registered NPCs.
     *
     * @return map of NPC IDs to NPCs
     */
    public Map<String, Npc> getNpcs() {
        return npcs;
    }

    public static final class Npc {
        private final String id;
        private final String name;
        private final BlockPos position;
        private String skin;
        private String dialogueId;
        private boolean glowing;
        private UUID entityUuid;

        public Npc(String id, String name, BlockPos position) {
            this.id = id;
            this.name = name;
            this.position = position;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public BlockPos getPosition() { return position; }
        public String getSkin() { return skin; }
        public String getDialogueId() { return dialogueId; }
        public boolean isGlowing() { return glowing; }
        public UUID getEntityUuid() { return entityUuid; }

        public void setSkin(String skin) { this.skin = skin; }
        public void setDialogueId(String dialogueId) { this.dialogueId = dialogueId; }
        public void setGlowing(boolean glowing) { this.glowing = glowing; }
        public void setEntityUuid(UUID entityUuid) { this.entityUuid = entityUuid; }
    }
}
