package net.hoggielibrary.modules.gui;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public final class VanillaBackground {

    public static final Identifier GENERIC_54 = Identifier.of("minecraft", "textures/gui/container/generic_54.png");
    public static final Identifier INVENTORY = Identifier.of("minecraft", "textures/gui/container/inventory.png");
    public static final Identifier FURNACE = Identifier.of("minecraft", "textures/gui/container/furnace.png");
    public static final Identifier DISPENSER = Identifier.of("minecraft", "textures/gui/container/dispenser.png");
    public static final Identifier HOPPER = Identifier.of("minecraft", "textures/gui/container/hopper.png");

    public static final int GENERIC_54_W = 176;
    public static final int GENERIC_54_H = 168;
    public static final int INVENTORY_W = 176;
    public static final int INVENTORY_H = 166;
    public static final int FURNACE_W = 176;
    public static final int FURNACE_H = 166;
    public static final int DISPENSER_W = 176;
    public static final int DISPENSER_H = 168;
    public static final int HOPPER_W = 176;
    public static final int HOPPER_H = 133;

    private VanillaBackground() {
    }

    public static int containerX(int screenWidth, int bgWidth) {
        return (screenWidth - bgWidth) / 2;
    }

    public static int containerY(int screenHeight, int bgHeight) {
        return (screenHeight - bgHeight) / 2;
    }

    public static void draw(DrawContext ctx, int screenWidth, int screenHeight, Identifier texture, int bgWidth, int bgHeight) {
        int cx = containerX(screenWidth, bgWidth);
        int cy = containerY(screenHeight, bgHeight);
        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, texture, cx, cy, 0, 0, bgWidth, bgHeight, 256, 256);
    }

    public static void drawWithDarkOverlay(DrawContext ctx, int screenWidth, int screenHeight, Identifier texture, int bgWidth, int bgHeight) {
        ctx.fill(0, 0, screenWidth, screenHeight, 0xC0101010);
        draw(ctx, screenWidth, screenHeight, texture, bgWidth, bgHeight);
    }
}
