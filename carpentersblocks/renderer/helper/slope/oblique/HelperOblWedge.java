package carpentersblocks.renderer.helper.slope.oblique;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.RenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperOblWedge extends RenderHelper {

    /**
     * Renders the given texture to the North sloped face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderSlopeZNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);

        switch (slopeID) {
            case Slope.ID_WEDGE_POS_N:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_NEG_N:
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_NW:
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, BOTTOM_RIGHT);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, TOP_RIGHT   );
                break;
            case Slope.ID_WEDGE_NE:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, BOTTOM_RIGHT);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, TOP_RIGHT   );
                break;
        }
    }

    /**
     * Renders the given texture to the South sloped face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderSlopeZPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);

        switch (slopeID) {
            case Slope.ID_WEDGE_POS_S:
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_NEG_S:
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_SW:
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, BOTTOM_RIGHT);
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, TOP_RIGHT   );
                break;
            case Slope.ID_WEDGE_SE:
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, TOP_LEFT    );
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, BOTTOM_LEFT );
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, BOTTOM_RIGHT);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, TOP_RIGHT   );
                break;
        }
    }

    /**
     * Renders the given texture to the West sloped face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderSlopeXNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);

        switch (slopeID) {
            case Slope.ID_WEDGE_POS_W:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_NEG_W:
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
                break;
        }
    }

    /**
     * Renders the given texture to the East sloped face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderSlopeXPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);

        switch (slopeID) {
            case Slope.ID_WEDGE_POS_E:
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_NEG_E:
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
                break;
        }
    }

}
