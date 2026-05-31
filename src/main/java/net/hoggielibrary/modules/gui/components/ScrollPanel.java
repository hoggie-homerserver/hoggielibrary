package net.hoggielibrary.modules.gui.components;

import net.minecraft.client.gui.DrawContext;

public class ScrollPanel extends Panel {

    protected int scrollY;
    protected int contentHeight;
    protected int scrollbarColor = 0x80404040;
    protected int scrollbarHoverColor = 0x80606060;
    protected boolean scrollbarHovered;
    protected boolean scrollbarDragging;
    protected int scrollbarWidth = 6;

    public ScrollPanel(int x, int y, int width, int height) {
        super(x, y, width, height);
        clipContent = true;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (!visible) return;
        renderBackground(ctx);
        renderBorder(ctx);

        int maxScroll = Math.max(0, contentHeight - height);
        scrollY = Math.max(0, Math.min(scrollY, maxScroll));

        ctx.enableScissor(x, y, x + width - (contentHeight > height ? scrollbarWidth : 0), y + height);

        for (HoggieWidget child : children) {
            if (!child.isVisible()) continue;
            int origY = child.getY();
            child.setPosition(child.getX(), origY - scrollY);
            child.render(ctx, mx, my, delta);
            child.setPosition(child.getX(), origY);
        }

        ctx.disableScissor();

        if (contentHeight > height) {
            int sbX = x + width - scrollbarWidth;
            int sbHeight = height * height / contentHeight;
            int sbY = y + scrollY * (height - sbHeight) / (contentHeight - height);
            scrollbarHovered = mx >= sbX && mx < sbX + scrollbarWidth && my >= sbY && my < sbY + sbHeight;
            ctx.fill(sbX, sbY, sbX + scrollbarWidth, sbY + sbHeight,
                    scrollbarHovered ? scrollbarHoverColor : scrollbarColor);
        }
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double horizontal, double vertical) {
        if (!visible || !isInside(mx, my)) return false;
        scrollY -= (int) (vertical * 20);
        int maxScroll = Math.max(0, contentHeight - height);
        scrollY = Math.max(0, Math.min(scrollY, maxScroll));
        return true;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (!visible || !isInside(mx, my)) return false;
        if (scrollbarHovered && button == 0) {
            scrollbarDragging = true;
            return true;
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        scrollbarDragging = false;
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (scrollbarDragging) {
            int maxScroll = Math.max(0, contentHeight - height);
            float ratio = (float) (height) / contentHeight;
            scrollY -= (int) (dy / ratio);
            scrollY = Math.max(0, Math.min(scrollY, maxScroll));
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public <T extends HoggieWidget> T add(T widget) {
        T result = super.add(widget);
        recalcContentHeight();
        return result;
    }

    public void recalcContentHeight() {
        contentHeight = 0;
        for (HoggieWidget child : children) {
            contentHeight = Math.max(contentHeight, child.getY() + child.getHeight() - y);
        }
    }
}
