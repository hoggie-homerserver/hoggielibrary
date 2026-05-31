package net.hoggielibrary.core.animation;

import net.hoggielibrary.util.math.MathUtilities;

import java.util.function.Function;

/**
 * Easing functions for animations.
 *
 * <p>Provides standard easing curves for smooth animation interpolation.
 */
public enum Easing implements Function<Float, Float> {
    /** Linear interpolation */
    LINEAR(t -> t),
    /** Quadratic ease-in */
    EASE_IN_QUAD(t -> t * t),
    /** Quadratic ease-out */
    EASE_OUT_QUAD(t -> t * (2 - t)),
    /** Quadratic ease-in-out */
    EASE_IN_OUT_QUAD(MathUtilities::easeInOutQuad),
    /** Cubic ease-in */
    EASE_IN_CUBIC(t -> t * t * t),
    /** Cubic ease-out */
    EASE_OUT_CUBIC(t -> 1 - (float) Math.pow(1 - t, 3)),
    /** Cubic ease-in-out */
    EASE_IN_OUT_CUBIC(MathUtilities::easeInOutCubic),
    /** Sine ease-in */
    EASE_IN_SINE(t -> 1 - (float) Math.cos(t * Math.PI / 2)),
    /** Sine ease-out */
    EASE_OUT_SINE(t -> (float) Math.sin(t * Math.PI / 2)),
    /** Bounce ease-out */
    EASE_OUT_BOUNCE(t -> {
        if (t < 1 / 2.75f) return 7.5625f * t * t;
        if (t < 2 / 2.75f) return 7.5625f * (t -= 1.5f / 2.75f) * t + 0.75f;
        if (t < 2.5f / 2.75f) return 7.5625f * (t -= 2.25f / 2.75f) * t + 0.9375f;
        return 7.5625f * (t -= 2.625f / 2.75f) * t + 0.984375f;
    }),
    /** Elastic ease-out */
    EASE_OUT_ELASTIC(t -> {
        if (t == 0 || t == 1) return t;
        float p = 0.3f;
        float s = p / 4;
        return (float) (Math.pow(2, -10 * t) * Math.sin((t - s) * (2 * Math.PI) / p) + 1);
    });

    private final Function<Float, Float> function;

    Easing(Function<Float, Float> function) {
        this.function = function;
    }

    @Override
    public Float apply(Float t) {
        return function.apply(Math.max(0.0f, Math.min(1.0f, t)));
    }
}
