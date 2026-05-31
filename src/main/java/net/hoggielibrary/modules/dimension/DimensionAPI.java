package net.hoggielibrary.modules.dimension;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public final class DimensionAPI {

    public static RegistryKey<World> registerDimension(Registry<DimensionType> registry, String modId, String name, DimensionType dimensionType) {
        Identifier id = Identifier.of(modId, name);
        RegistryKey<DimensionType> typeKey = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, id);
        Registry.register(registry, typeKey, dimensionType);
        return RegistryKey.of(RegistryKeys.WORLD, id);
    }

    public static RegistryKey<World> worldKey(String modId, String name) {
        return RegistryKey.of(RegistryKeys.WORLD, Identifier.of(modId, name));
    }

    public static boolean isRegistered(Registry<DimensionType> registry, Identifier id) {
        return registry.contains(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, id));
    }
}
