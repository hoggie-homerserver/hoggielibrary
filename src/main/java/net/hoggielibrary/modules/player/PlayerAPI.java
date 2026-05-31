package net.hoggielibrary.modules.player;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.Vec3d;

/**
 * Player management API for player state and movement.
 *
 * <p>Provides utilities for managing player state, movement,
 * and status effects.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.player.sprint(true);
 * Hoggie.player.sneak(true);
 * Hoggie.player.getHealth();
 * Hoggie.player.sendMessage("Hello!");
 * }</pre>
 */
public final class PlayerAPI {

    /**
     * Sets the player's sprinting state.
     *
     * @param sprinting whether to sprint
     */
    public void sprint(boolean sprinting) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.setSprinting(sprinting);
        }
    }

    /**
     * Sets the player's sneaking state.
     *
     * @param sneaking whether to sneak
     */
    public void sneak(boolean sneaking) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.setSneaking(sneaking);
        }
    }

    /**
     * Returns the player's current health.
     *
     * @return health value
     */
    public float getHealth() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null ? client.player.getHealth() : 0.0f;
    }

    /**
     * Returns the player's maximum health.
     *
     * @return max health
     */
    public float getMaxHealth() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null ? client.player.getMaxHealth() : 0.0f;
    }

    /**
     * Returns the player's current hunger.
     *
     * @return hunger value (0-20)
     */
    public int getHunger() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null ? client.player.getHungerManager().getFoodLevel() : 0;
    }

    /**
     * Returns the player's current XP level.
     *
     * @return XP level
     */
    public int getXpLevel() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null ? client.player.experienceLevel : 0;
    }

    /**
     * Returns the player's current position.
     *
     * @return position as Vec3d
     */
    public Vec3d getPosition() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null ? client.player.getSyncedPos() : Vec3d.ZERO;
    }

    /**
     * Sends a chat message as the player.
     *
     * @param message the message to send
     */
    public void sendMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal(message), true);
        }
    }

    /**
     * Returns whether the player is on the ground.
     *
     * @return true if on ground
     */
    public boolean isOnGround() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null && client.player.isOnGround();
    }

    /**
     * Returns the player's current velocity.
     *
     * @return velocity vector
     */
    public Vec3d getVelocity() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null ? client.player.getVelocity() : Vec3d.ZERO;
    }

    /**
     * Adds a velocity impulse to the player.
     *
     * @param velocity the velocity to add
     */
    public void addVelocity(Vec3d velocity) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.addVelocity(velocity.x, velocity.y, velocity.z);
        }
    }

    /**
     * Checks if the player has a specific status effect.
     *
     * @param effect the status effect
     * @return true if the player has the effect
     */
    public boolean hasEffect(StatusEffect effect) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return false;
        for (StatusEffectInstance instance : client.player.getStatusEffects()) {
            if (instance.getEffectType().value() == effect) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the player's network handler for sending packets.
     *
     * @return the player entity
     */
    public PlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }

    /**
     * Returns whether the player is in a creative mode.
     *
     * @return true if in creative
     */
    public boolean isCreative() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null && client.player.isCreative();
    }

    /**
     * Sends a system message to the player's action bar.
     *
     * @param message the message to display
     */
    public void sendActionBar(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal(message), true);
        }
    }
}
