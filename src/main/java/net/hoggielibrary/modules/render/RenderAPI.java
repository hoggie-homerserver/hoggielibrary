package net.hoggielibrary.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public final class RenderAPI {

    public void drawText(MatrixStack matrices, String text, float x, float y, int color) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer renderer = client.textRenderer;
        if (renderer != null) {
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            VertexConsumerProvider.Immediate vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();
            renderer.draw(text, x, y, color, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
            vertexConsumers.draw();
        }
    }

    public void drawCenteredText(MatrixStack matrices, String text, float x, float y, int color) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer renderer = client.textRenderer;
        if (renderer != null) {
            int width = renderer.getWidth(text);
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            VertexConsumerProvider.Immediate vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();
            renderer.draw(text, x - width / 2.0f, y, color, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
            vertexConsumers.draw();
        }
    }

    public void drawRect(MatrixStack matrices, float x, float y, float width, float height, int color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float a = (color >> 24 & 255) / 255.0f;
        float r = (color >> 16 & 255) / 255.0f;
        float g = (color >> 8 & 255) / 255.0f;
        float b = (color & 255) / 255.0f;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(r, g, b, a);
        GL11.glVertex3f(matrix.m00() * x + matrix.m01() * (y + height) + matrix.m03(),
                matrix.m10() * x + matrix.m11() * (y + height) + matrix.m13(), 0);
        GL11.glVertex3f(matrix.m00() * (x + width) + matrix.m01() * (y + height) + matrix.m03(),
                matrix.m10() * (x + width) + matrix.m11() * (y + height) + matrix.m13(), 0);
        GL11.glVertex3f(matrix.m00() * (x + width) + matrix.m01() * y + matrix.m03(),
                matrix.m10() * (x + width) + matrix.m11() * y + matrix.m13(), 0);
        GL11.glVertex3f(matrix.m00() * x + matrix.m01() * y + matrix.m03(),
                matrix.m10() * x + matrix.m11() * y + matrix.m13(), 0);
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawBoxOutline(BlockPos pos, int color) {
        Box box = new Box(pos);
        drawBoxOutline(box, color);
    }

    public void drawBoxOutline(Box box, int color) {
        // World renderer implementations would go through a WorldRenderEvents callback
    }

    public double[] worldToScreen(Vec3d pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getCameraEntity() == null) return null;

        Vec3d cameraPos = client.getCameraEntity().getSyncedPos();
        double x = pos.x - cameraPos.x;
        double y = pos.y - cameraPos.y;
        double z = pos.z - cameraPos.z;

        return new double[]{x, y, z};
    }

    public int getWindowWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    public int getWindowHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }

    public void drawLine(MatrixStack matrices, float x1, float y1, float x2, float y2, int color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float a = (color >> 24 & 255) / 255.0f;
        float r = (color >> 16 & 255) / 255.0f;
        float g = (color >> 8 & 255) / 255.0f;
        float b = (color & 255) / 255.0f;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor4f(r, g, b, a);
        GL11.glVertex3f(matrix.m00() * x1 + matrix.m01() * y1 + matrix.m03(),
                matrix.m10() * x1 + matrix.m11() * y1 + matrix.m13(), 0);
        GL11.glVertex3f(matrix.m00() * x2 + matrix.m01() * y2 + matrix.m03(),
                matrix.m10() * x2 + matrix.m11() * y2 + matrix.m13(), 0);
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
