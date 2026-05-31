package net.hoggielibrary.modules.bedwars.upgrade;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Upgrade API for managing Bedwars team upgrades.
 */
public final class UpgradeAPI {

    private final Map<String, Map<String, Integer>> teamUpgrades = new ConcurrentHashMap<>();

    /**
     * Applies an upgrade to a team.
     *
     * @param teamName the team name
     * @param upgradeName the upgrade name
     * @param level the upgrade level
     */
    public void applyUpgrade(String teamName, String upgradeName, int level) {
        teamUpgrades.computeIfAbsent(teamName, k -> new ConcurrentHashMap<>())
                .put(upgradeName, level);
    }

    /**
     * Gets the upgrade level for a team.
     *
     * @param teamName the team name
     * @param upgradeName the upgrade name
     * @return the upgrade level (0 if not applied)
     */
    public int getUpgradeLevel(String teamName, String upgradeName) {
        Map<String, Integer> upgrades = teamUpgrades.get(teamName);
        return upgrades != null ? upgrades.getOrDefault(upgradeName, 0) : 0;
    }

    /**
     * Returns all upgrades for a team.
     *
     * @param teamName the team name
     * @return map of upgrade names to levels
     */
    public Map<String, Integer> getTeamUpgrades(String teamName) {
        return teamUpgrades.getOrDefault(teamName, Map.of());
    }

    /**
     * Clears all upgrades for a team.
     *
     * @param teamName the team name
     */
    public void clearUpgrades(String teamName) {
        teamUpgrades.remove(teamName);
    }
}
