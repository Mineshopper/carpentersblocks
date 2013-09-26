package carpentersblocks.renderer.helper.slope;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.RenderHelper;

public class RenderHelperWedge extends RenderHelper
{

	/**
	 * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderSlopeYNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
		double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
		double vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
		double vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY - offset;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;

		switch (slopeID)
		{
			case Slope.ID_WEDGE_NEG_N:
				setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMax, 3);
				break;
			case Slope.ID_WEDGE_NEG_S:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMin, 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMax, 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMin, 3);
				break;
			case Slope.ID_WEDGE_NEG_W:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMin, 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMax, 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 3);
				break;
			case Slope.ID_WEDGE_NEG_E:
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMax, 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMin, 3);
				break;
		}
	}
	
	/**
	 * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
		double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
		double vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
		double vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY - offset;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;
		
		switch (slopeID)
		{
			case Slope.ID_WEDGE_NW:
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 3);
				break;				
			case Slope.ID_WEDGE_SW:
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 3);
				break;				
			case Slope.ID_WEDGE_NE:
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 1);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 3);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 3);
				break;				
			case Slope.ID_WEDGE_SE:
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2);
				break;
			default:
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 3);
		}
	}

	/**
	 * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderSlopeYPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
		double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
		double vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
		double vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY + offset;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;

		switch (slopeID)
		{
			case Slope.ID_WEDGE_POS_N:
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMin, 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMax, 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMin, 3);
				break;
			case Slope.ID_WEDGE_POS_S:
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 3);
				break;
			case Slope.ID_WEDGE_POS_W:
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMin, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMin, 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMax, 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMax, 3);
				break;
			case Slope.ID_WEDGE_POS_E:
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMax, 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMin, 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMin, 3);
				break;
		}
	}
	
	/**
	 * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
		double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
		double vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
		double vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY + offset;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;
		        
		switch (slopeID)
		{
			case Slope.ID_WEDGE_NW:
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 3);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 3);
				break;				
			case Slope.ID_WEDGE_SW:
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 2);
				break;				
			case Slope.ID_WEDGE_NE:
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 3);
				break;				
			case Slope.ID_WEDGE_SE:
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 3);
				break;
			default:
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 3);
		}
	}
	
	/**
	 * Renders the given texture to the North face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderSlopeZNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		double uMin = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
		double uMax = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
		double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxY * 16.0D);
		double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinY * 16.0D);

        double xMin = x + renderBlocks.renderMinX - offset;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;

		switch (slopeID)
		{
			case Slope.ID_WEDGE_NW:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, 0); // TR
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 1); // TL
				setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMin, 2); // BL
				setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, 3); // BR
				break;
			case Slope.ID_WEDGE_NE:
				setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, 0); // TR
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 1); // TL
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMin, 2); // BL
				setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 3); // BR
				break;
		}
	}

	/**
	 * Renders the given texture to the North (old was East) face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceZNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
		double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
		double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxY * 16.0D);
		double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinY * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ - offset;

		switch (slopeID)
		{
			case Slope.ID_WEDGE_POS_W:
				setupVertex(renderBlocks, xMin, yMin, zMin, uMax, iconHasFloatingHeight(icon) ? vMax : vMin, 3);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMax, iconHasFloatingHeight(icon) ? vMax : vMin, 3);
				break;
			case Slope.ID_WEDGE_POS_E:
				setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMin, iconHasFloatingHeight(icon) ? vMax : vMin, 2);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMin, iconHasFloatingHeight(icon) ? vMax : vMin, 2);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 3);
				break;
			case Slope.ID_WEDGE_NEG_W:
				setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, 0);
				break;
			case Slope.ID_WEDGE_NEG_E:
				setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 3);
				break;
			default:
				setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 3);
		}
	}
	
	/**
	 * Renders the given texture to the South face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderSlopeZPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		double uMin = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
		double uMax = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
		double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxY * 16.0D);
		double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinY * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX + offset;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;

		switch (slopeID)
		{
			case Slope.ID_WEDGE_SW:
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 0); // TL
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 1); // BL
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, 2); // BR
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 3); // TR
				break;
			case Slope.ID_WEDGE_SE:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0); // TL
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, 1); // BL
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2); // BR
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 3); // TR
				break;
		}
	}

	/**
	 * Renders the given texture to the South (old was West) face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceZPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
		double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
		double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxY * 16.0D);
		double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinY * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMax = z + renderBlocks.renderMaxZ + offset;
		
		switch (slopeID)
		{
			case Slope.ID_WEDGE_POS_W:
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, iconHasFloatingHeight(icon) ? vMax : vMin, 1);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, iconHasFloatingHeight(icon) ? vMax : vMin, 1);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 3);
				break;
			case Slope.ID_WEDGE_POS_E:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, 1);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, iconHasFloatingHeight(icon) ? vMax : vMin, 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, iconHasFloatingHeight(icon) ? vMax : vMin, 2);
				break;
			case Slope.ID_WEDGE_NEG_W:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 3);
				break;
			case Slope.ID_WEDGE_NEG_E:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, 1);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 3);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 3);
				break;
			default:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, 1);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 3);
		}
	}
	
	/**
	 * Renders the given texture to the West (old was North) face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceXNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		double uMin = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
		double uMax = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
		double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxY * 16.0D);
		double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinY * 16.0D);

        double xMin = x + renderBlocks.renderMinX - offset;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;
		
		switch (slopeID)
		{
			case Slope.ID_WEDGE_POS_N:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, iconHasFloatingHeight(icon) ? vMax : vMin, 2);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, iconHasFloatingHeight(icon) ? vMax : vMin, 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, 3);
				break;
			case Slope.ID_WEDGE_POS_S:
				setupVertex(renderBlocks, xMin, yMin, zMax, uMax, iconHasFloatingHeight(icon) ? vMax : vMin, 3);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMax, iconHasFloatingHeight(icon) ? vMax : vMin, 3);
				break;
			case Slope.ID_WEDGE_NEG_N:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, 3);
				break;
			case Slope.ID_WEDGE_NEG_S:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, 0);
				break;
			default:
				setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, 0);
				setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 1);
				setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 2);
				setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, 3);
		}
	}
	
	/**
	 * Renders the given texture to the East (old was South) face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceXPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		double uMin = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
		double uMax = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
		double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxY * 16.0D);
		double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinY * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX + offset;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;
		
		switch (slopeID)
		{
			case Slope.ID_WEDGE_POS_N:
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMin, 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, iconHasFloatingHeight(icon) ? vMax : vMin, 1);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, iconHasFloatingHeight(icon) ? vMax : vMin, 1);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 3);
				break;
			case Slope.ID_WEDGE_POS_S:
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, iconHasFloatingHeight(icon) ? vMax : vMin, 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 2);
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, iconHasFloatingHeight(icon) ? vMax : vMin, 0);
				break;
			case Slope.ID_WEDGE_NEG_N:
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMin, 0);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 2);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 3);
				break;
			case Slope.ID_WEDGE_NEG_S:
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 3);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 3);
				break;
			default:
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMin, 0);
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 1);
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 2);
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 3);
		}
	}
	
}