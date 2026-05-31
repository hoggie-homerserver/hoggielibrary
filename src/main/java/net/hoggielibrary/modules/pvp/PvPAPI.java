package net.hoggielibrary.modules.pvp;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.pvp.combat.CombatUtils;
import net.hoggielibrary.modules.pvp.rotation.RotationManager;
import net.hoggielibrary.modules.pvp.target.TargetManager;
import net.hoggielibrary.modules.pvp.reach.ReachCalculations;
import net.hoggielibrary.modules.pvp.damage.DamageCalculations;
import net.hoggielibrary.modules.pvp.prediction.PredictionUtilities;
import net.hoggielibrary.modules.pvp.cps.CpsTracker;
import net.hoggielibrary.modules.pvp.cooldown.AttackCooldownUtilities;
import net.hoggielibrary.modules.pvp.weapon.WeaponUtilities;

/**
 * Central PvP framework API.
 *
 * <p>Provides a complete PvP utility suite including combat mechanics,
 * rotation management, target selection, reach/damage calculations,
 * prediction, CPS tracking, and weapon utilities.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.pvp.combat.attack(target);
 * Hoggie.pvp.rotation.lookAt(entity);
 * Hoggie.pvp.target.getClosestPlayer();
 * Hoggie.pvp.reach.getReach();
 * Hoggie.pvp.cps.getCps();
 * }</pre>
 */
public final class PvPAPI {

    private final CombatUtils combat = new CombatUtils();
    private final RotationManager rotation = new RotationManager();
    private final TargetManager target = new TargetManager();
    private final ReachCalculations reach = new ReachCalculations();
    private final DamageCalculations damage = new DamageCalculations();
    private final PredictionUtilities prediction = new PredictionUtilities();
    private final CpsTracker cps = new CpsTracker();
    private final AttackCooldownUtilities cooldown = new AttackCooldownUtilities();
    private final WeaponUtilities weapon = new WeaponUtilities();

    public PvPAPI() {
        HoggieLogger.debug("PvP Framework initialized");
    }

    /**
     * Returns combat utilities.
     *
     * @return the combat utils instance
     */
    public CombatUtils combat() {
        return combat;
    }

    /**
     * Returns rotation management utilities.
     *
     * @return the rotation manager instance
     */
    public RotationManager rotation() {
        return rotation;
    }

    /**
     * Returns target management utilities.
     *
     * @return the target manager instance
     */
    public TargetManager target() {
        return target;
    }

    /**
     * Returns reach calculation utilities.
     *
     * @return the reach calculations instance
     */
    public ReachCalculations reach() {
        return reach;
    }

    /**
     * Returns damage calculation utilities.
     *
     * @return the damage calculations instance
     */
    public DamageCalculations damage() {
        return damage;
    }

    /**
     * Returns prediction utilities.
     *
     * @return the prediction utilities instance
     */
    public PredictionUtilities prediction() {
        return prediction;
    }

    /**
     * Returns CPS tracker.
     *
     * @return the CPS tracker instance
     */
    public CpsTracker cps() {
        return cps;
    }

    /**
     * Returns attack cooldown utilities.
     *
     * @return the attack cooldown utilities instance
     */
    public AttackCooldownUtilities cooldown() {
        return cooldown;
    }

    /**
     * Returns weapon utilities.
     *
     * @return the weapon utilities instance
     */
    public WeaponUtilities weapon() {
        return weapon;
    }
}
