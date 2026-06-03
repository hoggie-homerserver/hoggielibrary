package net.hoggielibrary.modules.gui.components;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SlotGrid extends Panel {

    protected final int columns;
    protected final int rows;
    protected final List<SlotWidget> slots = new ArrayList<>();
    protected int slotSpacing = 18;
    protected int startIndex = 0;

    private static final int DEFAULT_SLOT_SPACING = 18;

    public SlotGrid(int x, int y, int columns, int rows) {
        super(x, y, columns * DEFAULT_SLOT_SPACING, rows * DEFAULT_SLOT_SPACING);
        this.columns = columns;
        this.rows = rows;
        generateSlots();
    }

    private void generateSlots() {
        slots.clear();
        children.clear();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int sx = col * slotSpacing;
                int sy = row * slotSpacing;
                int index = startIndex + row * columns + col;
                SlotWidget slot = new SlotWidget(sx, sy, index);
                slots.add(slot);
                add(slot);
            }
        }
    }

    public SlotWidget getSlot(int index) {
        if (index >= 0 && index < slots.size()) {
            return slots.get(index);
        }
        return null;
    }

    public List<SlotWidget> getSlots() {
        return slots;
    }

    public void setSlot(int index, ItemStack stack) {
        SlotWidget slot = getSlot(index);
        if (slot != null) {
            slot.setStack(stack);
        }
    }

    public void setSlots(List<ItemStack> stacks) {
        for (int i = 0; i < Math.min(stacks.size(), slots.size()); i++) {
            slots.get(i).setStack(stacks.get(i));
        }
    }

    public void setAll(ItemStack stack) {
        for (SlotWidget slot : slots) {
            slot.setStack(stack);
        }
    }

    public void clearSlots() {
        for (SlotWidget slot : slots) {
            slot.setStack(ItemStack.EMPTY);
        }
    }

    public void setOnSlotClick(Consumer<Integer> onClick) {
        for (SlotWidget slot : slots) {
            slot.setOnClick(onClick);
        }
    }

    public void setOnSlotRightClick(Consumer<Integer> onRightClick) {
        for (SlotWidget slot : slots) {
            slot.setOnRightClick(onRightClick);
        }
    }

    public void setRenderTooltips(boolean render) {
        for (SlotWidget slot : slots) {
            slot.setRenderTooltip(render);
        }
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
        generateSlots();
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getSlotCount() {
        return slots.size();
    }

    public int getSlotSpacing() {
        return slotSpacing;
    }
}
