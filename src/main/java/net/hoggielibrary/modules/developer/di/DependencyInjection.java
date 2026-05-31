package net.hoggielibrary.modules.developer.di;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple dependency injection container for managing service dependencies.
 */
public final class DependencyInjection {

    private final Map<Class<?>, Object> singletons = new ConcurrentHashMap<>();
    private final Map<Class<?>, Class<?>> bindings = new ConcurrentHashMap<>();

    /**
     * Binds an interface to an implementation class.
     *
     * @param interfaceClass the interface
     * @param implClass the implementation
     * @param <T> the type
     */
    public <T> void bind(Class<T> interfaceClass, Class<? extends T> implClass) {
        bindings.put(interfaceClass, implClass);
        HoggieLogger.debug("DI binding: {} -> {}", interfaceClass.getSimpleName(), implClass.getSimpleName());
    }

    /**
     * Registers a singleton instance.
     *
     * @param type the type
     * @param instance the instance
     * @param <T> the type
     */
    public <T> void registerSingleton(Class<T> type, T instance) {
        singletons.put(type, instance);
    }

    /**
     * Gets an instance of a type.
     *
     * @param type the type to get
     * @param <T> the type
     * @return the instance
     * @throws RuntimeException if resolution fails
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        // Check singletons first
        T singleton = (T) singletons.get(type);
        if (singleton != null) return singleton;

        // Check bindings
        Class<?> implClass = bindings.get(type);
        if (implClass != null) {
            try {
                T instance = (T) implClass.getDeclaredConstructor().newInstance();
                singletons.put(type, instance);
                return instance;
            } catch (Exception e) {
                throw new RuntimeException("Failed to create instance of " + implClass.getName(), e);
            }
        }

        // Try direct instantiation
        try {
            T instance = type.getDeclaredConstructor().newInstance();
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("No binding found for " + type.getName(), e);
        }
    }

    /**
     * Checks if a type has been registered.
     *
     * @param type the type
     * @return true if registered
     */
    public boolean hasBinding(Class<?> type) {
        return singletons.containsKey(type) || bindings.containsKey(type);
    }

    /**
     * Clears all bindings and singletons.
     */
    public void clear() {
        singletons.clear();
        bindings.clear();
    }
}
