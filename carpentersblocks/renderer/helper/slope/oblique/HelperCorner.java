package carpentersblocks.renderer.helper.slope.oblique;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.RenderHelper;

public class HelperCorner extends RenderHelper {

	/**
	 * Renders the given texture to the North sloped face of the block.
	 */
	public static void renderSlopeZNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, NORTH, x, y, z, icon);

		switch (slopeID)
		{
		case Slope.ID_WEDGE_INT_NEG_NW:
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], BOTTOM_LEFT);
			break;
		case Slope.ID_WEDGE_INT_NEG_NE:
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], BOTTOM_LEFT);
			break;
		case Slope.ID_WEDGE_EXT_NEG_NW:
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], BOTTOM_LEFT);
			break;
		case Slope.ID_WEDGE_EXT_NEG_NE:
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], TOP_LEFT);
			break;
		case Slope.ID_WEDGE_INT_POS_NW:
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], TOP_RIGHT);
			break;
		case Slope.ID_WEDGE_INT_POS_NE:
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], TOP_RIGHT);
			break;
		case Slope.ID_WEDGE_EXT_POS_NW:
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], BOTTOM_RIGHT);
			break;
		case Slope.ID_WEDGE_EXT_POS_NE:
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], TOP_RIGHT);
			break;
		}
	}

	/**
	 * Renders the given texture to the South sloped face of the block.
	 */
	public static void renderSlopeZPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, SOUTH, x, y, z, icon);

		switch (slopeID)
		{
		case Slope.ID_WEDGE_INT_NEG_SW:
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], BOTTOM_RIGHT);
			break;
		case Slope.ID_WEDGE_INT_NEG_SE:
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], BOTTOM_RIGHT);
			break;
		case Slope.ID_WEDGE_EXT_NEG_SW:
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], TOP_RIGHT);
			break;
		case Slope.ID_WEDGE_EXT_NEG_SE:
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], TOP_RIGHT);
			break;
		case Slope.ID_WEDGE_INT_POS_SW:
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], BOTTOM_LEFT);
			break;
		case Slope.ID_WEDGE_INT_POS_SE:
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], TOP_LEFT);
			break;
		case Slope.ID_WEDGE_EXT_POS_SW:
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], BOTTOM_LEFT);
			break;
		case Slope.ID_WEDGE_EXT_POS_SE:
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], BOTTOM_LEFT);
			break;
		}
	}

	/**
	 * Renders the given texture to the West sloped face of the block.
	 */
	public static void renderSlopeXNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, WEST, x, y, z, icon);

		switch (slopeID)
		{
		case Slope.ID_WEDGE_INT_NEG_NW:
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
			break;
		case Slope.ID_WEDGE_INT_NEG_SW:
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
			break;
		case Slope.ID_WEDGE_EXT_NEG_NW:
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
			break;
		case Slope.ID_WEDGE_EXT_NEG_SW:
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
			break;
		case Slope.ID_WEDGE_INT_POS_NW:
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
			break;
		case Slope.ID_WEDGE_INT_POS_SW:
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
			break;
		case Slope.ID_WEDGE_EXT_POS_NW:
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
			break;
		case Slope.ID_WEDGE_EXT_POS_SW:
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
			break;
		}
	}

	/**
	 * Renders the given texture to the East sloped face of the block.
	 */
	public static void renderSlopeXPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, EAST, x, y, z, icon);

		switch (slopeID)
		{
		case Slope.ID_WEDGE_INT_NEG_NE:
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], TOP_RIGHT);
			break;
		case Slope.ID_WEDGE_INT_NEG_SE:
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], TOP_LEFT);
			break;
		case Slope.ID_WEDGE_EXT_NEG_NE:
			setupVertex(renderBlocks, xMin, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], TOP_LEFT);
			break;
		case Slope.ID_WEDGE_EXT_NEG_SE:
			setupVertex(renderBlocks, xMin, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], TOP_LEFT);
			break;
		case Slope.ID_WEDGE_INT_POS_NE:
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], TOP_LEFT);
			break;
		case Slope.ID_WEDGE_INT_POS_SE:
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], TOP_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], TOP_LEFT);
			break;
		case Slope.ID_WEDGE_EXT_POS_NE:
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], TOP_LEFT);
			break;
		case Slope.ID_WEDGE_EXT_POS_SE:
			setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], TOP_RIGHT);
			break;
		}
	}

}
