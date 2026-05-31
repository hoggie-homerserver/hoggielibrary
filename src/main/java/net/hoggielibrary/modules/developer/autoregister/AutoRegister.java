package net.hoggielibrary.modules.developer.autoregister;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Auto-registration system for automatic class discovery and registration.
 */
public final class AutoRegister {

    private final Map<Class<?>, Set<Class<?>>> registry = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<Consumer<?>>> callbacks = new ConcurrentHashMap<>();

    /**
     * Registers a class.
     *
     * @param superType the super type
     * @param implClass the implementation class
     * @param <T> the type
     */
    @SuppressWarnings("unchecked")
    public <T> void register(Class<T> superType, Class<? extends T> implClass) {
        registry.computeIfAbsent(superType, k -> ConcurrentHashMap.newKeySet()).add(implClass);
        List<Consumer<?>> cbs = callbacks.get(superType);
        if (cbs != null) {
            cbs.forEach(cb -> {
                try {
                    ((Consumer<T>) cb).accept(implClass.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    HoggieLogger.error("Auto-register callback failed for {}", implClass.getSimpleName(), e);
                }
            });
        }
        HoggieLogger.debug("Auto-registered: {} implements {}", implClass.getSimpleName(), superType.getSimpleName());
    }

    /**
     * Gets all registered implementations of a type.
     *
     * @param superType the super type
     * @param <T> the type
     * @return set of implementation classes
     */
    @SuppressWarnings("unchecked")
    public <T> Set<Class<? extends T>> getImplementations(Class<T> superType) {
        Set<Class<?>> classes = registry.get(superType);
        if (classes == null) return Set.of();
        Set<Class<? extends T>> result = new HashSet<>();
        for (Class<?> cls : classes) {
            result.add((Class<? extends T>) cls);
        }
        return result;
    }

    /**
     * Registers a callback for when implementations are registered.
     *
     * @param superType the super type to watch
     * @param callback the callback
     * @param <T> the type
     */
    public <T> void onRegister(Class<T> superType, Consumer<T> callback) {
        callbacks.computeIfAbsent(superType, k -> new ArrayList<>()).add(callback);
    }

    /**
     * Returns the count of registered implementations.
     *
     * @return the count
     */
    public int getRegistrationCount() {
        return registry.values().stream().mapToInt(Set::size).sum();
    }
}
