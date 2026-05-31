package net.hoggielibrary.modules.pvp.damage;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Set;

public final class DamageCalculations {

    private static final Set<Item> WEAPONS = Set.of(
            Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD,
            Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD,
            Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE,
            Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE
    );

    private static final Map<Item, Double> BASE_DAMAGES = Map.ofEntries(
            Map.entry(Items.WOODEN_SWORD, 4.0),
            Map.entry(Items.STONE_SWORD, 5.0),
            Map.entry(Items.IRON_SWORD, 6.0),
            Map.entry(Items.GOLDEN_SWORD, 4.0),
            Map.entry(Items.DIAMOND_SWORD, 7.0),
            Map.entry(Items.NETHERITE_SWORD, 8.0),
            Map.entry(Items.WOODEN_AXE, 7.0),
            Map.entry(Items.STONE_AXE, 9.0),
            Map.entry(Items.IRON_AXE, 9.0),
            Map.entry(Items.GOLDEN_AXE, 7.0),
            Map.entry(Items.DIAMOND_AXE, 9.0),
            Map.entry(Items.NETHERITE_AXE, 10.0)
    );

    public double getBaseAttackDamage() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return 1.0;

        ItemStack heldItem = player.getMainHandStack();
        if (WEAPONS.contains(heldItem.getItem())) {
            return getWeaponDamage(heldItem) + 1.0;
        }
        return 1.0;
    }

    public double getTotalAttackDamage(LivingEntity target) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return 1.0;

        double base = getBaseAttackDamage();

        ItemStack heldItem = player.getMainHandStack();
        int sharpness = getSharpnessLevel(heldItem);
        base += sharpness * 1.25;

        if (player.hasStatusEffect(StatusEffects.STRENGTH)) {
            int amplifier = player.getStatusEffect(StatusEffects.STRENGTH).getAmplifier();
            base += 3 * (amplifier + 1);
        }

        if (target != null && target.hasStatusEffect(StatusEffects.RESISTANCE)) {
            int amplifier = target.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier();
            base *= (1.0 - (amplifier + 1) * 0.2);
        }

        return base;
    }

    private int getSharpnessLevel(ItemStack stack) {
        return 0;
    }

    private double getWeaponDamage(ItemStack stack) {
        return BASE_DAMAGES.getOrDefault(stack.getItem(), 1.0);
    }

    public double getArmorReduction(LivingEntity target) {
        if (target == null) return 0.0;
        int armorValue = target.getArmor();
        return armorValue / 25.0;
    }

    public int getEstimatedHitsToKill(LivingEntity target) {
        if (target == null) return Integer.MAX_VALUE;

        double damagePerHit = getTotalAttackDamage(target);
        double reduction = getArmorReduction(target);
        double actualDamage = damagePerHit * (1.0 - reduction);

        if (actualDamage <= 0) return Integer.MAX_VALUE;
        return (int) Math.ceil(target.getHealth() / actualDamage);
    }

    public double getDistanceFalloff(double distance) {
        if (distance <= 1.0) return 1.0;
        if (distance >= 6.0) return 0.0;
        return 1.0 - (distance - 1.0) / 5.0;
    }
}
