package net.hoggielibrary.modules.bot.behavior;

import java.util.function.Supplier;

/**
 * Decision Tree implementation for bot decision making.
 *
 * <p>A decision tree evaluates conditions and executes corresponding actions.
 */
public final class DecisionTree {

    private DecisionNode root;

    /**
     * Sets the root decision node.
     *
     * @param node the root node
     */
    public void setRoot(DecisionNode node) {
        this.root = node;
    }

    /**
     * Evaluates the decision tree.
     */
    public void evaluate() {
        if (root != null) {
            root.evaluate();
        }
    }

    /**
     * Creates a decision node.
     *
     * @param condition the condition to evaluate
     * @param trueAction the action if condition is true
     * @param falseAction the action if condition is false
     * @return the decision node
     */
    public static DecisionNode decision(Supplier<Boolean> condition, Runnable trueAction, Runnable falseAction) {
        return new DecisionNode() {
            @Override
            public void evaluate() {
                if (condition.get()) {
                    trueAction.run();
                } else {
                    falseAction.run();
                }
            }
        };
    }

    public interface DecisionNode {
        void evaluate();
    }
}
