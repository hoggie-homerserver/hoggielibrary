package net.hoggielibrary.modules.modtoggle;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.hoggielibrary.api.Hoggie;
import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class ModToggleAPI {

    private static final String CONFIG_PREFIX = "modtoggle.";

    public ModToggleAPI() {
        HoggieLogger.debug("ModToggle API initialized");
    }

    public boolean isEnabled(String modId) {
        String key = CONFIG_PREFIX + modId;
        Boolean stored = Hoggie.config.get(key);
        return stored == null || stored;
    }

    public void setEnabled(String modId, boolean enabled) {
        String key = CONFIG_PREFIX + modId;
        Hoggie.config.set(key, enabled);
        Hoggie.config.save();
        HoggieLogger.info("Mod toggle {} set to {}", modId, enabled);
    }

    public Map<String, Boolean> getAllToggles() {
        Map<String, Boolean> toggles = new LinkedHashMap<>();
        for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
            String modId = container.getMetadata().getId();
            if (shouldShow(modId)) {
                toggles.put(modId, isEnabled(modId));
            }
        }
        return toggles;
    }

    private boolean shouldShow(String modId) {
        if (modId.equals("minecraft") || modId.equals("fabricloader") || modId.equals("fabric-api") || modId.equals("java")) {
            return false;
        }
        if (modId.startsWith("fabric-") || modId.startsWith("fabric_api-")) {
            return false;
        }
        return true;
    }
}
