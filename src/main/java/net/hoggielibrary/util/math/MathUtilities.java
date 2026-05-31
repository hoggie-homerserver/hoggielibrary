package net.hoggielibrary.util.math;

import net.minecraft.util.math.Vec3d;

/**
 * General math utility functions.
 */
public final class MathUtilities {

    private MathUtilities() {
    }

    /**
     * Clamps a value between a minimum and maximum.
     *
     * @param value the value to clamp
     * @param min the minimum
     * @param max the maximum
     * @return the clamped value
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamps a float between a minimum and maximum.
     *
     * @param value the value to clamp
     * @param min the minimum
     * @param max the maximum
     * @return the clamped value
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Linearly interpolates between start and end.
     *
     * @param start the start value
     * @param end the end value
     * @param progress the progress (0.0 to 1.0)
     * @return the interpolated value
     */
    public static double lerp(double start, double end, double progress) {
        return start + (end - start) * progress;
    }

    /**
     * Linearly interpolates between start and end.
     *
     * @param start the start value
     * @param end the end value
     * @param progress the progress (0.0 to 1.0)
     * @return the interpolated value
     */
    public static float lerp(float start, float end, float progress) {
        return start + (end - start) * progress;
    }

    /**
     * Calculates the distance between two 3D points ignoring Y (horizontal distance).
     *
     * @param a the first point
     * @param b the second point
     * @return the horizontal distance
     */
    public static double horizontalDistance(Vec3d a, Vec3d b) {
        double dx = a.x - b.x;
        double dz = a.z - b.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    /**
     * Wraps an angle to the range [-180, 180].
     *
     * @param angle the angle in degrees
     * @return the wrapped angle
     */
    public static float wrapAngle(float angle) {
        angle = angle % 360.0f;
        if (angle >= 180.0f) angle -= 360.0f;
        if (angle < -180.0f) angle += 360.0f;
        return angle;
    }

    /**
     * Squares a value.
     *
     * @param value the value
     * @return the squared value
     */
    public static double square(double value) {
        return value * value;
    }

    /**
     * Eases in-out using a cubic function.
     *
     * @param t the progress (0.0 to 1.0)
     * @return the eased value
     */
    public static float easeInOutCubic(float t) {
        return t < 0.5f ? 4 * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 3) / 2;
    }

    /**
     * Eases in-out using a quadratic function.
     *
     * @param t the progress (0.0 to 1.0)
     * @return the eased value
     */
    public static float easeInOutQuad(float t) {
        return t < 0.5f ? 2 * t * t : 1 - (float) Math.pow(-2 * t + 2, 2) / 2;
    }

    /**
     * Converts degrees to radians.
     *
     * @param degrees the angle in degrees
     * @return the angle in radians
     */
    public static double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    /**
     * Converts radians to degrees.
     *
     * @param radians the angle in radians
     * @return the angle in degrees
     */
    public static double toDegrees(double radians) {
        return Math.toDegrees(radians);
    }
}
