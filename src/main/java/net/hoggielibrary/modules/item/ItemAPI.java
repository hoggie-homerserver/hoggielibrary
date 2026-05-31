package net.hoggielibrary.modules.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public final class ItemAPI {

    public static Item registerItem(String modId, String name, Item.Settings settings) {
        Identifier id = Identifier.of(modId, name);
        return Registry.register(Registries.ITEM, id, new Item(settings));
    }

    public static <T extends Item> T registerItem(String modId, String name, java.util.function.Function<Item.Settings, T> factory, Item.Settings settings) {
        Identifier id = Identifier.of(modId, name);
        return Registry.register(Registries.ITEM, id, factory.apply(settings));
    }

    public static Item registerFood(String modId, String name, int nutrition, float saturationMod, Item.Settings settings) {
        FoodComponent food = new FoodComponent.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturationMod)
                .alwaysEdible()
                .build();
        return registerItem(modId, name, settings.component(DataComponentTypes.FOOD, food));
    }

    public static void registerItemGroup(String modId, String name, Text displayName, ItemStack icon, List<ItemStack> items) {
        Identifier id = Identifier.of(modId, name);
        ItemGroup group = FabricItemGroup.builder()
                .displayName(displayName)
                .icon(() -> icon)
                .entries((context, entries) -> items.forEach(entries::add))
                .build();
        Registry.register(Registries.ITEM_GROUP, id, group);
    }

    public static Item.Settings settings() {
        return new Item.Settings();
    }

    public static boolean isRegistered(Identifier id) {
        return Registries.ITEM.containsId(id);
    }

    public static Item getItem(Identifier id) {
        return Registries.ITEM.get(id);
    }
}
