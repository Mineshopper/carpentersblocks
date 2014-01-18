package carpentersblocks.renderer.helper.slope.oblique;

import static net.minecraftforge.common.ForgeDirection.NORTH;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.RenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperOblique extends RenderHelper {

    /**
     * Renders the given texture to the bottom sloped face.
     */
    public static void renderSlopeYNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        double uTM = uTR - (uTR - uTL) / 2;

        switch (slopeID) {
        case Slope.ID_OBL_EXT_NEG_NW:
            setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
            setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
            setupVertex(renderBlocks, xMax, yMin, zMax, uTM, vBL, SOUTHEAST);
            break;
        case Slope.ID_OBL_EXT_NEG_SW:
            setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
            setupVertex(renderBlocks, xMax, yMin, zMin, uTM, vBL, NORTHEAST);
            setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
            break;
        case Slope.ID_OBL_EXT_NEG_NE:
            setupVertex(renderBlocks, xMin, yMin, zMax, uTM, vBL, SOUTHWEST);
            setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
            setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
            break;
        case Slope.ID_OBL_EXT_NEG_SE:
            setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
            setupVertex(renderBlocks, xMin, yMin, zMin, uTM, vBL, NORTHWEST);
            setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
            break;
        case Slope.ID_OBL_INT_NEG_NW:
            setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
            setupVertex(renderBlocks, xMin, yMax, zMin, uTM, vTL, NORTHWEST);
            setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
            break;
        case Slope.ID_OBL_INT_NEG_SW:
            setupVertex(renderBlocks, xMin, yMax, zMax, uTM, vTL, SOUTHWEST);
            setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
            setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
            break;
        case Slope.ID_OBL_INT_NEG_NE:
            setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
            setupVertex(renderBlocks, xMax, yMax, zMin, uTM, vTL, NORTHEAST);
            setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
            break;
        case Slope.ID_OBL_INT_NEG_SE:
            setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
            setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
            setupVertex(renderBlocks, xMax, yMax, zMax, uTM, vTL, SOUTHEAST);
            break;
        }
    }

    /**
     * Renders the given texture to the top sloped face.
     */
    public static void renderSlopeYPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        double uTM = uTR - (uTR - uTL) / 2;

        switch (slopeID) {
        case Slope.ID_OBL_EXT_POS_NW:
            setupVertex(renderBlocks, xMax, yMax, zMax, uTM, vTL, SOUTHEAST);
            setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
            setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
            break;
        case Slope.ID_OBL_EXT_POS_SW:
            setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
            setupVertex(renderBlocks, xMax, yMax, zMin, uTM, vTL, NORTHEAST);
            setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
            break;
        case Slope.ID_OBL_EXT_POS_NE:
            setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
            setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
            setupVertex(renderBlocks, xMin, yMax, zMax, uTM, vTL, SOUTHWEST);
            break;
        case Slope.ID_OBL_EXT_POS_SE:
            setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
            setupVertex(renderBlocks, xMin, yMax, zMin, uTM, vTL, NORTHWEST);
            setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
            break;
        case Slope.ID_OBL_INT_POS_NW:
            setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
            setupVertex(renderBlocks, xMin, yMin, zMin, uTM, vBL, NORTHWEST);
            setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
            break;
        case Slope.ID_OBL_INT_POS_SW:
            setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
            setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
            setupVertex(renderBlocks, xMin, yMin, zMax, uTM, vBL, SOUTHWEST);
            break;
        case Slope.ID_OBL_INT_POS_NE:
            setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
            setupVertex(renderBlocks, xMax, yMin, zMin, uTM, vBL, NORTHEAST);
            setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
            break;
        case Slope.ID_OBL_INT_POS_SE:
            setupVertex(renderBlocks, xMax, yMin, zMax, uTM, vBL, SOUTHEAST);
            setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
            setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
            break;
        }
    }

}
