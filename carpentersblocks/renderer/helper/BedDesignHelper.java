package carpentersblocks.renderer.helper;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BedDesignHelper {

    public static final byte SIDE1 = 0;
    public static final byte SIDE2 = 1;
    public static final byte END   = 2;
    public static final byte SIDE4 = 3;
    public static final byte SIDE5 = 4;
    public static final byte HEAD  = 5;
    public static final byte FOOT  = 6;

    private static double offset1 = (double) 1 / 3;
    private static double offset2 = (double) 2 / 3;
    private static double offset3 = (double) 1 / 6;
    private static double offset4 = (double) 11 / 48;
    private static double offset5 = (double) 37 / 48;

    /**
     * Stores UV corners for design components.
     * [piece][corner][UV]
     *
     * [corner] Values:
     *
     * 0 = [TL]
     * 1 = [TR]
     * 2 = [BR]
     * 3 = [BL]
     */
    private static double[][][] designUV =
        {
        { { offset1, offset3 }, { offset1, offset1 }, { offset4, offset1 }, { offset4, offset3 } }, // Side 1
        { { offset1, offset1 }, { offset1, offset2 }, { offset4, offset2 }, { offset4, offset1 } }, // Side 2
        { { offset1, offset2 }, { offset2, offset2 }, { offset2, offset5 }, { offset1, offset5 } }, // End
        { { offset2, offset2 }, { offset2, offset1 }, { offset5, offset1 }, { offset5, offset2 } }, // Side 4
        { { offset2, offset1 }, { offset2, offset3 }, { offset5, offset3 }, { offset5, offset1 } }, // Side 5
        { { offset1, offset3 }, { offset2, offset3 }, { offset2, offset1 }, { offset1, offset1 } }, // Head
        { { offset1, offset1 }, { offset2, offset1 }, { offset2, offset2 }, { offset1, offset2 } }  // Foot
        };

    public static void renderFaceYPos(RenderBlocks renderBlocks, int piece, double x, double y, double z)
    {
        Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        double[] coord_TL = { designUV[piece][0][0], designUV[piece][0][1] };
        double[] coord_BL = { designUV[piece][3][0], designUV[piece][3][1] };
        double[] coord_BR = { designUV[piece][2][0], designUV[piece][2][1] };
        double[] coord_TR = { designUV[piece][1][0], designUV[piece][1][1] };

        double[] temp_TL = coord_TL;
        double[] temp_BL = coord_BL;
        double[] temp_BR = coord_BR;
        double[] temp_TR = coord_TR;

        /*
         * Rotate texture.
         */
        switch (renderBlocks.uvRotateTop)
        {
        case 1:
            temp_TL = coord_BL;
            temp_BL = coord_BR;
            temp_BR = coord_TR;
            temp_TR = coord_TL;
            break;
        case 2:
            temp_TL = coord_BR;
            temp_BL = coord_TR;
            temp_BR = coord_TL;
            temp_TR = coord_BL;
            break;
        case 3:
            temp_TL = coord_TR;
            temp_BL = coord_TL;
            temp_BR = coord_BL;
            temp_TR = coord_BR;
            break;
        }

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;

        VertexHelper.setupVertex(renderBlocks, xMax, yMax, zMax, temp_TL[0], temp_TL[1], 0); // SE
        VertexHelper.setupVertex(renderBlocks, xMax, yMax, zMin, temp_BL[0], temp_BL[1], 1); // NE
        VertexHelper.setupVertex(renderBlocks, xMin, yMax, zMin, temp_BR[0], temp_BR[1], 2); // NW
        VertexHelper.setupVertex(renderBlocks, xMin, yMax, zMax, temp_TR[0], temp_TR[1], 3); // SW
    }

    public static void renderFaceZNeg(RenderBlocks renderBlocks, int piece, double x, double y, double z)
    {
        Tessellator.instance.setColorOpaque_F(0.8F, 0.8F, 0.8F);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;

        VertexHelper.setupVertex(renderBlocks, xMin, yMax, zMin, designUV[piece][1][0], designUV[piece][1][1], 0); // TR
        VertexHelper.setupVertex(renderBlocks, xMax, yMax, zMin, designUV[piece][0][0], designUV[piece][0][1], 1); // TL
        VertexHelper.setupVertex(renderBlocks, xMax, yMin, zMin, designUV[piece][3][0], designUV[piece][3][1], 2); // BL
        VertexHelper.setupVertex(renderBlocks, xMin, yMin, zMin, designUV[piece][2][0], designUV[piece][2][1], 3); // BR
    }

    public static void renderFaceZPos(RenderBlocks renderBlocks, int piece, double x, double y, double z)
    {
        Tessellator.instance.setColorOpaque_F(0.8F, 0.8F, 0.8F);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMax = z + renderBlocks.renderMaxZ;

        VertexHelper.setupVertex(renderBlocks, xMin, yMax, zMax, designUV[piece][0][0], designUV[piece][0][1], 0); // TL
        VertexHelper.setupVertex(renderBlocks, xMin, yMin, zMax, designUV[piece][3][0], designUV[piece][3][1], 1); // BL
        VertexHelper.setupVertex(renderBlocks, xMax, yMin, zMax, designUV[piece][2][0], designUV[piece][2][1], 2); // BR
        VertexHelper.setupVertex(renderBlocks, xMax, yMax, zMax, designUV[piece][1][0], designUV[piece][1][1], 3); // TR
    }

    public static void renderFaceXNeg(RenderBlocks renderBlocks, int piece, double x, double y, double z)
    {
        Tessellator.instance.setColorOpaque_F(0.6F, 0.6F, 0.6F);

        double xMin = x + renderBlocks.renderMinX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;

        VertexHelper.setupVertex(renderBlocks, xMin, yMax, zMax, designUV[piece][1][0], designUV[piece][1][1], 0); // TR
        VertexHelper.setupVertex(renderBlocks, xMin, yMax, zMin, designUV[piece][0][0], designUV[piece][0][1], 1); // TL
        VertexHelper.setupVertex(renderBlocks, xMin, yMin, zMin, designUV[piece][3][0], designUV[piece][3][1], 2); // BL
        VertexHelper.setupVertex(renderBlocks, xMin, yMin, zMax, designUV[piece][2][0], designUV[piece][2][1], 3); // BR
    }

    public static void renderFaceXPos(RenderBlocks renderBlocks, int piece, double x, double y, double z)
    {
        Tessellator.instance.setColorOpaque_F(0.6F, 0.6F, 0.6F);

        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;

        VertexHelper.setupVertex(renderBlocks, xMax, yMin, zMax, designUV[piece][3][0], designUV[piece][3][1], 0); // BL
        VertexHelper.setupVertex(renderBlocks, xMax, yMin, zMin, designUV[piece][2][0], designUV[piece][2][1], 1); // BR
        VertexHelper.setupVertex(renderBlocks, xMax, yMax, zMin, designUV[piece][1][0], designUV[piece][1][1], 2); // TR
        VertexHelper.setupVertex(renderBlocks, xMax, yMax, zMax, designUV[piece][0][0], designUV[piece][0][1], 3); // TL
    }

}
