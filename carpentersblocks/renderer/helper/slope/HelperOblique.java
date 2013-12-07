package carpentersblocks.renderer.helper.slope;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.UP;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.RenderHelper;

public class HelperOblique extends RenderHelper {

	/**
	 * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderSlopeYNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, DOWN, x, y, z);
		setUV(renderBlocks, DOWN, renderBlocks.uvRotateBottom, icon);		
		
		double uMid = uMax - ((uMax - uMin) / 2);
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
				setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMin, 0);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMin, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMid, vMax, 3);
				break;
			case Slope.ID_OBL_EXT_NEG_SW:
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMid, vMin, 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 3);
				break;
			case Slope.ID_OBL_EXT_NEG_NE:
				setupVertex(renderBlocks, xMin, yMin, zMax, uMid, vMax, 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMin, 3);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMin, 3);
				break;
			case Slope.ID_OBL_EXT_NEG_SE:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMid, vMin, 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 2);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 2);
				break;				
			case Slope.ID_OBL_INT_NEG_NW:
				setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMid, vMin, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMax, 2);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMax, 2);
				break;				
			case Slope.ID_OBL_INT_NEG_SW:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMid, vMax, 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 1);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, 3);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, 3);
				break;				
			case Slope.ID_OBL_INT_NEG_NE:
				setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMax, 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMax, 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMid, vMin, 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMax, 3);
				break;				
			case Slope.ID_OBL_INT_NEG_SE:
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, 0);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMid, vMax, 3);
				break;
		}
	}
	
	/**
	 * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderSlopeYPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, UP, x, y, z);
        setUV(renderBlocks, UP, renderBlocks.uvRotateTop, icon);
		
		double uMid = uMax - ((uMax - uMin) / 2);
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
				setupVertex(renderBlocks, xMax, yMax, zMax, uMid, vMax, 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMin, 1);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, 3);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, 3);
				break;
			case Slope.ID_OBL_EXT_POS_SW:
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMid, vMin, 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMax, 2);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMax, 2);
				break;
			case Slope.ID_OBL_EXT_POS_NE:
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMin, 0);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMin, 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMid, vMax, 3);
				break;
			case Slope.ID_OBL_EXT_POS_SE:
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMax, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMax, 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMid, vMin, 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 3);
				break;				
			case Slope.ID_OBL_INT_POS_NW:
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMid, vMin, 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, 3);
				break;				
			case Slope.ID_OBL_INT_POS_SW:
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMin, 0);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMin, 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMid, vMax, 3);
				break;				
			case Slope.ID_OBL_INT_POS_NE:
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMid, vMin, 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, 2);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, 2);
				break;				
			case Slope.ID_OBL_INT_POS_SE:
				setupVertex(renderBlocks, xMax, yMin, zMax, uMid, vMax, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMin, 3);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMin, 3);
				break;
		}
	}
	
}