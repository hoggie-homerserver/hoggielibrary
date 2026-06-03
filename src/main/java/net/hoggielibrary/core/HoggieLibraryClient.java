package net.hoggielibrary.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hoggielibrary.core.command.HoggieClientCommandRegistry;
import net.hoggielibrary.core.keybind.HoggieKeybindManager;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.gui.GuiScreenRegistry;
import net.hoggielibrary.modules.gui.command.GuiCommand;
import net.hoggielibrary.modules.modtoggle.ModToggleScreen;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public final class HoggieLibraryClient implements ClientModInitializer {

    private static HoggieLibraryClient instance;
    private HoggieKeybindManager keybindManager;

    public static HoggieLibraryClient getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        HoggieLogger.info("Initializing Hoggie Library client");

        this.keybindManager = HoggieKeybindManager.getOrCreate();
        this.keybindManager.registerDefaults();

        HoggieClientCommandRegistry.registerCommands();
        GuiCommand.register();

        GuiScreenRegistry.register(Identifier.of("hoggielibrary", "hoggie_gui_modtoggle"), ModToggleScreen::new);

        this.keybindManager.register("key.hoggielibrary.modtoggle", -1,
                Identifier.of("hoggielibrary", "general"),
                () -> GuiScreenRegistry.open(Identifier.of("hoggielibrary", "hoggie_gui_modtoggle")));

        HoggieLogger.info("Hoggie Library client initialized");
    }

    public HoggieKeybindManager getKeybindManager() {
        return keybindManager;
    }
}
