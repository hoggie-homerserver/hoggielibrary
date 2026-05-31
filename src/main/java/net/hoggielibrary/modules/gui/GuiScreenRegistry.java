package net.hoggielibrary.modules.gui;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class GuiScreenRegistry {

    private static final Map<Identifier, Supplier<HoggieScreen>> screens = new ConcurrentHashMap<>();

    private GuiScreenRegistry() {
    }

    public static void register(Identifier id, Supplier<HoggieScreen> factory) {
        screens.put(id, factory);
        HoggieLogger.debug("Registered GUI screen: {}", id);
    }

    public static void register(String id, Supplier<HoggieScreen> factory) {
        register(Identifier.of(id), factory);
    }

    public static HoggieScreen open(Identifier id) {
        Supplier<HoggieScreen> factory = screens.get(id);
        if (factory == null) {
            HoggieLogger.warn("No GUI screen registered for: {}", id);
            return null;
        }
        HoggieScreen screen = factory.get();
        HoggieScreen.open(screen);
        return screen;
    }

    public static HoggieScreen open(String id) {
        return open(Identifier.of(id));
    }

    public static boolean isRegistered(Identifier id) {
        return screens.containsKey(id);
    }
}
