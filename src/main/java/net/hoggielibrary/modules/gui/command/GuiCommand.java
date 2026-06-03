package net.hoggielibrary.modules.gui.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.hoggielibrary.core.command.HoggieClientCommandRegistry;
import net.hoggielibrary.modules.gui.GuiScreenRegistry;
import net.hoggielibrary.modules.gui.HoggieScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public final class GuiCommand implements HoggieClientCommandRegistry.HoggieClientCommand {

    private static final String PREFIX = "hoggie_gui_";

    public static void register() {
        HoggieClientCommandRegistry.register(new GuiCommand());
    }

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("hoggie")
                        .then(literal("gui")
                                .then(argument("screen", StringArgumentType.word())
                                        .executes(ctx -> {
                                            String name = StringArgumentType.getString(ctx, "screen");
                                            Identifier id = Identifier.of("hoggielibrary", PREFIX + name);
                                            HoggieScreen screen = GuiScreenRegistry.open(id);
                                            if (screen == null) {
                                                ctx.getSource().sendError(
                                                        Text.literal("Unknown GUI screen: " + name));
                                                return 0;
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
        );
    }
}
