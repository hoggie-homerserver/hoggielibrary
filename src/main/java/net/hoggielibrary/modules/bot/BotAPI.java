package net.hoggielibrary.modules.bot;

import net.hoggielibrary.core.logging.HoggieLogger;
import net.hoggielibrary.modules.bot.ai.CombatAI;
import net.hoggielibrary.modules.bot.ai.TargetAI;
import net.hoggielibrary.modules.bot.navigation.NavigationAI;
import net.hoggielibrary.modules.bot.pathfinding.PathfindingAPI;
import net.hoggielibrary.modules.bot.behavior.BehaviorTree;
import net.hoggielibrary.modules.bot.behavior.DecisionTree;

/**
 * Bot Framework API for creating AI bots.
 *
 * <p>Provides combat AI, target selection, navigation, pathfinding,
 * behavior trees, and decision trees for bot opponents.
 */
public final class BotAPI {

    private final CombatAI combat = new CombatAI();
    private final TargetAI target = new TargetAI();
    private final NavigationAI navigation = new NavigationAI();
    private final PathfindingAPI pathfinding = new PathfindingAPI();
    private final BehaviorTree behaviorTree = new BehaviorTree();
    private final DecisionTree decisionTree = new DecisionTree();

    public BotAPI() {
        HoggieLogger.debug("Bot Framework initialized");
    }

    public CombatAI combat() { return combat; }
    public TargetAI target() { return target; }
    public NavigationAI navigation() { return navigation; }
    public PathfindingAPI pathfinding() { return pathfinding; }
    public BehaviorTree behaviorTree() { return behaviorTree; }
    public DecisionTree decisionTree() { return decisionTree; }
}
