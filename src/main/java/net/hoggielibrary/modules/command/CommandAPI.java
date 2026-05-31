package net.hoggielibrary.modules.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class CommandAPI {

    public static void registerCommand(String name, Consumer<LiteralArgumentBuilder<ServerCommandSource>> builder) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralArgumentBuilder<ServerCommandSource> literal = LiteralArgumentBuilder.literal(name);
            builder.accept(literal);
            dispatcher.register(literal);
        });
    }

    public static void registerCommand(String name, BiConsumer<CommandDispatcher<ServerCommandSource>, CommandRegistryAccess> builder) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            builder.accept(dispatcher, registryAccess);
        });
    }
}
