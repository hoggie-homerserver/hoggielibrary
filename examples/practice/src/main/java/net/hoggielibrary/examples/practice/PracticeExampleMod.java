package net.hoggielibrary.examples.practice;

import net.fabricmc.api.ModInitializer;
import net.hoggielibrary.api.Hoggie;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.practice.arena.Arena;
import net.hoggielibrary.modules.practice.duel.DuelAPI;
import net.hoggielibrary.modules.practice.match.MatchAPI;
import net.hoggielibrary.modules.practice.queue.QueueAPI;
import net.hoggielibrary.modules.practice.timer.TimerAPI;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

/**
 * Example mod demonstrating the Hoggie Library Practice Framework.
 */
public final class PracticeExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {
        HoggieLogger.info("Practice Example Mod initialized");

        // Example: Create arenas
        Arena arena = Hoggie.practice.arena().createArena("arena1",
                new BlockPos(-10, 64, -10), new BlockPos(10, 80, 10));

        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();

        // Example: Queue system
        QueueAPI queue = Hoggie.practice.queue();
        queue.addToQueue(player1, "nodebuff");
        queue.addToQueue(player2, "nodebuff");
        HoggieLogger.info("Queue size: {}", queue.getQueueSize("nodebuff"));

        // Example: Duel system
        DuelAPI duel = Hoggie.practice.duel();
        duel.createDuel(player1, player2, "arena1");

        // Example: Match system
        MatchAPI match = Hoggie.practice.match();
        match.createMatch("match1", player1, player2, "arena1");
        match.endMatch("match1", player1);

        // Example: Timer system
        TimerAPI timer = Hoggie.practice.timer();
        timer.start(player1);
        timer.stop(player1);

        // Example: Stats
        Hoggie.practice.stats().recordKill(player1);
        Hoggie.practice.stats().recordDeath(player2);
        HoggieLogger.info("K/D Ratio: {}", Hoggie.practice.stats().getKillDeathRatio(player1));

        // Example: Checkpoints
        Hoggie.practice.checkpoint().saveCheckpoint(player1,
                new net.minecraft.util.math.Vec3d(0, 70, 0), 0, 0);

        Hoggie.notifications.success("Practice Example Mod loaded");
    }
}
