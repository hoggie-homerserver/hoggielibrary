package net.hoggielibrary.modules.pvp.weapon;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Set;

public final class WeaponUtilities {

    private static final Set<Item> SWORDS = Set.of(
            Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD,
            Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD
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

    public int getBestSword() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return -1;

        int bestSlot = -1;
        double bestDamage = -1;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (SWORDS.contains(stack.getItem())) {
                double damage = getSwordDamage(stack);
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestSlot = i;
                }
            }
        }
        return bestSlot;
    }

    public int getBestAxe() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return -1;

        int bestSlot = -1;
        double bestDamage = -1;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() instanceof AxeItem) {
                double damage = getBaseDamage(stack);
                int sharpness = getSharpnessLevel(stack);
                damage += sharpness * 1.25;
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestSlot = i;
                }
            }
        }
        return bestSlot;
    }

    public int getBestWeapon() {
        int swordSlot = getBestSword();
        int axeSlot = getBestAxe();
        if (swordSlot == -1) return axeSlot;
        if (axeSlot == -1) return swordSlot;

        double swordDamage = getSwordDamage(
                MinecraftClient.getInstance().player.getInventory().getStack(swordSlot)
        );
        double axeDamage = getBaseDamage(
                MinecraftClient.getInstance().player.getInventory().getStack(axeSlot)
        );
        int axeSharpness = getSharpnessLevel(
                MinecraftClient.getInstance().player.getInventory().getStack(axeSlot)
        );
        axeDamage += axeSharpness * 1.25;

        return swordDamage >= axeDamage ? swordSlot : axeSlot;
    }

    public boolean isHoldingWeapon() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return false;
        ItemStack held = client.player.getMainHandStack();
        return SWORDS.contains(held.getItem()) || held.getItem() instanceof AxeItem;
    }

    public double getHeldWeaponDamage() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return 1.0;
        ItemStack held = client.player.getMainHandStack();
        if (SWORDS.contains(held.getItem())) {
            return getSwordDamage(held);
        } else if (held.getItem() instanceof AxeItem) {
            return getBaseDamage(held) + getSharpnessLevel(held) * 1.25;
        }
        return 1.0;
    }

    private double getSwordDamage(ItemStack stack) {
        double damage = getBaseDamage(stack);
        int sharpness = getSharpnessLevel(stack);
        damage += sharpness * 1.25;
        return damage;
    }

    private double getBaseDamage(ItemStack stack) {
        return BASE_DAMAGES.getOrDefault(stack.getItem(), 1.0);
    }

    private int getSharpnessLevel(ItemStack stack) {
        return 0;
    }
}
