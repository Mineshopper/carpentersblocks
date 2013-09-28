package carpentersblocks.renderer.helper.slope;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.UP;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import carpentersblocks.renderer.helper.RenderHelper;

public class HelperPyramid extends RenderHelper {

	/**
	 * Renders the given texture to the negative North (was East) slope of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYNegZNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, DOWN, x, y, z);
		setUV(renderBlocks, DOWN, renderBlocks.uvRotateBottom, icon);
		
		double vMid = vMax - ((vMax - vMin) / 2);
		double uMid = uMax - ((uMax - uMin) / 2);

		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 0);
		setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMin, 1);
		setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMin, 2);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 0);
	}
	
	/**
	 * Renders the given texture to the negative South (was West) slope of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYNegZPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, DOWN, x, y, z);
		setUV(renderBlocks, DOWN, renderBlocks.uvRotateBottom, icon);
		
		double vMid = vMax - ((vMax - vMin) / 2);
		double uMid = uMax - ((uMax - uMin) / 2);

		setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMin, 0);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 1);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 1);
		setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMin, 3);
	}
	
	/**
	 * Renders the given texture to the negative West (was North) slope of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYNegXNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, DOWN, x, y, z);
		setUV(renderBlocks, DOWN, renderBlocks.uvRotateBottom, icon);
		
		double vMid = vMax - ((vMax - vMin) / 2);
		double uMid = uMax - ((uMax - uMin) / 2);

		setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMin, 0);
		setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 1);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 2);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 2);
	}
	
	/**
	 * Renders the given texture to the negative East (was South) slope of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYNegXPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, DOWN, x, y, z);
		setUV(renderBlocks, DOWN, renderBlocks.uvRotateBottom, icon);
		
		double vMid = vMax - ((vMax - vMin) / 2);
		double uMid = uMax - ((uMax - uMin) / 2);

		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 0);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 0);
		setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 2);
		setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMin, 3);
	}

	/**
	 * Renders the given texture to the positive North (was East) slope of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYPosZNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, UP, x, y, z);
        setUV(renderBlocks, UP, renderBlocks.uvRotateTop, icon);
		
		double vMid = vMax - ((vMax - vMin) / 2);
		double uMid = uMax - ((uMax - uMin) / 2);

		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 0);
		setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMax, 1);
		setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMax, 2);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 0);
	}
	
	/**
	 * Renders the given texture to the positive South (was West) slope of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYPosZPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, UP, x, y, z);
        setUV(renderBlocks, UP, renderBlocks.uvRotateTop, icon);
		
		double vMid = vMax - ((vMax - vMin) / 2);
		double uMid = uMax - ((uMax - uMin) / 2);
		
		setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 0);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 1);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 1);
		setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 3);
	}
	
	/**
	 * Renders the given texture to the positive West (was North) slope of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYPosXNeg(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, UP, x, y, z);
        setUV(renderBlocks, UP, renderBlocks.uvRotateTop, icon);
		
		double vMid = vMax - ((vMax - vMin) / 2);
		double uMid = uMax - ((uMax - uMin) / 2);

		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 0);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 0);
		setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMax, 2);
		setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMax, 3);
	}
	
	/**
	 * Renders the given texture to the positive East (was South) slope of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYPosXPos(RenderBlocks renderBlocks, int slopeID, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, UP, x, y, z);
        setUV(renderBlocks, UP, renderBlocks.uvRotateTop, icon);
		
		double vMid = vMax - ((vMax - vMin) / 2);
		double uMid = uMax - ((uMax - uMin) / 2);

		setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMax, 0);
		setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMax, 1);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 2);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMid, 2);
	}
	
}
