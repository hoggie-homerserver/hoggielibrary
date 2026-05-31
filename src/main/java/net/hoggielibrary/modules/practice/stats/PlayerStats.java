package net.hoggielibrary.modules.practice.stats;

import java.util.UUID;

public final class PlayerStats {

    private final UUID playerUuid;
    private int wins;
    private int losses;
    private int kills;
    private int deaths;
    private int streak;

    public PlayerStats(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public UUID getPlayerUuid() { return playerUuid; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public int getStreak() { return streak; }

    public void incrementWins() { this.wins++; this.streak++; }
    public void incrementLosses() { this.losses++; this.streak = 0; }
    public void incrementKills() { this.kills++; }
    public void incrementDeaths() { this.deaths++; }
}
