package carpentersblocks.renderer.helper.slope.oblique;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.UP;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.RenderHelper;

public class HelperOblique extends RenderHelper {

	/**
	 * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderSlopeYNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, DOWN, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;
		double vTemp = vMin;

		/* Flip if necessary */
		switch (slopeID)
		{
		case Slope.ID_OBL_EXT_NEG_SW:
		case Slope.ID_OBL_EXT_NEG_SE:
		case Slope.ID_OBL_INT_NEG_SW:
		case Slope.ID_OBL_INT_NEG_SE:
			vMin = vMax;
			vMax = vTemp;
		}

		switch (slopeID)
		{
		case Slope.ID_OBL_EXT_NEG_NW:
			setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMin, TOP_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMin, TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMax, uMid, vMax, BOTTOM_LEFT);
			break;
		case Slope.ID_OBL_EXT_NEG_SW:
			setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, uMid, vMin, BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, TOP_RIGHT);
			break;
		case Slope.ID_OBL_EXT_NEG_NE:
			setupVertex(renderBlocks, xMin, yMin, zMax, uMid, vMax, BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMin, TOP_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMin, TOP_LEFT);
			break;
		case Slope.ID_OBL_EXT_NEG_SE:
			setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, uMid, vMin, BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, TOP_RIGHT);
			break;
		case Slope.ID_OBL_INT_NEG_NW:
			setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMax, BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, uMid, vMin, TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMax, BOTTOM_LEFT);
			break;
		case Slope.ID_OBL_INT_NEG_SW:
			setupVertex(renderBlocks, xMin, yMax, zMax, uMid, vMax, TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, BOTTOM_RIGHT);
			break;
		case Slope.ID_OBL_INT_NEG_NE:
			setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMax, BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, uMid, vMin, TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMax, BOTTOM_LEFT);
			break;
		case Slope.ID_OBL_INT_NEG_SE:
			setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMax, uMid, vMax, TOP_LEFT);
			break;
		}
	}

	/**
	 * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderSlopeYPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, UP, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;
		double vTemp = vMin;

		/* Flip if necessary */
		switch (slopeID)
		{
		case Slope.ID_OBL_EXT_POS_NW:
		case Slope.ID_OBL_EXT_POS_NE:
		case Slope.ID_OBL_INT_POS_NW:
		case Slope.ID_OBL_INT_POS_NE:
			vMin = vMax;
			vMax = vTemp;
		}

		switch (slopeID)
		{
		case Slope.ID_OBL_EXT_POS_NW:
			setupVertex(renderBlocks, xMax, yMax, zMax, uMid, vMax, TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMin, BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, BOTTOM_RIGHT);
			break;
		case Slope.ID_OBL_EXT_POS_SW:
			setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMax, yMax, zMin, uMid, vMin, TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMax, BOTTOM_LEFT);
			break;
		case Slope.ID_OBL_EXT_POS_NE:
			setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMin, BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMax, uMid, vMax, TOP_LEFT);
			break;
		case Slope.ID_OBL_EXT_POS_SE:
			setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMax, BOTTOM_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, uMid, vMin, TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, BOTTOM_LEFT);
			break;
		case Slope.ID_OBL_INT_POS_NW:
			setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMin, uMid, vMin, BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, TOP_RIGHT);
			break;
		case Slope.ID_OBL_INT_POS_SW:
			setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMin, TOP_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, TOP_LEFT);
			setupVertex(renderBlocks, xMin, yMin, zMax, uMid, vMax, BOTTOM_LEFT);
			break;
		case Slope.ID_OBL_INT_POS_NE:
			setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, TOP_LEFT);
			setupVertex(renderBlocks, xMax, yMin, zMin, uMid, vMin, BOTTOM_LEFT);
			setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, TOP_RIGHT);
			break;
		case Slope.ID_OBL_INT_POS_SE:
			setupVertex(renderBlocks, xMax, yMin, zMax, uMid, vMax, BOTTOM_LEFT);
			setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, TOP_RIGHT);
			setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMin, TOP_LEFT);
			break;
		}
	}

}