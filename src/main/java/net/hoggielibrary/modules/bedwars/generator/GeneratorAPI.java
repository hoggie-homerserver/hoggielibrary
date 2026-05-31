package net.hoggielibrary.modules.bedwars.generator;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Generator API for managing Bedwars resource generators.
 */
public final class GeneratorAPI {

    private final Map<String, Generator> generators = new ConcurrentHashMap<>();

    /**
     * Creates a resource generator.
     *
     * @param name the generator name
     * @param position the generator position
     * @param type the resource type
     * @param spawnInterval the spawn interval in ticks
     * @return the created generator
     */
    public Generator createGenerator(String name, BlockPos position, String type, int spawnInterval) {
        Generator generator = new Generator(name, position, type, spawnInterval);
        generators.put(name, generator);
        return generator;
    }

    /**
     * Gets a generator by name.
     *
     * @param name the generator name
     * @return the generator, or null
     */
    public Generator getGenerator(String name) {
        return generators.get(name);
    }

    /**
     * Removes a generator.
     *
     * @param name the generator name
     */
    public void removeGenerator(String name) {
        generators.remove(name);
    }

    /**
     * Returns all generators.
     *
     * @return map of generator names to generators
     */
    public Map<String, Generator> getGenerators() {
        return generators;
    }

    public static final class Generator {
        private final String name;
        private final BlockPos position;
        private final String type;
        private final int spawnInterval;
        private int tickCounter;
        private boolean enabled = true;

        public Generator(String name, BlockPos position, String type, int spawnInterval) {
            this.name = name;
            this.position = position;
            this.type = type;
            this.spawnInterval = spawnInterval;
        }

        public String getName() { return name; }
        public BlockPos getPosition() { return position; }
        public String getType() { return type; }
        public int getSpawnInterval() { return spawnInterval; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public void tick() {
            if (!enabled) return;
            tickCounter++;
        }

        public boolean shouldSpawn() {
            return tickCounter >= spawnInterval;
        }

        public void resetCounter() {
            tickCounter = 0;
        }
    }
}
