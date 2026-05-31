package net.hoggielibrary.modules.pvp.target;

/**
 * Target priority modes for the {@link TargetManager}.
 */
public enum TargetPriority {
    /** Selects the closest target */
    DISTANCE,
    /** Selects the target with the lowest health */
    HEALTH,
    /** Selects the target with the lowest armor */
    ARMOR,
    /** Selects the target closest to the crosshair */
    CROSSHAIR
}
