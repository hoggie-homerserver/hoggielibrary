package net.hoggielibrary.modules.world;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

/**
 * Biome utilities for querying biome information.
 */
public final class BiomeAPI {

    /**
     * Gets the biome at a given position.
     *
     * @param pos the block position
     * @return the biome, or null
     */
    public Biome getBiome(BlockPos pos) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return null;
        RegistryEntry<Biome> entry = world.getBiome(pos);
        return entry.value();
    }

    /**
     * Gets the biome name at a given position.
     *
     * @param pos the block position
     * @return the biome name as a string
     */
    public String getBiomeName(BlockPos pos) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return "unknown";
        RegistryEntry<Biome> entry = world.getBiome(pos);
        return entry.getKey().map(RegistryKey::getValue).toString();
    }

    /**
     * Gets the temperature at a position.
     *
     * @param pos the position
     * @return the temperature
     */
    public float getTemperature(BlockPos pos) {
        Biome biome = getBiome(pos);
        return biome != null ? biome.getTemperature() : 0.0f;
    }
}
