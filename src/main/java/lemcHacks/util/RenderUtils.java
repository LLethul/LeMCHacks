package lemcHacks.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.systems.RenderSystem;

import java.awt.Color;

public class RenderUtils {
    public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double rad, double samples) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        double toX1 = toX - rad;
        double toY1 = toY - rad;
        double fromX1 = fromX + rad;
        double fromY1 = fromY + rad;
        double[][] map = new double[][]{new double[]{toX1, toY1}, new double[]{toX1, fromY1}, new double[]{fromX1, fromY1}, new double[]{fromX1, toY1}};
        for (int i = 0; i < 4; i++) {
            double[] current = map[i];
            for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / samples)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
            }
        }
        BufferRenderer.drawWithShader(bufferBuilder.end());
    }

    public static void renderRoundedQuad(MatrixStack matrices, Color c, double fromX, double fromY, double toX, double toY, double rad, double samples) {
        int color = c.getRGB();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float f = (float) (color >> 24 & 255) / 255.0F;
        float g = (float) (color >> 16 & 255) / 255.0F;
        float h = (float) (color >> 8 & 255) / 255.0F;
        float k = (float) (color & 255) / 255.0F;
        renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, rad, samples);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public void drawBox(MatrixStack matrixStack, int x, int y, int width, int height, float r, float g, float b,
                        float alpha) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(r, g, b, alpha);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        {
            bufferBuilder.vertex(matrix, x, y, 0).next();
            bufferBuilder.vertex(matrix, x + width, y, 0).next();
            bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
            bufferBuilder.vertex(matrix, x, y + height, 0).next();
        }
        BufferRenderer.drawWithoutShader(bufferBuilder.end());

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawBox(MatrixStack matrixStack, int x, int y, int width, int height, lemcHacks.util.Color color, float alpha) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        {
            bufferBuilder.vertex(matrix, x, y, 0).next();
            bufferBuilder.vertex(matrix, x + width, y, 0).next();
            bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
            bufferBuilder.vertex(matrix, x, y + height, 0).next();
        }
        BufferRenderer.drawWithoutShader(bufferBuilder.end());

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawOutlinedBox(MatrixStack matrixStack, int x, int y, int width, int height, lemcHacks.util.Color color,
                                float alpha) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        {
            bufferBuilder.vertex(matrix, x, y, 0).next();
            bufferBuilder.vertex(matrix, x + width, y, 0).next();
            bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
            bufferBuilder.vertex(matrix, x, y + height, 0).next();
        }
        BufferRenderer.drawWithoutShader(bufferBuilder.end());

        RenderSystem.setShaderColor(0, 0, 0, alpha);

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        {
            bufferBuilder.vertex(matrix, x, y, 0).next();
            bufferBuilder.vertex(matrix, x + width, y, 0).next();
            bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
            bufferBuilder.vertex(matrix, x, y + height, 0).next();
            bufferBuilder.vertex(matrix, x, y, 0).next();
        }
        BufferRenderer.drawWithoutShader(bufferBuilder.end());

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawOutline(MatrixStack matrixStack, int x, int y, int width, int height) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionShader);

        RenderSystem.setShaderColor(0, 0, 0, 1);

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        {
            bufferBuilder.vertex(matrix, x, y, 0).next();
            bufferBuilder.vertex(matrix, x + width, y, 0).next();
            bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
            bufferBuilder.vertex(matrix, x, y + height, 0).next();
            bufferBuilder.vertex(matrix, x, y, 0).next();
        }
        BufferRenderer.drawWithoutShader(bufferBuilder.end());

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void draw3DBox(MatrixStack matrixStack, Box box, lemcHacks.util.Color color, float alpha) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), 1.0f);

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        {
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
        }
        BufferRenderer.drawWithoutShader(bufferBuilder.end());

        RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        {
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
        }
        BufferRenderer.drawWithoutShader(bufferBuilder.end());

    }
}