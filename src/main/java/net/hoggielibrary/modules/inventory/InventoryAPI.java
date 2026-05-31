package net.hoggielibrary.modules.inventory;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.Set;

/**
 * Inventory management API for item manipulation and searching.
 *
 * <p>Provides utilities for finding items, switching hotbar slots,
 * and managing player inventory.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.inventory.findSword();
 * Hoggie.inventory.selectSlot(1);
 * Hoggie.inventory.countItem(Items.DIAMOND);
 * }</pre>
 */
public final class InventoryAPI {

    private static final Set<Item> SWORDS = Set.of(
            Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD,
            Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD
    );

    /**
     * Finds the first sword in the player's hotbar or inventory.
     *
     * @return the slot index of the sword, or -1 if not found
     */
    public int findSword() {
        PlayerInventory inventory = getInventory();
        if (inventory == null) return -1;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (SWORDS.contains(stack.getItem())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first item of the given type in the inventory.
     *
     * @param item the item to find
     * @return the slot index, or -1 if not found
     */
    public int findItem(Item item) {
        PlayerInventory inventory = getInventory();
        if (inventory == null) return -1;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns whether the inventory contains an item.
     *
     * @param item the item to check
     * @return true if the item is found
     */
    public boolean hasItem(Item item) {
        return findItem(item) != -1;
    }

    /**
     * Counts the total number of a specific item across the inventory.
     *
     * @param item the item to count
     * @return the total count
     */
    public int countItem(Item item) {
        PlayerInventory inventory = getInventory();
        if (inventory == null) return 0;
        int count = 0;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }

    /**
     * Selects a hotbar slot (0-8).
     *
     * @param slot the slot to select
     */
    public void selectSlot(int slot) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && slot >= 0 && slot <= 8) {
            client.player.getInventory().setSelectedSlot(slot);
        }
    }

    /**
     * Gets the currently selected slot.
     *
     * @return the selected hotbar slot (0-8)
     */
    public int getSelectedSlot() {
        PlayerInventory inventory = getInventory();
        return inventory != null ? inventory.getSelectedSlot() : -1;
    }

    /**
     * Gets the currently held ItemStack.
     *
     * @return the held ItemStack, or ItemStack.EMPTY
     */
    public ItemStack getHeldItem() {
        PlayerInventory inventory = getInventory();
        if (inventory == null) return ItemStack.EMPTY;
        return inventory.getSelectedStack();
    }

    /**
     * Gets the offhand ItemStack.
     *
     * @return the offhand ItemStack
     */
    public ItemStack getOffhandItem() {
        PlayerInventory inventory = getInventory();
        if (inventory == null) return ItemStack.EMPTY;
        return inventory.player.getOffHandStack();
    }

    /**
     * Swaps the main hand and offhand items.
     */
    public void swapHands() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() != null && client.player != null) {
            client.getNetworkHandler().sendPacket(
                    new net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket(
                            net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND,
                            net.minecraft.util.math.BlockPos.ORIGIN,
                            net.minecraft.util.math.Direction.DOWN
                    )
            );
        }
    }

    /**
     * Drops the currently held item.
     */
    public void dropHeldItem() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.dropSelectedItem(true);
        }
    }

    /**
     * Drops all items of a specific type from the inventory.
     *
     * @param item the item to drop
     */
    public void dropAll(Item item) {
        PlayerInventory inventory = getInventory();
        if (inventory == null) return;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).getItem() == item) {
                inventory.setStack(i, ItemStack.EMPTY);
            }
        }
    }

    private PlayerInventory getInventory() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null ? client.player.getInventory() : null;
    }
}
