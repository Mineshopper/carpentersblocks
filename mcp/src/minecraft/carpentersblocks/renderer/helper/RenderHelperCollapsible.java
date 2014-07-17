package carpentersblocks.renderer.helper;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.util.collapsible.CollapsibleUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelperCollapsible extends RenderHelper {

    /**
     * Renders the given texture to the top North slope.
     */
    public static void renderSlopeYPosZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.UP, x, y, z, icon);

        double uTM = uTR - (uTR - uTL) / 2;
        double xMid = xMax - (xMax - xMin) / 2;

        setupVertex(renderBlocks, xMid, y + CollapsibleUtil.CENTER_YMAX, zMax, uTM, vTR, TOP_CENTER  );
        setupVertex(renderBlocks, xMax, y + CollapsibleUtil.offset_XZPN, zMin, uBL, vBL, NORTHEAST   );
        setupVertex(renderBlocks, xMin, y + CollapsibleUtil.offset_XZNN, zMin, uBR, vBR, NORTHWEST   );
    }

    /**
     * Renders the given texture to the top South slope.
     */
    public static void renderSlopeYPosZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.UP, x, y, z, icon);

        double uBM = uBR - (uBR - uBL) / 2;
        double xMid = xMax - (xMax - xMin) / 2;

        setupVertex(renderBlocks, xMax, y + CollapsibleUtil.offset_XZPP, zMax, uTL, vTL, SOUTHEAST    );
        setupVertex(renderBlocks, xMid, y + CollapsibleUtil.CENTER_YMAX, zMin, uBM, vBR, BOTTOM_CENTER);
        setupVertex(renderBlocks, xMin, y + CollapsibleUtil.offset_XZNP, zMax, uTR, vTR, SOUTHWEST    );
    }

    /**
     * Renders the given texture to the top West slope.
     */
    public static void renderSlopeXNegYPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.UP, x, y, z, icon);

        double vLM = vBL - (vBL - vTL) / 2;
        double zMid = zMax - (zMax - zMin) / 2;

        setupVertex(renderBlocks, xMax, y + CollapsibleUtil.CENTER_YMAX, zMid, uBL, vLM, LEFT_CENTER );
        setupVertex(renderBlocks, xMin, y + CollapsibleUtil.offset_XZNN, zMin, uBR, vBR, NORTHWEST   );
        setupVertex(renderBlocks, xMin, y + CollapsibleUtil.offset_XZNP, zMax, uTR, vTR, SOUTHWEST   );
    }

    /**
     * Renders the given texture to the top East slope.
     */
    public static void renderSlopeXPosYPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.UP, x, y, z, icon);

        double vRM = vBR - (vBR - vTR) / 2;
        double zMid = zMax - (zMax - zMin) / 2;

        setupVertex(renderBlocks, xMax, y + CollapsibleUtil.offset_XZPP, zMax, uTL, vTL, SOUTHEAST   );
        setupVertex(renderBlocks, xMax, y + CollapsibleUtil.offset_XZPN, zMin, uBL, vBL, NORTHEAST   );
        setupVertex(renderBlocks, xMin, y + CollapsibleUtil.CENTER_YMAX, zMid, uBR, vRM, RIGHT_CENTER);
    }

    /**
     * Renders the given texture to the North face of the block.
     */
    public static void renderFaceZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);

        if (floatingIcon) {
            vBL = vTL - (vTL - vBL) * CollapsibleUtil.offset_XZPN;
            vBR = vTR - (vTR - vBR) * CollapsibleUtil.offset_XZNN;
        } else {
            vTL = vBL + (vTL - vBL) * CollapsibleUtil.offset_XZPN;
            vTR = vBR + (vTR - vBR) * CollapsibleUtil.offset_XZNN;
        }

        setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPN, zMin, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMax,                               yMin, zMin, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin,                               yMin, zMin, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNN, zMin, uTR, vTR, TOP_RIGHT   );
    }

    /**
     * Renders the given texture to the South face of the block.
     */
    public static void renderFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);

        if (floatingIcon) {
            vBL = vTL - (vTL - vBL) * CollapsibleUtil.offset_XZNP;
            vBR = vTR - (vTR - vBR) * CollapsibleUtil.offset_XZPP;
        } else {
            vTL = vBL + (vTL - vBL) * CollapsibleUtil.offset_XZNP;
            vTR = vBR + (vTR - vBR) * CollapsibleUtil.offset_XZPP;
        }

        setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNP, zMax, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMin,                               yMin, zMax, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax,                               yMin, zMax, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPP, zMax, uTR, vTR, TOP_RIGHT   );
    }

    /**
     * Renders the given texture to the West face of the block.
     */
    public static void renderFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);

        if (floatingIcon) {
            vBL = vTL - (vTL - vBL) * CollapsibleUtil.offset_XZNN;
            vBR = vTR - (vTR - vBR) * CollapsibleUtil.offset_XZNP;
        } else {
            vTL = vBL + (vTL - vBL) * CollapsibleUtil.offset_XZNN;
            vTR = vBR + (vTR - vBR) * CollapsibleUtil.offset_XZNP;
        }

        setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNN, zMin, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMin,                               yMin, zMin, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin,                               yMin, zMax, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNP, zMax, uTR, vTR, TOP_RIGHT   );
    }

    /**
     * Renders the given texture to the East face of the block.
     */
    public static void renderFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);

        if (floatingIcon) {
            vBL = vTL - (vTL - vBL) * CollapsibleUtil.offset_XZPP;
            vBR = vTR - (vTR - vBR) * CollapsibleUtil.offset_XZPN;
        } else {
            vTL = vBL + (vTL - vBL) * CollapsibleUtil.offset_XZPP;
            vTR = vBR + (vTR - vBR) * CollapsibleUtil.offset_XZPN;
        }

        setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPP, zMax, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMax,                               yMin, zMax, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax,                               yMin, zMin, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPN, zMin, uTR, vTR, TOP_RIGHT   );
    }

}
