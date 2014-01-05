package carpentersblocks.renderer.helper.slope.orthogonal;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;
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
		prepareRender(renderBlocks, DOWN, x, y, z, icon);

		switch (slopeID)
		{
		case Slope.ID_OBL_EXT_POS_NW:
		case Slope.ID_OBL_INT_NEG_NW:
		case Slope.ID_WEDGE_NW:
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][0][0], UV_DOWN[renderBlocks.uvRotateBottom][0][1], SOUTHWEST);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][2][0], UV_DOWN[renderBlocks.uvRotateBottom][2][1], NORTHEAST);
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][3][0], UV_DOWN[renderBlocks.uvRotateBottom][3][1], SOUTHEAST);
			break;
		case Slope.ID_OBL_EXT_POS_SW:
		case Slope.ID_OBL_INT_NEG_SW:
		case Slope.ID_WEDGE_SW:
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][1][0], UV_DOWN[renderBlocks.uvRotateBottom][1][1], NORTHWEST);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][2][0], UV_DOWN[renderBlocks.uvRotateBottom][2][1], NORTHEAST);
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][3][0], UV_DOWN[renderBlocks.uvRotateBottom][3][1], SOUTHEAST);
			break;
		case Slope.ID_OBL_EXT_POS_NE:
		case Slope.ID_OBL_INT_NEG_NE:
		case Slope.ID_WEDGE_NE:
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][0][0], UV_DOWN[renderBlocks.uvRotateBottom][0][1], SOUTHWEST);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][1][0], UV_DOWN[renderBlocks.uvRotateBottom][1][1], NORTHWEST);
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][3][0], UV_DOWN[renderBlocks.uvRotateBottom][3][1], SOUTHEAST);
			break;
		case Slope.ID_OBL_EXT_POS_SE:
		case Slope.ID_OBL_INT_NEG_SE:
		case Slope.ID_WEDGE_SE:
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][0][0], UV_DOWN[renderBlocks.uvRotateBottom][0][1], SOUTHWEST);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][1][0], UV_DOWN[renderBlocks.uvRotateBottom][1][1], NORTHWEST);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][2][0], UV_DOWN[renderBlocks.uvRotateBottom][2][1], NORTHEAST);
			break;
		}
	}

	/**
	 * Renders the given texture to the top face of the block.
	 */
	public static void renderFaceYPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, UP, x, y, z, icon);

		switch (slopeID)
		{
		case Slope.ID_OBL_EXT_NEG_NW:
		case Slope.ID_OBL_INT_POS_NW:
		case Slope.ID_WEDGE_NW:
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], SOUTHEAST);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], NORTHEAST);
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], SOUTHWEST);
			break;
		case Slope.ID_OBL_EXT_NEG_SW:
		case Slope.ID_OBL_INT_POS_SW:
		case Slope.ID_WEDGE_SW:
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], SOUTHEAST);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], NORTHEAST);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], NORTHWEST);
			break;
		case Slope.ID_OBL_EXT_NEG_NE:
		case Slope.ID_OBL_INT_POS_NE:
		case Slope.ID_WEDGE_NE:
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], SOUTHEAST);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], NORTHWEST);
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], SOUTHWEST);
			break;
		case Slope.ID_OBL_EXT_NEG_SE:
		case Slope.ID_OBL_INT_POS_SE:
		case Slope.ID_WEDGE_SE:
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], NORTHEAST);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], NORTHWEST);
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], SOUTHWEST);
			break;
		}
	}

	/**
	 * Renders the given texture to the North face of the block.
	 */
	public static void renderFaceZNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, NORTH, x, y, z, icon);

		boolean floatingHeight = iconHasFloatingHeight(icon);
		Slope slope = Slope.slopesList[slopeID];

		if (slope.isPositive) {
			if (slope.facings.contains(ForgeDirection.WEST)) {
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateNorth][1][0], UV_NORTH[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][2][0], UV_NORTH[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][3][0], floatingHeight ? vMax : UV_NORTH[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
			} else {
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateNorth][0][0], UV_NORTH[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][2][0], floatingHeight ? vMax : UV_NORTH[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][3][0], UV_NORTH[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
			}
		} else {
			if (slope.facings.contains(ForgeDirection.WEST)) {
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateNorth][0][0], UV_NORTH[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateNorth][1][0], UV_NORTH[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][2][0], UV_NORTH[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
			} else {
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateNorth][0][0], UV_NORTH[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateNorth][1][0], UV_NORTH[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][3][0], UV_NORTH[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
			}
		}
	}

	/**
	 * Renders the given texture to the South face of the block.
	 */
	public static void renderFaceZPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, SOUTH, x, y, z, icon);

		boolean floatingHeight = iconHasFloatingHeight(icon);
		Slope slope = Slope.slopesList[slopeID];

		if (slope.isPositive) {
			if (slope.facings.contains(ForgeDirection.WEST)) {
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][1][0], floatingHeight ? vMax : UV_SOUTH[renderBlocks.uvRotateSouth][1][1], BOTTOM_LEFT);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][2][0], UV_SOUTH[renderBlocks.uvRotateSouth][2][1], BOTTOM_RIGHT);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][3][0], UV_SOUTH[renderBlocks.uvRotateSouth][3][1], TOP_RIGHT);
			} else {
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][0][0], UV_SOUTH[renderBlocks.uvRotateSouth][0][1], TOP_LEFT);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][1][0], UV_SOUTH[renderBlocks.uvRotateSouth][1][1], BOTTOM_LEFT);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][2][0], floatingHeight ? vMax : UV_SOUTH[renderBlocks.uvRotateSouth][2][1], BOTTOM_RIGHT);
			}
		} else {
			if (slope.facings.contains(ForgeDirection.WEST)) {
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][0][0], UV_SOUTH[renderBlocks.uvRotateSouth][0][1], TOP_LEFT);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][2][0], UV_SOUTH[renderBlocks.uvRotateSouth][2][1], BOTTOM_RIGHT);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][3][0], UV_SOUTH[renderBlocks.uvRotateSouth][3][1], TOP_RIGHT);
			} else {
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][0][0], UV_SOUTH[renderBlocks.uvRotateSouth][0][1], TOP_LEFT);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][1][0], UV_SOUTH[renderBlocks.uvRotateSouth][1][1], BOTTOM_LEFT);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][3][0], UV_SOUTH[renderBlocks.uvRotateSouth][3][1], TOP_RIGHT);
			}
		}
	}

	/**
	 * Renders the given texture to the West face of the block.
	 */
	public static void renderFaceXNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, WEST, x, y, z, icon);

		boolean floatingHeight = iconHasFloatingHeight(icon);
		Slope slope = Slope.slopesList[slopeID];

		if (slope.isPositive) {
			if (slope.facings.contains(ForgeDirection.NORTH)) {
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateWest][0][0], UV_WEST[renderBlocks.uvRotateWest][0][1], TOP_RIGHT);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateWest][2][0], floatingHeight ? vMax : UV_WEST[renderBlocks.uvRotateWest][2][1], BOTTOM_LEFT);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateWest][3][0], UV_WEST[renderBlocks.uvRotateWest][3][1], BOTTOM_RIGHT);
			} else {
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateWest][1][0], UV_WEST[renderBlocks.uvRotateWest][1][1], TOP_LEFT);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateWest][2][0], UV_WEST[renderBlocks.uvRotateWest][2][1], BOTTOM_LEFT);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateWest][3][0], floatingHeight ? vMax : UV_WEST[renderBlocks.uvRotateWest][3][1], BOTTOM_RIGHT);
			}
		} else {
			if (slope.facings.contains(ForgeDirection.NORTH)) {
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateWest][0][0], UV_WEST[renderBlocks.uvRotateWest][0][1], TOP_RIGHT);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateWest][1][0], UV_WEST[renderBlocks.uvRotateWest][1][1], TOP_LEFT);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateWest][3][0], UV_WEST[renderBlocks.uvRotateWest][3][1], BOTTOM_RIGHT);
			} else {
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateWest][0][0], UV_WEST[renderBlocks.uvRotateWest][0][1], TOP_RIGHT);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateWest][1][0], UV_WEST[renderBlocks.uvRotateWest][1][1], TOP_LEFT);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateWest][2][0], UV_WEST[renderBlocks.uvRotateWest][2][1], BOTTOM_LEFT);
			}
		}
	}

	/**
	 * Renders the given texture to the East face of the block.
	 */
	public static void renderFaceXPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, EAST, x, y, z, icon);

		boolean floatingHeight = iconHasFloatingHeight(icon);
		Slope slope = Slope.slopesList[slopeID];

		if (slope.isPositive) {
			if (slope.facings.contains(ForgeDirection.NORTH)) {
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateEast][0][0], UV_EAST[renderBlocks.uvRotateEast][0][1], BOTTOM_LEFT);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateEast][1][0], floatingHeight ? vMax : UV_EAST[renderBlocks.uvRotateEast][1][1], BOTTOM_RIGHT);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateEast][3][0], UV_EAST[renderBlocks.uvRotateEast][3][1], TOP_LEFT);
			} else {
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateEast][0][0], floatingHeight ? vMax : UV_EAST[renderBlocks.uvRotateEast][0][1], BOTTOM_LEFT);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateEast][1][0], UV_EAST[renderBlocks.uvRotateEast][1][1], BOTTOM_RIGHT);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateEast][2][0], UV_EAST[renderBlocks.uvRotateEast][2][1], TOP_RIGHT);
			}
		} else {
			if (slope.facings.contains(ForgeDirection.NORTH)) {
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateEast][0][0], UV_EAST[renderBlocks.uvRotateEast][0][1], BOTTOM_LEFT);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateEast][2][0], UV_EAST[renderBlocks.uvRotateEast][2][1], TOP_RIGHT);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateEast][3][0], UV_EAST[renderBlocks.uvRotateEast][3][1], TOP_LEFT);
			} else {
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateEast][1][0], UV_EAST[renderBlocks.uvRotateEast][1][1], BOTTOM_RIGHT);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateEast][2][0], UV_EAST[renderBlocks.uvRotateEast][2][1], TOP_RIGHT);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateEast][3][0], UV_EAST[renderBlocks.uvRotateEast][3][1], TOP_LEFT);
			}
		}
	}

}