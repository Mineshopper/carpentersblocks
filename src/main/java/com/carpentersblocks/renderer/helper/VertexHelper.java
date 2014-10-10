package com.carpentersblocks.renderer.helper;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VertexHelper {

    public final static int TOP_LEFT     = 0;
    public final static int BOTTOM_LEFT  = 1;
    public final static int BOTTOM_RIGHT = 2;
    public final static int TOP_RIGHT    = 3;

    public final static int SOUTHEAST = TOP_LEFT;
    public final static int NORTHEAST = BOTTOM_LEFT;
    public final static int NORTHWEST = BOTTOM_RIGHT;
    public final static int SOUTHWEST = TOP_RIGHT;

    public final static int TOP_CENTER    = 4;
    public final static int BOTTOM_CENTER = 5;
    public final static int LEFT_CENTER   = 6;
    public final static int RIGHT_CENTER  = 7;

    private static boolean   clearFloat       = false;
    protected static boolean floatingIcon     = false;

    private static int drawMode;

    /** Keeps track of vertices drawn per pass. */
    public static int vertexCount = 0;

    /** Keeps track of vertex draws when in triangle mode. */
    private static int triVertexCount = 0;

    /**
     * Offset used for faces.
     */
    protected static double offset = 0.0D;

    /**
     * Sets draw mode internally.
     * <p>
     * This would ordinarily force a draw and prepare the {@link Tessellator}
     * with a new draw mode, but because alpha pass and ShadersModCore
     * have issues with {@link GL11#GL11_GL_TRIANGLES Triangles}, we'll use
     * a faux draw mode and transform {@link GL11#GL11_GL_TRIANGLES Triangles}
     * to {@link GL11#GL11_GL_QUADS Quads} instead in method {@link #setupVertex}.
     */
    public static void startDrawing(int inDrawMode)
    {
        drawMode = inDrawMode;
    }

    /**
     * Gets floating icon flag.
     * <p>
     * Floating icons translate down on y-axis so top of icon
     * meets {@link RenderBlocks#renderMaxY}.
     *
     * @return <code>true</code> if icon is floating
     */
    public static boolean hasFloatingIcon()
    {
        return floatingIcon;
    }

    /**
     * Temporarily sets floating icon flag for current face draw.
     * <p>
     * To keep it enabled, call {@link #setFloatingIconLock()} instead.
     */
    public static void setFloatingIcon()
    {
        floatingIcon = clearFloat = true;
    }

    /**
     * Locks floating icon flag.
     */
    public static void setFloatingIconLock()
    {
        floatingIcon = true;
    }

    /**
     * Clears floating icon flag.
     */
    public static void clearFloatingIconLock()
    {
        floatingIcon = false;
    }

    /**
     * Sets offset for drawing face.
     */
    public static void setOffset(double render_offset)
    {
        offset = render_offset;
    }

    /**
     * Clears offset.
     */
    public static void clearOffset()
    {
        offset = 0.0D;
    }

    /**
     * Prepare helper for next face draw.
     */
    public static void postRender()
    {
        if (clearFloat) {
            floatingIcon = clearFloat = false;
        }
    }

    /**
     * Adds vertex to Tessellator and increments draw count.
     */
    public static void drawVertex(RenderBlocks renderBlocks, double x, double y, double z, double u, double v)
    {
        Tessellator.instance.addVertexWithUV(x, y, z, u, v);
        ++vertexCount;
    }

    /**
     * Applies brightness, color, and adds vertex through tessellator.
     * <p>
     * If {@link #drawMode} is {@link GL11#GL_TRIANGLES Triangles}, will automatically
     * duplicate the third vertex to form a {@link GL11#GL_QUADS Quad}.
     *
     * @param renderBlocks the {@link RenderBlocks}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param u the texture coordinate x-offset
     * @param v the texture coordinate y-offset
     * @param vertex the vertex corner
     * @see   {@link #startDrawing(int)}
     */
    public static void setupVertex(RenderBlocks renderBlocks, double x, double y, double z, double u, double v, int vertex)
    {
        Tessellator tessellator = Tessellator.instance;

        if (renderBlocks != null && renderBlocks.enableAO) {

            switch(vertex) {
                case BOTTOM_CENTER:
                    tessellator.setColorOpaque_F((renderBlocks.colorRedBottomLeft + renderBlocks.colorRedBottomRight) / 2.0F, (renderBlocks.colorGreenBottomLeft + renderBlocks.colorGreenBottomRight) / 2.0F, (renderBlocks.colorBlueBottomLeft + renderBlocks.colorBlueBottomRight) / 2.0F);
                    tessellator.setBrightness(LightingHelper.getAverageBrightness(renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight));
                    break;
                case TOP_CENTER:
                    tessellator.setColorOpaque_F((renderBlocks.colorRedTopLeft + renderBlocks.colorRedTopRight) / 2.0F, (renderBlocks.colorGreenTopLeft + renderBlocks.colorGreenTopRight) / 2.0F, (renderBlocks.colorBlueTopLeft + renderBlocks.colorBlueTopRight) / 2);
                    tessellator.setBrightness(LightingHelper.getAverageBrightness(renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight));
                    break;
                case LEFT_CENTER:
                    tessellator.setColorOpaque_F((renderBlocks.colorRedTopLeft + renderBlocks.colorRedBottomLeft) / 2.0F, (renderBlocks.colorGreenTopLeft + renderBlocks.colorGreenBottomLeft) / 2.0F, (renderBlocks.colorBlueTopLeft + renderBlocks.colorBlueBottomLeft) / 2.0F);
                    tessellator.setBrightness(LightingHelper.getAverageBrightness(renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft));
                    break;
                case RIGHT_CENTER:
                    tessellator.setColorOpaque_F((renderBlocks.colorRedTopRight + renderBlocks.colorRedBottomRight) / 2.0F, (renderBlocks.colorGreenTopRight + renderBlocks.colorGreenBottomRight) / 2.0F, (renderBlocks.colorBlueTopRight + renderBlocks.colorBlueBottomRight) / 2);
                    tessellator.setBrightness(LightingHelper.getAverageBrightness(renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight));
                    break;
                case TOP_LEFT:
                    tessellator.setColorOpaque_F(renderBlocks.colorRedTopLeft, renderBlocks.colorGreenTopLeft, renderBlocks.colorBlueTopLeft);
                    tessellator.setBrightness(renderBlocks.brightnessTopLeft);
                    break;
                case BOTTOM_LEFT:
                    tessellator.setColorOpaque_F(renderBlocks.colorRedBottomLeft, renderBlocks.colorGreenBottomLeft, renderBlocks.colorBlueBottomLeft);
                    tessellator.setBrightness(renderBlocks.brightnessBottomLeft);
                    break;
                case BOTTOM_RIGHT:
                    tessellator.setColorOpaque_F(renderBlocks.colorRedBottomRight, renderBlocks.colorGreenBottomRight, renderBlocks.colorBlueBottomRight);
                    tessellator.setBrightness(renderBlocks.brightnessBottomRight);
                    break;
                case TOP_RIGHT:
                    tessellator.setColorOpaque_F(renderBlocks.colorRedTopRight, renderBlocks.colorGreenTopRight, renderBlocks.colorBlueTopRight);
                    tessellator.setBrightness(renderBlocks.brightnessTopRight);
                    break;
            }

        }

        drawVertex(renderBlocks, x, y, z, u, v);

        /* Alpha quad sorting and ShadersModeCore won't work with triangles, so make them a quad. */

        if (drawMode == GL11.GL_TRIANGLES) {
            if (++triVertexCount > 2) {
                drawVertex(renderBlocks, x, y, z, u, v);
                triVertexCount = 0;
            }
        }
    }

}
