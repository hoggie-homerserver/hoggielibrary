package net.hoggielibrary.modules.gui.components;

import net.minecraft.client.gui.DrawContext;

public class Label extends HoggieWidget {

    protected String text;
    protected int textColor = 0xFFFFFFFF;
    protected boolean shadow = true;

    public Label(int x, int y, String text) {
        super(x, y, 0, 0);
        this.text = text;
        this.width = getTextRenderer().getWidth(text);
        this.height = 9;
    }

    public Label(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (!visible) return;
        renderBackground(ctx);
        renderBorder(ctx);

        if (shadow) {
            ctx.drawText(getTextRenderer(), text, x, y, textColor, true);
        } else {
            ctx.drawText(getTextRenderer(), text, x, y, textColor, false);
        }
    }

    public void setText(String text) {
        this.text = text;
        if (width == 0) this.width = getTextRenderer().getWidth(text);
    }

    public String getText() { return text; }
    public void setTextColor(int color) { this.textColor = color; }
    public void setShadow(boolean shadow) { this.shadow = shadow; }
}
