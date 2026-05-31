package net.hoggielibrary.modules.world;

import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Schematic utilities for saving and loading schematic files.
 */
public final class SchematicAPI {

    private final Map<String, Schematic> schematics = new ConcurrentHashMap<>();

    /**
     * Saves a schematic from the world.
     *
     * @param name the schematic name
     * @param pos1 the first corner
     * @param pos2 the second corner
     */
    public void saveSchematic(String name, BlockPos pos1, BlockPos pos2) {
        schematics.put(name, new Schematic(pos1, pos2));
    }

    /**
     * Pastes a schematic at a position.
     *
     * @param name the schematic name
     * @param target the target position
     */
    public void pasteSchematic(String name, BlockPos target) {
        // Schematic paste implementation
    }

    /**
     * Gets a schematic by name.
     *
     * @param name the schematic name
     * @return the schematic, or null
     */
    public Schematic getSchematic(String name) {
        return schematics.get(name);
    }

    public record Schematic(BlockPos pos1, BlockPos pos2) {
    }
}
