package net.hoggielibrary.modules.world;

import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Structure utilities for saving and loading schematic-like structures.
 */
public final class StructureAPI {

    private final Map<String, Structure> structures = new ConcurrentHashMap<>();

    /**
     * Saves a structure from the world.
     *
     * @param name the structure name
     * @param pos1 the first corner
     * @param pos2 the second corner
     */
    public void saveStructure(String name, BlockPos pos1, BlockPos pos2) {
        structures.put(name, new Structure(pos1, pos2));
    }

    /**
     * Loads a structure at a position.
     *
     * @param name the structure name
     * @param target the target position
     */
    public void loadStructure(String name, BlockPos target) {
        // Structure loading implementation
    }

    /**
     * Gets a structure by name.
     *
     * @param name the structure name
     * @return the structure, or null
     */
    public Structure getStructure(String name) {
        return structures.get(name);
    }

    public record Structure(BlockPos pos1, BlockPos pos2) {
    }
}
