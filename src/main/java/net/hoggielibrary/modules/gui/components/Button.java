package net.hoggielibrary.modules.gui.components;

import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class Button extends HoggieWidget {

    protected String text;
    protected int textColor = 0xFFFFFFFF;
    protected int hoverBgColor = 0x80404040;
    protected int normalBgColor = 0x80202020;
    protected int pressBgColor = 0x80606060;
    protected boolean pressed;
    protected Consumer<Button> onClick;

    public Button(int x, int y, int width, int height, String text, Consumer<Button> onClick) {
        super(x, y, width, height);
        this.text = text;
        this.onClick = onClick;
    }

    public Button(int x, int y, String text, Consumer<Button> onClick) {
        this(x, y, 100, 20, text, onClick);
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (!visible) return;
        hovered = isInside(mx, my);

        int color = normalBgColor;
        if (pressed && hovered) color = pressBgColor;
        else if (hovered) color = hoverBgColor;

        ctx.fill(x, y, x + width, y + height, color);
        renderBorder(ctx);

        int tx = x + (width - getTextRenderer().getWidth(text)) / 2;
        int ty = y + (height - 9) / 2;
        ctx.drawText(getTextRenderer(), text, tx, ty, textColor, false);
    }

    @Override
    protected boolean onClick(double mx, double my, int button) {
        if (button == 0 && onClick != null) {
            pressed = true;
            onClick.accept(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        pressed = false;
        return super.mouseReleased(mx, my, button);
    }

    public void setText(String text) { this.text = text; }
    public String getText() { return text; }
    public void setTextColor(int color) { this.textColor = color; }
    public void setHoverBgColor(int color) { this.hoverBgColor = color; }
    public void setNormalBgColor(int color) { this.normalBgColor = color; }
    public void setPressBgColor(int color) { this.pressBgColor = color; }
    public void setOnClick(Consumer<Button> onClick) { this.onClick = onClick; }
}
