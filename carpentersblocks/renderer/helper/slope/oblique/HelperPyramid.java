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
public class HelperPyramid extends RenderHelper {

	/**
	 * Renders the given texture to the negative North slope of the block.
	 */
	public static void renderFaceYNegZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, NORTH, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;

		setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, TOP_RIGHT);
		setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, TOP_LEFT);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMin, BOTTOM_CENTER);
	}

	/**
	 * Renders the given texture to the negative South slope of the block.
	 */
	public static void renderFaceYNegZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, SOUTH, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;

		setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, TOP_LEFT);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMin, BOTTOM_CENTER);
		setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, TOP_RIGHT);
	}

	/**
	 * Renders the given texture to the negative West slope of the block.
	 */
	public static void renderFaceYNegXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, WEST, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;

		setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, TOP_RIGHT);
		setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, TOP_LEFT);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMin, BOTTOM_CENTER);
	}

	/**
	 * Renders the given texture to the negative East slope of the block.
	 */
	public static void renderFaceYNegXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, EAST, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;

		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMin, BOTTOM_CENTER);
		setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, TOP_RIGHT);
		setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, TOP_LEFT);
	}

	/**
	 * Renders the given texture to the positive North slope of the block.
	 */
	public static void renderFaceYPosZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, NORTH, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;

		setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMin, BOTTOM_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, BOTTOM_RIGHT);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMax, TOP_CENTER);
	}

	/**
	 * Renders the given texture to the positive South slope of the block.
	 */
	public static void renderFaceYPosZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, SOUTH, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;

		setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, BOTTOM_RIGHT);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMax, TOP_CENTER);
		setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, BOTTOM_LEFT);
	}

	/**
	 * Renders the given texture to the positive West slope of the block.
	 */
	public static void renderFaceYPosXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, WEST, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;

		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMax, TOP_CENTER);
		setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, BOTTOM_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, BOTTOM_RIGHT);
	}

	/**
	 * Renders the given texture to the positive East slope of the block.
	 */
	public static void renderFaceYPosXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, EAST, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;

		setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMin, BOTTOM_LEFT);
		setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, BOTTOM_RIGHT);
		setupVertex(renderBlocks, x + 0.5F, y + 0.5F, z + 0.5F, uMid, vMax, TOP_CENTER);
	}

}
