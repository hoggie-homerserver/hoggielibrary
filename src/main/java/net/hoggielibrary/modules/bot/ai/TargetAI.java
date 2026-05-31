package net.hoggielibrary.modules.bot.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Comparator;
import java.util.List;

/**
 * Target AI for bot target selection.
 */
public final class TargetAI {

    private TargetPriority priority = TargetPriority.CLOSEST;
    private double aggroRange = 16.0;

    /**
     * Selects the best target from a list of entities.
     *
     * @param possibleTargets list of potential targets
     * @return the selected target, or null
     */
    public LivingEntity selectTarget(List<? extends LivingEntity> possibleTargets) {
        if (possibleTargets == null || possibleTargets.isEmpty()) return null;

        return switch (priority) {
            case CLOSEST -> possibleTargets.stream()
                    .min(Comparator.comparingDouble(e -> e.distanceTo(e)))
                    .orElse(null);
            case LOWEST_HEALTH -> possibleTargets.stream()
                    .min(Comparator.comparingDouble(LivingEntity::getHealth))
                    .orElse(null);
            case WEAKEST -> possibleTargets.stream()
                    .min(Comparator.comparingDouble(e -> e.getHealth() + e.getArmor()))
                    .orElse(null);
            case STRONGEST -> possibleTargets.stream()
                    .max(Comparator.comparingDouble(e -> e.getHealth() + e.getArmor()))
                    .orElse(null);
        };
    }

    /**
     * Sets the target priority strategy.
     *
     * @param priority the priority mode
     */
    public void setPriority(TargetPriority priority) {
        this.priority = priority;
    }

    /**
     * Sets the aggro range.
     *
     * @param range the range in blocks
     */
    public void setAggroRange(double range) {
        this.aggroRange = range;
    }

    /**
     * Gets the aggro range.
     *
     * @return the aggro range
     */
    public double getAggroRange() {
        return aggroRange;
    }

    public enum TargetPriority {
        CLOSEST,
        LOWEST_HEALTH,
        WEAKEST,
        STRONGEST
    }
}
