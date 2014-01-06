package carpentersblocks.renderer.helper.slope.orthogonal;

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
public class HelperTriangle extends RenderHelper {

    /**
     * Renders the given texture to the North face of the block.
     */
    public static void renderFaceZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        /* Render left (XPos) half of triangle. */

        renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateNorth][0][0], floatingHeight ? vMax : UV_NORTH[renderBlocks.uvRotateNorth][0][1], TOP_CENTER);
        setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][2][0], floatingHeight ? vMax : UV_NORTH[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
        setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][3][0], UV_NORTH[renderBlocks.uvRotateNorth][3][1], BOTTOM_CENTER);

        /* Render right (XNeg) half of triangle. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
        prepareRender(renderBlocks, NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateNorth][1][0], floatingHeight ? vMax : UV_NORTH[renderBlocks.uvRotateNorth][0][1], TOP_CENTER);
        setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][2][0], UV_NORTH[renderBlocks.uvRotateNorth][2][1], BOTTOM_CENTER);
        setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][3][0], floatingHeight ? vMax : UV_NORTH[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
    }

    /**
     * Renders the given texture to the South face of the block.
     */
    public static void renderFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        /* Render left (XNeg) half of triangle. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
        prepareRender(renderBlocks, SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][1][0], floatingHeight ? vMax : UV_SOUTH[renderBlocks.uvRotateSouth][1][1], BOTTOM_LEFT);
        setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][2][0], UV_SOUTH[renderBlocks.uvRotateSouth][2][1], BOTTOM_CENTER);
        setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][3][0], floatingHeight ? vMax : UV_SOUTH[renderBlocks.uvRotateSouth][3][1], TOP_CENTER);

        /* Render right (XPos) half of triangle. */

        renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
        prepareRender(renderBlocks, SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][1][0], UV_SOUTH[renderBlocks.uvRotateSouth][1][1], BOTTOM_CENTER);
        setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][2][0], floatingHeight ? vMax : UV_SOUTH[renderBlocks.uvRotateSouth][2][1], BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][0][0], floatingHeight ? vMax : UV_SOUTH[renderBlocks.uvRotateSouth][3][1], TOP_CENTER);
    }

    /**
     * Renders the given texture to the West face of the block.
     */
    public static void renderFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        /* Render left (ZNeg) half of triangle. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
        prepareRender(renderBlocks, WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateWest][0][0], floatingHeight ? vMax : UV_WEST[renderBlocks.uvRotateWest][1][1], TOP_CENTER);
        setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateWest][2][0], floatingHeight ? vMax : UV_WEST[renderBlocks.uvRotateWest][2][1], BOTTOM_LEFT);
        setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateWest][3][0], UV_WEST[renderBlocks.uvRotateWest][3][1], BOTTOM_CENTER);

        /* Render right (ZPos) half of triangle. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
        prepareRender(renderBlocks, WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateWest][1][0], floatingHeight ? vMax : UV_WEST[renderBlocks.uvRotateWest][1][1], TOP_CENTER);
        setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateWest][2][0], UV_WEST[renderBlocks.uvRotateWest][2][1], BOTTOM_CENTER);
        setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateWest][3][0], floatingHeight ? vMax : UV_WEST[renderBlocks.uvRotateWest][3][1], BOTTOM_RIGHT);
    }

    /**
     * Renders the given texture to the East face of the block.
     */
    public static void renderFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        boolean floatingHeight = iconHasFloatingHeight(icon);

        /* Render left (ZPos) half of triangle. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
        prepareRender(renderBlocks, EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateEast][0][0], floatingHeight ? vMax : UV_EAST[renderBlocks.uvRotateEast][0][1], BOTTOM_LEFT);
        setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateEast][1][0], UV_EAST[renderBlocks.uvRotateEast][1][1], BOTTOM_CENTER);
        setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateEast][2][0], floatingHeight ? vMax : UV_EAST[renderBlocks.uvRotateEast][3][1], TOP_CENTER);

        /* Render right (ZNeg) half of triangle. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
        prepareRender(renderBlocks, EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateEast][0][0], UV_EAST[renderBlocks.uvRotateEast][0][1], BOTTOM_CENTER);
        setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateEast][1][0], floatingHeight ? vMax : UV_EAST[renderBlocks.uvRotateEast][1][1], BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateEast][3][0], floatingHeight ? vMax : UV_EAST[renderBlocks.uvRotateEast][3][1], TOP_CENTER);
    }

}
