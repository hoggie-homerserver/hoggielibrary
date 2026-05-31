package net.hoggielibrary.modules.pvp.cooldown;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Attack cooldown utilities for optimal PvP timing.
 *
 * <p>Provides methods for tracking and managing the attack
 * cooldown (1.9+ combat system).
 */
public final class AttackCooldownUtilities {

    private long lastAttackTime = 0;
    private static final long COOLDOWN_MULTIPLIER = 1000L;

    /**
     * Returns the attack cooldown progress (0.0 to 1.0).
     *
     * @return the cooldown progress
     */
    public float getCooldownProgress() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return 1.0f;

        return player.getAttackCooldownProgress(0.5f);
    }

    /**
     * Returns whether the attack cooldown is ready.
     *
     * @return true if ready to attack
     */
    public boolean isReady() {
        return getCooldownProgress() >= 0.9f;
    }

    /**
     * Records an attack and returns the time since the last attack.
     *
     * @return time in ms since the last attack
     */
    public long recordAttack() {
        long now = System.currentTimeMillis();
        long timeSince = now - lastAttackTime;
        lastAttackTime = now;
        return timeSince;
    }

    /**
     * Gets the cooldown period for the current weapon in milliseconds.
     *
     * @return the cooldown period in ms
     */
    public long getWeaponCooldownPeriod() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return 500L;

        double attackSpeed = getAttackSpeed(player);
        return (long) (COOLDOWN_MULTIPLIER / attackSpeed);
    }

    public int getOptimalTickDelay() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return 10;

        double attackSpeed = getAttackSpeed(client.player);
        return (int) Math.round(20.0 / attackSpeed);
    }

    private double getAttackSpeed(ClientPlayerEntity player) {
        return 20.0 / player.getAttackCooldownProgressPerTick();
    }

    /**
     * Returns the time until the next attack can occur.
     *
     * @return time in ms until ready
     */
    public long getTimeUntilReady() {
        long period = getWeaponCooldownPeriod();
        long elapsed = System.currentTimeMillis() - lastAttackTime;
        return Math.max(0, period - elapsed);
    }

}
