package carpentersblocks.renderer.helper;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import carpentersblocks.util.collapsible.CollapsibleUtil;

public class RenderHelperCollapsible extends RenderHelper {

	/**
	 * Renders the given texture to the top North slope.
	 */
	public static void renderSlopeYPosZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, UP, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;

		setupVertex(renderBlocks, xMax - 0.5F, yMin + CollapsibleUtil.CENTER_YMAX, zMax, uMid, UV_UP[renderBlocks.uvRotateTop][0][1], BOTTOM_CENTER);
		setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPN, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], NORTHEAST);
		setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNN, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], NORTHWEST);
	}

	/**
	 * Renders the given texture to the top South slope.
	 */
	public static void renderSlopeYPosZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, UP, x, y, z, icon);

		double uMid = uMax - (uMax - uMin) / 2;

		setupVertex(renderBlocks, xMin + 0.5F, yMin + CollapsibleUtil.CENTER_YMAX, zMin, uMid, UV_UP[renderBlocks.uvRotateTop][1][1], TOP_CENTER);
		setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNP, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], SOUTHWEST);
		setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPP, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], SOUTHEAST);
	}

	/**
	 * Renders the given texture to the top West slope.
	 */
	public static void renderSlopeXNegYPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, UP, x, y, z, icon);

		double vMid = vMax - (vMax - vMin) / 2;

		setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.CENTER_YMAX, zMin + 0.5F, UV_UP[renderBlocks.uvRotateTop][1][0], vMid, RIGHT_CENTER);
		setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNN, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], NORTHWEST);
		setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNP, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], SOUTHWEST);
	}

	/**
	 * Renders the given texture to the top East slope.
	 */
	public static void renderSlopeXPosYPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, UP, x, y, z, icon);

		double vMid = vMax - (vMax - vMin) / 2;

		setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.CENTER_YMAX, zMax - 0.5F, UV_UP[renderBlocks.uvRotateTop][3][0], vMid, LEFT_CENTER);
		setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPP, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], SOUTHEAST);
		setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPN, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], NORTHEAST);
	}

	/**
	 * Renders the given texture to the North face of the block.
	 */
	public static void renderFaceZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, NORTH, x, y, z, icon);

		double VL_XZNN = UV_NORTH[renderBlocks.uvRotateNorth][3][1];
		double VL_XZPN = UV_NORTH[renderBlocks.uvRotateNorth][2][1];
		double VH_XZNN = UV_NORTH[renderBlocks.uvRotateNorth][0][1];
		double VH_XZPN = UV_NORTH[renderBlocks.uvRotateNorth][1][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZNN = UV_NORTH[renderBlocks.uvRotateNorth][3][1] + (UV_NORTH[renderBlocks.uvRotateNorth][0][1] - UV_NORTH[renderBlocks.uvRotateNorth][3][1]) * CollapsibleUtil.offset_XZNN;
			VH_XZPN = UV_NORTH[renderBlocks.uvRotateNorth][2][1] + (UV_NORTH[renderBlocks.uvRotateNorth][1][1] - UV_NORTH[renderBlocks.uvRotateNorth][2][1]) * CollapsibleUtil.offset_XZPN;
		} else {
			VL_XZNN = UV_NORTH[renderBlocks.uvRotateNorth][0][1] - (UV_NORTH[renderBlocks.uvRotateNorth][0][1] - UV_NORTH[renderBlocks.uvRotateNorth][3][1]) * CollapsibleUtil.offset_XZNN;
			VL_XZPN = UV_NORTH[renderBlocks.uvRotateNorth][1][1] - (UV_NORTH[renderBlocks.uvRotateNorth][1][1] - UV_NORTH[renderBlocks.uvRotateNorth][2][1]) * CollapsibleUtil.offset_XZPN;
		}

		setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNN, zMin, UV_NORTH[renderBlocks.uvRotateNorth][0][0], VH_XZNN, TOP_RIGHT);
		setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPN, zMin, UV_NORTH[renderBlocks.uvRotateNorth][1][0], VH_XZPN, TOP_LEFT);
		setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][2][0], VL_XZPN, BOTTOM_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][3][0], VL_XZNN, BOTTOM_RIGHT);
	}

	/**
	 * Renders the given texture to the South face of the block.
	 */
	public static void renderFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, SOUTH, x, y, z, icon);

		double VL_XZNP = UV_SOUTH[renderBlocks.uvRotateSouth][1][1];
		double VL_XZPP = UV_SOUTH[renderBlocks.uvRotateSouth][2][1];
		double VH_XZNP = UV_SOUTH[renderBlocks.uvRotateSouth][0][1];
		double VH_XZPP = UV_SOUTH[renderBlocks.uvRotateSouth][3][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZNP = UV_SOUTH[renderBlocks.uvRotateSouth][1][1] + (UV_SOUTH[renderBlocks.uvRotateSouth][0][1] - UV_SOUTH[renderBlocks.uvRotateSouth][1][1]) * CollapsibleUtil.offset_XZNP;
			VH_XZPP = UV_SOUTH[renderBlocks.uvRotateSouth][2][1] + (UV_SOUTH[renderBlocks.uvRotateSouth][3][1] - UV_SOUTH[renderBlocks.uvRotateSouth][2][1]) * CollapsibleUtil.offset_XZPP;
		} else {
			VL_XZNP = UV_SOUTH[renderBlocks.uvRotateSouth][0][1] - (UV_SOUTH[renderBlocks.uvRotateSouth][0][1] - UV_SOUTH[renderBlocks.uvRotateSouth][1][1]) * CollapsibleUtil.offset_XZNP;
			VL_XZPP = UV_SOUTH[renderBlocks.uvRotateSouth][3][1] - (UV_SOUTH[renderBlocks.uvRotateSouth][3][1] - UV_SOUTH[renderBlocks.uvRotateSouth][2][1]) * CollapsibleUtil.offset_XZPP;
		}

		setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNP, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][0][0], VH_XZNP, TOP_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][1][0], VL_XZNP, BOTTOM_LEFT);
		setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][2][0], VL_XZPP, BOTTOM_RIGHT);
		setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPP, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][3][0], VH_XZPP, TOP_RIGHT);
	}

	/**
	 * Renders the given texture to the West face of the block.
	 */
	public static void renderFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, WEST, x, y, z, icon);

		double VL_XZNP = UV_WEST[renderBlocks.uvRotateWest][3][1];
		double VL_XZNN = UV_WEST[renderBlocks.uvRotateWest][2][1];
		double VH_XZNP = UV_WEST[renderBlocks.uvRotateWest][0][1];
		double VH_XZNN = UV_WEST[renderBlocks.uvRotateWest][1][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZNP = UV_WEST[renderBlocks.uvRotateWest][3][1] + (UV_WEST[renderBlocks.uvRotateWest][0][1] - UV_WEST[renderBlocks.uvRotateWest][3][1]) * CollapsibleUtil.offset_XZNP;
			VH_XZNN = UV_WEST[renderBlocks.uvRotateWest][2][1] + (UV_WEST[renderBlocks.uvRotateWest][1][1] - UV_WEST[renderBlocks.uvRotateWest][2][1]) * CollapsibleUtil.offset_XZNN;
		} else {
			VL_XZNP = UV_WEST[renderBlocks.uvRotateWest][0][1] - (UV_WEST[renderBlocks.uvRotateWest][0][1] - UV_WEST[renderBlocks.uvRotateWest][3][1]) * CollapsibleUtil.offset_XZNP;
			VL_XZNN = UV_WEST[renderBlocks.uvRotateWest][1][1] - (UV_WEST[renderBlocks.uvRotateWest][1][1] - UV_WEST[renderBlocks.uvRotateWest][2][1]) * CollapsibleUtil.offset_XZNN;
		}

		setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNP, zMax, UV_WEST[renderBlocks.uvRotateWest][0][0], VH_XZNP, TOP_RIGHT);
		setupVertex(renderBlocks, xMin, yMin + CollapsibleUtil.offset_XZNN, zMin, UV_WEST[renderBlocks.uvRotateWest][1][0], VH_XZNN, TOP_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateWest][2][0], VL_XZNN, BOTTOM_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateWest][3][0], VL_XZNP, BOTTOM_RIGHT);
	}

	/**
	 * Renders the given texture to the East face of the block.
	 */
	public static void renderFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		prepareRender(renderBlocks, EAST, x, y, z, icon);

		double VL_XZPN = UV_EAST[renderBlocks.uvRotateEast][1][1];
		double VL_XZPP = UV_EAST[renderBlocks.uvRotateEast][0][1];
		double VH_XZPN = UV_EAST[renderBlocks.uvRotateEast][2][1];
		double VH_XZPP = UV_EAST[renderBlocks.uvRotateEast][3][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZPN = UV_EAST[renderBlocks.uvRotateEast][1][1] + (UV_EAST[renderBlocks.uvRotateEast][2][1] - UV_EAST[renderBlocks.uvRotateEast][1][1]) * CollapsibleUtil.offset_XZPN;
			VH_XZPP = UV_EAST[renderBlocks.uvRotateEast][0][1] + (UV_EAST[renderBlocks.uvRotateEast][3][1] - UV_EAST[renderBlocks.uvRotateEast][0][1]) * CollapsibleUtil.offset_XZPP;
		} else {
			VL_XZPN = UV_EAST[renderBlocks.uvRotateEast][2][1] - (UV_EAST[renderBlocks.uvRotateEast][2][1] - UV_EAST[renderBlocks.uvRotateEast][1][1]) * CollapsibleUtil.offset_XZPN;
			VL_XZPP = UV_EAST[renderBlocks.uvRotateEast][3][1] - (UV_EAST[renderBlocks.uvRotateEast][3][1] - UV_EAST[renderBlocks.uvRotateEast][0][1]) * CollapsibleUtil.offset_XZPP;
		}

		setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateEast][0][0], VL_XZPP, BOTTOM_LEFT);
		setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateEast][1][0], VL_XZPN, BOTTOM_RIGHT);
		setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPN, zMin, UV_EAST[renderBlocks.uvRotateEast][2][0], VH_XZPN, TOP_RIGHT);
		setupVertex(renderBlocks, xMax, yMin + CollapsibleUtil.offset_XZPP, zMax, UV_EAST[renderBlocks.uvRotateEast][3][0], VH_XZPP, TOP_LEFT);
	}

}