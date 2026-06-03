package net.hoggielibrary.modules.modtoggle;

import net.hoggielibrary.api.Hoggie;
import net.hoggielibrary.modules.gui.ContainerScreen;
import net.hoggielibrary.modules.gui.VanillaBackground;
import net.hoggielibrary.modules.gui.components.Button;
import net.hoggielibrary.modules.gui.components.Label;
import net.hoggielibrary.modules.gui.components.ScrollPanel;
import net.hoggielibrary.modules.gui.components.Toggle;
import net.minecraft.text.Text;

import java.util.Map;

public class ModToggleScreen extends ContainerScreen {

    public ModToggleScreen() {
        super(Text.literal("Mod Toggle"), VanillaBackground.GENERIC_54_W, VanillaBackground.GENERIC_54_H);
        setBgSize(Math.max(VanillaBackground.GENERIC_54_W, 240), Math.max(VanillaBackground.GENERIC_54_H, 200));
    }

    @Override
    public void init() {
        super.init();
        children.clear();

        Map<String, Boolean> toggles = Hoggie.modToggle.getAllToggles();

        int panelX = containerX + 7;
        int panelY = containerY + 18;
        int panelW = bgWidth - 14;
        int panelH = bgHeight - 40;

        ScrollPanel scroll = new ScrollPanel(panelX, panelY, panelW, panelH);
        scroll.setBackgroundColor(0x00000000);

        int sy = panelY + 4;
        for (Map.Entry<String, Boolean> entry : toggles.entrySet()) {
            String modId = entry.getKey();
            boolean enabled = entry.getValue();

            String label = modId;
            String displayName = getModName(modId);
            if (!displayName.equals(modId)) {
                label = displayName + " §7(" + modId + ")";
            }

            Toggle toggle = new Toggle(panelX + 8, sy, label, enabled, val -> {
                Hoggie.modToggle.setEnabled(modId, val);
                String msg = val ? "§aEnabled" : "§cDisabled";
                String modName = getModName(modId);
                Hoggie.notifications.info(msg + " §f" + modName);
            });
            toggle.setLabelColor(0xFFE0E0E0);
            toggle.setTrackOnColor(0xFF40AA40);
            scroll.add(toggle);

            sy += 24;
        }

        if (toggles.isEmpty()) {
            scroll.add(new Label(panelX + 8, panelY + 8, "§7No toggleable mods found"));
        }

        scroll.recalcContentHeight();
        add(scroll);

        int btnW = 60;
        int btnX = containerX + (bgWidth - btnW) / 2;
        int btnY = containerY + bgHeight - 22;

        add(new Button(btnX, btnY, btnW, 18, "Close", b -> close()));
    }

    private static String getModName(String modId) {
        try {
            return net.fabricmc.loader.api.FabricLoader.getInstance()
                    .getModContainer(modId)
                    .map(c -> c.getMetadata().getName())
                    .orElse(modId);
        } catch (Exception e) {
            return modId;
        }
    }
}
