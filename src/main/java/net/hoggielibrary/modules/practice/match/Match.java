package net.hoggielibrary.modules.practice.match;

import java.util.UUID;

public final class Match {

    private final String matchId;
    private final UUID player1;
    private final UUID player2;
    private final String arena;
    private UUID winner;
    private MatchState state = MatchState.STARTING;
    private int player1Score;
    private int player2Score;

    public Match(String matchId, UUID player1, UUID player2, String arena) {
        this.matchId = matchId;
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
    }

    public String getMatchId() { return matchId; }
    public UUID getPlayer1() { return player1; }
    public UUID getPlayer2() { return player2; }
    public String getArena() { return arena; }
    public UUID getWinner() { return winner; }
    public MatchState getState() { return state; }
    public int getPlayer1Score() { return player1Score; }
    public int getPlayer2Score() { return player2Score; }

    public void setWinner(UUID winner) { this.winner = winner; }
    public void setState(MatchState state) { this.state = state; }
    public void addPlayer1Score() { this.player1Score++; }
    public void addPlayer2Score() { this.player2Score++; }
}
