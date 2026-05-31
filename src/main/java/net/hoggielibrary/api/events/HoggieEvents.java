package net.hoggielibrary.api.events;

import net.hoggielibrary.core.event.CancellableEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Built-in event types for the Hoggie Event Bus.
 *
 * <p>These events are fired by various framework systems and can
 * be subscribed to using {@code Hoggie.events.subscribe(...)}.
 */
public final class HoggieEvents {

    private HoggieEvents() {
    }

    /**
     * Fired when the player attacks an entity.
     */
    public static final class AttackEvent implements CancellableEvent {
        private final Entity target;
        private boolean cancelled;

        public AttackEvent(Entity target) {
            this.target = target;
        }

        public Entity getTarget() { return target; }

        @Override
        public void cancel() { this.cancelled = true; }

        @Override
        public boolean isCancelled() { return cancelled; }
    }

    /**
     * Fired when the player places a block.
     */
    public static final class BlockPlaceEvent implements CancellableEvent {
        private final BlockPos pos;
        private boolean cancelled;

        public BlockPlaceEvent(BlockPos pos) {
            this.pos = pos;
        }

        public BlockPos getPos() { return pos; }

        @Override
        public void cancel() { this.cancelled = true; }

        @Override
        public boolean isCancelled() { return cancelled; }
    }

    /**
     * Fired when the player breaks a block.
     */
    public static final class BlockBreakEvent implements CancellableEvent {
        private final BlockPos pos;
        private boolean cancelled;

        public BlockBreakEvent(BlockPos pos) {
            this.pos = pos;
        }

        public BlockPos getPos() { return pos; }

        @Override
        public void cancel() { this.cancelled = true; }

        @Override
        public boolean isCancelled() { return cancelled; }
    }

    /**
     * Fired when the player takes damage.
     */
    public static final class DamageEvent implements CancellableEvent {
        private final LivingEntity target;
        private float damage;
        private boolean cancelled;

        public DamageEvent(LivingEntity target, float damage) {
            this.target = target;
            this.damage = damage;
        }

        public LivingEntity getTarget() { return target; }
        public float getDamage() { return damage; }
        public void setDamage(float damage) { this.damage = damage; }

        @Override
        public void cancel() { this.cancelled = true; }

        @Override
        public boolean isCancelled() { return cancelled; }
    }

    /**
     * Fired on each client tick.
     */
    public static final class ClientTickEvent {
    }

    /**
     * Fired when the player joins a world.
     */
    public static final class WorldJoinEvent {
    }

    /**
     * Fired when the player disconnects from a world.
     */
    public static final class WorldLeaveEvent {
    }

    /**
     * Fired when a module is enabled or disabled.
     */
    public static final class ModuleToggleEvent {
        private final String moduleId;
        private final boolean enabled;

        public ModuleToggleEvent(String moduleId, boolean enabled) {
            this.moduleId = moduleId;
            this.enabled = enabled;
        }

        public String getModuleId() { return moduleId; }
        public boolean isEnabled() { return enabled; }
    }
}
