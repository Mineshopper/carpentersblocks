package carpentersblocks.renderer.helper.slope.orthogonal;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.RenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperOrthoWedge extends RenderHelper {

    /**
     * Renders the given texture to the bottom face of the block.
     */
    public static void renderFaceYNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.DOWN, x, y, z, icon);

        switch (slopeID)
        {
            case Slope.ID_OBL_EXT_POS_NW:
            case Slope.ID_OBL_INT_NEG_NW:
            case Slope.ID_WEDGE_NW:
                setupVertex(renderBlocks, xMin, yMin, zMax, uTR, vTR, SOUTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMax, uTL, vTL, SOUTHEAST);
                break;
            case Slope.ID_OBL_EXT_POS_SW:
            case Slope.ID_OBL_INT_NEG_SW:
            case Slope.ID_WEDGE_SW:
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMax, uTL, vTL, SOUTHEAST);
                break;
            case Slope.ID_OBL_EXT_POS_NE:
            case Slope.ID_OBL_INT_NEG_NE:
            case Slope.ID_WEDGE_NE:
                setupVertex(renderBlocks, xMin, yMin, zMax, uTR, vTR, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMax, uTL, vTL, SOUTHEAST);
                break;
            case Slope.ID_OBL_EXT_POS_SE:
            case Slope.ID_OBL_INT_NEG_SE:
            case Slope.ID_WEDGE_SE:
                setupVertex(renderBlocks, xMin, yMin, zMax, uTR, vTR, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
                break;
        }
    }

    /**
     * Renders the given texture to the top face of the block.
     */
    public static void renderFaceYPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.UP, x, y, z, icon);

        switch (slopeID)
        {
            case Slope.ID_OBL_EXT_NEG_NW:
            case Slope.ID_OBL_INT_POS_NW:
            case Slope.ID_WEDGE_NW:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
                break;
            case Slope.ID_OBL_EXT_NEG_SW:
            case Slope.ID_OBL_INT_POS_SW:
            case Slope.ID_WEDGE_SW:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uBR, vBR, NORTHWEST);
                break;
            case Slope.ID_OBL_EXT_NEG_NE:
            case Slope.ID_OBL_INT_POS_NE:
            case Slope.ID_WEDGE_NE:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
                break;
            case Slope.ID_OBL_EXT_NEG_SE:
            case Slope.ID_OBL_INT_POS_SE:
            case Slope.ID_WEDGE_SE:
                setupVertex(renderBlocks, xMax, yMax, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
                break;
        }
    }

    /**
     * Renders the given texture to the North face of the block.
     */
    public static void renderFaceZNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);

        Slope slope = Slope.slopesList[slopeID];

        if (slope.isPositive) {
            if (slope.facings.contains(ForgeDirection.WEST)) {
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL,                      vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL,                      vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, floatingIcon ? vTR : vBR, BOTTOM_RIGHT);
            } else {
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, floatingIcon ? vTL : vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR,                      vBR, BOTTOM_RIGHT);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR,                      vTR, TOP_RIGHT   );
            }
        } else {
            if (slope.facings.contains(ForgeDirection.WEST)) {
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, TOP_RIGHT   );
            } else {
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, BOTTOM_RIGHT);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, TOP_RIGHT   );
            }
        }
    }

    /**
     * Renders the given texture to the South face of the block.
     */
    public static void renderFaceZPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);

        Slope slope = Slope.slopesList[slopeID];

        if (slope.isPositive) {
            if (slope.facings.contains(ForgeDirection.WEST)) {
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL, floatingIcon ? vTL : vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR,                      vBR, BOTTOM_RIGHT);
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR,                      vTR, TOP_RIGHT   );
            } else {
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL,                      vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL,                      vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR, floatingIcon ? vTR : vBR, BOTTOM_RIGHT);
            }
        } else {
            if (slope.facings.contains(ForgeDirection.WEST)) {
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, BOTTOM_RIGHT);
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, TOP_RIGHT   );
            } else {
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, TOP_RIGHT   );
            }
        }
    }

    /**
     * Renders the given texture to the West face of the block.
     */
    public static void renderFaceXNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);

        Slope slope = Slope.slopesList[slopeID];

        if (slope.isPositive) {
            if (slope.facings.contains(ForgeDirection.NORTH)) {
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL, floatingIcon ? vTL : vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR,                      vBR, BOTTOM_RIGHT);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR,                      vTR, TOP_RIGHT   );
            } else {
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL,                      vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL,                      vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR, floatingIcon ? vTR : vBR, BOTTOM_RIGHT);
            }
        } else {
            if (slope.facings.contains(ForgeDirection.NORTH)) {
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, TOP_RIGHT   );
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, BOTTOM_RIGHT);
            } else {
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, TOP_RIGHT   );
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, BOTTOM_LEFT );
            }
        }
    }

    /**
     * Renders the given texture to the East face of the block.
     */
    public static void renderFaceXPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);

        Slope slope = Slope.slopesList[slopeID];

        if (slope.isPositive) {
            if (slope.facings.contains(ForgeDirection.NORTH)) {
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL,                      vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL,                      vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR, floatingIcon ? vTR : vBR, BOTTOM_RIGHT);
            } else {
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL, floatingIcon ? vTL : vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR,                      vBR, BOTTOM_RIGHT);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR,                      vTR, TOP_RIGHT   );
            }
        } else {
            if (slope.facings.contains(ForgeDirection.NORTH)) {
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, TOP_RIGHT   );
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, TOP_LEFT    );
            } else {
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, BOTTOM_RIGHT);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, TOP_RIGHT   );
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, TOP_LEFT    );
            }
        }
    }

}
