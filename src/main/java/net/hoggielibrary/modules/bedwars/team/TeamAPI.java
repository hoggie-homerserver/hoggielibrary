package net.hoggielibrary.modules.bedwars.team;

import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Team API for managing Bedwars teams.
 */
public final class TeamAPI {

    private final Map<String, BedwarsTeam> teams = new ConcurrentHashMap<>();

    /**
     * Creates a new Bedwars team.
     *
     * @param name the team name
     * @param color the team color code
     * @param spawn the team spawn position
     * @return the created team
     */
    public BedwarsTeam createTeam(String name, String color, BlockPos spawn) {
        BedwarsTeam team = new BedwarsTeam(name, color, spawn);
        teams.put(name, team);
        return team;
    }

    /**
     * Gets a team by name.
     *
     * @param name the team name
     * @return the team, or null
     */
    public BedwarsTeam getTeam(String name) {
        return teams.get(name);
    }

    /**
     * Adds a player to a team.
     *
     * @param teamName the team name
     * @param playerUuid the player UUID
     */
    public void addPlayer(String teamName, UUID playerUuid) {
        BedwarsTeam team = teams.get(teamName);
        if (team != null) {
            team.addPlayer(playerUuid);
        }
    }

    /**
     * Removes a player from their team.
     *
     * @param playerUuid the player UUID
     */
    public void removePlayer(UUID playerUuid) {
        for (BedwarsTeam team : teams.values()) {
            team.removePlayer(playerUuid);
        }
    }

    /**
     * Gets the team a player belongs to.
     *
     * @param playerUuid the player UUID
     * @return the team, or null
     */
    public BedwarsTeam getPlayerTeam(UUID playerUuid) {
        for (BedwarsTeam team : teams.values()) {
            if (team.hasPlayer(playerUuid)) return team;
        }
        return null;
    }

    /**
     * Returns all teams.
     *
     * @return map of team names to teams
     */
    public Map<String, BedwarsTeam> getTeams() {
        return teams;
    }
}
