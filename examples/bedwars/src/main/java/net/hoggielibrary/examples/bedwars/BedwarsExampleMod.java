package net.hoggielibrary.examples.bedwars;

import net.fabricmc.api.ModInitializer;
import net.hoggielibrary.api.Hoggie;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.bedwars.generator.GeneratorAPI;
import net.hoggielibrary.modules.bedwars.shop.ShopAPI;
import net.hoggielibrary.modules.bedwars.team.BedwarsTeam;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/**
 * Example mod demonstrating the Hoggie Library Bedwars Framework.
 */
public final class BedwarsExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {
        HoggieLogger.info("Bedwars Example Mod initialized");

        // Example: Create teams
        BedwarsTeam red = Hoggie.bedwars.team().createTeam("red", "§c", new BlockPos(0, 64, 0));
        BedwarsTeam blue = Hoggie.bedwars.team().createTeam("blue", "§9", new BlockPos(100, 64, 100));
        HoggieLogger.info("Teams created: {} and {}", red.getName(), blue.getName());

        // Example: Register beds
        Hoggie.bedwars.bed().registerBed("red", new BlockPos(0, 65, 0), new BlockPos(0, 65, 1));
        Hoggie.bedwars.bed().registerBed("blue", new BlockPos(100, 65, 100), new BlockPos(100, 65, 101));
        HoggieLogger.info("Beds alive: {}", Hoggie.bedwars.bed().getAliveBedCount());

        // Example: Generators
        GeneratorAPI gen = Hoggie.bedwars.generator();
        gen.createGenerator("red_iron", new BlockPos(0, 64, 3), "iron", 20);
        gen.createGenerator("blue_iron", new BlockPos(100, 64, 103), "iron", 20);
        gen.createGenerator("diamond", new BlockPos(50, 64, 50), "diamond", 80);

        // Example: Shop items
        ShopAPI shop = Hoggie.bedwars.shop();
        shop.createCategory("blocks");
        shop.addItem("blocks", new ShopAPI.ShopItem(Items.WHITE_WOOL, 4, "iron", "Wool", 16));
        shop.addItem("blocks", new ShopAPI.ShopItem(Items.OBSIDIAN, 4, "emerald", "Obsidian", 4));

        shop.createCategory("weapons");
        shop.addItem("weapons", new ShopAPI.ShopItem(Items.STONE_SWORD, 10, "iron", "Stone Sword", 1));
        shop.addItem("weapons", new ShopAPI.ShopItem(Items.DIAMOND_SWORD, 3, "gold", "Diamond Sword", 1));

        // Example: Upgrades
        Hoggie.bedwars.upgrade().applyUpgrade("red", "sharpness", 1);
        Hoggie.bedwars.upgrade().applyUpgrade("red", "protection", 2);
        HoggieLogger.info("Red upgrades: {}", Hoggie.bedwars.upgrade().getTeamUpgrades("red"));

        // Example: Create match
        var match = Hoggie.bedwars.match().createMatch("bedwars_1");
        Hoggie.bedwars.match().startMatch("bedwars_1");

        // Simulate bed destruction
        Hoggie.bedwars.bed().destroyBed("blue");
        HoggieLogger.info("Blue bed destroyed! Alive beds: {}", Hoggie.bedwars.bed().getAliveBedCount());

        // End match
        Hoggie.bedwars.match().endMatch("bedwars_1", "red");
        HoggieLogger.info("Match ended. Winner: {}", match.getWinner());

        Hoggie.notifications.success("Bedwars Example Mod loaded");
    }
}
