package net.hoggielibrary.modules.practice.replay;

import net.minecraft.util.math.Vec3d;

public record ReplayFrame(
        Vec3d position,
        float yaw,
        float pitch,
        float health,
        int hotbarSlot,
        long timestamp
) {
}
