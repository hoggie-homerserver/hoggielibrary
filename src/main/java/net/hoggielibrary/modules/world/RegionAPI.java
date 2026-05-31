package net.hoggielibrary.modules.world;

import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Region utilities for defining and querying world regions.
 */
public final class RegionAPI {

    private final Map<String, Region> regions = new ConcurrentHashMap<>();

    /**
     * Defines a new region.
     *
     * @param name the region name
     * @param pos1 the first corner
     * @param pos2 the second corner
     * @return the created region
     */
    public Region defineRegion(String name, BlockPos pos1, BlockPos pos2) {
        Region region = new Region(name, pos1, pos2);
        regions.put(name, region);
        return region;
    }

    /**
     * Gets a region by name.
     *
     * @param name the region name
     * @return the region, or null
     */
    public Region getRegion(String name) {
        return regions.get(name);
    }

    /**
     * Removes a region.
     *
     * @param name the region name
     */
    public void removeRegion(String name) {
        regions.remove(name);
    }

    /**
     * Checks if a position is within a region.
     *
     * @param pos the position
     * @param regionName the region name
     * @return true if inside
     */
    public boolean isInside(BlockPos pos, String regionName) {
        Region region = regions.get(regionName);
        return region != null && region.contains(pos);
    }

    public record Region(String name, BlockPos pos1, BlockPos pos2) {
        public boolean contains(BlockPos pos) {
            int minX = Math.min(pos1.getX(), pos2.getX());
            int minY = Math.min(pos1.getY(), pos2.getY());
            int minZ = Math.min(pos1.getZ(), pos2.getZ());
            int maxX = Math.max(pos1.getX(), pos2.getX());
            int maxY = Math.max(pos1.getY(), pos2.getY());
            int maxZ = Math.max(pos1.getZ(), pos2.getZ());
            return pos.getX() >= minX && pos.getX() <= maxX
                    && pos.getY() >= minY && pos.getY() <= maxY
                    && pos.getZ() >= minZ && pos.getZ() <= maxZ;
        }
    }
}
