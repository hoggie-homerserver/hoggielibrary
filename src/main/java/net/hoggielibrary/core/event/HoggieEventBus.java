package net.hoggielibrary.core.event;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * High-performance event bus for inter-component communication.
 *
 * <p>Supports both annotation-based and lambda-based event handling,
 * event prioritization, cancellation, and thread-safe dispatch.
 *
 * <p>Usage:
 * <pre>{@code
 * Hoggie.events.subscribe(MyEvent.class, event -> { ... });
 * Hoggie.events.post(new MyEvent());
 * }</pre>
 */
public final class HoggieEventBus {

    private final Map<Class<?>, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();
    private final Map<Object, List<Method>> annotatedHandlers = new ConcurrentHashMap<>();

    /**
     * Registers an event handler via lambda.
     *
     * @param eventClass the event class to subscribe to
     * @param handler the handler function
     * @param <T> the event type
     * @return this event bus for chaining
     */
    public <T> HoggieEventBus subscribe(Class<T> eventClass, Consumer<T> handler) {
        return subscribe(eventClass, handler, EventPriority.NORMAL);
    }

    /**
     * Registers an event handler via lambda with a specified priority.
     *
     * @param eventClass the event class to subscribe to
     * @param handler the handler function
     * @param priority the handler priority
     * @param <T> the event type
     * @return this event bus for chaining
     */
    @SuppressWarnings("unchecked")
    public <T> HoggieEventBus subscribe(Class<T> eventClass, Consumer<T> handler, EventPriority priority) {
        handlers.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>())
                .add(new EventHandler<>((Consumer<Object>) handler, priority));
        handlers.get(eventClass).sort(Comparator.comparingInt(h -> h.priority.ordinal()));
        return this;
    }

    /**
     * Registers an object whose methods annotated with {@link Subscribe} will
     * be automatically registered as event handlers.
     *
     * @param listener the listener object
     */
    public void register(Object listener) {
        List<Method> methods = new ArrayList<>();
        for (Method method : listener.getClass().getMethods()) {
            if (method.isAnnotationPresent(Subscribe.class) && method.getParameterCount() == 1) {
                method.setAccessible(true);
                methods.add(method);
                Class<?> eventType = method.getParameterTypes()[0];
                Subscribe annotation = method.getAnnotation(Subscribe.class);
                EventPriority priority = annotation.priority();
                handlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                        .add(new EventHandler<>(event -> {
                            try {
                                method.invoke(listener, event);
                            } catch (Exception e) {
                                HoggieLogger.error("Error invoking event handler {}#{}",
                                        listener.getClass().getSimpleName(), method.getName(), e);
                            }
                        }, priority));
                handlers.get(eventType).sort(Comparator.comparingInt(h -> h.priority.ordinal()));
            }
        }
        annotatedHandlers.put(listener, methods);
    }

    /**
     * Unregisters all handlers for a given listener object.
     *
     * @param listener the listener to unregister
     */
    public void unregister(Object listener) {
        List<Method> methods = annotatedHandlers.remove(listener);
        if (methods != null) {
            for (Method method : methods) {
                Class<?> eventType = method.getParameterTypes()[0];
                List<EventHandler<?>> eventHandlers = handlers.get(eventType);
                if (eventHandlers != null) {
                    eventHandlers.clear();
                }
            }
        }
    }

    /**
     * Posts an event to all registered handlers.
     *
     * @param event the event to post
     * @param <T> the event type
     */
    @SuppressWarnings("unchecked")
    public <T> void post(T event) {
        Class<?> eventClass = event.getClass();
        List<EventHandler<?>> eventHandlers = handlers.get(eventClass);
        if (eventHandlers != null) {
            for (EventHandler<?> handler : eventHandlers) {
                try {
                    ((EventHandler<T>) handler).handler.accept(event);
                } catch (Exception e) {
                    HoggieLogger.error("Error in event handler for {}", eventClass.getSimpleName(), e);
                }
            }
        }
        // Also dispatch to superclass/interface handlers
        for (Map.Entry<Class<?>, List<EventHandler<?>>> entry : handlers.entrySet()) {
            if (entry.getKey().isAssignableFrom(eventClass) && entry.getKey() != eventClass) {
                for (EventHandler<?> handler : entry.getValue()) {
                    try {
                        ((EventHandler<T>) handler).handler.accept(event);
                    } catch (Exception e) {
                        HoggieLogger.error("Error in event handler for {}", eventClass.getSimpleName(), e);
                    }
                }
            }
        }
    }

    /**
     * Posts an event and returns whether it was cancelled.
     *
     * @param event the cancellable event to post
     * @return true if the event was cancelled
     */
    public boolean postCancellable(CancellableEvent event) {
        post(event);
        return event.isCancelled();
    }

    /**
     * Removes all handlers from the event bus.
     */
    public void clear() {
        handlers.clear();
        annotatedHandlers.clear();
    }

    private record EventHandler<T>(Consumer<T> handler, EventPriority priority) {
    }
}
