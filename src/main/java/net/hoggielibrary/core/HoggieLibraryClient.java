package net.hoggielibrary.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hoggielibrary.core.command.HoggieClientCommandRegistry;
import net.hoggielibrary.core.keybind.HoggieKeybindManager;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.gui.command.GuiCommand;

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

        HoggieLogger.info("Hoggie Library client initialized");
    }

    public HoggieKeybindManager getKeybindManager() {
        return keybindManager;
    }
}
