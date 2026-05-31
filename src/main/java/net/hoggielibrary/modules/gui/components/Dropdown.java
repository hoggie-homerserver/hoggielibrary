package net.hoggielibrary.modules.gui.components;

import net.minecraft.client.gui.DrawContext;

import java.util.List;
import java.util.function.Consumer;

public class Dropdown extends HoggieWidget {

    protected final List<String> options;
    protected int selectedIndex;
    protected Consumer<Integer> onChange;
    protected boolean expanded;
    protected int optionHeight = 20;
    protected int maxVisible = 6;
    protected int buttonColor = 0xFF303030;
    protected int hoverColor = 0xFF505050;
    protected int textColor = 0xFFFFFFFF;

    public Dropdown(int x, int y, int width, List<String> options, int defaultIndex, Consumer<Integer> onChange) {
        super(x, y, width, 20);
        this.options = options;
        this.selectedIndex = defaultIndex;
        this.onChange = onChange;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (!visible) return;
        hovered = isInside(mx, my);

        ctx.fill(x, y, x + width, y + height, hovered ? hoverColor : buttonColor);
        renderBorder(ctx);

        String selected = selectedIndex >= 0 && selectedIndex < options.size() ? options.get(selectedIndex) : "";
        ctx.drawText(getTextRenderer(), selected, x + 4, y + (height - 9) / 2, textColor, false);
        ctx.drawText(getTextRenderer(), "▼", x + width - 12, y + (height - 9) / 2, textColor, false);

        if (expanded) {
            int listH = Math.min(options.size(), maxVisible) * optionHeight;
            int scrollOffset = 0;
            ctx.fill(x, y + height, x + width, y + height + listH, 0xFF202020);

            for (int i = 0; i < Math.min(options.size(), maxVisible); i++) {
                int oy = y + height + i * optionHeight;
                boolean ohover = mx >= x && mx < x + width && my >= oy && my < oy + optionHeight;
                if (ohover) {
                    ctx.fill(x, oy, x + width, oy + optionHeight, hoverColor);
                }
                ctx.drawText(getTextRenderer(), options.get(i), x + 4, oy + (optionHeight - 9) / 2, textColor, false);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (!visible || !isInside(mx, my)) return false;
        if (button == 0) {
            expanded = !expanded;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        if (expanded && button == 0) {
            int relY = (int) my - (y + height);
            int index = relY / optionHeight;
            if (index >= 0 && index < options.size() && index < maxVisible) {
                selectedIndex = index;
                expanded = false;
                if (onChange != null) onChange.accept(selectedIndex);
                return true;
            }
            expanded = false;
        }
        return super.mouseReleased(mx, my, button);
    }

    public int getSelectedIndex() { return selectedIndex; }
    public String getSelected() { return selectedIndex >= 0 && selectedIndex < options.size() ? options.get(selectedIndex) : null; }
    public void setSelectedIndex(int index) {
        if (index >= 0 && index < options.size()) {
            selectedIndex = index;
        }
    }
    public void setOnChange(Consumer<Integer> onChange) { this.onChange = onChange; }
}
