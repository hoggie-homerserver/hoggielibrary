package net.hoggielibrary.modules.gui.components;

import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class Slider extends HoggieWidget {

    protected double min, max, value;
    protected double step;
    protected String label;
    protected Consumer<Double> onChange;
    protected int barColor = 0xFF404040;
    protected int fillColor = 0xFF4080FF;
    protected int handleColor = 0xFFFFFFFF;
    protected int labelColor = 0xFFFFFFFF;
    protected boolean dragging;

    public Slider(int x, int y, int width, double min, double max, double value, Consumer<Double> onChange) {
        super(x, y, width, 20);
        this.min = min;
        this.max = max;
        this.value = value;
        this.onChange = onChange;
        this.step = (max - min) / 100.0;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (!visible) return;
        renderBackground(ctx);

        int barY = y + height / 2 - 2;
        int barH = 4;
        ctx.fill(x, barY, x + width, barY + barH, barColor);

        double fraction = (value - min) / (max - min);
        int fillEnd = x + (int) (width * fraction);
        ctx.fill(x, barY, fillEnd, barY + barH, fillColor);

        int handleX = fillEnd;
        int handleSize = 8;
        ctx.fill(handleX - handleSize / 2, y + height / 2 - handleSize / 2,
                handleX + handleSize / 2, y + height / 2 + handleSize / 2, handleColor);

        if (label != null) {
            ctx.drawText(getTextRenderer(), label + ": " + String.format("%.1f", value),
                    x, y - 10, labelColor, true);
        }

        renderBorder(ctx);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (!visible || !isInside(mx, my)) return false;
        if (button == 0) {
            setValueFromMouse(mx);
            dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        dragging = false;
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (dragging && button == 0) {
            setValueFromMouse(mx);
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    private void setValueFromMouse(double mx) {
        double fraction = (mx - x) / width;
        double newValue = min + fraction * (max - min);
        if (step > 0) {
            newValue = Math.round(newValue / step) * step;
        }
        newValue = Math.max(min, Math.min(max, newValue));
        if (newValue != value) {
            value = newValue;
            if (onChange != null) onChange.accept(value);
        }
    }

    public void setValue(double value) {
        this.value = Math.max(min, Math.min(max, value));
    }

    public double getValue() { return value; }
    public void setMin(double min) { this.min = min; }
    public void setMax(double max) { this.max = max; }
    public void setStep(double step) { this.step = step; }
    public void setLabel(String label) { this.label = label; }
    public void setBarColor(int color) { this.barColor = color; }
    public void setFillColor(int color) { this.fillColor = color; }
    public void setHandleColor(int color) { this.handleColor = color; }
}
