package net.hoggielibrary.modules.bot.behavior;

import java.util.*;
import java.util.function.Supplier;

/**
 * Behavior Tree implementation for bot AI decision making.
 *
 * <p>Supports sequences, selectors, conditions, and actions.
 */
public final class BehaviorTree {

    private Node root;
    private final Map<String, Object> blackboard = new HashMap<>();

    /**
     * Sets the root node of the behavior tree.
     *
     * @param node the root node
     */
    public void setRoot(Node node) {
        this.root = node;
    }

    /**
     * Ticks the behavior tree, executing the root node.
     *
     * @return the status of the root node
     */
    public Status tick() {
        if (root == null) return Status.FAILURE;
        return root.execute();
    }

    /**
     * Sets a value in the blackboard.
     *
     * @param key the key
     * @param value the value
     */
    public void setBlackboard(String key, Object value) {
        blackboard.put(key, value);
    }

    /**
     * Gets a value from the blackboard.
     *
     * @param key the key
     * @param <T> the expected type
     * @return the value, or null
     */
    @SuppressWarnings("unchecked")
    public <T> T getBlackboard(String key) {
        return (T) blackboard.get(key);
    }

    /**
     * Creates a sequence node (runs children in order, fails if any fail).
     *
     * @param children the child nodes
     * @return the sequence node
     */
    public static Node sequence(Node... children) {
        return new SequenceNode(List.of(children));
    }

    /**
     * Creates a selector node (runs children until one succeeds).
     *
     * @param children the child nodes
     * @return the selector node
     */
    public static Node selector(Node... children) {
        return new SelectorNode(List.of(children));
    }

    /**
     * Creates a condition node.
     *
     * @param condition the condition supplier
     * @return the condition node
     */
    public static Node condition(Supplier<Boolean> condition) {
        return new ConditionNode(condition);
    }

    /**
     * Creates an action node.
     *
     * @param action the action to execute
     * @return the action node
     */
    public static Node action(Runnable action) {
        return new ActionNode(action);
    }

    public enum Status {
        SUCCESS,
        FAILURE,
        RUNNING
    }

    public interface Node {
        Status execute();
    }

    private record SequenceNode(List<Node> children) implements Node {
        @Override
        public Status execute() {
            for (Node child : children) {
                Status status = child.execute();
                if (status != Status.SUCCESS) return status;
            }
            return Status.SUCCESS;
        }
    }

    private record SelectorNode(List<Node> children) implements Node {
        @Override
        public Status execute() {
            for (Node child : children) {
                Status status = child.execute();
                if (status == Status.SUCCESS) return Status.SUCCESS;
            }
            return Status.FAILURE;
        }
    }

    private record ConditionNode(Supplier<Boolean> condition) implements Node {
        @Override
        public Status execute() {
            return condition.get() ? Status.SUCCESS : Status.FAILURE;
        }
    }

    private record ActionNode(Runnable action) implements Node {
        @Override
        public Status execute() {
            action.run();
            return Status.SUCCESS;
        }
    }
}
