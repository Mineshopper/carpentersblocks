package carpentersblocks.renderer.helper;

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

public class RenderHelperWedge extends RenderHelper {

	/**
	 * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, DOWN, x, y, z);
		setUV(renderBlocks, DOWN, renderBlocks.uvRotateBottom, icon);

		switch (slopeID)
		{
			case Slope.ID_OBL_EXT_POS_NW:
			case Slope.ID_OBL_INT_NEG_NW:
			case Slope.ID_WEDGE_NW:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][0][0], UV_DOWN[renderBlocks.uvRotateBottom][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][0][0], UV_DOWN[renderBlocks.uvRotateBottom][0][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][2][0], UV_DOWN[renderBlocks.uvRotateBottom][2][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][3][0], UV_DOWN[renderBlocks.uvRotateBottom][3][1], 3);
				break;
			case Slope.ID_OBL_EXT_POS_SW:
			case Slope.ID_OBL_INT_NEG_SW:
			case Slope.ID_WEDGE_SW:
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][1][0], UV_DOWN[renderBlocks.uvRotateBottom][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][1][0], UV_DOWN[renderBlocks.uvRotateBottom][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][2][0], UV_DOWN[renderBlocks.uvRotateBottom][2][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][3][0], UV_DOWN[renderBlocks.uvRotateBottom][3][1], 3);
				break;
			case Slope.ID_OBL_EXT_POS_NE:
			case Slope.ID_OBL_INT_NEG_NE:
			case Slope.ID_WEDGE_NE:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][0][0], UV_DOWN[renderBlocks.uvRotateBottom][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][1][0], UV_DOWN[renderBlocks.uvRotateBottom][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][3][0], UV_DOWN[renderBlocks.uvRotateBottom][3][1], 3);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][3][0], UV_DOWN[renderBlocks.uvRotateBottom][3][1], 3);
				break;
			case Slope.ID_OBL_EXT_POS_SE:
			case Slope.ID_OBL_INT_NEG_SE:
			case Slope.ID_WEDGE_SE:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][0][0], UV_DOWN[renderBlocks.uvRotateBottom][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][1][0], UV_DOWN[renderBlocks.uvRotateBottom][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][2][0], UV_DOWN[renderBlocks.uvRotateBottom][2][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][2][0], UV_DOWN[renderBlocks.uvRotateBottom][2][1], 2);
				break;
			default:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][0][0], UV_DOWN[renderBlocks.uvRotateBottom][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][1][0], UV_DOWN[renderBlocks.uvRotateBottom][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][2][0], UV_DOWN[renderBlocks.uvRotateBottom][2][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][3][0], UV_DOWN[renderBlocks.uvRotateBottom][3][1], 3);
		}
	}
	
	/**
	 * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, UP, x, y, z);
        setUV(renderBlocks, UP, renderBlocks.uvRotateTop, icon);

		switch (slopeID)
		{
			case Slope.ID_OBL_EXT_NEG_NW:
     		case Slope.ID_OBL_INT_POS_NW:
			case Slope.ID_WEDGE_NW:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], 3);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], 3);
				break;
			case Slope.ID_OBL_EXT_NEG_SW:
         	case Slope.ID_OBL_INT_POS_SW:
			case Slope.ID_WEDGE_SW:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], 2);
				break;
			case Slope.ID_OBL_EXT_NEG_NE:
         	case Slope.ID_OBL_INT_POS_NE:
			case Slope.ID_WEDGE_NE:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], 3);
				break;
			case Slope.ID_OBL_EXT_NEG_SE:
			case Slope.ID_OBL_INT_POS_SE:
			case Slope.ID_WEDGE_SE:
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], 3);
				break;
			default:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], 3);
		}
	}

	/**
	 * Renders the given texture to the North face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceZNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, NORTH, x, y, z);
        setUV(renderBlocks, ForgeDirection.NORTH, renderBlocks.uvRotateEast, icon);

        boolean floatingHeight = iconHasFloatingHeight(icon);

		switch (slopeID)
		{
			case Slope.ID_WEDGE_INT_POS_NW:
	     	case Slope.ID_WEDGE_EXT_POS_SW:
	     	case Slope.ID_OBL_EXT_POS_SW:
	     	case Slope.ID_OBL_INT_POS_NW:
			case Slope.ID_WEDGE_POS_W:
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], floatingHeight ? vMax : UV_NORTH[renderBlocks.uvRotateEast][3][1], 3);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], floatingHeight ? vMax : UV_NORTH[renderBlocks.uvRotateEast][3][1], 3);
				break;
			case Slope.ID_WEDGE_INT_POS_NE:
         	case Slope.ID_WEDGE_EXT_POS_SE:
			case Slope.ID_OBL_EXT_POS_SE:
			case Slope.ID_OBL_INT_POS_NE:
			case Slope.ID_WEDGE_POS_E:
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], floatingHeight ? vMax : UV_NORTH[renderBlocks.uvRotateEast][2][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], floatingHeight ? vMax : UV_NORTH[renderBlocks.uvRotateEast][2][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], 3);
				break;
			case Slope.ID_WEDGE_INT_NEG_NW:
			case Slope.ID_WEDGE_EXT_NEG_SW:
			case Slope.ID_OBL_EXT_NEG_SW:
			case Slope.ID_OBL_INT_NEG_NW:
			case Slope.ID_WEDGE_NEG_W:
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 0);
				break;
			case Slope.ID_WEDGE_INT_NEG_NE:
			case Slope.ID_WEDGE_EXT_NEG_SE:
			case Slope.ID_OBL_EXT_NEG_SE:
			case Slope.ID_OBL_INT_NEG_NE:
			case Slope.ID_WEDGE_NEG_E:
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], 3);
				break;
			default:
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], UV_NORTH[renderBlocks.uvRotateEast][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], UV_NORTH[renderBlocks.uvRotateEast][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], UV_NORTH[renderBlocks.uvRotateEast][2][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], UV_NORTH[renderBlocks.uvRotateEast][3][1], 3);
		}
	}
	
	/**
	 * Renders the given texture to the South face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceZPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, SOUTH, x, y, z);
		setUV(renderBlocks, SOUTH, renderBlocks.uvRotateWest, icon);

        boolean floatingHeight = iconHasFloatingHeight(icon);

		switch (slopeID)
		{
			case Slope.ID_OBL_EXT_POS_NW:
	        case Slope.ID_OBL_INT_POS_SW:
	        case Slope.ID_WEDGE_INT_POS_SW:
	        case Slope.ID_WEDGE_EXT_POS_NW:
			case Slope.ID_WEDGE_POS_W:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], floatingHeight ? vMax : UV_SOUTH[renderBlocks.uvRotateWest][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], floatingHeight ? vMax : UV_SOUTH[renderBlocks.uvRotateWest][1][1], 1); 
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 3);
				break;
			case Slope.ID_OBL_EXT_POS_NE:
			case Slope.ID_OBL_INT_POS_SE:
	        case Slope.ID_WEDGE_INT_POS_SE:
	        case Slope.ID_WEDGE_EXT_POS_NE:
			case Slope.ID_WEDGE_POS_E:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], floatingHeight ? vMax : UV_SOUTH[renderBlocks.uvRotateWest][2][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], floatingHeight ? vMax : UV_SOUTH[renderBlocks.uvRotateWest][2][1], 2);
				break;
			case Slope.ID_OBL_EXT_NEG_NW:
         	case Slope.ID_OBL_INT_NEG_SW:
         	case Slope.ID_WEDGE_INT_NEG_SW:
         	case Slope.ID_WEDGE_EXT_NEG_NW:
			case Slope.ID_WEDGE_NEG_W:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 3);
				break;
			case Slope.ID_OBL_EXT_NEG_NE:
			case Slope.ID_OBL_INT_NEG_SE:
			case Slope.ID_WEDGE_INT_NEG_SE:
			case Slope.ID_WEDGE_EXT_NEG_NE:
			case Slope.ID_WEDGE_NEG_E:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 3);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 3);
				break;
			default:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], UV_SOUTH[renderBlocks.uvRotateWest][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], UV_SOUTH[renderBlocks.uvRotateWest][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], UV_SOUTH[renderBlocks.uvRotateWest][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], UV_SOUTH[renderBlocks.uvRotateWest][3][1], 3);
		}
	}
	
	/**
	 * Renders the given texture to the West face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceXNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, WEST, x, y, z);
		setUV(renderBlocks, WEST, renderBlocks.uvRotateNorth, icon);

        boolean floatingHeight = iconHasFloatingHeight(icon);

		switch (slopeID)
		{
			case Slope.ID_WEDGE_INT_POS_NW:
	     	case Slope.ID_WEDGE_EXT_POS_NE:
	     	case Slope.ID_OBL_EXT_POS_NE:
	     	case Slope.ID_OBL_INT_POS_NW:
			case Slope.ID_WEDGE_POS_N:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], floatingHeight ? vMax : UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], floatingHeight ? vMax : UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				break;
			case Slope.ID_WEDGE_INT_POS_SW:
			case Slope.ID_WEDGE_EXT_POS_SE:
			case Slope.ID_OBL_EXT_POS_SE:
			case Slope.ID_OBL_INT_POS_SW:
			case Slope.ID_WEDGE_POS_S:
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], floatingHeight ? vMax : UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], floatingHeight ? vMax : UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				break;
			case Slope.ID_WEDGE_INT_NEG_NW:
			case Slope.ID_WEDGE_EXT_NEG_NE:
			case Slope.ID_OBL_EXT_NEG_NE:
			case Slope.ID_OBL_INT_NEG_NW:
			case Slope.ID_WEDGE_NEG_N:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
				break;
			case Slope.ID_WEDGE_INT_NEG_SW:
			case Slope.ID_WEDGE_EXT_NEG_SE:
			case Slope.ID_OBL_EXT_NEG_SE:
			case Slope.ID_OBL_INT_NEG_SW:
			case Slope.ID_WEDGE_NEG_S:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				break;
			default:
				setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], UV_WEST[renderBlocks.uvRotateNorth][0][1], 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], UV_WEST[renderBlocks.uvRotateNorth][1][1], 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], UV_WEST[renderBlocks.uvRotateNorth][2][1], 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], UV_WEST[renderBlocks.uvRotateNorth][3][1], 3);
		}
	}
	
	/**
	 * Renders the given texture to the East face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceXPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, EAST, x, y, z);
		setUV(renderBlocks, EAST, renderBlocks.uvRotateSouth, icon);

        boolean floatingHeight = iconHasFloatingHeight(icon);

		switch (slopeID)
		{
			case Slope.ID_WEDGE_INT_POS_NE:
	    	case Slope.ID_WEDGE_EXT_POS_NW:
	    	case Slope.ID_OBL_EXT_POS_NW:
	    	case Slope.ID_OBL_INT_POS_NE:
			case Slope.ID_WEDGE_POS_N:
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], floatingHeight ? vMax : UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], floatingHeight ? vMax : UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				break;
			case Slope.ID_WEDGE_INT_POS_SE:
			case Slope.ID_WEDGE_EXT_POS_SW:
			case Slope.ID_OBL_EXT_POS_SW:
			case Slope.ID_OBL_INT_POS_SE:
			case Slope.ID_WEDGE_POS_S:
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], floatingHeight ? vMax : UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], floatingHeight ? vMax : UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				break;
			case Slope.ID_WEDGE_INT_NEG_NE:
			case Slope.ID_WEDGE_EXT_NEG_NW:
			case Slope.ID_OBL_EXT_NEG_NW:
			case Slope.ID_OBL_INT_NEG_NE:
			case Slope.ID_WEDGE_NEG_N:
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				break;
			case Slope.ID_WEDGE_INT_NEG_SE:
			case Slope.ID_WEDGE_EXT_NEG_SW:
			case Slope.ID_OBL_EXT_NEG_SW:
			case Slope.ID_OBL_INT_NEG_SE:
			case Slope.ID_WEDGE_NEG_S:
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
				break;
			default:
				setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], UV_EAST[renderBlocks.uvRotateSouth][0][1], 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], UV_EAST[renderBlocks.uvRotateSouth][1][1], 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], UV_EAST[renderBlocks.uvRotateSouth][2][1], 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], UV_EAST[renderBlocks.uvRotateSouth][3][1], 3);
		}
	}

}