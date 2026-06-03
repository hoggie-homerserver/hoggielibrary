package net.hoggielibrary.modules.modtoggle;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.hoggielibrary.api.Hoggie;
import net.hoggielibrary.core.logging.HoggieLogger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ModToggleAPI {

    public record ModEntry(String modId, String displayName, Path jarPath, boolean enabled) {}

    public ModToggleAPI() {
        HoggieLogger.debug("ModToggle API initialized");
    }

    public boolean isEnabled(String modId) {
        if (modId.equals("hoggielibrary")) return true;
        for (ModEntry entry : scanModsFolder()) {
            if (entry.modId().equals(modId)) {
                return entry.enabled();
            }
        }
        return true;
    }

    public void setEnabled(String modId, boolean enabled) {
        if (modId.equals("hoggielibrary")) {
            Hoggie.notifications.warning("Cannot disable Hoggie Library");
            return;
        }
        for (ModEntry entry : scanModsFolder()) {
            if (entry.modId().equals(modId)) {
                if (entry.enabled() == enabled) return;
                Path jarPath = entry.jarPath();
                Path disabledPath = jarPath.resolveSibling(jarPath.getFileName() + ".disabled");
                Path target = enabled ? disabledPath : jarPath;
                Path source = enabled ? jarPath : disabledPath;
                if (!Files.exists(source)) {
                    HoggieLogger.error("File not found: {}", source);
                    Hoggie.notifications.error("File not found: " + source.getFileName());
                    return;
                }
                try {
                    Files.move(source, target);
                    HoggieLogger.info("Renamed {} -> {}", source, target);
                    String name = entry.displayName();
                    Hoggie.notifications.info((enabled ? "§aEnabled" : "§cDisabled") + " §f" + name);
                    Hoggie.notifications.warning("Restart game for changes to take effect");
                } catch (IOException e) {
                    HoggieLogger.error("Failed to rename {} -> {}", source, target, e);
                    Hoggie.notifications.error("Failed to " + (enabled ? "enable" : "disable") + " " + entry.displayName());
                }
                return;
            }
        }
        Hoggie.notifications.warning("Mod not found in mods folder: " + modId);
    }

    public List<ModEntry> getAllToggles() {
        return scanModsFolder();
    }

    private List<ModEntry> scanModsFolder() {
        List<ModEntry> entries = new ArrayList<>();
        Path modsDir = FabricLoader.getInstance().getGameDir().resolve("mods");
        if (!Files.isDirectory(modsDir)) return entries;

        List<Path> jars = new ArrayList<>();
        try (var stream = Files.list(modsDir)) {
            stream.forEach(jars::add);
        } catch (IOException e) {
            HoggieLogger.error("Failed to list mods folder", e);
            return entries;
        }

        for (Path path : jars) {
            String name = path.getFileName().toString();
            boolean enabled;
            Path jarPath;

            if (name.endsWith(".jar")) {
                enabled = true;
                jarPath = path;
            } else if (name.endsWith(".jar.disabled")) {
                enabled = false;
                jarPath = path;
            } else {
                continue;
            }

            String modId = readModId(jarPath);
            if (modId == null) continue;
            if (shouldHide(modId)) continue;

            String displayName = readDisplayName(jarPath, modId);
            entries.add(new ModEntry(modId, displayName, jarPath, enabled));
        }

        entries.sort(Comparator.comparing(e -> e.displayName().toLowerCase()));
        return entries;
    }

    private String readModId(Path jarPath) {
        try (ZipFile zip = new ZipFile(jarPath.toFile())) {
            ZipEntry entry = zip.getEntry("fabric.mod.json");
            if (entry == null) return null;
            JsonObject json = JsonParser.parseReader(new InputStreamReader(zip.getInputStream(entry))).getAsJsonObject();
            if (json.has("id")) return json.get("id").getAsString();
        } catch (Exception ignored) {}
        return null;
    }

    private String readDisplayName(Path jarPath, String fallback) {
        try (ZipFile zip = new ZipFile(jarPath.toFile())) {
            ZipEntry entry = zip.getEntry("fabric.mod.json");
            if (entry == null) return fallback;
            JsonObject json = JsonParser.parseReader(new InputStreamReader(zip.getInputStream(entry))).getAsJsonObject();
            if (json.has("name")) return json.get("name").getAsString();
        } catch (Exception ignored) {}
        return fallback;
    }

    private boolean shouldHide(String modId) {
        return modId.equals("minecraft")
                || modId.equals("fabricloader")
                || modId.equals("fabric-api")
                || modId.equals("java")
                || modId.startsWith("fabric-")
                || modId.startsWith("fabric_api-");
    }
}
