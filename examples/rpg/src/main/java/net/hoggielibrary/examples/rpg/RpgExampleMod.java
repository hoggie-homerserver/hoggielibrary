package net.hoggielibrary.examples.rpg;

import net.fabricmc.api.ModInitializer;
import net.hoggielibrary.api.Hoggie;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.rpg.classes.ClassAPI;
import net.hoggielibrary.modules.rpg.economy.EconomyAPI;
import net.hoggielibrary.modules.rpg.npc.NpcAPI;
import net.hoggielibrary.modules.rpg.perk.PerkAPI;
import net.hoggielibrary.modules.rpg.quest.QuestAPI;
import net.hoggielibrary.modules.rpg.skilltree.SkillTreeAPI;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

/**
 * Example mod demonstrating the Hoggie Library RPG Framework.
 */
public final class RpgExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {
        HoggieLogger.info("RPG Example Mod initialized");

        UUID player = UUID.randomUUID();

        // Example: Create NPCs
        NpcAPI npc = Hoggie.rpg.npc();
        npc.createNpc("blacksmith", "Blacksmith", new BlockPos(100, 64, 100));
        npc.createNpc("quest_giver", "Quest Giver", new BlockPos(102, 64, 100));

        // Example: Create dialogue
        Hoggie.rpg.dialogue().createDialogue("welcome", "quest_giver");
        var dialogue = Hoggie.rpg.dialogue().getDialogue("welcome");
        if (dialogue != null) {
            dialogue.addNode("Welcome, adventurer!");
            dialogue.addNode("I have a quest for you.");
            dialogue.addNode("Defeat 10 monsters and return to me.");
        }

        // Example: Create quests
        QuestAPI quest = Hoggie.rpg.quest();
        var monsterQuest = quest.createQuest("monster_hunt", "Monster Hunt",
                "Defeat 10 monsters in the forest");
        monsterQuest.addObjective("Defeat 5 slimes");
        monsterQuest.addObjective("Defeat 5 skeletons");
        monsterQuest.addReward("experience", 500);
        monsterQuest.addReward("gold", 100);
        quest.assignQuest("monster_hunt", player);

        // Example: Create skills
        SkillTreeAPI skills = Hoggie.rpg.skillTree();
        skills.createSkill("double_strike", "Double Strike",
                "Chance to strike twice", 5, 10);
        skills.createSkill("iron_skin", "Iron Skin",
                "Increase defense", 3, 15);
        skills.addPrerequisite("double_strike", "iron_skin");

        // Example: Create classes
        ClassAPI classes = Hoggie.rpg.classSystem();
        var warrior = classes.registerClass("warrior", "Warrior", "A mighty melee fighter");
        warrior.setBaseHealth(40.0);
        warrior.setBaseDamage(2.0);
        classes.setPlayerClass(player, "warrior");

        // Example: Economy
        EconomyAPI economy = Hoggie.rpg.economy();
        economy.deposit(player, "gold", 1000);
        economy.withdraw(player, "gold", 100);
        HoggieLogger.info("Gold balance: {}", economy.getBalance(player, "gold"));

        // Example: Perks
        PerkAPI perks = Hoggie.rpg.perk();
        perks.registerPerk("berserker", "Berserker",
                "Deal more damage when low on health");
        perks.addPerk("berserker", player);

        // Example: Stats
        Hoggie.rpg.stats().addExperience(player, 250);
        HoggieLogger.info("Player level: {}", Hoggie.rpg.stats().getLevel(player));

        // Example: Achievements
        Hoggie.rpg.achievement().createAchievement("first_blood", "First Blood",
                "Kill your first monster");
        Hoggie.rpg.achievement().awardAchievement("first_blood", player);

        Hoggie.notifications.success("RPG Example Mod loaded");
    }
}
