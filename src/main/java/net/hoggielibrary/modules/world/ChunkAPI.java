package net.hoggielibrary.modules.world;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkPos;

/**
 * Chunk utilities for analyzing loaded chunks.
 */
public final class ChunkAPI {

    /**
     * Checks if a chunk is loaded at the given position.
     *
     * @param chunkPos the chunk position
     * @return true if loaded
     */
    public boolean isChunkLoaded(ChunkPos chunkPos) {
        ClientWorld world = MinecraftClient.getInstance().world;
        return world != null && world.isChunkLoaded(chunkPos.x, chunkPos.z);
    }

    /**
     * Gets the chunk position from block coordinates.
     *
     * @param x the block x coordinate
     * @param z the block z coordinate
     * @return the chunk position
     */
    public ChunkPos getChunkPos(int x, int z) {
        return new ChunkPos(x >> 4, z >> 4);
    }
}
