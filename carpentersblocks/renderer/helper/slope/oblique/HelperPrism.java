package carpentersblocks.renderer.helper.slope.oblique;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.RenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HelperPrism extends RenderHelper {

    /**
     * Renders the given texture to the West prism on the North face.
     */
    public static void renderWestFaceZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
    }

    /**
     * Renders the given texture to the West prism on the South face.
     */
    public static void renderWestFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
        setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, SOUTHWEST);
        setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
    }

    /**
     * Renders the given texture to the East prism on the North face.
     */
    public static void renderEastFaceZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
        setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
    }

    /**
     * Renders the given texture to the East prism on the South face.
     */
    public static void renderEastFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, NORTHWEST);
        setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, SOUTHEAST);
        setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, NORTHEAST);
    }

    /**
     * Renders the given texture to the North prism on the West face.
     */
    public static void renderNorthFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
        setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, NORTHWEST);
        setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
    }

    /**
     * Renders the given texture to the North prism on the East face.
     */
    public static void renderNorthFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
        setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, NORTHEAST);
        setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
    }

    /**
     * Renders the given texture to the South prism on the West face.
     */
    public static void renderSouthFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, NORTHEAST);
        setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, SOUTHWEST);
        setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, SOUTHEAST);
    }

    /**
     * Renders the given texture to the South prism on the East face.
     */
    public static void renderSouthFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, SOUTHWEST);
        setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, SOUTHEAST);
        setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, NORTHWEST);
    }

    /**
     * Renders the given texture to the North sloped face of the block.
     */
    public static void renderSlopeZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        HelperOblWedge.renderSlopeZNeg(renderBlocks, Slope.ID_WEDGE_POS_N, x, y, z, icon);
    }

    /**
     * Renders the given texture to the South sloped face of the block.
     */
    public static void renderSlopeZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        HelperOblWedge.renderSlopeZPos(renderBlocks, Slope.ID_WEDGE_POS_S, x, y, z, icon);
    }

    /**
     * Renders the given texture to the West sloped face of the block.
     */
    public static void renderSlopeXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        HelperOblWedge.renderSlopeXNeg(renderBlocks, Slope.ID_WEDGE_POS_W, x, y, z, icon);
    }

    /**
     * Renders the given texture to the East sloped face of the block.
     */
    public static void renderSlopeXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        HelperOblWedge.renderSlopeXPos(renderBlocks, Slope.ID_WEDGE_POS_E, x, y, z, icon);
    }

}
