package net.hoggielibrary.core.registry;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A lightweight service registry for dependency injection.
 *
 * <p>Services are registered by interface class and can be retrieved
 * anywhere in the framework. This enables loose coupling between
 * components.
 *
 * <p>Usage:
 * <pre>{@code
 * ServiceRegistry.register(MyInterface.class, myImplementation);
 * MyInterface service = ServiceRegistry.get(MyInterface.class);
 * }</pre>
 */
public final class ServiceRegistry {

    private static final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    private ServiceRegistry() {
    }

    /**
     * Registers a service implementation.
     *
     * @param serviceClass the service interface class
     * @param implementation the service implementation
     * @param <T> the service type
     */
    @SuppressWarnings("unchecked")
    public static <T> void register(Class<T> serviceClass, T implementation) {
        if (services.containsKey(serviceClass)) {
            HoggieLogger.warn("Overriding existing service registration for {}", serviceClass.getSimpleName());
        }
        services.put(serviceClass, implementation);
        HoggieLogger.debug("Registered service: {}", serviceClass.getSimpleName());
    }

    /**
     * Retrieves a registered service.
     *
     * @param serviceClass the service interface class
     * @param <T> the service type
     * @return the service implementation
     * @throws IllegalStateException if the service is not registered
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> serviceClass) {
        T service = (T) services.get(serviceClass);
        if (service == null) {
            throw new IllegalStateException("No service registered for " + serviceClass.getName());
        }
        return service;
    }

    /**
     * Retrieves a registered service, or returns a default value if not found.
     *
     * @param serviceClass the service interface class
     * @param defaultValue the default value if not registered
     * @param <T> the service type
     * @return the service or default value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getOrDefault(Class<T> serviceClass, T defaultValue) {
        T service = (T) services.get(serviceClass);
        return service != null ? service : defaultValue;
    }

    /**
     * Returns whether a service is registered.
     *
     * @param serviceClass the service interface class
     * @return true if registered
     */
    public static boolean isRegistered(Class<?> serviceClass) {
        return services.containsKey(serviceClass);
    }

    /**
     * Removes a service registration.
     *
     * @param serviceClass the service class to remove
     */
    public static void unregister(Class<?> serviceClass) {
        services.remove(serviceClass);
        HoggieLogger.debug("Unregistered service: {}", serviceClass.getSimpleName());
    }

    /**
     * Clears all service registrations.
     */
    public static void clear() {
        services.clear();
    }

    /**
     * Returns the number of registered services.
     *
     * @return the service count
     */
    public static int getServiceCount() {
        return services.size();
    }
}
