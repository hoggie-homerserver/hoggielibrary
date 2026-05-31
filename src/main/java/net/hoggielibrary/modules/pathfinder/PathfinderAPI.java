package net.hoggielibrary.modules.pathfinder;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;

/**
 * Pathfinding API for navigation and movement control.
 *
 * <p>Provides utilities for walking to positions, following entities,
 * and calculating paths.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.pathfinder.walkTo(pos);
 * Hoggie.pathfinder.follow(entity);
 * Hoggie.pathfinder.stop();
 * Hoggie.pathfinder.isPathing();
 * }</pre>
 */
public final class PathfinderAPI {

    private static boolean pathing;
    private static Vec3d target;
    private static Entity targetEntity;
    private static Deque<Vec3d> path = new ArrayDeque<>();
    private static double speed = 0.5;
    private static double targetReachDistance = 1.5;
    private static int pathUpdateInterval = 20;
    private static int ticksSinceUpdate = 0;

    /**
     * Commands the player to walk to a specific position.
     *
     * @param pos the target position
     */
    public void walkTo(BlockPos pos) {
        walkTo(Vec3d.ofCenter(pos));
    }

    /**
     * Commands the player to walk to a specific position.
     *
     * @param pos the target position
     */
    public void walkTo(Vec3d pos) {
        target = pos;
        targetEntity = null;
        pathing = true;
        path.clear();
        ticksSinceUpdate = pathUpdateInterval;
        HoggieLogger.debug("Pathfinding to {}", pos);
    }

    /**
     * Commands the player to follow an entity.
     *
     * @param entity the entity to follow
     */
    public void follow(Entity entity) {
        targetEntity = entity;
        target = null;
        pathing = true;
        path.clear();
        HoggieLogger.debug("Following entity: {}", entity.getName().getString());
    }

    /**
     * Stops pathfinding and movement.
     */
    public void stop() {
        pathing = false;
        target = null;
        targetEntity = null;
        path.clear();
    }

    /**
     * Returns whether the pathfinder is currently active.
     *
     * @return true if pathing
     */
    public boolean isPathing() {
        return pathing;
    }

    /**
     * Returns the current pathfinding target.
     *
     * @return the target position, or null
     */
    public Vec3d getTarget() {
        return target;
    }

    /**
     * Sets the movement speed for pathfinding.
     *
     * @param speed the movement speed
     */
    public void setSpeed(double speed) {
        this.speed = Math.max(0.1, Math.min(1.0, speed));
    }

    /**
     * Sets the distance at which the target is considered reached.
     *
     * @param distance the reach distance in blocks
     */
    public void setTargetReachDistance(double distance) {
        this.targetReachDistance = Math.max(0.5, distance);
    }

    /**
     * Sets how often the path is recalculated (in ticks).
     *
     * @param ticks the update interval
     */
    public void setPathUpdateInterval(int ticks) {
        this.pathUpdateInterval = Math.max(1, ticks);
    }

    /**
     * Called on each client tick to process pathfinding movement.
     */
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || !pathing) return;

        if (targetEntity != null) {
            target = targetEntity.getSyncedPos();
        }

        if (target == null) {
            stop();
            return;
        }

        double distance = player.getSyncedPos().distanceTo(target);

        if (distance <= targetReachDistance) {
            if (targetEntity != null) {
                // Continue following
            } else {
                stop();
                return;
            }
        }

        ticksSinceUpdate++;
        if (ticksSinceUpdate >= pathUpdateInterval) {
            recalculatePath(player);
            ticksSinceUpdate = 0;
        }

        moveTowards(player, target);
    }

    private void recalculatePath(ClientPlayerEntity player) {
        path.clear();
        path.add(target);
    }

    private void moveTowards(ClientPlayerEntity player, Vec3d goal) {
        Vec3d to = goal.subtract(player.getSyncedPos()).normalize();
        player.setVelocity(to.x * speed, player.getVelocity().y, to.z * speed);

        // Look in the direction of movement
        float yaw = (float) Math.toDegrees(Math.atan2(-to.x, to.z));
        player.setYaw(yaw);
    }
}
