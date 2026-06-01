package net.hoggielibrary.modules.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocation;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

public final class EntityAPI {

    // ── Entity Type ──

    public static <T extends Entity> EntityType<T> registerEntity(String modId, String name, EntityType.Builder<T> builder) {
        RegistryKey<EntityType<?>> key = RegistryKey.of(Registries.ENTITY_TYPE.getKey(), Identifier.of(modId, name));
        EntityType<T> type = builder.build(key);
        return Registry.register(Registries.ENTITY_TYPE, key, type);
    }

    public static <T extends Entity> EntityType.Builder<T> createEntityType(EntityType.EntityFactory<T> factory, SpawnGroup group) {
        return EntityType.Builder.create(factory, group);
    }

    public static <T extends Entity> EntityType<T> registerSimpleEntity(String modId, String name, EntityType.EntityFactory<T> factory, SpawnGroup group, float width, float height) {
        return registerEntity(modId, name, EntityType.Builder.create(factory, group).dimensions(width, height));
    }

    // ── Attributes ──

    public static <T extends LivingEntity> EntityType<T> registerEntity(String modId, String name, EntityType.Builder<T> builder, DefaultAttributeContainer.Builder attributes) {
        EntityType<T> type = registerEntity(modId, name, builder);
        FabricDefaultAttributeRegistry.register(type, attributes);
        return type;
    }

    public static <T extends LivingEntity> EntityType<T> registerSimpleEntity(String modId, String name, EntityType.EntityFactory<T> factory, SpawnGroup group, float width, float height, DefaultAttributeContainer.Builder attributes) {
        return registerEntity(modId, name, EntityType.Builder.create(factory, group).dimensions(width, height), attributes);
    }

    // ── Quick presets ──

    public static <T extends MobEntity> EntityType<T> registerSimpleMob(String modId, String name, EntityType.EntityFactory<T> factory, float width, float height, DefaultAttributeContainer.Builder attributes) {
        return registerSimpleEntity(modId, name, factory, SpawnGroup.CREATURE, width, height, attributes);
    }

    public static <T extends MobEntity> EntityType<T> registerSimpleHostile(String modId, String name, EntityType.EntityFactory<T> factory, float width, float height, DefaultAttributeContainer.Builder attributes) {
        return registerSimpleEntity(modId, name, factory, SpawnGroup.MONSTER, width, height, attributes);
    }

    // ── Renderer (client) ──

    @Environment(EnvType.CLIENT)
    public static <T extends Entity> void registerRenderer(EntityType<? extends T> type, EntityRendererFactory<T> factory) {
        EntityRendererRegistry.register(type, factory);
    }

    // ── Model Layer (client) ──

    @Environment(EnvType.CLIENT)
    public static void registerModelLayer(EntityModelLayer layer, TexturedModelData data) {
        EntityModelLayerRegistry.registerModelLayer(layer, () -> data);
    }

    @Environment(EnvType.CLIENT)
    public static EntityModelLayer modelLayer(String modId, String name) {
        return new EntityModelLayer(Identifier.of(modId, name), "main");
    }

    @Environment(EnvType.CLIENT)
    public static TexturedModelData cubeModelData(float width, float height, float depth) {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        root.addChild("main",
                ModelPartBuilder.create().uv(0, 0).cuboid(-width / 2, -height / 2, -depth / 2, width, height, depth),
                ModelTransform.origin(0.0F, 24.0F - height, 0.0F));
        return TexturedModelData.of(data, (int) width * 2, (int) height * 2);
    }

    // ── Spawn Egg ──

    public static Item registerSpawnEgg(String modId, String name, EntityType<? extends MobEntity> type, int primaryColor, int secondaryColor) {
        Identifier id = Identifier.of(modId, name);
        RegistryKey<Item> key = RegistryKey.of(Registries.ITEM.getKey(), id);
        Item egg = new SpawnEggItem(new Item.Settings().registryKey(key).spawnEgg(type));
        return Registry.register(Registries.ITEM, key, egg);
    }

    public static Item registerSpawnEgg(String modId, String name, EntityType<? extends MobEntity> type, int primaryColor, int secondaryColor, Item.Settings settings) {
        Identifier id = Identifier.of(modId, name);
        RegistryKey<Item> key = RegistryKey.of(Registries.ITEM.getKey(), id);
        Item egg = new SpawnEggItem(settings.registryKey(key).spawnEgg(type));
        return Registry.register(Registries.ITEM, key, egg);
    }

    // ── Entity + Attributes + Spawn Egg (all-in-one) ──

    public static <T extends MobEntity> EntityType<T> registerMobWithSpawnEgg(String modId, String name, EntityType.Builder<T> builder, DefaultAttributeContainer.Builder attributes, int primaryColor, int secondaryColor) {
        EntityType<T> type = registerEntity(modId, name, builder, attributes);
        registerSpawnEgg(modId, name + "_spawn_egg", type, primaryColor, secondaryColor);
        return type;
    }

    public static <T extends MobEntity> EntityType<T> registerSimpleMobWithSpawnEgg(String modId, String name, EntityType.EntityFactory<T> factory, float width, float height, DefaultAttributeContainer.Builder attributes, int primaryColor, int secondaryColor) {
        return registerMobWithSpawnEgg(modId, name, EntityType.Builder.create(factory, SpawnGroup.CREATURE).dimensions(width, height), attributes, primaryColor, secondaryColor);
    }

    public static <T extends MobEntity> EntityType<T> registerSimpleHostileWithSpawnEgg(String modId, String name, EntityType.EntityFactory<T> factory, float width, float height, DefaultAttributeContainer.Builder attributes, int primaryColor, int secondaryColor) {
        return registerMobWithSpawnEgg(modId, name, EntityType.Builder.create(factory, SpawnGroup.MONSTER).dimensions(width, height), attributes, primaryColor, secondaryColor);
    }

    // ── Spawn Restriction ──

    public static <T extends MobEntity> void registerSpawnRestriction(EntityType<T> type, SpawnLocation location, Heightmap.Type heightmap, SpawnRestriction.SpawnPredicate<T> predicate) {
        SpawnRestriction.register(type, location, heightmap, predicate);
    }

    public static <T extends MobEntity> void registerNaturalSpawn(EntityType<T> type, SpawnRestriction.SpawnPredicate<T> predicate) {
        SpawnRestriction.register(type, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    // ── Lookups ──

    public static boolean isRegistered(Identifier id) {
        return Registries.ENTITY_TYPE.containsId(id);
    }

    public static EntityType<?> getType(Identifier id) {
        return Registries.ENTITY_TYPE.get(id);
    }
}
