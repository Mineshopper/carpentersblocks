package carpentersblocks.renderer.helper;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

public class RenderHelper extends VertexHelper {

	protected static Tessellator tessellator = Tessellator.instance;

	/** Tessellator draw mode for triangles. */
	public final static int TRIANGLES = 4;
	public final static int QUADS = 7;

	protected static double uMin;
	protected static double uMax;
	protected static double vMin;
	protected static double vMax;

	protected static double xMin;
	protected static double xMax;
	protected static double yMin;
	protected static double yMax;
	protected static double zMin;
	protected static double zMax;

	/*
	 * Provides UV for all face corners.
	 * [ROTATION][VERTEX][U/V]
	 */
	protected static double[][][] UV_DOWN;
	protected static double[][][] UV_UP;
	protected static double[][][] UV_NORTH;
	protected static double[][][] UV_SOUTH;
	protected static double[][][] UV_WEST;
	protected static double[][][] UV_EAST;

	/**
	 * Sets draw mode in tessellator.
	 */
	public static void startDrawing(int drawMode)
	{
		Tessellator.instance.draw();
		Tessellator.instance.startDrawing(drawMode);
	}

	/**
	 * Sets side icon UV with rotation.
	 * 
	 * Keep in mind that renderBlocks.uvRotateX directions don't match Forge directions,
	 * and this will not mirror some rotations like vanilla does (which is a bug).
	 */
	protected static void setUV(RenderBlocks renderBlocks, ForgeDirection side, int rotation, Icon icon)
	{
		switch (side)
		{
		case DOWN:

			switch (rotation)
			{
			case 0:
				uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);
				break;
			case 1:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);
				break;
			case 2:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);
				break;
			case 3:
				uMin = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);
				break;
			}

			UV_DOWN = new double[][][] {
					{{ uMin, vMax }, { uMin, vMin }, { uMax, vMin }, { uMax, vMax }},
					{{ uMin, vMin }, { uMax, vMin }, { uMax, vMax }, { uMin, vMax }},
					{{ uMin, vMax }, { uMin, vMin }, { uMax, vMin }, { uMax, vMax }},
					{{ uMin, vMin }, { uMax, vMin }, { uMax, vMax }, { uMin, vMax }}
			};

			break;
		case UP:

			switch (rotation)
			{
			case 0:
				uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);
				break;
			case 1:
				uMin = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);
				break;
			case 2:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);
				break;
			case 3:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);
				break;
			}

			UV_UP = new double[][][] {
					{{ uMax, vMax }, { uMax, vMin }, { uMin, vMin }, { uMin, vMax }},
					{{ uMin, vMax }, { uMax, vMax }, { uMax, vMin }, { uMin, vMin }},
					{{ uMax, vMax }, { uMax, vMin }, { uMin, vMin }, { uMin, vMax }},
					{{ uMin, vMax }, { uMax, vMax }, { uMax, vMin }, { uMin, vMin }}
			};

			break;
		case NORTH:

			/*
			 * A note regarding this side rotation:
			 * 
			 * Vanilla rotation rotates and mirrors this side, whereas mine simply rotates.
			 * This bug is easily detectable with fences and other objects that sometimes
			 * rotate and cause texture alignment problems.
			 */

			switch (rotation)
			{
			case 0:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY) : renderBlocks.renderMinY) * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
				break;
			case 1:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);
				break;
			case 2:
				uMin = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);
				break;
			case 3:
				uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);
				break;
			}

			UV_NORTH = new double[][][] {
					{{ uMax, vMax }, { uMin, vMax }, { uMin, vMin }, { uMax, vMin }},
					{{ uMin, vMax }, { uMin, vMin }, { uMax, vMin }, { uMax, vMax }},
					{{ uMax, vMax }, { uMin, vMax }, { uMin, vMin }, { uMax, vMin }},
					{{ uMin, vMax }, { uMin, vMin }, { uMax, vMin }, { uMax, vMax }}
			};

			break;
		case SOUTH:

			switch (rotation)
			{
			case 0:
				uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY) : renderBlocks.renderMinY) * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
				break;
			case 1:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);
				break;
			case 2:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);
				break;
			case 3:
				uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);
				break;
			}

			UV_SOUTH = new double[][][] {
					{{ uMin, vMax }, { uMin, vMin }, { uMax, vMin }, { uMax, vMax }},
					{{ uMin, vMin }, { uMax, vMin }, { uMax, vMax }, { uMin, vMax }},
					{{ uMin, vMax }, { uMin, vMin }, { uMax, vMin }, { uMax, vMax }},
					{{ uMin, vMin }, { uMax, vMin }, { uMax, vMax }, { uMin, vMax }}
			};

			break;
		case WEST:

			switch (rotation)
			{
			case 0:
				uMin = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY) : renderBlocks.renderMinY) * 16.0D);
				break;
			case 1:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);
				break;
			case 2:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);
				break;
			case 3:
				uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);
				break;
			}

			UV_WEST = new double[][][] {
					{{ uMax, vMax }, { uMin, vMax }, { uMin, vMin }, { uMax, vMin }},
					{{ uMin, vMax }, { uMin, vMin }, { uMax, vMin }, { uMax, vMax }},
					{{ uMax, vMax }, { uMin, vMax }, { uMin, vMin }, { uMax, vMin }},
					{{ uMin, vMax }, { uMin, vMin }, { uMax, vMin }, { uMax, vMax }}
			};

			break;
		case EAST:

			/*
			 * A note regarding this side rotation:
			 * 
			 * Vanilla rotation rotates and mirrors this side, whereas mine simply rotates.
			 * This bug is easily detectable with fences and other objects that sometimes
			 * rotate and cause texture alignment problems.
			 */

			switch (rotation)
			{
			case 0:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY) : renderBlocks.renderMinY) * 16.0D);
				break;
			case 1:
				uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
				uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
				break;
			case 2:
				uMin = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
				vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
				vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);
				break;
			case 3:
				uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
				uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
				vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);
				vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);
				break;
			}

			UV_EAST = new double[][][] {
					{{ uMin, vMin }, { uMax, vMin }, { uMax, vMax }, { uMin, vMax }},
					{{ uMax, vMin }, { uMax, vMax }, { uMin, vMax }, { uMin, vMin }},
					{{ uMin, vMin }, { uMax, vMin }, { uMax, vMax }, { uMin, vMax }},
					{{ uMax, vMin }, { uMax, vMax }, { uMin, vMax }, { uMin, vMin }}
			};

			break;
		default: {}
		}
	}

	protected static void setBounds(RenderBlocks renderBlocks, ForgeDirection side, double x, double y, double z)
	{
		yMin = y + renderBlocks.renderMinY - (side.equals(DOWN) ? offset : 0);
		yMax = y + renderBlocks.renderMaxY + (side.equals(UP) ? offset : 0);
		zMin = z + renderBlocks.renderMinZ - (side.equals(NORTH) ? offset : 0);
		zMax = z + renderBlocks.renderMaxZ + (side.equals(SOUTH) ? offset : 0);
		xMin = x + renderBlocks.renderMinX - (side.equals(WEST) ? offset : 0);
		xMax = x + renderBlocks.renderMaxX + (side.equals(EAST) ? offset : 0);
	}

	/**
	 * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, DOWN, x, y, z);
		setUV(renderBlocks, DOWN, renderBlocks.uvRotateBottom, icon);

		setupVertex(renderBlocks, xMin, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][0][0], UV_DOWN[renderBlocks.uvRotateBottom][0][1], SOUTHWEST);
		setupVertex(renderBlocks, xMin, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][1][0], UV_DOWN[renderBlocks.uvRotateBottom][1][1], NORTHWEST);
		setupVertex(renderBlocks, xMax, yMin, zMin, UV_DOWN[renderBlocks.uvRotateBottom][2][0], UV_DOWN[renderBlocks.uvRotateBottom][2][1], NORTHEAST);
		setupVertex(renderBlocks, xMax, yMin, zMax, UV_DOWN[renderBlocks.uvRotateBottom][3][0], UV_DOWN[renderBlocks.uvRotateBottom][3][1], SOUTHEAST);
	}

	/**
	 * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, UP, x, y, z);
		setUV(renderBlocks, UP, renderBlocks.uvRotateTop, icon);

		setupVertex(renderBlocks, xMax, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], SOUTHEAST);
		setupVertex(renderBlocks, xMax, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], NORTHEAST);
		setupVertex(renderBlocks, xMin, yMax, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], NORTHWEST);
		setupVertex(renderBlocks, xMin, yMax, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], SOUTHWEST);
	}

	/**
	 * Renders the given texture to the North face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, NORTH, x, y, z);
		setUV(renderBlocks, NORTH, renderBlocks.uvRotateNorth, icon);

		setupVertex(renderBlocks, xMin, yMax, zMin, UV_NORTH[renderBlocks.uvRotateNorth][0][0], UV_NORTH[renderBlocks.uvRotateNorth][0][1], TOP_RIGHT);
		setupVertex(renderBlocks, xMax, yMax, zMin, UV_NORTH[renderBlocks.uvRotateNorth][1][0], UV_NORTH[renderBlocks.uvRotateNorth][1][1], TOP_LEFT);
		setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][2][0], UV_NORTH[renderBlocks.uvRotateNorth][2][1], BOTTOM_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][3][0], UV_NORTH[renderBlocks.uvRotateNorth][3][1], BOTTOM_RIGHT);
	}

	/**
	 * Renders the given texture to the South face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, SOUTH, x, y, z);
		setUV(renderBlocks, SOUTH, renderBlocks.uvRotateSouth, icon);

		setupVertex(renderBlocks, xMin, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][0][0], UV_SOUTH[renderBlocks.uvRotateSouth][0][1], TOP_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][1][0], UV_SOUTH[renderBlocks.uvRotateSouth][1][1], BOTTOM_LEFT);
		setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][2][0], UV_SOUTH[renderBlocks.uvRotateSouth][2][1], BOTTOM_RIGHT);
		setupVertex(renderBlocks, xMax, yMax, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][3][0], UV_SOUTH[renderBlocks.uvRotateSouth][3][1], TOP_RIGHT);
	}

	/**
	 * Renders the given texture to the West face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, WEST, x, y, z);
		setUV(renderBlocks, WEST, renderBlocks.uvRotateWest, icon);

		setupVertex(renderBlocks, xMin, yMax, zMax, UV_WEST[renderBlocks.uvRotateWest][0][0], UV_WEST[renderBlocks.uvRotateWest][0][1], TOP_RIGHT);
		setupVertex(renderBlocks, xMin, yMax, zMin, UV_WEST[renderBlocks.uvRotateWest][1][0], UV_WEST[renderBlocks.uvRotateWest][1][1], TOP_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateWest][2][0], UV_WEST[renderBlocks.uvRotateWest][2][1], BOTTOM_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateWest][3][0], UV_WEST[renderBlocks.uvRotateWest][3][1], BOTTOM_RIGHT);
	}

	/**
	 * Renders the given texture to the East face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, EAST, x, y, z);
		setUV(renderBlocks, EAST, renderBlocks.uvRotateEast, icon);

		setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateEast][0][0], UV_EAST[renderBlocks.uvRotateEast][0][1], BOTTOM_LEFT);
		setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateEast][1][0], UV_EAST[renderBlocks.uvRotateEast][1][1], BOTTOM_RIGHT);
		setupVertex(renderBlocks, xMax, yMax, zMin, UV_EAST[renderBlocks.uvRotateEast][2][0], UV_EAST[renderBlocks.uvRotateEast][2][1], TOP_RIGHT);
		setupVertex(renderBlocks, xMax, yMax, zMax, UV_EAST[renderBlocks.uvRotateEast][3][0], UV_EAST[renderBlocks.uvRotateEast][3][1], TOP_LEFT);
	}

}