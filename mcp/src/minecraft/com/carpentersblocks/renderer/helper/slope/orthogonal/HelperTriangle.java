package com.carpentersblocks.renderer.helper.slope.orthogonal;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import com.carpentersblocks.renderer.helper.RenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperTriangle extends RenderHelper {

    /**
     * Renders a half triangle on the North left face of the block.
     */
    public static void renderFaceZNegXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMin, zMin, uBL, floatingIcon ? vTL : vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR,                      vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMin, uTR,                      vTR, TOP_RIGHT   );
    }

    /**
     * Renders a half triangle on the North right face of the block.
     */
    public static void renderFaceZNegXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMin, uTL,                      vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBL,                      vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR, floatingIcon ? vTR : vBR, BOTTOM_RIGHT);
    }

    /**
     * Renders a half triangle on the South left face of the block.
     */
    public static void renderFaceZPosXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMin, zMax, uBL, floatingIcon ? vTL: vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBR,                     vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMax, uTR,                     vTR, TOP_RIGHT   );
    }

    /**
     * Renders a half triangle on the South right face of the block.
     */
    public static void renderFaceZPosXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMin, zMax, uBL,                      vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBR, floatingIcon ? vTR : vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTL,                      vTL, TOP_LEFT    );
    }

    /**
     * Renders a half triangle on the West left face of the block.
     */
    public static void renderFaceXNegZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMin, zMin, uBL, floatingIcon ? vTL : vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMax, uBR,                      vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR,                      vTR, TOP_RIGHT   );
    }

    /**
     * Renders a half triangle on the West right face of the block.
     */
    public static void renderFaceXNegZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMin, uTL,                      vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBL,                      vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMax, uBR, floatingIcon ? vTR : vBR, BOTTOM_RIGHT);
    }

    /**
     * Renders a half triangle on the East left face of the block.
     */
    public static void renderFaceXPosZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMin, zMax, uBL, floatingIcon ? vTL : vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBR,                      vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMin, uTR,                      vTR, TOP_RIGHT   );
    }

    /**
     * Renders a half triangle on the East right face of the block.
     */
    public static void renderFaceXPosZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMax, uTL,                      vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBL,                      vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBR, floatingIcon ? vTR : vBR, BOTTOM_RIGHT);
    }

}
