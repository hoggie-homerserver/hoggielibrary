package net.hoggielibrary.modules.pvp.combat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

/**
 * Advanced combat utilities for PvP.
 *
 * <p>Provides methods for attacking, critting, and managing
 * combat state with optimal timing.
 */
public final class CombatUtils {

    /**
     * Attacks a target entity with the current held item.
     *
     * @param target the entity to attack
     */
    public void attack(Entity target) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.interactionManager == null) return;
        client.interactionManager.attackEntity(client.player, target);
        client.player.swingHand(Hand.MAIN_HAND);
    }

    /**
     * Performs a critical hit if possible.
     *
     * @param target the entity to critically hit
     */
    public void critical(Entity target) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        if (player.isOnGround() && !player.isInLava() && !player.isTouchingWater()) {
            player.jump();
        }
        attack(target);
    }

    /**
     * Attacks with optimal timing based on weapon cooldown.
     *
     * @param target the entity to attack
     * @return true if the attack was executed
     */
    public boolean attackTimed(Entity target) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return false;

        float progress = client.player.getAttackCooldownProgress(0.5f);
        if (progress >= 0.9f) {
            attack(target);
            return true;
        }
        return false;
    }

    /**
     * Strafes around a target while attacking.
     *
     * @param target the target entity
     * @param clockwise whether to strafe clockwise
     */
    public void strafeAttack(LivingEntity target, boolean clockwise) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || target == null) return;

        double dx = target.getX() - player.getX();
        double dz = target.getZ() - player.getZ();
        double angle = clockwise ? Math.PI / 2 : -Math.PI / 2;
        double strafeX = dx * Math.cos(angle) - dz * Math.sin(angle);
        double strafeZ = dx * Math.sin(angle) + dz * Math.cos(angle);

        player.setVelocity(
                player.getVelocity().x + strafeX * 0.1,
                player.getVelocity().y,
                player.getVelocity().z + strafeZ * 0.1
        );
    }

    /**
     * Returns whether the player can perform a critical hit.
     *
     * @return true if a critical hit is possible
     */
    public boolean canCritical() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return false;
        return player.isOnGround() && !player.isInLava() && !player.isTouchingWater()
                && !player.isSneaking() && !player.isClimbing();
    }

    /**
     * Returns the current attack speed (1.6 is default for swords).
     *
     * @return the attack speed
     */
    public float getAttackSpeed() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return 4.0f;
        return client.player.getAttackCooldownProgressPerTick();
    }
}
