package net.hoggielibrary.modules.combat;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

/**
 * Combat API for attacking entities and managing combat interactions.
 *
 * <p>Provides high-level combat utilities including entity attacking,
 * cooldown-aware attacks, and combat state checks.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.combat.attack(target);
 * Hoggie.combat.attackCooldown();
 * Hoggie.combat.isInCombat();
 * }</pre>
 */
public final class CombatAPI {

    /**
     * Attacks a target entity with the current held item.
     *
     * @param target the entity to attack
     */
    public void attack(Entity target) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || target == null) return;
        client.interactionManager.attackEntity(client.player, target);
        client.player.swingHand(Hand.MAIN_HAND);
    }

    /**
     * Attacks a target entity with a specific hand.
     *
     * @param target the entity to attack
     * @param hand the hand to use
     */
    public void attack(Entity target, Hand hand) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || target == null) return;
        client.interactionManager.attackEntity(client.player, target);
        client.player.swingHand(hand);
    }

    /**
     * Returns the player's attack cooldown progress.
     *
     * @param baseTime base time for the cooldown calculation
     * @return progress from 0.0 to 1.0
     */
    public float getAttackCooldownProgress(float baseTime) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return 1.0f;
        return client.player.getAttackCooldownProgress(baseTime);
    }

    /**
     * Returns the current attack cooldown progress (0.0 to 1.0) with default timing.
     *
     * @return the attack cooldown progress
     */
    public float getAttackCooldownProgressDefault() {
        return getAttackCooldownProgress(0.5f);
    }

    /**
     * Returns whether the player can attack (cooldown ready).
     *
     * @return true if attack is ready
     */
    public boolean canAttack() {
        return getAttackCooldownProgressDefault() >= 0.9f;
    }

    /**
     * Returns whether the player is in combat (has a recent attack target).
     *
     * @return true if in combat
     */
    public boolean isInCombat() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return false;
        return client.player.getAttacking() != null;
    }

    /**
     * Returns the current attack target.
     *
     * @return the attack target, or null
     */
    public LivingEntity getAttackTarget() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return null;
        Entity target = client.player.getAttacking();
        return target instanceof LivingEntity living ? living : null;
    }

    /**
     * Critically hits a target (requires jumping).
     *
     * @param target the entity to critically hit
     */
    public void criticalHit(Entity target) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.player.isOnGround() && !client.player.isInLava() && !client.player.isTouchingWater()) {
            client.player.jump();
        }
        attack(target);
    }
}
