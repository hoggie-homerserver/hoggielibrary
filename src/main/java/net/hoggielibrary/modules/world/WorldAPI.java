package net.hoggielibrary.modules.world;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * World interaction API for block manipulation and world state.
 *
 * <p>Provides utilities for placing/breaking blocks, querying
 * block states, and world-level operations.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.world.placeBlock(pos);
 * Hoggie.world.breakBlock(pos);
 * Hoggie.world.getBlock(pos);
 * }</pre>
 */
public final class WorldAPI {

    private static boolean onWorld;

    /**
     * Called internally when the client joins a world.
     */
    public static void onWorldJoin() {
        onWorld = true;
    }

    /**
     * Places a block at the given position using the held item.
     *
     * @param pos the position to place at
     */
    public void placeBlock(BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.interactionManager == null || client.player == null) return;

        Vec3d hitPos = Vec3d.ofCenter(pos);
        Direction direction = Direction.UP;
        BlockHitResult hit = new BlockHitResult(hitPos, direction, pos, false);
        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
        client.player.swingHand(Hand.MAIN_HAND);
    }

    /**
     * Places a block at the given position with a specific hand and direction.
     *
     * @param pos the position
     * @param hand the hand to use
     * @param direction the direction to place against
     */
    public void placeBlock(BlockPos pos, Hand hand, Direction direction) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.interactionManager == null || client.player == null) return;

        Vec3d hitPos = Vec3d.ofCenter(pos);
        BlockHitResult hit = new BlockHitResult(hitPos, direction, pos, false);
        client.interactionManager.interactBlock(client.player, hand, hit);
        client.player.swingHand(hand);
    }

    /**
     * Breaks a block at the given position.
     *
     * @param pos the block position to break
     */
    public void breakBlock(BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.interactionManager == null) return;
        client.interactionManager.attackBlock(pos, Direction.UP);
    }

    /**
     * Gets the block state at a given position.
     *
     * @param pos the block position
     * @return the block state, or air if world is null
     */
    public BlockState getBlock(BlockPos pos) {
        ClientWorld world = getWorld();
        return world != null ? world.getBlockState(pos) : Blocks.AIR.getDefaultState();
    }

    /**
     * Gets the block at a given position.
     *
     * @param pos the block position
     * @return the block type
     */
    public Block getBlockType(BlockPos pos) {
        return getBlock(pos).getBlock();
    }

    /**
     * Returns whether the block at a position is air.
     *
     * @param pos the block position
     * @return true if the block is air
     */
    public boolean isAir(BlockPos pos) {
        return getBlock(pos).isAir();
    }

    /**
     * Returns the current client world instance.
     *
     * @return the client world, or null
     */
    public ClientWorld getWorld() {
        return MinecraftClient.getInstance().world;
    }

    /**
     * Returns whether the client is currently in a world.
     *
     * @return true if in a world
     */
    public boolean isOnWorld() {
        return onWorld && getWorld() != null;
    }

    /**
     * Gets the light level at a position.
     *
     * @param pos the position
     * @return the light level (0-15)
     */
    public int getLightLevel(BlockPos pos) {
        ClientWorld world = getWorld();
        return world != null ? world.getLightLevel(pos) : 0;
    }

    /**
     * Checks if a position is loaded in the world.
     *
     * @param pos the position
     * @return true if the chunk is loaded
     */
    public boolean isLoaded(BlockPos pos) {
        ClientWorld world = getWorld();
        return world != null && world.isChunkLoaded(pos);
    }
}
