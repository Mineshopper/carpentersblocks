package carpentersblocks.renderer.helper;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;
import carpentersblocks.data.Collapsible;
import carpentersblocks.tileentity.TECarpentersBlock;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

public class RenderHelperCollapsible extends RenderHelper
{

	private static double offset_XZNN;
	private static double offset_XZNP;
	private static double offset_XZPN;
	private static double offset_XZPP;
	
	private static void computeOffsets(TECarpentersBlock TE)
	{
		offset_XZNN = Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN) / 16.0D;
		offset_XZNP = Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) / 16.0D;
		offset_XZPN = Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN) / 16.0D;
		offset_XZPP = Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP) / 16.0D;
	}
	
	/**
	 * Renders the given texture to the top face of the block.
	 */
	public static void renderFaceYPos(TECarpentersBlock TE, RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, UP, x, y, z);
        setUV(renderBlocks, UP, renderBlocks.uvRotateTop, icon);
        computeOffsets(TE);

        setupVertex(renderBlocks, xMax, yMin + offset_XZPP, zMax, UV_UP[renderBlocks.uvRotateTop][0][0], UV_UP[renderBlocks.uvRotateTop][0][1], 0);
        setupVertex(renderBlocks, xMax, yMin + offset_XZPN, zMin, UV_UP[renderBlocks.uvRotateTop][1][0], UV_UP[renderBlocks.uvRotateTop][1][1], 1);
        setupVertex(renderBlocks, xMin, yMin + offset_XZNN, zMin, UV_UP[renderBlocks.uvRotateTop][2][0], UV_UP[renderBlocks.uvRotateTop][2][1], 2);
        setupVertex(renderBlocks, xMin, yMin + offset_XZNP, zMax, UV_UP[renderBlocks.uvRotateTop][3][0], UV_UP[renderBlocks.uvRotateTop][3][1], 3);
	}

	/**
	 * Renders the given texture to the North face of the block.
	 */
	public static void renderFaceZNeg(TECarpentersBlock TE, RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{        
		setBounds(renderBlocks, NORTH, x, y, z);
		setUV(renderBlocks, NORTH, renderBlocks.uvRotateEast, icon);
        computeOffsets(TE);

        double VL_XZNN = UV_NORTH[renderBlocks.uvRotateEast][3][1];
        double VL_XZPN = UV_NORTH[renderBlocks.uvRotateEast][2][1];
		double VH_XZNN = UV_NORTH[renderBlocks.uvRotateEast][0][1];
		double VH_XZPN = UV_NORTH[renderBlocks.uvRotateEast][1][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZNN = UV_NORTH[renderBlocks.uvRotateEast][3][1] + (UV_NORTH[renderBlocks.uvRotateEast][0][1] - UV_NORTH[renderBlocks.uvRotateEast][3][1]) * offset_XZNN;
			VH_XZPN = UV_NORTH[renderBlocks.uvRotateEast][2][1] + (UV_NORTH[renderBlocks.uvRotateEast][1][1] - UV_NORTH[renderBlocks.uvRotateEast][2][1]) * offset_XZPN;
		} else {
			VL_XZNN = UV_NORTH[renderBlocks.uvRotateEast][0][1] - (UV_NORTH[renderBlocks.uvRotateEast][0][1] - UV_NORTH[renderBlocks.uvRotateEast][3][1]) * offset_XZNN;
			VL_XZPN = UV_NORTH[renderBlocks.uvRotateEast][1][1] - (UV_NORTH[renderBlocks.uvRotateEast][1][1] - UV_NORTH[renderBlocks.uvRotateEast][2][1]) * offset_XZPN;
		}

		setupVertex(renderBlocks, xMin, yMin + offset_XZNN, zMin, UV_NORTH[renderBlocks.uvRotateEast][0][0], VH_XZNN, 0);
		setupVertex(renderBlocks, xMax, yMin + offset_XZPN, zMin, UV_NORTH[renderBlocks.uvRotateEast][1][0], VH_XZPN, 1);
		setupVertex(renderBlocks, xMax, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][2][0], VL_XZPN, 2);
		setupVertex(renderBlocks, xMin, yMin, zMin, UV_NORTH[renderBlocks.uvRotateEast][3][0], VL_XZNN, 3);
	}

	/**
	 * Renders the given texture to the South face of the block.
	 */
	public static void renderFaceZPos(TECarpentersBlock TE, RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, SOUTH, x, y, z);
		setUV(renderBlocks, SOUTH, renderBlocks.uvRotateWest, icon);
        computeOffsets(TE);

        double VL_XZNP = UV_SOUTH[renderBlocks.uvRotateWest][1][1];
        double VL_XZPP = UV_SOUTH[renderBlocks.uvRotateWest][2][1];
		double VH_XZNP = UV_SOUTH[renderBlocks.uvRotateWest][0][1];
		double VH_XZPP = UV_SOUTH[renderBlocks.uvRotateWest][3][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZNP = UV_SOUTH[renderBlocks.uvRotateWest][1][1] + (UV_SOUTH[renderBlocks.uvRotateWest][0][1] - UV_SOUTH[renderBlocks.uvRotateWest][1][1]) * offset_XZNP;
			VH_XZPP = UV_SOUTH[renderBlocks.uvRotateWest][2][1] + (UV_SOUTH[renderBlocks.uvRotateWest][3][1] - UV_SOUTH[renderBlocks.uvRotateWest][2][1]) * offset_XZPP;
		} else {
			VL_XZNP = UV_SOUTH[renderBlocks.uvRotateWest][0][1] - (UV_SOUTH[renderBlocks.uvRotateWest][0][1] - UV_SOUTH[renderBlocks.uvRotateWest][1][1]) * offset_XZNP;
			VL_XZPP = UV_SOUTH[renderBlocks.uvRotateWest][3][1] - (UV_SOUTH[renderBlocks.uvRotateWest][3][1] - UV_SOUTH[renderBlocks.uvRotateWest][2][1]) * offset_XZPP;
		}
		
		setupVertex(renderBlocks, xMin, yMin + offset_XZNP, zMax, UV_SOUTH[renderBlocks.uvRotateWest][0][0], VH_XZNP, 0);
		setupVertex(renderBlocks, xMin, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][1][0], VL_XZNP, 1);
		setupVertex(renderBlocks, xMax, yMin, zMax, UV_SOUTH[renderBlocks.uvRotateWest][2][0], VL_XZPP, 2);
		setupVertex(renderBlocks, xMax, yMin + offset_XZPP, zMax, UV_SOUTH[renderBlocks.uvRotateWest][3][0], VH_XZPP, 3);
	}

	/**
	 * Renders the given texture to the West face of the block.
	 */
	public static void renderFaceXNeg(TECarpentersBlock TE, RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, WEST, x, y, z);
		setUV(renderBlocks, WEST, renderBlocks.uvRotateNorth, icon);
        computeOffsets(TE);

        double VL_XZNP = UV_WEST[renderBlocks.uvRotateNorth][3][1];
        double VL_XZNN = UV_WEST[renderBlocks.uvRotateNorth][2][1];
		double VH_XZNP = UV_WEST[renderBlocks.uvRotateNorth][0][1];
		double VH_XZNN = UV_WEST[renderBlocks.uvRotateNorth][1][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZNP = UV_WEST[renderBlocks.uvRotateNorth][3][1] + (UV_WEST[renderBlocks.uvRotateNorth][0][1] - UV_WEST[renderBlocks.uvRotateNorth][3][1]) * offset_XZNP;
			VH_XZNN = UV_WEST[renderBlocks.uvRotateNorth][2][1] + (UV_WEST[renderBlocks.uvRotateNorth][1][1] - UV_WEST[renderBlocks.uvRotateNorth][2][1]) * offset_XZNN;
		} else {
			VL_XZNP = UV_WEST[renderBlocks.uvRotateNorth][0][1] - (UV_WEST[renderBlocks.uvRotateNorth][0][1] - UV_WEST[renderBlocks.uvRotateNorth][3][1]) * offset_XZNP;
			VL_XZNN = UV_WEST[renderBlocks.uvRotateNorth][1][1] - (UV_WEST[renderBlocks.uvRotateNorth][1][1] - UV_WEST[renderBlocks.uvRotateNorth][2][1]) * offset_XZNN;
		}
		
		setupVertex(renderBlocks, xMin, yMin + offset_XZNP, zMax, UV_WEST[renderBlocks.uvRotateNorth][0][0], VH_XZNP, 0);
		setupVertex(renderBlocks, xMin, yMin + offset_XZNN, zMin, UV_WEST[renderBlocks.uvRotateNorth][1][0], VH_XZNN, 1);
		setupVertex(renderBlocks, xMin, yMin, zMin, UV_WEST[renderBlocks.uvRotateNorth][2][0], VL_XZNN, 2);
		setupVertex(renderBlocks, xMin, yMin, zMax, UV_WEST[renderBlocks.uvRotateNorth][3][0], VL_XZNP, 3);
	}

	/**
	 * Renders the given texture to the East face of the block.
	 */
	public static void renderFaceXPos(TECarpentersBlock TE, RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		setBounds(renderBlocks, EAST, x, y, z);
		setUV(renderBlocks, EAST, renderBlocks.uvRotateSouth, icon);
        computeOffsets(TE);

        double VL_XZPN = UV_EAST[renderBlocks.uvRotateSouth][1][1];
        double VL_XZPP = UV_EAST[renderBlocks.uvRotateSouth][0][1];
		double VH_XZPN = UV_EAST[renderBlocks.uvRotateSouth][2][1];
		double VH_XZPP = UV_EAST[renderBlocks.uvRotateSouth][3][1];

		if (!iconHasFloatingHeight(icon)) {
			VH_XZPN = UV_EAST[renderBlocks.uvRotateSouth][1][1] + (UV_EAST[renderBlocks.uvRotateSouth][2][1] - UV_EAST[renderBlocks.uvRotateSouth][1][1]) * offset_XZPN;
			VH_XZPP = UV_EAST[renderBlocks.uvRotateSouth][0][1] + (UV_EAST[renderBlocks.uvRotateSouth][3][1] - UV_EAST[renderBlocks.uvRotateSouth][0][1]) * offset_XZPP;
		} else {
			VL_XZPN = UV_EAST[renderBlocks.uvRotateSouth][2][1] - (UV_EAST[renderBlocks.uvRotateSouth][2][1] - UV_EAST[renderBlocks.uvRotateSouth][1][1]) * offset_XZPN;
			VL_XZPP = UV_EAST[renderBlocks.uvRotateSouth][3][1] - (UV_EAST[renderBlocks.uvRotateSouth][3][1] - UV_EAST[renderBlocks.uvRotateSouth][0][1]) * offset_XZPP;
		}
		
		setupVertex(renderBlocks, xMax, yMin, zMax, UV_EAST[renderBlocks.uvRotateSouth][0][0], VL_XZPP, 0);
		setupVertex(renderBlocks, xMax, yMin, zMin, UV_EAST[renderBlocks.uvRotateSouth][1][0], VL_XZPN, 1);
		setupVertex(renderBlocks, xMax, yMin + offset_XZPN, zMin, UV_EAST[renderBlocks.uvRotateSouth][2][0], VH_XZPN, 2);
		setupVertex(renderBlocks, xMax, yMin + offset_XZPP, zMax, UV_EAST[renderBlocks.uvRotateSouth][3][0], VH_XZPP, 3);
	}
	
}