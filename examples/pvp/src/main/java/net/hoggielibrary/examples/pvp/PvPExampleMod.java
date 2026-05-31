package net.hoggielibrary.examples.pvp;

import net.fabricmc.api.ModInitializer;
import net.hoggielibrary.api.Hoggie;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.pvp.combat.CombatUtils;
import net.hoggielibrary.modules.pvp.rotation.RotationManager;
import net.hoggielibrary.modules.pvp.target.TargetManager;
import net.hoggielibrary.modules.pvp.cps.CpsTracker;
import net.hoggielibrary.modules.pvp.weapon.WeaponUtilities;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Example mod demonstrating the Hoggie Library PvP Framework.
 */
public final class PvPExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {
        HoggieLogger.info("PvP Example Mod initialized");

        // Example: Using combat utilities
        CombatUtils combat = Hoggie.pvp.combat();
        HoggieLogger.info("Attack speed: {}", combat.getAttackSpeed());

        // Example: Using weapon utilities
        WeaponUtilities weapon = Hoggie.pvp.weapon();
        int bestSword = weapon.getBestSword();
        if (bestSword != -1) {
            HoggieLogger.info("Best sword in slot {}", bestSword);
        }

        // Example: Using target manager
        TargetManager target = Hoggie.pvp.target();
        target.setPriority(net.hoggielibrary.modules.pvp.target.TargetPriority.DISTANCE);

        // Example: Using rotation manager
        RotationManager rotation = Hoggie.pvp.rotation();

        // Example: Using CPS tracker
        CpsTracker cps = Hoggie.pvp.cps();
        cps.recordLeftClick();
        cps.recordLeftClick();
        HoggieLogger.info("Current CPS: {}", cps.getLeftCps());

        // Example: Reach calculations
        double reach = Hoggie.pvp.reach().getMaxReach();
        HoggieLogger.info("Max reach: {}", reach);

        // Example: Damage calculations
        double baseDmg = Hoggie.pvp.damage().getBaseAttackDamage();
        HoggieLogger.info("Base damage: {}", baseDmg);

        Hoggie.notifications.success("PvP Example Mod loaded");
    }
}
