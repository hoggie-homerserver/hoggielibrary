package net.hoggielibrary.modules.bot.ai;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Combat AI for bot combat behavior.
 */
public final class CombatAI {

    private CombatMode mode = CombatMode.NORMAL;
    private double attackRange = 3.5;
    private int reactionTicks = 5;

    /**
     * Executes combat logic against a target.
     *
     * @param bot the bot entity
     * @param target the target entity
     */
    public void executeCombat(LivingEntity bot, LivingEntity target) {
        if (bot == null || target == null) return;

        double distance = bot.distanceTo(target);

        switch (mode) {
            case AGGRESSIVE -> {
                if (distance <= attackRange) {
                    // Attack immediately
                    bot.swingHand(net.minecraft.util.Hand.MAIN_HAND);
                }
            }
            case NORMAL -> {
                if (distance <= attackRange && isCooldownReady(bot)) {
                    bot.swingHand(net.minecraft.util.Hand.MAIN_HAND);
                }
            }
            case DEFENSIVE -> {
                if (distance < 2.0) {
                    // Back away
                    bot.setVelocity(bot.getVelocity().add(0, 0, -0.3));
                } else if (distance <= attackRange) {
                    bot.swingHand(net.minecraft.util.Hand.MAIN_HAND);
                }
            }
        }
    }

    /**
     * Sets the combat mode.
     *
     * @param mode the combat mode
     */
    public void setMode(CombatMode mode) {
        this.mode = mode;
    }

    /**
     * Sets the attack range.
     *
     * @param range the attack range in blocks
     */
    public void setAttackRange(double range) {
        this.attackRange = range;
    }

    /**
     * Sets the reaction delay.
     *
     * @param ticks the reaction delay in ticks
     */
    public void setReactionTicks(int ticks) {
        this.reactionTicks = ticks;
    }

    private boolean isCooldownReady(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            return player.getAttackCooldownProgress(0.5f) >= 0.9f;
        }
        return true;
    }

    public enum CombatMode {
        AGGRESSIVE,
        NORMAL,
        DEFENSIVE,
        EVASIVE
    }
}
