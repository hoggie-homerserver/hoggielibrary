package net.hoggielibrary.core.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.ArrayList;
import java.util.List;

public final class HoggieClientCommandRegistry {

    private static final List<HoggieClientCommand> commands = new ArrayList<>();
    private static boolean registered = false;

    private HoggieClientCommandRegistry() {
    }

    public static void registerCommands() {
        if (registered) return;

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            for (HoggieClientCommand command : commands) {
                command.register(dispatcher);
            }
            HoggieLogger.debug("Registered {} Hoggie client commands", commands.size());
        });

        registered = true;
    }

    public static void register(HoggieClientCommand command) {
        commands.add(command);
    }

    public interface HoggieClientCommand {
        void register(CommandDispatcher<FabricClientCommandSource> dispatcher);
    }
}
