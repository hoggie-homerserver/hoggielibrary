package net.hoggielibrary.modules.practice.duel;

import java.util.UUID;

public final class Duel {

    private final UUID player1;
    private final UUID player2;
    private final String arena;
    private UUID winner;
    private DuelState state = DuelState.STARTING;

    public Duel(UUID player1, UUID player2, String arena) {
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
    }

    public UUID getPlayer1() { return player1; }
    public UUID getPlayer2() { return player2; }
    public String getArena() { return arena; }
    public UUID getWinner() { return winner; }
    public DuelState getState() { return state; }

    public void setWinner(UUID winner) { this.winner = winner; }
    public void setState(DuelState state) { this.state = state; }

    public boolean containsPlayer(UUID player) {
        return player1.equals(player) || player2.equals(player);
    }

    public UUID getOpponent(UUID player) {
        return player.equals(player1) ? player2 : player1;
    }
}
