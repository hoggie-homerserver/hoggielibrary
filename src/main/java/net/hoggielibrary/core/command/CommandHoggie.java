package net.hoggielibrary.core.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.hoggielibrary.core.HoggieLibraryMod;
import net.hoggielibrary.core.command.HoggieCommandRegistry.HoggieCommand;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.developer.DeveloperAPI;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;

import static net.minecraft.server.command.CommandManager.literal;

/**
 * The /hlib command - main management command for Hoggie Library.
 *
 * <p>Sub-commands:
 * <ul>
 *   <li>{@code /hlib reload} - Reloads configuration</li>
 *   <li>{@code /hlib debug} - Toggles debug mode</li>
 *   <li>{@code /hlib profile} - Shows profiling info</li>
 *   <li>{@code /hlib dump} - Dumps framework state</li>
 *   <li>{@code /hlib version} - Shows version info</li>
 *   <li>{@code /hlib stats} - Shows runtime statistics</li>
 * </ul>
 */
public final class CommandHoggie implements HoggieCommand {

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("hlib")
                        .requires(source -> true)
                        .then(literal("reload").executes(this::reload))
                        .then(literal("debug").executes(this::debug))
                        .then(literal("profile").executes(this::profile))
                        .then(literal("dump").executes(this::dump))
                        .then(literal("version").executes(this::version))
                        .then(literal("stats").executes(this::stats))
        );
    }

    private int reload(CommandContext<ServerCommandSource> ctx) {
        HoggieLibraryMod.getInstance().getConfig().reload();
        ctx.getSource().sendFeedback(() -> Text.literal("§a[Hoggie] Configuration reloaded"), true);
        return Command.SINGLE_SUCCESS;
    }

    private int debug(CommandContext<ServerCommandSource> ctx) {
        boolean enabled = DeveloperAPI.toggleDebug();
        ctx.getSource().sendFeedback(() ->
                Text.literal("§a[Hoggie] Debug mode " + (enabled ? "enabled" : "disabled")), true);
        return Command.SINGLE_SUCCESS;
    }

    private int profile(CommandContext<ServerCommandSource> ctx) {
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        long usedMB = (memory.getHeapMemoryUsage().getUsed() / (1024 * 1024));
        long maxMB = (memory.getHeapMemoryUsage().getMax() / (1024 * 1024));
        long uptimeSeconds = runtime.getUptime() / 1000;

        ctx.getSource().sendFeedback(() -> Text.literal("§6=== Hoggie Library Profile ==="), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal(String.format("§eMemory: §f%dMB / %dMB", usedMB, maxMB)), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal(String.format("§eUptime: §f%d seconds", uptimeSeconds)), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal(String.format("§eScheduled Tasks: §f%d",
                        HoggieLibraryMod.getInstance().getScheduler().getPendingTaskCount())), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal(String.format("§eThreads: §f%d", Thread.activeCount())), false);

        return Command.SINGLE_SUCCESS;
    }

    private int dump(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(() -> Text.literal("§6=== Hoggie Library State Dump ==="), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal("§eMod ID: §fhoggielibrary"), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal(String.format("§eInitialized: §f%s",
                        HoggieLibraryMod.getInstance().isInitialized())), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal(String.format("§eConfig Keys: §f%d",
                        HoggieLibraryMod.getInstance().getConfig().getAll().size())), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal(String.format("§ePending Tasks: §f%d",
                        HoggieLibraryMod.getInstance().getScheduler().getPendingTaskCount())), false);

        return Command.SINGLE_SUCCESS;
    }

    private int version(CommandContext<ServerCommandSource> ctx) {
        String version = CommandHoggie.class.getPackage().getImplementationVersion();
        String displayVersion = (version != null) ? version : "1.0.0";
        ctx.getSource().sendFeedback(() ->
                Text.literal("§a[Hoggie] Hoggie Library v" + displayVersion), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal("§7Framework for Minecraft Fabric 1.21.1"), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal("§7Java 21+ | By Hoggie"), false);
        return Command.SINGLE_SUCCESS;
    }

    private int stats(CommandContext<ServerCommandSource> ctx) {
        Runtime runtime = Runtime.getRuntime();
        long usedMB = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        long totalMB = runtime.totalMemory() / (1024 * 1024);
        long maxMB = runtime.maxMemory() / (1024 * 1024);
        int processors = runtime.availableProcessors();

        ctx.getSource().sendFeedback(() -> Text.literal("§6=== Hoggie Library Statistics ==="), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal(String.format("§eJVM Memory: §f%dMB / %dMB (Max: %dMB)", usedMB, totalMB, maxMB)), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal(String.format("§eProcessors: §f%d", processors)), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal(String.format("§eTick: §f%d",
                        HoggieLibraryMod.getInstance().getScheduler().getCurrentTick())), false);
        ctx.getSource().sendFeedback(() ->
                Text.literal(String.format("§eConfig Size: §f%d entries",
                        HoggieLibraryMod.getInstance().getConfig().getAll().size())), false);

        return Command.SINGLE_SUCCESS;
    }
}
