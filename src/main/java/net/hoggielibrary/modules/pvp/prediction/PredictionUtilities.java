package net.hoggielibrary.modules.pvp.prediction;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Prediction utilities for anticipating entity movement in PvP.
 *
 * <p>Provides methods for predicting entity positions, leading
 * shots, and calculating interception points.
 */
public final class PredictionUtilities {

    /**
     * Predicts a target's position after a given number of ticks.
     *
     * @param entity the target entity
     * @param ticks the number of ticks to predict ahead
     * @return the predicted position
     */
    public Vec3d predictPosition(LivingEntity entity, int ticks) {
        if (entity == null) return Vec3d.ZERO;

        Vec3d currentPos = entity.getSyncedPos();
        Vec3d velocity = entity.getVelocity();

        return new Vec3d(
                currentPos.x + velocity.x * ticks,
                currentPos.y + velocity.y * ticks,
                currentPos.z + velocity.z * ticks
        );
    }

    /**
     * Predicts a target's position with gravity.
     *
     * @param entity the target entity
     * @param ticks the number of ticks
     * @return the predicted position with gravity
     */
    public Vec3d predictPositionWithGravity(LivingEntity entity, int ticks) {
        if (entity == null) return Vec3d.ZERO;

        Vec3d currentPos = entity.getSyncedPos();
        Vec3d velocity = entity.getVelocity();

        double gravity = -0.08;
        double verticalVel = velocity.y;

        for (int i = 0; i < ticks; i++) {
            verticalVel += gravity;
        }

        return new Vec3d(
                currentPos.x + velocity.x * ticks,
                currentPos.y + verticalVel * ticks,
                currentPos.z + velocity.z * ticks
        );
    }

    /**
     * Calculates the interception point for a projectile.
     *
     * @param from the source position
     * @param target the target position
     * @param targetVelocity the target's velocity
     * @param projectileSpeed the projectile speed
     * @return the interception point
     */
    public Vec3d calculateInterception(Vec3d from, Vec3d target, Vec3d targetVelocity, double projectileSpeed) {
        double dx = target.x - from.x;
        double dy = target.y - from.y;
        double dz = target.z - from.z;

        double vx = targetVelocity.x;
        double vy = targetVelocity.y;
        double vz = targetVelocity.z;

        double a = vx * vx + vy * vy + vz * vz - projectileSpeed * projectileSpeed;
        double b = 2 * (dx * vx + dy * vy + dz * vz);
        double c = dx * dx + dy * dy + dz * dz;

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) return target;

        double t = (-b - Math.sqrt(discriminant)) / (2 * a);
        if (t < 0) {
            t = (-b + Math.sqrt(discriminant)) / (2 * a);
        }
        if (t < 0) return target;

        return new Vec3d(
                target.x + targetVelocity.x * t,
                target.y + targetVelocity.y * t,
                target.z + targetVelocity.z * t
        );
    }

    /**
     * Predicts where a target will be after an arrow travel time.
     *
     * @param entity the target
     * @param arrowSpeed the arrow speed
     * @param distance the distance to target
     * @return the predicted position
     */
    public Vec3d predictArrowHit(LivingEntity entity, double arrowSpeed, double distance) {
        int travelTicks = (int) (distance / arrowSpeed);
        return predictPosition(entity, travelTicks);
    }
}
