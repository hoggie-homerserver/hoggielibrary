package net.hoggielibrary.modules.rpg;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.rpg.npc.NpcAPI;
import net.hoggielibrary.modules.rpg.dialogue.DialogueAPI;
import net.hoggielibrary.modules.rpg.quest.QuestAPI;
import net.hoggielibrary.modules.rpg.skilltree.SkillTreeAPI;
import net.hoggielibrary.modules.rpg.achievement.AchievementAPI;
import net.hoggielibrary.modules.rpg.stats.RpgStatsAPI;
import net.hoggielibrary.modules.rpg.economy.EconomyAPI;
import net.hoggielibrary.modules.rpg.classes.ClassAPI;
import net.hoggielibrary.modules.rpg.perk.PerkAPI;

/**
 * RPG Framework API for creating RPG-style game modes.
 *
 * <p>Provides NPC management, dialogue systems, quests, skill trees,
 * achievements, stats, economy, class systems, and perks.
 */
public final class RpgAPI {

    private final NpcAPI npc = new NpcAPI();
    private final DialogueAPI dialogue = new DialogueAPI();
    private final QuestAPI quest = new QuestAPI();
    private final SkillTreeAPI skillTree = new SkillTreeAPI();
    private final AchievementAPI achievement = new AchievementAPI();
    private final RpgStatsAPI stats = new RpgStatsAPI();
    private final EconomyAPI economy = new EconomyAPI();
    private final ClassAPI classSystem = new ClassAPI();
    private final PerkAPI perk = new PerkAPI();

    public RpgAPI() {
        HoggieLogger.debug("RPG Framework initialized");
    }

    public NpcAPI npc() { return npc; }
    public DialogueAPI dialogue() { return dialogue; }
    public QuestAPI quest() { return quest; }
    public SkillTreeAPI skillTree() { return skillTree; }
    public AchievementAPI achievement() { return achievement; }
    public RpgStatsAPI stats() { return stats; }
    public EconomyAPI economy() { return economy; }
    public ClassAPI classSystem() { return classSystem; }
    public PerkAPI perk() { return perk; }
}
