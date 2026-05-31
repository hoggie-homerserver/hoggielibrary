package net.hoggielibrary.modules.pvp.rotation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Rotation manager for PvP-oriented look control.
 *
 * <p>Provides aim assistance, smooth aiming, and automatic
 * entity tracking during combat.
 */
public final class RotationManager {

    private float targetYaw;
    private float targetPitch;
    private boolean rotating;
    private int smoothTicks;
    private int currentTick;
    private float startYaw;
    private float startPitch;

    /**
     * Instantly rotates to face an entity.
     *
     * @param entity the entity to face
     */
    public void lookAt(Entity entity) {
        if (entity == null) return;
        Vec3d targetPos = entity.getBoundingBox().getCenter();
        lookAt(targetPos);
    }

    /**
     * Instantly rotates to face a position.
     *
     * @param pos the target position
     */
    public void lookAt(Vec3d pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        float[] rotations = getRotations(player.getCameraPosVec(1.0f), pos);
        player.setYaw(rotations[0]);
        player.setPitch(rotations[1]);
    }

    /**
     * Smoothly rotates to face an entity over a duration.
     *
     * @param entity the target entity
     * @param ticks the duration in ticks
     */
    public void smoothLook(Entity entity, int ticks) {
        if (entity == null) return;
        Vec3d targetPos = entity.getBoundingBox().getCenter();
        smoothLook(targetPos, ticks);
    }

    /**
     * Smoothly rotates to face a position over a duration.
     *
     * @param pos the target position
     * @param ticks the duration in ticks
     */
    public void smoothLook(Vec3d pos, int ticks) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        float[] rotations = getRotations(player.getCameraPosVec(1.0f), pos);
        targetYaw = rotations[0];
        targetPitch = rotations[1];
        startYaw = player.getYaw();
        startPitch = player.getPitch();
        smoothTicks = ticks;
        currentTick = 0;
        rotating = true;
    }

    /**
     * Aims at the closest targetable point on an entity.
     *
     * @param entity the target entity
     */
    public void aimAtEntity(LivingEntity entity) {
        if (entity == null) return;
        Vec3d aimPoint = getBestAimPoint(entity);
        lookAt(aimPoint);
    }

    /**
     * Gets the best aim point on an entity (preferring body center).
     *
     * @param entity the target entity
     * @return the aim position
     */
    public Vec3d getBestAimPoint(LivingEntity entity) {
        return entity.getBoundingBox().getCenter();
    }

    /**
     * Calculates the rotations needed to look at a position.
     *
     * @param from the source position
     * @param to the target position
     * @return an array of [yaw, pitch]
     */
    public float[] getRotations(Vec3d from, Vec3d to) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, horizontalDistance));

        return new float[]{yaw, pitch};
    }

    /**
     * Ticks the rotation manager, processing smooth rotations.
     */
    public void tick() {
        if (!rotating) return;

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        currentTick++;
        float progress = Math.min(1.0f, (float) currentTick / smoothTicks);
        float eased = easeInOutQuad(progress);

        player.setYaw(startYaw + (targetYaw - startYaw) * eased);
        player.setPitch(startPitch + (targetPitch - startPitch) * eased);

        if (progress >= 1.0f) {
            rotating = false;
        }
    }

    /**
     * Resets any active rotation.
     */
    public void reset() {
        rotating = false;
        currentTick = 0;
    }

    /**
     * Returns whether the manager is currently rotating.
     *
     * @return true if rotating
     */
    public boolean isRotating() {
        return rotating;
    }

    private float easeInOutQuad(float t) {
        return t < 0.5f ? 2 * t * t : 1 - (float) Math.pow(-2 * t + 2, 2) / 2;
    }
}
