package net.hoggielibrary.core.animation;

import net.hoggielibrary.util.math.MathUtilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Animation system for smooth value transitions.
 *
 * <p>Supports easing functions, callbacks, and concurrent animations.
 *
 * <p>Usage:
 * <pre>{@code
 * Animation anim = new Animation(0, 100, 20, Easing.EASE_IN_OUT_QUAD);
 * anim.onUpdate(value -> System.out.println(value));
 * anim.start();
 * }</pre>
 */
public final class AnimationSystem {

    private final Map<String, Animation> animations = new ConcurrentHashMap<>();

    /**
     * Creates and starts an animation.
     *
     * @param id the animation identifier
     * @param from the start value
     * @param to the end value
     * @param duration the duration in ticks
     * @return the animation instance
     */
    public Animation animate(String id, double from, double to, int duration) {
        return animate(id, from, to, duration, Easing.LINEAR);
    }

    /**
     * Creates and starts an animation with easing.
     *
     * @param id the animation identifier
     * @param from the start value
     * @param to the end value
     * @param duration the duration in ticks
     * @param easing the easing function
     * @return the animation instance
     */
    public Animation animate(String id, double from, double to, int duration, Easing easing) {
        Animation existing = animations.get(id);
        if (existing != null && !existing.isFinished()) {
            existing.finish();
        }
        Animation animation = new Animation(from, to, duration, easing);
        animations.put(id, animation);
        return animation;
    }

    /**
     * Gets an animation by ID.
     *
     * @param id the animation ID
     * @return the animation, or null
     */
    public Animation get(String id) {
        return animations.get(id);
    }

    /**
     * Removes a completed animation.
     *
     * @param id the animation ID
     */
    public void remove(String id) {
        animations.remove(id);
    }

    /**
     * Ticks all active animations.
     */
    public void tick() {
        animations.values().removeIf(animation -> {
            if (!animation.isFinished()) {
                animation.tick();
            }
            return animation.isFinished();
        });
    }

    /**
     * Represents a single animation instance.
     */
    public static final class Animation {
        private final double from;
        private final double to;
        private final int duration;
        private final Easing easing;
        private int currentTick;
        private Consumer<Double> updateCallback;
        private Runnable finishCallback;
        private boolean finished;

        Animation(double from, double to, int duration, Easing easing) {
            this.from = from;
            this.to = to;
            this.duration = duration;
            this.easing = easing;
        }

        /**
         * Sets the update callback.
         *
         * @param callback receives the current value
         * @return this animation for chaining
         */
        public Animation onUpdate(Consumer<Double> callback) {
            this.updateCallback = callback;
            return this;
        }

        /**
         * Sets the completion callback.
         *
         * @param callback runs when animation finishes
         * @return this animation for chaining
         */
        public Animation onFinish(Runnable callback) {
            this.finishCallback = callback;
            return this;
        }

        /**
         * Gets the current interpolated value.
         *
         * @return the current value
         */
        public double getValue() {
            float progress = Math.min(1.0f, (float) currentTick / duration);
            float eased = easing.apply(progress);
            return MathUtilities.lerp(from, to, eased);
        }

        /**
         * Returns progress as a float (0.0 to 1.0).
         *
         * @return the progress
         */
        public float getProgress() {
            return Math.min(1.0f, (float) currentTick / duration);
        }

        /**
         * Returns whether the animation has finished.
         *
         * @return true if finished
         */
        public boolean isFinished() {
            return finished;
        }

        void tick() {
            if (finished) return;
            currentTick++;
            if (updateCallback != null) {
                updateCallback.accept(getValue());
            }
            if (currentTick >= duration) {
                finished = true;
                if (finishCallback != null) {
                    finishCallback.run();
                }
            }
        }

        void finish() {
            currentTick = duration;
            finished = true;
            if (updateCallback != null) {
                updateCallback.accept(to);
            }
            if (finishCallback != null) {
                finishCallback.run();
            }
        }
    }
}
