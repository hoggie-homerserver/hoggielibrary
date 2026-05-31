package net.hoggielibrary.modules.bedwars;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.bedwars.team.TeamAPI;
import net.hoggielibrary.modules.bedwars.bed.BedAPI;
import net.hoggielibrary.modules.bedwars.generator.GeneratorAPI;
import net.hoggielibrary.modules.bedwars.shop.ShopAPI;
import net.hoggielibrary.modules.bedwars.upgrade.UpgradeAPI;
import net.hoggielibrary.modules.bedwars.match.MatchAPI;

/**
 * Bedwars Framework API.
 *
 * <p>Provides a complete Bedwars game framework including team
 * management, bed destruction, resource generators, shop/upgrade
 * systems, and match management.
 */
public final class BedwarsAPI {

    private final TeamAPI team = new TeamAPI();
    private final BedAPI bed = new BedAPI();
    private final GeneratorAPI generator = new GeneratorAPI();
    private final ShopAPI shop = new ShopAPI();
    private final UpgradeAPI upgrade = new UpgradeAPI();
    private final MatchAPI match = new MatchAPI();

    public BedwarsAPI() {
        HoggieLogger.debug("Bedwars Framework initialized");
    }

    public TeamAPI team() { return team; }
    public BedAPI bed() { return bed; }
    public GeneratorAPI generator() { return generator; }
    public ShopAPI shop() { return shop; }
    public UpgradeAPI upgrade() { return upgrade; }
    public MatchAPI match() { return match; }
}
