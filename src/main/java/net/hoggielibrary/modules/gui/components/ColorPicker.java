package net.hoggielibrary.modules.gui.components;

import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class ColorPicker extends HoggieWidget {

    protected int color = 0xFFFFFFFF;
    protected Consumer<Integer> onChange;
    protected int swatchSize = 14;
    protected int swatchesPerRow = 8;

    private static final int[] PRESET_COLORS = {
            0xFFFFFFFF, 0xFFAAAAAA, 0xFF555555, 0xFF000000,
            0xFFFF0000, 0xFFFF5500, 0xFFFFFF00, 0xFF00FF00,
            0xFF00FFFF, 0xFF0000FF, 0xFF5500FF, 0xFFFF00FF,
            0xFF880000, 0xFF884400, 0xFF888800, 0xFF008800,
            0xFF008888, 0xFF000088, 0xFF440088, 0xFF880088,
            0xFFFF8888, 0xFFFFAA44, 0xFFFFFF88, 0xFF88FF88,
            0xFF88FFFF, 0xFF8888FF, 0xFFAA44FF, 0xFFFF88FF,
    };

    public ColorPicker(int x, int y, Consumer<Integer> onChange) {
        super(x, y, 0, 0);
        this.onChange = onChange;
        recalcSize();
    }

    private void recalcSize() {
        int cols = Math.min(swatchesPerRow, PRESET_COLORS.length);
        int rows = (PRESET_COLORS.length + cols - 1) / cols;
        width = cols * (swatchSize + 2) + 2;
        height = rows * (swatchSize + 2) + 2;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (!visible) return;
        renderBackground(ctx);

        int cols = Math.min(swatchesPerRow, PRESET_COLORS.length);

        for (int i = 0; i < PRESET_COLORS.length; i++) {
            int col = i % cols;
            int row = i / cols;
            int sx = x + 2 + col * (swatchSize + 2);
            int sy = y + 2 + row * (swatchSize + 2);

            int swatchColor = PRESET_COLORS[i];
            ctx.fill(sx, sy, sx + swatchSize, sy + swatchSize, swatchColor);

            if (swatchColor == color) {
                ctx.fill(sx - 1, sy - 1, sx + swatchSize + 1, sy, 0xFFFFFFFF);
                ctx.fill(sx - 1, sy + swatchSize, sx + swatchSize + 1, sy + swatchSize + 1, 0xFFFFFFFF);
                ctx.fill(sx - 1, sy - 1, sx, sy + swatchSize + 1, 0xFFFFFFFF);
                ctx.fill(sx + swatchSize, sy - 1, sx + swatchSize + 1, sy + swatchSize + 1, 0xFFFFFFFF);
            }
        }

        renderBorder(ctx);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (!visible || !isInside(mx, my) || button != 0) return false;

        int cols = Math.min(swatchesPerRow, PRESET_COLORS.length);
        for (int i = 0; i < PRESET_COLORS.length; i++) {
            int col = i % cols;
            int row = i / cols;
            int sx = x + 2 + col * (swatchSize + 2);
            int sy = y + 2 + row * (swatchSize + 2);

            if (mx >= sx && mx < sx + swatchSize && my >= sy && my < sy + swatchSize) {
                color = PRESET_COLORS[i];
                if (onChange != null) onChange.accept(color);
                return true;
            }
        }
        return false;
    }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }
    public void setOnChange(Consumer<Integer> onChange) { this.onChange = onChange; }
    public void setSwatchSize(int size) {
        this.swatchSize = size;
        recalcSize();
    }
    public void setSwatchesPerRow(int count) {
        this.swatchesPerRow = count;
        recalcSize();
    }
}
