package com.carpentersblocks.renderer.helper.slope.oblique;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.data.Slope;
import com.carpentersblocks.renderer.helper.RenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperPrism extends RenderHelper {

    /**
     * Renders the given texture to the negative North slope of the block.
     */
    public static void renderSlopeYNegZNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);

        double uBM  = uBR  - (uBR  -  uBL) / 2;
        double xMid = xMax - (xMax - xMin) / 2;

        setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST );
        setupVertex(renderBlocks, xMid, yMin, zMax, uBM, vBR, TOP_CENTER);
        setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST );
    }

    /**
     * Renders the given texture to the negative South slope of the block.
     */
    public static void renderSlopeYNegZPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);

        double uBM  = uBR  - (uBR  -  uBL) / 2;
        double xMid = xMax - (xMax - xMin) / 2;

        setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST    );
        setupVertex(renderBlocks, xMid, yMin, zMin, uBM, vBR, BOTTOM_CENTER);
        setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST    );
    }

    /**
     * Renders the given texture to the negative West slope of the block.
     */
    public static void renderSlopeYNegXNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);

        double uBM  = uBR  - (uBR  -  uBL) / 2;
        double zMid = zMax - (zMax - zMin) / 2;

        setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST  );
        setupVertex(renderBlocks, xMax, yMin, zMid, uBM, vBR, LEFT_CENTER);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST  );
    }

    /**
     * Renders the given texture to the negative East slope of the block.
     */
    public static void renderSlopeYNegXPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);

        double uBM  = uBR  - (uBR  -  uBL) / 2;
        double zMid = zMax - (zMax - zMin) / 2;

        setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST   );
        setupVertex(renderBlocks, xMin, yMin, zMid, uBM, vBR, RIGHT_CENTER);
        setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST   );
    }

    /**
     * Renders the given texture to the positive North slope of the block.
     */
    public static void renderSlopeYPosZNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);

        double uTM  = uTR  - (uTR  -  uTL) / 2;
        double xMid = xMax - (xMax - xMin) / 2;

        setupVertex(renderBlocks, xMid, yMax, zMax, uTM, vTL, TOP_CENTER  );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST   );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST   );
    }

    /**
     * Renders the given texture to the positive South slope of the block.
     */
    public static void renderSlopeYPosZPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);

        double uTM  = uTR  - (uTR  -  uTL) / 2;
        double xMid = xMax - (xMax - xMin) / 2;

        setupVertex(renderBlocks, xMid, yMax, zMin, uTM, vTL, BOTTOM_CENTER);
        setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST    );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST    );
    }

    /**
     * Renders the given texture to the positive West slope of the block.
     */
    public static void renderSlopeYPosXNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);

        double uTM  = uTR  - (uTR  -  uTL) / 2;
        double zMid = zMax - (zMax - zMin) / 2;

        setupVertex(renderBlocks, xMax, yMax, zMid, uTM, vTL, LEFT_CENTER );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST   );
        setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST   );
    }

    /**
     * Renders the given texture to the positive East slope of the block.
     */
    public static void renderSlopeYPosXPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);

        double uTM  = uTR  - (uTR  -  uTL) / 2;
        double zMid = zMax - (zMax - zMin) / 2;

        setupVertex(renderBlocks, xMin, yMax, zMid, uTM, vTR, RIGHT_CENTER);
        setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST   );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST   );
    }

    /**
     * Renders the given texture to the West prism on the North face.
     */
    public static void renderWestPointSlopeZNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
    }

    /**
     * Renders the given texture to the West prism on the South face.
     */
    public static void renderWestPointSlopeZPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
        setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
        setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
    }

    /**
     * Renders the given texture to the East prism on the North face.
     */
    public static void renderEastPointSlopeZNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
        setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
    }

    /**
     * Renders the given texture to the East prism on the South face.
     */
    public static void renderEastPointSlopeZPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
        setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
        setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
    }

    /**
     * Renders the given texture to the North prism on the West face.
     */
    public static void renderNorthPointSlopeXNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
        setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
        setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
    }

    /**
     * Renders the given texture to the North prism on the East face.
     */
    public static void renderNorthPointSlopeXPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
        setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
        setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
    }

    /**
     * Renders the given texture to the South prism on the West face.
     */
    public static void renderSouthPointSlopeXNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
        setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
        setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
    }

    /**
     * Renders the given texture to the South prism on the East face.
     */
    public static void renderSouthPointSlopeXPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
        setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
        setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
    }

    /**
     * Renders the given texture to the North sloped face of the block.
     */
    public static void renderWedgeSlopeZNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        HelperOblWedge.renderSlopeZNeg(renderBlocks, Slope.ID_WEDGE_POS_N, x, y, z, icon);
    }

    /**
     * Renders the given texture to the South sloped face of the block.
     */
    public static void renderWedgeSlopeZPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        HelperOblWedge.renderSlopeZPos(renderBlocks, Slope.ID_WEDGE_POS_S, x, y, z, icon);
    }

    /**
     * Renders the given texture to the West sloped face of the block.
     */
    public static void renderWedgeSlopeXNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        HelperOblWedge.renderSlopeXNeg(renderBlocks, Slope.ID_WEDGE_POS_W, x, y, z, icon);
    }

    /**
     * Renders the given texture to the East sloped face of the block.
     */
    public static void renderWedgeSlopeXPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        HelperOblWedge.renderSlopeXPos(renderBlocks, Slope.ID_WEDGE_POS_E, x, y, z, icon);
    }

}
