package carpentersblocks.renderer.helper.slope.oblique;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
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

        setupVertex(renderBlocks, xMin, yMax, zMin + 0.5F, UV_NORTH[renderBlocks.uvRotateNorth][0][0], UV_NORTH[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMin + 0.5F, UV_NORTH[renderBlocks.uvRotateNorth][1][0], UV_NORTH[renderBlocks.uvRotateNorth][1][1], TOP_CENTER);
        setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][3][0], UV_NORTH[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
    }

    /**
     * Renders the given texture to the West prism on the South face.
     */
    public static void renderWestFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMax - 0.5F, UV_SOUTH[renderBlocks.uvRotateSouth][0][0], UV_SOUTH[renderBlocks.uvRotateSouth][0][1], TOP_LEFT);
        setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][1][0], UV_SOUTH[renderBlocks.uvRotateSouth][1][1], BOTTOM_LEFT);
        setupVertex(renderBlocks, xMax, yMax, zMax - 0.5F, UV_SOUTH[renderBlocks.uvRotateSouth][3][0], UV_SOUTH[renderBlocks.uvRotateSouth][3][1], TOP_CENTER);
    }

    /**
     * Renders the given texture to the East prism on the North face.
     */
    public static void renderEastFaceZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMin + 0.5F, UV_NORTH[renderBlocks.uvRotateNorth][0][0], UV_NORTH[renderBlocks.uvRotateNorth][0][1], TOP_CENTER);
        setupVertex(renderBlocks, xMax, yMax, zMin + 0.5F, UV_NORTH[renderBlocks.uvRotateNorth][1][0], UV_NORTH[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
        setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][2][0], UV_NORTH[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
    }

    /**
     * Renders the given texture to the East prism on the South face.
     */
    public static void renderEastFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMax - 0.5F, UV_SOUTH[renderBlocks.uvRotateSouth][0][0], UV_SOUTH[renderBlocks.uvRotateSouth][0][1], TOP_CENTER);
        setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][2][0], UV_SOUTH[renderBlocks.uvRotateSouth][2][1], BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMax - 0.5F, UV_SOUTH[renderBlocks.uvRotateSouth][3][0], UV_SOUTH[renderBlocks.uvRotateSouth][3][1], TOP_RIGHT);
    }

    /**
     * Renders the given texture to the North prism on the West face.
     */
    public static void renderNorthFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMin + 0.5F, yMax, zMax, UV_WEST[renderBlocks.uvRotateWest][0][0], UV_WEST[renderBlocks.uvRotateWest][0][1], TOP_CENTER);
        setupVertex(renderBlocks, xMin + 0.5F, yMax, zMin, UV_WEST[renderBlocks.uvRotateWest][1][0], UV_WEST[renderBlocks.uvRotateWest][1][1], TOP_LEFT);
        setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateWest][2][0], UV_WEST[renderBlocks.uvRotateWest][2][1], BOTTOM_LEFT);
    }

    /**
     * Renders the given texture to the North prism on the East face.
     */
    public static void renderNorthFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateEast][1][0], UV_EAST[renderBlocks.uvRotateEast][1][1], BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax - 0.5F, yMax, zMin, UV_EAST[renderBlocks.uvRotateEast][2][0], UV_EAST[renderBlocks.uvRotateEast][2][1], TOP_RIGHT);
        setupVertex(renderBlocks, xMax - 0.5F, yMax, zMax, UV_EAST[renderBlocks.uvRotateEast][3][0], UV_EAST[renderBlocks.uvRotateEast][3][1], TOP_CENTER);
    }

    /**
     * Renders the given texture to the South prism on the West face.
     */
    public static void renderSouthFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMin + 0.5F, yMax, zMax, UV_WEST[renderBlocks.uvRotateWest][0][0], UV_WEST[renderBlocks.uvRotateWest][0][1], TOP_RIGHT);
        setupVertex(renderBlocks, xMin + 0.5F, yMax, zMin, UV_WEST[renderBlocks.uvRotateWest][1][0], UV_WEST[renderBlocks.uvRotateWest][1][1], TOP_CENTER);
        setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateWest][3][0], UV_WEST[renderBlocks.uvRotateWest][3][1], BOTTOM_RIGHT);
    }

    /**
     * Renders the given texture to the South prism on the East face.
     */
    public static void renderSouthFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateEast][0][0], UV_EAST[renderBlocks.uvRotateEast][0][1], BOTTOM_LEFT);
        setupVertex(renderBlocks, xMax - 0.5F, yMax, zMin, UV_EAST[renderBlocks.uvRotateEast][2][0], UV_EAST[renderBlocks.uvRotateEast][2][1], TOP_CENTER);
        setupVertex(renderBlocks, xMax - 0.5F, yMax, zMax, UV_EAST[renderBlocks.uvRotateEast][3][0], UV_EAST[renderBlocks.uvRotateEast][3][1], TOP_LEFT);
    }

    // WEDGE SLOPES BELOW FOR PRISM_SLOPE TYPE

    /**
     * Renders the given texture to the North sloped face of the block.
     */
    public static void renderSlopeZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMax, UV_NORTH[renderBlocks.uvRotateNorth][1][0], UV_NORTH[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
        setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][2][0], UV_NORTH[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
        setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][3][0], UV_NORTH[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMax, UV_NORTH[renderBlocks.uvRotateNorth][0][0], UV_NORTH[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
    }

    /**
     * Renders the given texture to the South sloped face of the block.
     */
    public static void renderSlopeZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][2][0], UV_SOUTH[renderBlocks.uvRotateSouth][2][1], BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateSouth][3][0], UV_SOUTH[renderBlocks.uvRotateSouth][3][1], TOP_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateSouth][0][0], UV_SOUTH[renderBlocks.uvRotateSouth][0][1], TOP_LEFT);
        setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][1][0], UV_SOUTH[renderBlocks.uvRotateSouth][1][1], BOTTOM_LEFT);
    }

    /**
     * Renders the given texture to the West sloped face of the block.
     */
    public static void renderSlopeXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMax, UV_WEST[renderBlocks.uvRotateWest][0][0], UV_WEST[renderBlocks.uvRotateWest][0][1], TOP_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMin, UV_WEST[renderBlocks.uvRotateWest][1][0], UV_WEST[renderBlocks.uvRotateWest][1][1], TOP_LEFT);
        setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateWest][2][0], UV_WEST[renderBlocks.uvRotateWest][2][1], BOTTOM_LEFT);
        setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateWest][3][0], UV_WEST[renderBlocks.uvRotateWest][3][1], BOTTOM_RIGHT);
    }

    /**
     * Renders the given texture to the East sloped face of the block.
     */
    public static void renderSlopeXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateEast][0][0], UV_EAST[renderBlocks.uvRotateEast][0][1], BOTTOM_LEFT);
        setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateEast][1][0], UV_EAST[renderBlocks.uvRotateEast][1][1], BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMin, UV_EAST[renderBlocks.uvRotateEast][2][0], UV_EAST[renderBlocks.uvRotateEast][2][1], TOP_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMax, UV_EAST[renderBlocks.uvRotateEast][3][0], UV_EAST[renderBlocks.uvRotateEast][3][1], TOP_LEFT);
    }

}
