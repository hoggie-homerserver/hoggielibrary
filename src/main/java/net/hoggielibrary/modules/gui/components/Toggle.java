package net.hoggielibrary.modules.gui.components;

import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class Toggle extends HoggieWidget {

    protected boolean toggled;
    protected String label;
    protected Consumer<Boolean> onChange;
    protected int trackOffColor = 0xFF404040;
    protected int trackOnColor = 0xFF4080FF;
    protected int handleColor = 0xFFFFFFFF;
    protected int labelColor = 0xFFFFFFFF;

    public Toggle(int x, int y, String label, boolean defaultValue, Consumer<Boolean> onChange) {
        super(x, y, 36, 20);
        this.label = label;
        this.toggled = defaultValue;
        this.onChange = onChange;
        this.width = label != null ? getTextRenderer().getWidth(label) + 44 : 36;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (!visible) return;

        int trackColor = toggled ? trackOnColor : trackOffColor;
        int trackW = 36;
        int trackH = 14;
        int trackX = x;
        int trackY = y + (height - trackH) / 2;
        ctx.fill(trackX, trackY, trackX + trackW, trackY + trackH, trackColor);

        int handleX = toggled ? trackX + trackW - 10 : trackX + 2;
        int handleSize = 10;
        ctx.fill(handleX, trackY + 2, handleX + handleSize, trackY + trackH - 2, handleColor);

        if (label != null) {
            ctx.drawText(getTextRenderer(), label, x + trackW + 8, y + (height - 9) / 2, labelColor, true);
        }
    }

    @Override
    protected boolean onClick(double mx, double my, int button) {
        if (button == 0) {
            toggled = !toggled;
            if (onChange != null) onChange.accept(toggled);
            return true;
        }
        return false;
    }

    public boolean isToggled() { return toggled; }
    public void setToggled(boolean toggled) { this.toggled = toggled; }
    public void setLabel(String label) { this.label = label; }
    public void setOnChange(Consumer<Boolean> onChange) { this.onChange = onChange; }
    public void setTrackOffColor(int c) { this.trackOffColor = c; }
    public void setTrackOnColor(int c) { this.trackOnColor = c; }
    public void setHandleColor(int c) { this.handleColor = c; }
    public void setLabelColor(int c) { this.labelColor = c; }
}
