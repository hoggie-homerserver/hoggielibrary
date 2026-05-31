package net.hoggielibrary.modules.bedwars.team;

import net.minecraft.util.math.BlockPos;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class BedwarsTeam {

    private final String name;
    private final String color;
    private final BlockPos spawn;
    private final Set<UUID> players = ConcurrentHashMap.newKeySet();
    private boolean bedAlive = true;
    private int maxPlayers = 4;

    public BedwarsTeam(String name, String color, BlockPos spawn) {
        this.name = name;
        this.color = color;
        this.spawn = spawn;
    }

    public String getName() { return name; }
    public String getColor() { return color; }
    public BlockPos getSpawn() { return spawn; }
    public Set<UUID> getPlayers() { return players; }
    public boolean isBedAlive() { return bedAlive; }
    public int getMaxPlayers() { return maxPlayers; }
    public int getPlayerCount() { return players.size(); }

    public void setBedAlive(boolean bedAlive) { this.bedAlive = bedAlive; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public void addPlayer(UUID player) { players.add(player); }
    public void removePlayer(UUID player) { players.remove(player); }
    public boolean hasPlayer(UUID player) { return players.contains(player); }
    public boolean isEliminated() { return !bedAlive && players.isEmpty(); }
}
