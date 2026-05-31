package net.hoggielibrary.core.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.hoggielibrary.core.logging.HoggieLogger;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Manages key bindings for the Hoggie Library framework.
 *
 * <p>Provides registration, event-driven callbacks, and state tracking
 * for custom key bindings.
 *
 * <p>Usage:
 * <pre>{@code
 * Identifier category = Identifier.of("hoggielibrary", "general");
 * KeyBinding kb = HoggieKeybindManager.register("key.hoggielibrary.test", GLFW.GLFW_KEY_R, category, () -> {
 *     // handle press
 * });
 * }</pre>
 * <p>The category display name is provided via the language file with key
 * {@code "key.categories.<namespace>.<path>"}.
 */
public final class HoggieKeybindManager {

    private static volatile HoggieKeybindManager instance;

    public static HoggieKeybindManager getOrCreate() {
        if (instance == null) {
            synchronized (HoggieKeybindManager.class) {
                if (instance == null) {
                    instance = new HoggieKeybindManager();
                }
            }
        }
        return instance;
    }

    static HoggieKeybindManager getInstanceOrNull() {
        return instance;
    }

    private final Map<String, KeyBinding> keyBindings = new ConcurrentHashMap<>();
    private final Map<String, Runnable> pressCallbacks = new ConcurrentHashMap<>();
    private final Map<String, Consumer<KeyBindingState>> stateCallbacks = new ConcurrentHashMap<>();
    private final Map<String, KeyBindingState> previousStates = new ConcurrentHashMap<>();

    /**
     * Creates a new keybind manager and registers the client tick handler.
     */
    public HoggieKeybindManager() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            keyBindings.forEach((id, kb) -> {
                boolean wasPressed = previousStates.getOrDefault(id, KeyBindingState.RELEASED) == KeyBindingState.PRESSED;
                boolean isPressed = kb.isPressed();
                KeyBindingState currentState = isPressed ? KeyBindingState.PRESSED : KeyBindingState.RELEASED;
                previousStates.put(id, currentState);

                if (isPressed && !wasPressed) {
                    if (pressCallbacks.containsKey(id)) {
                        pressCallbacks.get(id).run();
                    }
                }
                if (stateCallbacks.containsKey(id)) {
                    stateCallbacks.get(id).accept(currentState);
                }
            });
        });
    }

    /**
     * Registers a key binding with a press callback.
     *
     * @param translationKey the translation key for the keybind
     * @param keyCode the GLFW key code
     * @param categoryId the category identifier (use {@link #category(String, String)})
     * @param onPress the callback to run when the key is pressed
     * @return the registered KeyBinding
     */
    public KeyBinding register(String translationKey, int keyCode, Identifier categoryId, Runnable onPress) {
        KeyBinding kb = new KeyBinding(translationKey, InputUtil.Type.KEYSYM, keyCode, new KeyBinding.Category(categoryId));
        KeyBindingHelper.registerKeyBinding(kb);
        keyBindings.put(translationKey, kb);
        pressCallbacks.put(translationKey, onPress);
        previousStates.put(translationKey, KeyBindingState.RELEASED);
        return kb;
    }

    /**
     * Registers a key binding with a state callback.
     *
     * @param translationKey the translation key for the keybind
     * @param keyCode the GLFW key code
     * @param categoryId the category identifier
     * @param onStateChange the callback receiving the key state
     * @return the registered KeyBinding
     */
    public KeyBinding registerState(String translationKey, int keyCode, Identifier categoryId,
                                    Consumer<KeyBindingState> onStateChange) {
        KeyBinding kb = new KeyBinding(translationKey, InputUtil.Type.KEYSYM, keyCode, new KeyBinding.Category(categoryId));
        KeyBindingHelper.registerKeyBinding(kb);
        keyBindings.put(translationKey, kb);
        stateCallbacks.put(translationKey, onStateChange);
        previousStates.put(translationKey, KeyBindingState.RELEASED);
        return kb;
    }

    /**
     * Creates a category identifier for use with {@link #register}.
     *
     * <p>The display name is defined in the language file with key
     * {@code "key.categories.<namespace>.<path>"}.
     *
     * @param namespace the category namespace
     * @param path the category path
     * @return the category identifier
     */
    public static Identifier category(String namespace, String path) {
        return Identifier.of(namespace, path);
    }

    /**
     * Registers default keybindings for the framework.
     */
    public void registerDefaults() {
        Identifier category = Identifier.of("hoggielibrary", "general");
        register("key.hoggielibrary.debug", GLFW.GLFW_KEY_F8, category,
                () -> HoggieLogger.info("Debug key pressed"));
        register("key.hoggielibrary.menu", GLFW.GLFW_KEY_R, category,
                () -> HoggieLogger.debug("Menu key pressed"));
    }

    /**
     * Returns the KeyBinding for a given translation key.
     *
     * @param translationKey the translation key
     * @return the KeyBinding, or null if not registered
     */
    public KeyBinding get(String translationKey) {
        return keyBindings.get(translationKey);
    }

    /**
     * Checks if a key binding is currently pressed.
     *
     * @param translationKey the translation key
     * @return true if pressed
     */
    public boolean isPressed(String translationKey) {
        KeyBinding kb = keyBindings.get(translationKey);
        return kb != null && kb.isPressed();
    }
}
