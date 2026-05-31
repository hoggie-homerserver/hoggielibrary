package net.hoggielibrary.modules.bot.navigation;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Navigation AI for bot movement control.
 */
public final class NavigationAI {

    private double speed = 0.3;
    private boolean avoidWater = true;
    private boolean avoidLava = true;

    /**
     * Moves a bot towards a target position.
     *
     * @param bot the bot entity
     * @param target the target position
     */
    public void moveTo(LivingEntity bot, Vec3d target) {
        if (bot == null || target == null) return;

        Vec3d direction = target.subtract(bot.getSyncedPos()).normalize();
        bot.setVelocity(direction.x * speed, bot.getVelocity().y, direction.z * speed);

        float yaw = (float) Math.toDegrees(Math.atan2(-direction.x, direction.z));
        bot.setYaw(yaw);
        bot.setHeadYaw(yaw);
    }

    /**
     * Makes a bot follow another entity.
     *
     * @param bot the bot entity
     * @param target the entity to follow
     * @param followDistance the distance to maintain
     */
    public void follow(LivingEntity bot, LivingEntity target, double followDistance) {
        if (bot == null || target == null) return;

        double distance = bot.distanceTo(target);
        if (distance > followDistance) {
            moveTo(bot, target.getSyncedPos());
        } else {
            bot.setVelocity(0, bot.getVelocity().y, 0);
        }
    }

    /**
     * Makes a bot flee from a threat.
     *
     * @param bot the bot entity
     * @param threat the threat entity
     * @param fleeDistance the distance to flee to
     */
    public void flee(LivingEntity bot, LivingEntity threat, double fleeDistance) {
        if (bot == null || threat == null) return;

        Vec3d away = bot.getSyncedPos().subtract(threat.getSyncedPos()).normalize();
        Vec3d fleePos = bot.getSyncedPos().add(away.multiply(fleeDistance));
        moveTo(bot, fleePos);
    }

    /**
     * Sets the bot movement speed.
     *
     * @param speed the speed value
     */
    public void setSpeed(double speed) {
        this.speed = Math.max(0.1, Math.min(1.0, speed));
    }

    /**
     * Gets the current speed.
     *
     * @return the speed value
     */
    public double getSpeed() {
        return speed;
    }
}
