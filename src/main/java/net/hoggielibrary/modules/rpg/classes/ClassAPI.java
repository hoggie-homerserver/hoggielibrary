package net.hoggielibrary.modules.rpg.classes;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class API for RPG class system.
 */
public final class ClassAPI {

    private final Map<String, RpgClass> classes = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerClasses = new ConcurrentHashMap<>();

    /**
     * Registers a new RPG class.
     *
     * @param id the class ID
     * @param name the display name
     * @param description the class description
     * @return the created class
     */
    public RpgClass registerClass(String id, String name, String description) {
        RpgClass rpgClass = new RpgClass(id, name, description);
        classes.put(id, rpgClass);
        return rpgClass;
    }

    /**
     * Sets a player's class.
     *
     * @param playerUuid the player UUID
     * @param classId the class ID
     */
    public void setPlayerClass(UUID playerUuid, String classId) {
        if (classes.containsKey(classId)) {
            playerClasses.put(playerUuid, classId);
            HoggieLogger.info("Player {} set to class {}", playerUuid, classId);
        }
    }

    /**
     * Gets a player's class.
     *
     * @param playerUuid the player UUID
     * @return the class, or null
     */
    public RpgClass getPlayerClass(UUID playerUuid) {
        String classId = playerClasses.get(playerUuid);
        return classId != null ? classes.get(classId) : null;
    }

    /**
     * Gets a class by ID.
     *
     * @param classId the class ID
     * @return the class, or null
     */
    public RpgClass getClass(String classId) {
        return classes.get(classId);
    }

    /**
     * Returns all registered classes.
     *
     * @return map of class IDs to classes
     */
    public Map<String, RpgClass> getClasses() {
        return classes;
    }

    public static final class RpgClass {
        private final String id;
        private final String name;
        private final String description;
        private double baseHealth = 20.0;
        private double baseDamage = 1.0;
        private double baseSpeed = 0.1;
        private final Map<String, Integer> baseStats = new ConcurrentHashMap<>();

        public RpgClass(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getBaseHealth() { return baseHealth; }
        public double getBaseDamage() { return baseDamage; }
        public double getBaseSpeed() { return baseSpeed; }

        public void setBaseHealth(double health) { this.baseHealth = health; }
        public void setBaseDamage(double damage) { this.baseDamage = damage; }
        public void setBaseSpeed(double speed) { this.baseSpeed = speed; }
        public void setBaseStat(String stat, int value) { baseStats.put(stat, value); }
        public int getBaseStat(String stat) { return baseStats.getOrDefault(stat, 0); }
    }
}
