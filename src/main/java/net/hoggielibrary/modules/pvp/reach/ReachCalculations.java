package net.hoggielibrary.modules.pvp.reach;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * Reach calculation utilities for PvP.
 *
 * <p>Provides methods for calculating entity distances,
 * reach requirements, and hit validation.
 */
public final class ReachCalculations {

    private static final double DEFAULT_REACH = 3.0;
    private static final double CREATIVE_REACH = 5.0;
    private static final double SPRINT_REACH_BONUS = 0.5;

    /**
     * Calculates the distance to an entity's closest point.
     *
     * @param entity the entity to measure
     * @return the distance in blocks
     */
    public double getDistanceToEntity(Entity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || entity == null) return Double.MAX_VALUE;

        Vec3d playerPos = player.getCameraPosVec(1.0f);
        Vec3d entityPos = entity.getSyncedPos();

        Box entityBox = entity.getBoundingBox();
        Vec3d closest = closestPointOnBox(playerPos, entityBox);

        return playerPos.distanceTo(closest);
    }

    /**
     * Returns whether a target entity is within attack reach.
     *
     * @param entity the target entity
     * @return true if within reach
     */
    public boolean isWithinReach(Entity entity) {
        return getDistanceToEntity(entity) <= getMaxReach();
    }

    /**
     * Returns the maximum attack reach.
     *
     * @return the maximum reach in blocks
     */
    public double getMaxReach() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return DEFAULT_REACH;

        double base = player.isCreative() ? CREATIVE_REACH : DEFAULT_REACH;
        if (player.isSprinting()) {
            base += SPRINT_REACH_BONUS;
        }
        return base;
    }

    /**
     * Returns the default attack reach (without modifiers).
     *
     * @return the default reach
     */
    public double getBaseReach() {
        return DEFAULT_REACH;
    }

    /**
     * Calculates the squared distance to an entity (faster for comparisons).
     *
     * @param entity the entity
     * @return the squared distance
     */
    public double getSquaredDistance(Entity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || entity == null) return Double.MAX_VALUE;

        return player.squaredDistanceTo(entity);
    }

    private Vec3d closestPointOnBox(Vec3d point, Box box) {
        double x = Math.max(box.minX, Math.min(point.x, box.maxX));
        double y = Math.max(box.minY, Math.min(point.y, box.maxY));
        double z = Math.max(box.minZ, Math.min(point.z, box.maxZ));
        return new Vec3d(x, y, z);
    }
}
