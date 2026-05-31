package net.hoggielibrary.modules.bridge;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Bridging utilities for placing blocks while moving.
 *
 * <p>Provides automated bridging mechanics including speed bridging,
 * diagonal bridging, and safe bridge building.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.bridge.placeBridge(direction);
 * Hoggie.bridge.startBridging(direction);
 * Hoggie.bridge.stopBridging();
 * }</pre>
 */
public final class BridgeAPI {

    private static boolean bridging;
    private static Direction bridgeDirection;
    private static int blocksPlaced;
    private static int ticksBetweenPlacements = 2;
    private static int tickCounter = 0;

    /**
     * Places a single bridge block in the given direction.
     *
     * @param direction the direction to bridge
     */
    public void placeBridge(Direction direction) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        BlockPos playerPos = player.getBlockPos();
        BlockPos below = playerPos.down();
        BlockPos placePos = below.offset(direction);

        if (client.world != null && client.world.getBlockState(placePos).isAir()) {
            Vec3d hitPos = Vec3d.ofCenter(below);
            BlockHitResult hit = new BlockHitResult(hitPos, Direction.UP, below, false);
            client.interactionManager.interactBlock(player, Hand.MAIN_HAND, hit);
            player.swingHand(Hand.MAIN_HAND);
            blocksPlaced++;
        }
    }

    /**
     * Starts automatic bridging in the given direction.
     *
     * @param direction the direction to bridge
     */
    public void startBridging(Direction direction) {
        bridgeDirection = direction;
        bridging = true;
        blocksPlaced = 0;
        tickCounter = 0;
        HoggieLogger.info("Started bridging {}", direction);
    }

    /**
     * Stops automatic bridging.
     */
    public void stopBridging() {
        bridging = false;
        HoggieLogger.info("Stopped bridging. Blocks placed: {}", blocksPlaced);
    }

    /**
     * Returns whether the bridger is currently active.
     *
     * @return true if bridging
     */
    public boolean isBridging() {
        return bridging;
    }

    /**
     * Returns the number of blocks placed in the current bridge session.
     *
     * @return blocks placed count
     */
    public int getBlocksPlaced() {
        return blocksPlaced;
    }

    /**
     * Sets the tick delay between block placements.
     *
     * @param ticks the delay in ticks
     */
    public void setPlacementDelay(int ticks) {
        this.ticksBetweenPlacements = Math.max(1, ticks);
    }

    /**
     * Ticks the bridge system - called once per client tick.
     */
    public void tick() {
        if (!bridging) return;

        tickCounter++;
        if (tickCounter >= ticksBetweenPlacements) {
            placeBridge(bridgeDirection);
            tickCounter = 0;
        }
    }

    /**
     * Returns whether the player has blocks in their hotbar for bridging.
     *
     * @return true if blocks are available
     */
    public boolean hasBlocks() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return false;

        for (int i = 0; i < 9; i++) {
            var stack = player.getInventory().getStack(i);
            if (stack.getItem() == Items.OBSIDIAN ||
                stack.getItem() == Items.END_STONE ||
                stack.getItem() == Items.WHITE_WOOL ||
                stack.getItem() == Items.SANDSTONE) {
                return true;
            }
        }
        return false;
    }
}
