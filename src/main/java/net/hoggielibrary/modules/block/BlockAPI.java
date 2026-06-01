package net.hoggielibrary.modules.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class BlockAPI {

    public static Block registerBlock(String modId, String name, AbstractBlock.Settings settings) {
        Identifier id = Identifier.of(modId, name);
        RegistryKey<Block> key = RegistryKey.of(Registries.BLOCK.getKey(), id);
        return Registry.register(Registries.BLOCK, key, new Block(settings.registryKey(key)));
    }

    public static Block registerBlock(String modId, String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Identifier id = Identifier.of(modId, name);
        RegistryKey<Block> key = RegistryKey.of(Registries.BLOCK.getKey(), id);
        return Registry.register(Registries.BLOCK, key, factory.apply(settings.registryKey(key)));
    }

    public static Block registerBlockWithItem(String modId, String name, AbstractBlock.Settings blockSettings, Item.Settings itemSettings) {
        Identifier id = Identifier.of(modId, name);
        RegistryKey<Block> blockKey = RegistryKey.of(Registries.BLOCK.getKey(), id);
        RegistryKey<Item> itemKey = RegistryKey.of(Registries.ITEM.getKey(), id);
        Block block = Registry.register(Registries.BLOCK, blockKey, new Block(blockSettings.registryKey(blockKey)));
        Registry.register(Registries.ITEM, itemKey, new BlockItem(block, itemSettings.registryKey(itemKey)));
        return block;
    }

    public static Block registerBlockWithItem(String modId, String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings blockSettings, Item.Settings itemSettings) {
        Identifier id = Identifier.of(modId, name);
        RegistryKey<Block> blockKey = RegistryKey.of(Registries.BLOCK.getKey(), id);
        RegistryKey<Item> itemKey = RegistryKey.of(Registries.ITEM.getKey(), id);
        Block block = Registry.register(Registries.BLOCK, blockKey, factory.apply(blockSettings.registryKey(blockKey)));
        Registry.register(Registries.ITEM, itemKey, new BlockItem(block, itemSettings.registryKey(itemKey)));
        return block;
    }

    @SafeVarargs
    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String modId, String name, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        Identifier id = Identifier.of(modId, name);
        BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory, blocks).build();
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, type);
    }

    public static AbstractBlock.Settings blockSettings() {
        return AbstractBlock.Settings.create();
    }

    public static boolean isRegistered(Identifier id) {
        return Registries.BLOCK.containsId(id);
    }

    public static Block getBlock(Identifier id) {
        return Registries.BLOCK.get(id);
    }
}
