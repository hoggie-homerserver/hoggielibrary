package net.hoggielibrary.modules.bedwars.bed;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bed API for managing Bedwars bed destruction.
 */
public final class BedAPI {

    private final Map<String, BlockPos[]> teamBeds = new ConcurrentHashMap<>();
    private final Map<String, Boolean> bedStatus = new ConcurrentHashMap<>();

    /**
     * Registers a bed for a team.
     *
     * @param teamName the team name
     * @param bedHead the head of the bed
     * @param bedFoot the foot of the bed
     */
    public void registerBed(String teamName, BlockPos bedHead, BlockPos bedFoot) {
        teamBeds.put(teamName, new BlockPos[]{bedHead, bedFoot});
        bedStatus.put(teamName, true);
    }

    /**
     * Destroys a team's bed.
     *
     * @param teamName the team name
     */
    public void destroyBed(String teamName) {
        bedStatus.put(teamName, false);
        HoggieLogger.info("Bed destroyed for team: {}", teamName);
    }

    /**
     * Returns whether a team's bed is alive.
     *
     * @param teamName the team name
     * @return true if the bed is intact
     */
    public boolean isBedAlive(String teamName) {
        return bedStatus.getOrDefault(teamName, false);
    }

    /**
     * Gets the bed positions for a team.
     *
     * @param teamName the team name
     * @return array of [head, foot] positions
     */
    public BlockPos[] getBedPositions(String teamName) {
        return teamBeds.get(teamName);
    }

    /**
     * Returns the number of teams with alive beds.
     *
     * @return alive bed count
     */
    public long getAliveBedCount() {
        return bedStatus.values().stream().filter(b -> b).count();
    }
}
