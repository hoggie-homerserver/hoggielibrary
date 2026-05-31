package net.hoggielibrary.modules.gui.components;

import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class Panel extends HoggieWidget {

    public enum Layout {
        NONE, VERTICAL, HORIZONTAL, GRID
    }

    protected final List<HoggieWidget> children = new ArrayList<>();
    protected Layout layout = Layout.NONE;
    protected int spacing = 4;
    protected int columns = 2;
    protected int padding = 4;
    protected boolean clipContent = true;

    public Panel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public <T extends HoggieWidget> T add(T widget) {
        children.add(widget);
        relayout();
        return widget;
    }

    public void remove(HoggieWidget widget) {
        children.remove(widget);
        relayout();
    }

    public void clear() {
        children.clear();
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        relayout();
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
        relayout();
    }

    public void setPadding(int padding) {
        this.padding = padding;
        relayout();
    }

    public void setColumns(int columns) {
        this.columns = Math.max(1, columns);
        relayout();
    }

    public void setClipContent(boolean clipContent) {
        this.clipContent = clipContent;
    }

    public void relayout() {
        if (layout == Layout.NONE) return;

        int cx = x + padding;
        int cy = y + padding;
        int availW = width - padding * 2;
        int availH = height - padding * 2;

        switch (layout) {
            case VERTICAL -> {
                for (HoggieWidget child : children) {
                    child.setPosition(cx, cy);
                    child.setSize(availW, child.getHeight());
                    cy += child.getHeight() + spacing;
                }
            }
            case HORIZONTAL -> {
                for (HoggieWidget child : children) {
                    child.setPosition(cx, cy);
                    child.setSize(child.getWidth(), availH);
                    cx += child.getWidth() + spacing;
                }
            }
            case GRID -> {
                int index = 0;
                int cellW = (availW - spacing * (columns - 1)) / columns;
                for (HoggieWidget child : children) {
                    int col = index % columns;
                    int row = index / columns;
                    child.setPosition(cx + col * (cellW + spacing), cy + row * (child.getHeight() + spacing));
                    child.setSize(cellW, child.getHeight());
                    index++;
                }
            }
        }
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (!visible) return;
        renderBackground(ctx);
        renderBorder(ctx);

        for (HoggieWidget child : children) {
            if (!child.isVisible()) continue;
            if (clipContent && !isInside(child.getX(), child.getY())) continue;
            child.render(ctx, mx, my, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (!visible || !isInside(mx, my)) return false;
        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i).mouseClicked(mx, my, button)) return true;
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i).mouseReleased(mx, my, button)) return true;
        }
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i).mouseDragged(mx, my, button, dx, dy)) return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double horizontal, double vertical) {
        for (HoggieWidget child : children) {
            if (child.mouseScrolled(mx, my, horizontal, vertical)) return true;
        }
        return super.mouseScrolled(mx, my, horizontal, vertical);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (HoggieWidget child : children) {
            if (child.keyPressed(keyCode, scanCode, modifiers)) return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (HoggieWidget child : children) {
            if (child.charTyped(chr, modifiers)) return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public void tick() {
        for (HoggieWidget child : children) {
            child.tick();
        }
    }

    @Override
    public void removed() {
        for (HoggieWidget child : children) {
            child.removed();
        }
    }

    public List<HoggieWidget> getChildren() {
        return children;
    }
}
