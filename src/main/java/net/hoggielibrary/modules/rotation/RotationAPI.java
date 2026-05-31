package net.hoggielibrary.modules.rotation;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Rotation API for managing player look direction and entity aiming.
 *
 * <p>Provides utilities for smooth and instant rotations, entity
 * aiming, and rotation calculations.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.rotation.lookAt(entity);
 * Hoggie.rotation.lookAt(pos);
 * Hoggie.rotation.smoothLook(entity, 10);
 * Hoggie.rotation.getYaw();
 * Hoggie.rotation.getPitch();
 * }</pre>
 */
public final class RotationAPI {

    private static float serverYaw;
    private static float serverPitch;
    private static float lastYaw;
    private static float lastPitch;
    private static boolean rotating;
    private static float targetYaw;
    private static float targetPitch;
    private static int rotationTicks;
    private static int currentRotationTick;

    /**
     * Instantly looks at an entity.
     *
     * @param entity the target entity
     */
    public void lookAt(Entity entity) {
        if (entity == null) return;
        Vec3d targetPos = entity.getBoundingBox().getCenter();
        lookAt(targetPos);
    }

    /**
     * Instantly looks at a position.
     *
     * @param pos the target position
     */
    public void lookAt(Vec3d pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        Vec3d playerPos = player.getCameraPosVec(1.0f);
        double dx = pos.x - playerPos.x;
        double dy = pos.y - playerPos.y;
        double dz = pos.z - playerPos.z;

        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, horizontalDistance));

        setRotation(yaw, pitch);
    }

    /**
     * Smoothly rotates to look at an entity over a number of ticks.
     *
     * @param entity the target entity
     * @param ticks the duration of the rotation in ticks
     */
    public void smoothLook(Entity entity, int ticks) {
        if (entity == null) return;
        Vec3d targetPos = entity.getBoundingBox().getCenter();
        smoothLook(targetPos, ticks);
    }

    /**
     * Smoothly rotates to look at a position over a number of ticks.
     *
     * @param pos the target position
     * @param ticks the duration in ticks
     */
    public void smoothLook(Vec3d pos, int ticks) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        Vec3d playerPos = player.getCameraPosVec(1.0f);
        double dx = pos.x - playerPos.x;
        double dy = pos.y - playerPos.y;
        double dz = pos.z - playerPos.z;

        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        targetYaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        targetPitch = (float) -Math.toDegrees(Math.atan2(dy, horizontalDistance));

        rotating = true;
        rotationTicks = ticks;
        currentRotationTick = 0;
        lastYaw = player.getYaw();
        lastPitch = player.getPitch();
    }

    /**
     * Sets the player's rotation instantly.
     *
     * @param yaw the yaw value
     * @param pitch the pitch value
     */
    public void setRotation(float yaw, float pitch) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        player.setYaw(yaw);
        player.setPitch(pitch);
        serverYaw = yaw;
        serverPitch = pitch;
        sendRotationPacket(yaw, pitch);
    }

    /**
     * Sets only the player's yaw.
     *
     * @param yaw the yaw value
     */
    public void setYaw(float yaw) {
        setRotation(yaw, getPitch());
    }

    /**
     * Sets only the player's pitch.
     *
     * @param pitch the pitch value
     */
    public void setPitch(float pitch) {
        setRotation(getYaw(), pitch);
    }

    /**
     * Returns the current yaw.
     *
     * @return the yaw value
     */
    public float getYaw() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null ? client.player.getYaw() : 0.0f;
    }

    /**
     * Returns the current pitch.
     *
     * @return the pitch value
     */
    public float getPitch() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null ? client.player.getPitch() : 0.0f;
    }

    /**
     * Resets any active smooth rotation.
     */
    public void resetRotation() {
        rotating = false;
        currentRotationTick = 0;
    }

    /**
     * Returns whether a smooth rotation is in progress.
     *
     * @return true if rotating
     */
    public boolean isRotating() {
        return rotating;
    }

    /**
     * Calculates the yaw needed to look at a position.
     *
     * @param from the source position
     * @param to the target position
     * @return the yaw angle
     */
    public float calculateYaw(Vec3d from, Vec3d to) {
        double dx = to.x - from.x;
        double dz = to.z - from.z;
        return (float) Math.toDegrees(Math.atan2(-dx, dz));
    }

    /**
     * Calculates the pitch needed to look at a position.
     *
     * @param from the source position
     * @param to the target position
     * @return the pitch angle
     */
    public float calculatePitch(Vec3d from, Vec3d to) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        return (float) -Math.toDegrees(Math.atan2(dy, horizontalDistance));
    }

    /**
     * Called on each client tick to process smooth rotations.
     */
    public static void onPlayerTick() {
        if (!rotating) return;

        currentRotationTick++;
        float progress = Math.min(1.0f, (float) currentRotationTick / rotationTicks);
        float easedProgress = easeInOutCubic(progress);

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        float newYaw = lastYaw + (targetYaw - lastYaw) * easedProgress;
        float newPitch = lastPitch + (targetPitch - lastPitch) * easedProgress;

        player.setYaw(newYaw);
        player.setPitch(newPitch);
        serverYaw = newYaw;
        serverPitch = newPitch;

        if (progress >= 1.0f) {
            rotating = false;
            currentRotationTick = 0;
        }
    }

    private static float easeInOutCubic(float t) {
        return t < 0.5f ? 4 * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 3) / 2;
    }

    private static void sendRotationPacket(float yaw, float pitch) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) return;
        client.getNetworkHandler().sendPacket(
                new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, client.player != null && client.player.isOnGround(), false)
        );
    }
}
