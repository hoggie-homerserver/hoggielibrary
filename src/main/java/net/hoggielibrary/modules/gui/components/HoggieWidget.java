package net.hoggielibrary.modules.gui.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public abstract class HoggieWidget {

    protected int x, y, width, height;
    protected boolean visible = true;
    protected boolean hovered;
    protected boolean focused;
    protected boolean draggable;
    protected boolean dragging;
    protected double dragOffsetX, dragOffsetY;
    protected int bgColor = 0x00000000;
    protected int borderColor = 0x00000000;
    protected int borderWidth;

    protected HoggieWidget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void render(DrawContext ctx, int mx, int my, float delta);

    public boolean mouseClicked(double mx, double my, int button) {
        if (!visible || !isInside(mx, my)) return false;
        if (draggable && button == 0) {
            dragging = true;
            dragOffsetX = mx - x;
            dragOffsetY = my - y;
        }
        focused = true;
        return onClick(mx, my, button);
    }

    public boolean mouseReleased(double mx, double my, int button) {
        if (dragging && button == 0) {
            dragging = false;
            return true;
        }
        return false;
    }

    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (dragging && draggable) {
            x = (int) (mx - dragOffsetX);
            y = (int) (my - dragOffsetY);
            return true;
        }
        return false;
    }

    public boolean mouseScrolled(double mx, double my, double horizontal, double vertical) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        return false;
    }

    public void tick() {}

    public void removed() {}

    protected boolean onClick(double mx, double my, int button) {
        return true;
    }

    protected boolean isInside(double mx, double my) {
        return mx >= x && mx < x + width && my >= y && my < y + height;
    }

    protected void renderBorder(DrawContext ctx) {
        if (borderWidth > 0 && (borderColor >>> 24) != 0) {
            ctx.fill(x, y, x + width, y + borderWidth, borderColor);
            ctx.fill(x, y + height - borderWidth, x + width, y + height, borderColor);
            ctx.fill(x, y, x + borderWidth, y + height, borderColor);
            ctx.fill(x + width - borderWidth, y, x + width, y + height, borderColor);
        }
    }

    protected void renderBackground(DrawContext ctx) {
        if ((bgColor >>> 24) != 0) {
            ctx.fill(x, y, x + width, y + height, bgColor);
        }
    }

    protected TextRenderer getTextRenderer() {
        return MinecraftClient.getInstance().textRenderer;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean isFocused() { return focused; }
    public void setFocused(boolean focused) { this.focused = focused; }
    public boolean isDraggable() { return draggable; }
    public void setDraggable(boolean draggable) { this.draggable = draggable; }
    public void setBackgroundColor(int color) { this.bgColor = color; }
    public void setBorderColor(int color) { this.borderColor = color; }
    public void setBorderWidth(int width) { this.borderWidth = width; }
}
