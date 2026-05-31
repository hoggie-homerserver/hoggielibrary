package net.hoggielibrary.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.hoggielibrary.core.HoggieLibraryMod;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

/**
 * Registry for Hoggie Library built-in commands.
 *
 * <p>Commands are registered with the Brigadier command dispatcher
 * using Fabric's command registration callback.
 */
public final class HoggieCommandRegistry {

    private static final List<HoggieCommand> commands = new ArrayList<>();
    private static boolean registered = false;

    private HoggieCommandRegistry() {
    }

    /**
     * Registers all built-in commands.
     */
    public static void registerCommands() {
        if (registered) return;

        // Register built-in commands
        registerBuiltInCommands();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            for (HoggieCommand command : commands) {
                command.register(dispatcher);
            }
            HoggieLogger.debug("Registered {} Hoggie commands", commands.size());
        });

        registered = true;
    }

    /**
     * Registers a custom command.
     *
     * @param command the command to register
     */
    public static void register(HoggieCommand command) {
        commands.add(command);
    }

    /**
     * Registers all built-in commands.
     */
    private static void registerBuiltInCommands() {
        register(new CommandHoggie());
    }

    /**
     * Interface for Hoggie commands.
     */
    public interface HoggieCommand {
        /**
         * Registers this command with the dispatcher.
         *
         * @param dispatcher the command dispatcher
         */
        void register(CommandDispatcher<ServerCommandSource> dispatcher);
    }
}
