package net.hoggielibrary.modules.gui;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ContainerScreen extends HoggieScreen {

    protected Identifier texture;
    protected int bgWidth;
    protected int bgHeight;
    protected int containerX;
    protected int containerY;
    protected int titleColor = 0x404040;
    protected boolean drawDarkOverlay = true;

    private static final Identifier DEFAULT_TEXTURE = Identifier.of("minecraft", "textures/gui/container/generic_54.png");

    protected ContainerScreen(Text title, int bgWidth, int bgHeight) {
        this(title, DEFAULT_TEXTURE, bgWidth, bgHeight);
    }

    protected ContainerScreen(Text title, Identifier texture, int bgWidth, int bgHeight) {
        super(title);
        this.texture = texture;
        this.bgWidth = bgWidth;
        this.bgHeight = bgHeight;
    }

    @Override
    public void init() {
        super.init();
        containerX = (width - bgWidth) / 2;
        containerY = (height - bgHeight) / 2;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (drawDarkOverlay) {
            renderBackground(ctx, mx, my, delta);
        }
        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, texture, containerX, containerY, 0, 0, bgWidth, bgHeight, 256, 256);
        ctx.drawText(getTextRenderer(), title, containerX + 8, containerY + 6, titleColor, false);
        super.render(ctx, mx, my, delta);
    }

    public void setTexture(Identifier texture) {
        this.texture = texture;
    }

    public void setBgSize(int width, int height) {
        this.bgWidth = width;
        this.bgHeight = height;
        containerX = (this.width - bgWidth) / 2;
        containerY = (this.height - bgHeight) / 2;
    }

    public void setTitleColor(int color) {
        this.titleColor = color;
    }

    public void setDrawDarkOverlay(boolean drawDarkOverlay) {
        this.drawDarkOverlay = drawDarkOverlay;
    }
}
