package carpentersblocks.renderer.helper;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;
import carpentersblocks.data.Collapsible;
import carpentersblocks.tileentity.TEBase;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

public class RenderHelperCollapsible extends RenderHelper {

	private static double offset_XZNN;
	private static double offset_XZNP;
	private static double offset_XZPN;
	private static double offset_XZPP;
	
	private static void computeOffsets(TEBase TE)
	{
		offset_XZNN = Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN) / 16.0D;
		offset_XZNP = Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) / 16.0D;
		offset_XZPN = Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN) / 16.0D;
		offset_XZPP = Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP) / 16.0D;
	}
	
	/**
	 * Renders the given texture to the top face of the block.
	 */
	public static void renderFaceYPos(TEBase TE, RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, UP, x, y, z);
        setUV(renderBlocks, UP, renderBlocks.uvRotateTop, icon);
        computeOffsets(TE);

        setupVertex(renderBlocks, xMax, yMin + offset_XZPP, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], SOUTHEAST);
        setupVertex(renderBlocks, xMax, yMin + offset_XZPN, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], NORTHEAST);
        setupVertex(renderBlocks, xMin, yMin + offset_XZNN, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], NORTHWEST);
        setupVertex(renderBlocks, xMin, yMin + offset_XZNP, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], SOUTHWEST);
	}

	/**
	 * Renders the given texture to the North face of the block.
	 */
	public static void renderFaceZNeg(TEBase TE, RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{        
		setBounds(renderBlocks, NORTH, x, y, z);
		setUV(renderBlocks, NORTH, renderBlocks.uvRotateNorth, icon);
        computeOffsets(TE);

        double VL_XZNN = UV_NORTH[renderBlocks.uvRotateNorth][3][1];
        double VL_XZPN = UV_NORTH[renderBlocks.uvRotateNorth][2][1];
		double VH_XZNN = UV_NORTH[renderBlocks.uvRotateNorth][0][1];
		double VH_XZPN = UV_NORTH[renderBlocks.uvRotateNorth][1][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZNN = UV_NORTH[renderBlocks.uvRotateNorth][3][1] + (UV_NORTH[renderBlocks.uvRotateNorth][0][1] - UV_NORTH[renderBlocks.uvRotateNorth][3][1]) * offset_XZNN;
			VH_XZPN = UV_NORTH[renderBlocks.uvRotateNorth][2][1] + (UV_NORTH[renderBlocks.uvRotateNorth][1][1] - UV_NORTH[renderBlocks.uvRotateNorth][2][1]) * offset_XZPN;
		} else {
			VL_XZNN = UV_NORTH[renderBlocks.uvRotateNorth][0][1] - (UV_NORTH[renderBlocks.uvRotateNorth][0][1] - UV_NORTH[renderBlocks.uvRotateNorth][3][1]) * offset_XZNN;
			VL_XZPN = UV_NORTH[renderBlocks.uvRotateNorth][1][1] - (UV_NORTH[renderBlocks.uvRotateNorth][1][1] - UV_NORTH[renderBlocks.uvRotateNorth][2][1]) * offset_XZPN;
		}

		setupVertex(renderBlocks, xMin, yMin + offset_XZNN, zMin, UV_NORTH[renderBlocks.uvRotateNorth][0][0], VH_XZNN, TOP_RIGHT);
		setupVertex(renderBlocks, xMax, yMin + offset_XZPN, zMin, UV_NORTH[renderBlocks.uvRotateNorth][1][0], VH_XZPN, TOP_LEFT);
		setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][2][0], VL_XZPN, BOTTOM_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateNorth][3][0], VL_XZNN, BOTTOM_RIGHT);
	}

	/**
	 * Renders the given texture to the South face of the block.
	 */
	public static void renderFaceZPos(TEBase TE, RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, SOUTH, x, y, z);
		setUV(renderBlocks, SOUTH, renderBlocks.uvRotateSouth, icon);
        computeOffsets(TE);

        double VL_XZNP = UV_SOUTH[renderBlocks.uvRotateSouth][1][1];
        double VL_XZPP = UV_SOUTH[renderBlocks.uvRotateSouth][2][1];
		double VH_XZNP = UV_SOUTH[renderBlocks.uvRotateSouth][0][1];
		double VH_XZPP = UV_SOUTH[renderBlocks.uvRotateSouth][3][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZNP = UV_SOUTH[renderBlocks.uvRotateSouth][1][1] + (UV_SOUTH[renderBlocks.uvRotateSouth][0][1] - UV_SOUTH[renderBlocks.uvRotateSouth][1][1]) * offset_XZNP;
			VH_XZPP = UV_SOUTH[renderBlocks.uvRotateSouth][2][1] + (UV_SOUTH[renderBlocks.uvRotateSouth][3][1] - UV_SOUTH[renderBlocks.uvRotateSouth][2][1]) * offset_XZPP;
		} else {
			VL_XZNP = UV_SOUTH[renderBlocks.uvRotateSouth][0][1] - (UV_SOUTH[renderBlocks.uvRotateSouth][0][1] - UV_SOUTH[renderBlocks.uvRotateSouth][1][1]) * offset_XZNP;
			VL_XZPP = UV_SOUTH[renderBlocks.uvRotateSouth][3][1] - (UV_SOUTH[renderBlocks.uvRotateSouth][3][1] - UV_SOUTH[renderBlocks.uvRotateSouth][2][1]) * offset_XZPP;
		}
		
		setupVertex(renderBlocks, xMin, yMin + offset_XZNP, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][0][0], VH_XZNP, TOP_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][1][0], VL_XZNP, BOTTOM_LEFT);
		setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][2][0], VL_XZPP, BOTTOM_RIGHT);
		setupVertex(renderBlocks, xMax, yMin + offset_XZPP, zMax, UV_SOUTH[renderBlocks.uvRotateSouth][3][0], VH_XZPP, TOP_RIGHT);
	}

	/**
	 * Renders the given texture to the West face of the block.
	 */
	public static void renderFaceXNeg(TEBase TE, RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, WEST, x, y, z);
		setUV(renderBlocks, WEST, renderBlocks.uvRotateWest, icon);
        computeOffsets(TE);

        double VL_XZNP = UV_WEST[renderBlocks.uvRotateWest][3][1];
        double VL_XZNN = UV_WEST[renderBlocks.uvRotateWest][2][1];
		double VH_XZNP = UV_WEST[renderBlocks.uvRotateWest][0][1];
		double VH_XZNN = UV_WEST[renderBlocks.uvRotateWest][1][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZNP = UV_WEST[renderBlocks.uvRotateWest][3][1] + (UV_WEST[renderBlocks.uvRotateWest][0][1] - UV_WEST[renderBlocks.uvRotateWest][3][1]) * offset_XZNP;
			VH_XZNN = UV_WEST[renderBlocks.uvRotateWest][2][1] + (UV_WEST[renderBlocks.uvRotateWest][1][1] - UV_WEST[renderBlocks.uvRotateWest][2][1]) * offset_XZNN;
		} else {
			VL_XZNP = UV_WEST[renderBlocks.uvRotateWest][0][1] - (UV_WEST[renderBlocks.uvRotateWest][0][1] - UV_WEST[renderBlocks.uvRotateWest][3][1]) * offset_XZNP;
			VL_XZNN = UV_WEST[renderBlocks.uvRotateWest][1][1] - (UV_WEST[renderBlocks.uvRotateWest][1][1] - UV_WEST[renderBlocks.uvRotateWest][2][1]) * offset_XZNN;
		}
		
		setupVertex(renderBlocks, xMin, yMin + offset_XZNP, zMax, UV_WEST[renderBlocks.uvRotateWest][0][0], VH_XZNP, TOP_RIGHT);
		setupVertex(renderBlocks, xMin, yMin + offset_XZNN, zMin, UV_WEST[renderBlocks.uvRotateWest][1][0], VH_XZNN, TOP_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateWest][2][0], VL_XZNN, BOTTOM_LEFT);
		setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateWest][3][0], VL_XZNP, BOTTOM_RIGHT);
	}

	/**
	 * Renders the given texture to the East face of the block.
	 */
	public static void renderFaceXPos(TEBase TE, RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, EAST, x, y, z);
		setUV(renderBlocks, EAST, renderBlocks.uvRotateEast, icon);
        computeOffsets(TE);

        double VL_XZPN = UV_EAST[renderBlocks.uvRotateEast][1][1];
        double VL_XZPP = UV_EAST[renderBlocks.uvRotateEast][0][1];
		double VH_XZPN = UV_EAST[renderBlocks.uvRotateEast][2][1];
		double VH_XZPP = UV_EAST[renderBlocks.uvRotateEast][3][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZPN = UV_EAST[renderBlocks.uvRotateEast][1][1] + (UV_EAST[renderBlocks.uvRotateEast][2][1] - UV_EAST[renderBlocks.uvRotateEast][1][1]) * offset_XZPN;
			VH_XZPP = UV_EAST[renderBlocks.uvRotateEast][0][1] + (UV_EAST[renderBlocks.uvRotateEast][3][1] - UV_EAST[renderBlocks.uvRotateEast][0][1]) * offset_XZPP;
		} else {
			VL_XZPN = UV_EAST[renderBlocks.uvRotateEast][2][1] - (UV_EAST[renderBlocks.uvRotateEast][2][1] - UV_EAST[renderBlocks.uvRotateEast][1][1]) * offset_XZPN;
			VL_XZPP = UV_EAST[renderBlocks.uvRotateEast][3][1] - (UV_EAST[renderBlocks.uvRotateEast][3][1] - UV_EAST[renderBlocks.uvRotateEast][0][1]) * offset_XZPP;
		}
		
		setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateEast][0][0], VL_XZPP, BOTTOM_LEFT);
		setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateEast][1][0], VL_XZPN, BOTTOM_RIGHT);
		setupVertex(renderBlocks, xMax, yMin + offset_XZPN, zMin, UV_EAST[renderBlocks.uvRotateEast][2][0], VH_XZPN, TOP_RIGHT);
		setupVertex(renderBlocks, xMax, yMin + offset_XZPP, zMax, UV_EAST[renderBlocks.uvRotateEast][3][0], VH_XZPP, TOP_LEFT);
	}
	
}