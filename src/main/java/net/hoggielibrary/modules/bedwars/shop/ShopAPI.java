package net.hoggielibrary.modules.bedwars.shop;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shop API for managing Bedwars item shops.
 */
public final class ShopAPI {

    private final Map<String, List<ShopItem>> shops = new ConcurrentHashMap<>();

    /**
     * Creates a new shop category.
     *
     * @param name the category name
     */
    public void createCategory(String name) {
        shops.put(name, new ArrayList<>());
    }

    /**
     * Adds an item to a shop category.
     *
     * @param category the category name
     * @param item the shop item
     */
    public void addItem(String category, ShopItem item) {
        shops.computeIfAbsent(category, k -> new ArrayList<>()).add(item);
    }

    /**
     * Gets all items in a category.
     *
     * @param category the category name
     * @return list of shop items
     */
    public List<ShopItem> getItems(String category) {
        return shops.getOrDefault(category, List.of());
    }

    /**
     * Returns all category names.
     *
     * @return set of category names
     */
    public java.util.Set<String> getCategories() {
        return shops.keySet();
    }

    public record ShopItem(Item item, int cost, String currencyType, String displayName, int amount) {
        public ShopItem {
            if (amount <= 0) amount = 1;
        }
    }
}
