package carpentersblocks.renderer.helper.slope;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.RenderHelper;

public class HelperWedgeCorner extends RenderHelper
{

	/**
	 * Renders the given texture to the North sloped face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderSlopeZNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, NORTH, x, y, z);
        setUV(renderBlocks, ForgeDirection.NORTH, renderBlocks.uvRotateEast, icon);
		
		switch (slopeID)
		{
			case Slope.ID_WEDGE_INT_NEG_NW:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], 3);
				break;
			case Slope.ID_WEDGE_INT_NEG_NE:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_NEG_NW:
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], 3);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_NEG_NE:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], 0);
				break;
			case Slope.ID_WEDGE_INT_POS_NW:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 3);
				break;
			case Slope.ID_WEDGE_INT_POS_NE:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_POS_NW:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 0);
				break;
			case Slope.ID_WEDGE_EXT_POS_NE:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 3);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 3);
				break;
		}
	}
	
	/**
	 * Renders the given texture to the South sloped face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderSlopeZPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, SOUTH, x, y, z);
		setUV(renderBlocks, SOUTH, renderBlocks.uvRotateWest, icon);

		switch (slopeID)
		{
			case Slope.ID_WEDGE_INT_NEG_SW:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 0);
				break;
			case Slope.ID_WEDGE_INT_NEG_SE:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 3);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_NEG_SW:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_NEG_SE:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 3);
				break;
			case Slope.ID_WEDGE_INT_POS_SW:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], 3);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], 3);
				break;
			case Slope.ID_WEDGE_INT_POS_SE:
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], 0);
				break;
			case Slope.ID_WEDGE_EXT_POS_SW:
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_POS_SE:
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], 3);
				break;
		}
	}
	
	/**
	 * Renders the given texture to the West sloped face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderSlopeXNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, WEST, x, y, z);
		setUV(renderBlocks, WEST, renderBlocks.uvRotateNorth, icon);

		switch (slopeID)
		{
			case Slope.ID_WEDGE_INT_NEG_NW:
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				break;
			case Slope.ID_WEDGE_INT_NEG_SW:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_NEG_NW:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_NEG_SW:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				break;
			case Slope.ID_WEDGE_INT_POS_NW:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				break;
			case Slope.ID_WEDGE_INT_POS_SW:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_POS_NW:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_POS_SW:
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				break;
		}
	}
	
	/**
	 * Renders the given texture to the East sloped face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderSlopeXPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, EAST, x, y, z);
		setUV(renderBlocks, EAST, renderBlocks.uvRotateSouth, icon);

		switch (slopeID)
		{
			case Slope.ID_WEDGE_INT_NEG_NE:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				break;
			case Slope.ID_WEDGE_INT_NEG_SE:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_NEG_NE:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_NEG_SE:
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				break;
			case Slope.ID_WEDGE_INT_POS_NE:
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				break;
			case Slope.ID_WEDGE_INT_POS_SE:
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_POS_NE:
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				break;
			case Slope.ID_WEDGE_EXT_POS_SE:
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				break;
		}
	}

}
