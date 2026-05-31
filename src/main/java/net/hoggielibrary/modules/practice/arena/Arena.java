package net.hoggielibrary.modules.practice.arena;

import net.minecraft.util.math.BlockPos;

import java.util.*;

/**
 * Represents a practice arena with boundaries and state.
 */
public final class Arena {

    private final String name;
    private final BlockPos pos1;
    private final BlockPos pos2;
    private final Set<UUID> players = new HashSet<>();
    private ArenaState state = ArenaState.AVAILABLE;

    public Arena(String name, BlockPos pos1, BlockPos pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public String getName() { return name; }
    public BlockPos getPos1() { return pos1; }
    public BlockPos getPos2() { return pos2; }
    public Set<UUID> getPlayers() { return players; }
    public ArenaState getState() { return state; }
    public void setState(ArenaState state) { this.state = state; }

    public void addPlayer(UUID player) { players.add(player); }
    public void removePlayer(UUID player) { players.remove(player); }
    public boolean contains(UUID player) { return players.contains(player); }
    public boolean isOccupied() { return !players.isEmpty(); }
}
