package net.hoggielibrary.modules.practice;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.practice.arena.ArenaAPI;
import net.hoggielibrary.modules.practice.duel.DuelAPI;
import net.hoggielibrary.modules.practice.queue.QueueAPI;
import net.hoggielibrary.modules.practice.match.MatchAPI;
import net.hoggielibrary.modules.practice.spectator.SpectatorAPI;
import net.hoggielibrary.modules.practice.checkpoint.CheckpointAPI;
import net.hoggielibrary.modules.practice.timer.TimerAPI;
import net.hoggielibrary.modules.practice.replay.ReplayAPI;
import net.hoggielibrary.modules.practice.stats.StatsAPI;

/**
 * Practice Framework API for PvP practice servers.
 *
 * <p>Provides a complete practice server framework including
 * arenas, duels, queueing, matches, spectating, checkpoints,
 * timers, replays, and statistics.
 */
public final class PracticeAPI {

    private final ArenaAPI arena = new ArenaAPI();
    private final DuelAPI duel = new DuelAPI();
    private final QueueAPI queue = new QueueAPI();
    private final MatchAPI match = new MatchAPI();
    private final SpectatorAPI spectator = new SpectatorAPI();
    private final CheckpointAPI checkpoint = new CheckpointAPI();
    private final TimerAPI timer = new TimerAPI();
    private final ReplayAPI replay = new ReplayAPI();
    private final StatsAPI stats = new StatsAPI();

    public PracticeAPI() {
        HoggieLogger.debug("Practice Framework initialized");
    }

    public ArenaAPI arena() { return arena; }
    public DuelAPI duel() { return duel; }
    public QueueAPI queue() { return queue; }
    public MatchAPI match() { return match; }
    public SpectatorAPI spectator() { return spectator; }
    public CheckpointAPI checkpoint() { return checkpoint; }
    public TimerAPI timer() { return timer; }
    public ReplayAPI replay() { return replay; }
    public StatsAPI stats() { return stats; }
}
