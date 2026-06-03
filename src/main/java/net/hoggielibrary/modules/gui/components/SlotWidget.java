package net.hoggielibrary.modules.gui.components;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class SlotWidget extends HoggieWidget {

    public static final int SLOT_SIZE = 18;

    protected int slotIndex;
    protected ItemStack stack = ItemStack.EMPTY;
    protected boolean renderItem = true;
    protected Consumer<Integer> onClick;
    protected Consumer<Integer> onRightClick;
    protected int slotBgColor = 0xFF8B8B8B;
    protected int slotBorderColor = 0xFF373737;
    protected int hoverBorderColor = 0xFFFFFFA0;
    protected boolean renderTooltip = true;

    public SlotWidget(int x, int y) {
        super(x, y, SLOT_SIZE, SLOT_SIZE);
    }

    public SlotWidget(int x, int y, int slotIndex) {
        super(x, y, SLOT_SIZE, SLOT_SIZE);
        this.slotIndex = slotIndex;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (!visible) return;
        hovered = isInside(mx, my);

        ctx.fill(x, y, x + width, y + height, slotBgColor);
        ctx.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF6B6B6B);
        ctx.fill(x + 1, y + 1, x + width - 1, y + 2, slotBorderColor);
        ctx.fill(x + 1, y + 1, x + 2, y + height - 1, slotBorderColor);

        if (hovered) {
            ctx.fill(x, y, x + width, y + height, hoverBorderColor);
        }

        if (renderItem && !stack.isEmpty()) {
            ctx.drawItem(stack, x + 1, y + 1);
            if (hovered && renderTooltip) {
                ctx.drawItemTooltip(getTextRenderer(), stack, mx, my);
            }
        }
    }

    @Override
    protected boolean onClick(double mx, double my, int button) {
        if (button == 0 && onClick != null) {
            onClick.accept(slotIndex);
            return true;
        }
        if (button == 1 && onRightClick != null) {
            onRightClick.accept(slotIndex);
            return true;
        }
        return false;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack != null ? stack : ItemStack.EMPTY;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    public void setRenderItem(boolean renderItem) {
        this.renderItem = renderItem;
    }

    public void setOnClick(Consumer<Integer> onClick) {
        this.onClick = onClick;
    }

    public void setOnRightClick(Consumer<Integer> onRightClick) {
        this.onRightClick = onRightClick;
    }

    public void setSlotBgColor(int color) {
        this.slotBgColor = color;
    }

    public void setSlotBorderColor(int color) {
        this.slotBorderColor = color;
    }

    public void setHoverBorderColor(int color) {
        this.hoverBorderColor = color;
    }

    public void setRenderTooltip(boolean renderTooltip) {
        this.renderTooltip = renderTooltip;
    }
}
