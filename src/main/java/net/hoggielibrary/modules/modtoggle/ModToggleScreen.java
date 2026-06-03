package net.hoggielibrary.modules.modtoggle;

import net.hoggielibrary.api.Hoggie;
import net.hoggielibrary.modules.gui.ContainerScreen;
import net.hoggielibrary.modules.gui.VanillaBackground;
import net.hoggielibrary.modules.gui.components.Button;
import net.hoggielibrary.modules.gui.components.Label;
import net.hoggielibrary.modules.gui.components.ScrollPanel;
import net.hoggielibrary.modules.gui.components.Toggle;
import net.minecraft.text.Text;

import java.util.List;

public class ModToggleScreen extends ContainerScreen {

    public ModToggleScreen() {
        super(Text.literal("Mod Toggle"), VanillaBackground.GENERIC_54_W, VanillaBackground.GENERIC_54_H);
        setBgSize(Math.max(VanillaBackground.GENERIC_54_W, 260), Math.max(VanillaBackground.GENERIC_54_H, 210));
    }

    @Override
    public void init() {
        super.init();
        children.clear();

        List<ModToggleAPI.ModEntry> entries = Hoggie.modToggle.getAllToggles();

        Label header = new Label(containerX + 8, containerY + 18, "§lMods (" + entries.size() + " found)");
        header.setTextColor(0xFF88CCFF);
        add(header);

        int panelX = containerX + 7;
        int panelY = containerY + 30;
        int panelW = bgWidth - 14;
        int panelH = bgHeight - 54;

        ScrollPanel scroll = new ScrollPanel(panelX, panelY, panelW, panelH);
        scroll.setBackgroundColor(0x00000000);

        int sy = panelY + 4;
        for (ModToggleAPI.ModEntry mod : entries) {
            String label = mod.displayName();
            if (!mod.displayName().equals(mod.modId())) {
                label = mod.displayName() + " §7(" + mod.modId() + ")";
            }

            String status = mod.enabled() ? "§aON" : "§cOFF";
            Label statusLabel = new Label(panelX + panelW - 50, sy, status);
            statusLabel.setTextColor(mod.enabled() ? 0xFF40AA40 : 0xFFAA4040);
            scroll.add(statusLabel);

            Toggle toggle = new Toggle(panelX + 8, sy, label, mod.enabled(), val -> {
                Hoggie.modToggle.setEnabled(mod.modId(), val);
            });
            toggle.setLabelColor(0xFFE0E0E0);
            toggle.setTrackOnColor(0xFF40AA40);
            scroll.add(toggle);

            sy += 24;
        }

        if (entries.isEmpty()) {
            scroll.add(new Label(panelX + 8, panelY + 8, "§7No mods found in mods folder"));
        }

        scroll.recalcContentHeight();
        add(scroll);

        int btnW = 80;
        int btnX = containerX + (bgWidth - btnW) / 2;
        add(new Button(btnX, containerY + bgHeight - 22, btnW, 18, "Close", b -> close()));
    }
}
