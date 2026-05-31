package net.hoggielibrary.modules.gui;

import net.hoggielibrary.modules.gui.components.HoggieWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class HoggieScreen extends Screen {

    protected final List<HoggieWidget> children = new ArrayList<>();
    protected int bgColor = 0xC0101010;

    protected HoggieScreen(Text title) {
        super(title);
    }

    protected HoggieScreen() {
        super(Text.literal(""));
    }

    public <T extends HoggieWidget> T add(T widget) {
        children.add(widget);
        return widget;
    }

    public void remove(HoggieWidget widget) {
        children.remove(widget);
    }

    public void clear() {
        children.clear();
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        ctx.fill(0, 0, width, height, bgColor);
        for (HoggieWidget child : children) {
            if (child.isVisible()) {
                child.render(ctx, mx, my, delta);
            }
        }
        super.render(ctx, mx, my, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean boolParam) {
        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i).mouseClicked(click.x(), click.y(), click.button())) return true;
        }
        return super.mouseClicked(click, boolParam);
    }

    @Override
    public boolean mouseReleased(Click click) {
        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i).mouseReleased(click.x(), click.y(), click.button())) return true;
        }
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(Click click, double dx, double dy) {
        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i).mouseDragged(click.x(), click.y(), click.button(), dx, dy)) return true;
        }
        return super.mouseDragged(click, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double horizontal, double vertical) {
        for (HoggieWidget child : children) {
            if (child.mouseScrolled(mx, my, horizontal, vertical)) return true;
        }
        return super.mouseScrolled(mx, my, horizontal, vertical);
    }

    @Override
    public boolean keyPressed(KeyInput keyInput) {
        for (HoggieWidget child : children) {
            if (child.keyPressed(keyInput.key(), keyInput.scancode(), keyInput.modifiers())) return true;
        }
        if (keyInput.key() == 256) {
            close();
            return true;
        }
        return super.keyPressed(keyInput);
    }

    @Override
    public boolean charTyped(CharInput charInput) {
        for (HoggieWidget child : children) {
            if (child.charTyped((char) charInput.codepoint(), charInput.modifiers())) return true;
        }
        return super.charTyped(charInput);
    }

    @Override
    public void tick() {
        for (HoggieWidget child : children) {
            child.tick();
        }
    }

    @Override
    public void removed() {
        for (HoggieWidget child : children) {
            child.removed();
        }
    }

    public void setBackgroundColor(int color) {
        this.bgColor = color;
    }

    public static void open(HoggieScreen screen) {
        MinecraftClient.getInstance().execute(() ->
                MinecraftClient.getInstance().setScreen(screen));
    }
}
