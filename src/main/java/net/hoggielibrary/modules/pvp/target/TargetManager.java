package net.hoggielibrary.modules.pvp.target;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Target management for selecting and tracking combat targets.
 *
 * <p>Provides utilities for finding the closest player, filtering targets,
 * and managing target priorities.
 */
public final class TargetManager {

    private LivingEntity currentTarget;
    private TargetPriority priority = TargetPriority.DISTANCE;

    /**
     * Gets the closest player within a given range.
     *
     * @param range the maximum range in blocks
     * @return the closest player, or null
     */
    public PlayerEntity getClosestPlayer(double range) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return null;

        return client.world.getPlayers().stream()
                .filter(p -> p != client.player && !p.isDead() && p.distanceTo(client.player) <= range)
                .min(Comparator.comparingDouble(p -> p.distanceTo(client.player)))
                .orElse(null);
    }

    /**
     * Gets the closest living entity within range.
     *
     * @param range the maximum range in blocks
     * @return the closest living entity, or null
     */
    public LivingEntity getClosestEntity(double range) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return null;

        Box box = client.player.getBoundingBox().expand(range);
        return client.world.getEntitiesByClass(LivingEntity.class, box,
                        e -> e != client.player && e.isAlive()).stream()
                .min(Comparator.comparingDouble(e -> e.distanceTo(client.player)))
                .orElse(null);
    }

    /**
     * Gets all players within range.
     *
     * @param range the maximum range
     * @return list of nearby players
     */
    public List<PlayerEntity> getPlayersInRange(double range) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return List.of();

        return client.world.getPlayers().stream()
                .filter(p -> p != client.player && !p.isDead() && p.distanceTo(client.player) <= range)
                .collect(Collectors.toList());
    }

    /**
     * Gets entities matching a custom filter within range.
     *
     * @param range the maximum range
     * @param filter the entity filter
     * @return list of matching entities
     */
    public List<LivingEntity> getFilteredEntities(double range, Predicate<LivingEntity> filter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return List.of();

        Box box = client.player.getBoundingBox().expand(range);
        return client.world.getEntitiesByClass(LivingEntity.class, box,
                        e -> e != client.player && e.isAlive() && filter.test(e));
    }

    /**
     * Sets the current combat target.
     *
     * @param target the target entity
     */
    public void setTarget(LivingEntity target) {
        this.currentTarget = target;
    }

    /**
     * Gets the current combat target.
     *
     * @return the current target
     */
    public LivingEntity getTarget() {
        return currentTarget;
    }

    /**
     * Clears the current target.
     */
    public void clearTarget() {
        this.currentTarget = null;
    }

    /**
     * Returns whether a target is set and alive.
     *
     * @return true if has a valid target
     */
    public boolean hasTarget() {
        return currentTarget != null && currentTarget.isAlive();
    }

    /**
     * Sets the target priority algorithm.
     *
     * @param priority the priority mode
     */
    public void setPriority(TargetPriority priority) {
        this.priority = priority;
    }

    /**
     * Gets the current target priority mode.
     *
     * @return the priority mode
     */
    public TargetPriority getPriority() {
        return priority;
    }

    /**
     * Selects the best target based on the current priority.
     *
     * @param range the maximum range
     * @return the best target, or null
     */
    public LivingEntity selectBestTarget(double range) {
        return switch (priority) {
            case DISTANCE -> getClosestEntity(range);
            case HEALTH -> getFilteredEntities(range, e -> true).stream()
                    .min(Comparator.comparingDouble(LivingEntity::getHealth))
                    .orElse(null);
            case ARMOR -> getFilteredEntities(range, e -> true).stream()
                    .min(Comparator.comparingDouble(e -> e.getArmor()))
                    .orElse(null);
            case CROSSHAIR -> getFilteredEntities(range, e -> true).stream()
                    .min(Comparator.comparingDouble(e -> angleTo(e)))
                    .orElse(null);
        };
    }

    private double angleTo(Entity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return Double.MAX_VALUE;

        Vec3d to = entity.getSyncedPos().subtract(client.player.getCameraPosVec(1.0f)).normalize();
        Vec3d look = client.player.getRotationVec(1.0f);
        return Math.toDegrees(Math.acos(Math.min(1.0, look.dotProduct(to))));
    }
}
