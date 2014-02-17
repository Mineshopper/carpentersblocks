package carpentersblocks.renderer.helper.slope.oblique;

import javax.swing.Icon;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.RenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperCorner extends RenderHelper {
    
    /**
     * Renders the given texture to the North sloped face of the block.
     */
    public static void renderSlopeZNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);
        
        switch (slopeID)
        {
            case Slope.ID_WEDGE_INT_NEG_NW:
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_INT_NEG_NE:
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_EXT_NEG_NW:
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_EXT_NEG_NE:
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
                break;
            case Slope.ID_WEDGE_INT_POS_NW:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_INT_POS_NE:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_EXT_POS_NW:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
                break;
            case Slope.ID_WEDGE_EXT_POS_NE:
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
                break;
        }
    }
    
    /**
     * Renders the given texture to the South sloped face of the block.
     */
    public static void renderSlopeZPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);
        
        switch (slopeID)
        {
            case Slope.ID_WEDGE_INT_NEG_SW:
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
                break;
            case Slope.ID_WEDGE_INT_NEG_SE:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
                break;
            case Slope.ID_WEDGE_EXT_NEG_SW:
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_EXT_NEG_SE:
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_INT_POS_SW:
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_INT_POS_SE:
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
                break;
            case Slope.ID_WEDGE_EXT_POS_SW:
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_EXT_POS_SE:
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
                break;
        }
    }
    
    /**
     * Renders the given texture to the West sloped face of the block.
     */
    public static void renderSlopeXNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);
        
        switch (slopeID)
        {
            case Slope.ID_WEDGE_INT_NEG_NW:
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_INT_NEG_SW:
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_EXT_NEG_NW:
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_EXT_NEG_SW:
                setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
                break;
            case Slope.ID_WEDGE_INT_POS_NW:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
                break;
            case Slope.ID_WEDGE_INT_POS_SW:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_EXT_POS_NW:
                setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_EXT_POS_SW:
                setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
                break;
        }
    }
    
    /**
     * Renders the given texture to the East sloped face of the block.
     */
    public static void renderSlopeXPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);
        
        switch (slopeID)
        {
            case Slope.ID_WEDGE_INT_NEG_NE:
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
                break;
            case Slope.ID_WEDGE_INT_NEG_SE:
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_EXT_NEG_NE:
                setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_EXT_NEG_SE:
                setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
                setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
                setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
                break;
            case Slope.ID_WEDGE_INT_POS_NE:
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_INT_POS_SE:
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_EXT_POS_NE:
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
                break;
            case Slope.ID_WEDGE_EXT_POS_SE:
                setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
                setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
                setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
                break;
        }
    }
    
}
