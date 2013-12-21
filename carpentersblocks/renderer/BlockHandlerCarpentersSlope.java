package carpentersblocks.renderer;

import static carpentersblocks.renderer.helper.RenderHelper.QUADS;
import static carpentersblocks.renderer.helper.RenderHelper.TRIANGLES;
import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_LEFT;
import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_RIGHT;
import static carpentersblocks.renderer.helper.VertexHelper.NORTHEAST;
import static carpentersblocks.renderer.helper.VertexHelper.NORTHWEST;
import static carpentersblocks.renderer.helper.VertexHelper.SOUTHEAST;
import static carpentersblocks.renderer.helper.VertexHelper.SOUTHWEST;
import static carpentersblocks.renderer.helper.VertexHelper.TOP_LEFT;
import static carpentersblocks.renderer.helper.VertexHelper.TOP_RIGHT;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import carpentersblocks.data.Slope;
import carpentersblocks.data.Slope.Type;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.slope.oblique.HelperCorner;
import carpentersblocks.renderer.helper.slope.oblique.HelperOblWedge;
import carpentersblocks.renderer.helper.slope.oblique.HelperOblique;
import carpentersblocks.renderer.helper.slope.oblique.HelperPrism;
import carpentersblocks.renderer.helper.slope.oblique.HelperPyramid;
import carpentersblocks.renderer.helper.slope.orthogonal.HelperOrthoWedge;
import carpentersblocks.renderer.helper.slope.orthogonal.HelperTriangle;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.registry.IconRegistry;

public class BlockHandlerCarpentersSlope extends BlockHandlerBase {

	/* RENDER IDS */
	private static final int NORMAL_YN 				= 0;
	private static final int NORMAL_YP 				= 1;
	private static final int NORMAL_ZN				= 2;
	private static final int NORMAL_ZP 				= 3;
	private static final int NORMAL_XN 				= 4;
	private static final int NORMAL_XP 				= 5;
	private static final int TRIANGLE_XN			= 6;
	private static final int TRIANGLE_XP			= 7;
	private static final int TRIANGLE_ZN			= 8;
	private static final int TRIANGLE_ZP			= 9;
	private static final int WEDGE_YN 				= 10;
	private static final int WEDGE_YP 				= 11;
	private static final int WEDGE_SLOPED_ZN 		= 12;
	private static final int WEDGE_ZN 				= 13;
	private static final int WEDGE_SLOPED_ZP		= 14;
	private static final int WEDGE_ZP 				= 15;
	private static final int WEDGE_SLOPED_XN 		= 16;
	private static final int WEDGE_XN 				= 17;
	private static final int WEDGE_SLOPED_XP 		= 18;
	private static final int WEDGE_XP 				= 19;
	private static final int WEDGE_CORNER_SLOPED_ZN = 20;
	private static final int WEDGE_CORNER_SLOPED_ZP = 21;
	private static final int WEDGE_CORNER_SLOPED_XN = 22;
	private static final int WEDGE_CORNER_SLOPED_XP = 23;
	private static final int OBL_CORNER_SLOPED_YN 	= 24;
	private static final int OBL_CORNER_SLOPED_YP 	= 25;
	private static final int PYR_YZNN 				= 26;
	private static final int PYR_YZNP 				= 27;
	private static final int PYR_YXNN 				= 28;
	private static final int PYR_YXNP 				= 29;
	private static final int PYR_YZPN 				= 30;
	private static final int PYR_YZPP 				= 31;
	private static final int PYR_YXPN 				= 32;
	private static final int PYR_YXPP 				= 33;
	private static final int PRISM_NORTH_XN 		= 34;
	private static final int PRISM_NORTH_XP 		= 35;
	private static final int PRISM_SOUTH_XN 		= 36;
	private static final int PRISM_SOUTH_XP 		= 37;
	private static final int PRISM_WEST_ZN 			= 38;
	private static final int PRISM_WEST_ZP 			= 39;
	private static final int PRISM_EAST_ZN 			= 40;
	private static final int PRISM_EAST_ZP 			= 41;
	private static final int PRISM_WEDGE_ZN			= 42;
	private static final int PRISM_WEDGE_ZP			= 43;
	private static final int PRISM_WEDGE_XN			= 44;
	private static final int PRISM_WEDGE_XP			= 45;

	private final float LIGHTNESS_YN 				= 0.5F;
	private final float LIGHTNESS_YP 				= 1.0F;
	private final float LIGHTNESS_ZN 				= 0.8F;
	private final float LIGHTNESS_ZP 				= 0.8F;
	private final float LIGHTNESS_XN 				= 0.6F;
	private final float LIGHTNESS_XP 				= 0.6F;
	private final float LIGHTNESS_XYNN 				= 0.55F;
	private final float LIGHTNESS_XYPN 				= 0.55F;
	private final float LIGHTNESS_ZYNN 				= 0.65F;
	private final float LIGHTNESS_ZYPN 				= 0.65F;
	private final float LIGHTNESS_XYNP 				= 0.8F;
	private final float LIGHTNESS_XYPP 				= 0.8F;
	private final float LIGHTNESS_ZYNP 				= 0.9F;
	private final float LIGHTNESS_ZYPP 				= 0.9F;
	private final float LIGHTNESS_SIDE_WEDGE 		= 0.7F;
	private final float LIGHTNESS_POS_OBL 			= 0.9F;
	private final float LIGHTNESS_NEG_OBL			= 0.55F;

	/** Identifies which render helper to use. */
	protected int slopeRenderID	= 0;

	/** Represents a value for all sides. */
	private final int SIDE_ALL = 6;

	/** Holds AO values for all six faces. */
	private float[][] ao = new float[6][4];

	/** Holds brightness values for all six faces. */
	private int[][] brightness = new int[6][4];

	/** Holds offset AO values for all six faces. */
	private float[][] offset_ao = new float[6][4];

	/** Holds offset brightness values for all six faces. */
	private int[][] offset_brightness = new int[6][4];

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks) {

		Tessellator tessellator = Tessellator.instance;
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		renderBlocks.setRenderBounds(0, 0, 0, 1.0D, 1.0D, 1.0D);

		tessellator.startDrawing(TRIANGLES);
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		HelperOrthoWedge.renderFaceZNeg(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, block.getIcon(2, EventHandler.BLOCKICON_BASE_ID));
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		HelperOrthoWedge.renderFaceZPos(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, block.getIcon(2, EventHandler.BLOCKICON_BASE_ID));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		RenderHelper.renderFaceYNeg(renderBlocks, 0.0D, 0.0D, 0.0D, block.getIcon(0, EventHandler.BLOCKICON_BASE_ID));
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		RenderHelper.renderFaceXPos(renderBlocks, 0.0D, 0.0D, 0.0D, block.getIcon(2, EventHandler.BLOCKICON_BASE_ID));
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		HelperOblWedge.renderSlopeXNeg(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, block.getIcon(1, EventHandler.BLOCKICON_BASE_ID));
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	/**
	 * Override to provide custom icons.
	 */
	protected Icon getUniqueIcon(Block block, int side, Icon icon)
	{
		if (isSideSloped)
		{
			Slope slope = Slope.slopesList[BlockProperties.getData(TE)];
			int metadata = BlockProperties.getCoverMetadata(TE, coverRendering);

			/* Uncovered sloped oblique faces use triangular frame icons. */
			if (!BlockProperties.hasCover(TE, 6))
			{
				if (slope.type.equals(Type.OBLIQUE_INT)) {
					icon = slope.isPositive ? IconRegistry.icon_slope_oblique_pt_low : IconRegistry.icon_slope_oblique_pt_high;
				} else if (slope.type.equals(Type.OBLIQUE_EXT)) {
					icon = slope.isPositive ? IconRegistry.icon_slope_oblique_pt_high : IconRegistry.icon_slope_oblique_pt_low;
				}
			}

			/* For directional blocks, make sure sloped icons match regardless of side. */
			if (BlockProperties.blockRotates(TE.worldObj, block, TE.xCoord, TE.yCoord, TE.zCoord)) {
				if (metadata % 8 == 0) {
					icon = block.getIcon(slope.isPositive ? 1 : 0, metadata);
				} else {
					icon = block.getIcon(2, metadata);
				}
			} else if (block instanceof BlockDirectional && !slope.type.equals(Type.WEDGE_Y)) {
				icon = block.getBlockTextureFromSide(1);
			}
		}

		return icon;
	}

	/**
	 * Applies full rendering bounds and calculates lighting for side.
	 * Side 6 represents all sides.
	 */
	private void setRenderBoundsAndRelight(Block block, int side, double xMin, double yMin, double zMin, double xMax, double yMax, double zMax)
	{
		renderBlocks.setRenderBounds(xMin, yMin, zMin, xMax, yMax, zMax);

		boolean allSides = side == SIDE_ALL;

		if (allSides || side == DOWN) {
			lightingHelper.setLightness(LIGHTNESS_YN).setLightingYNeg(block, TE.xCoord, TE.yCoord, TE.zCoord);
			if (renderBlocks.enableAO) {
				ao[DOWN] = new float[] { lightingHelper.ao[NORTHWEST], lightingHelper.ao[SOUTHWEST], lightingHelper.ao[SOUTHEAST], lightingHelper.ao[NORTHEAST] };
				brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
			}
		}

		if (allSides || side == UP) {
			lightingHelper.setLightness(LIGHTNESS_YP).setLightingYPos(block, TE.xCoord, TE.yCoord, TE.zCoord);
			if (renderBlocks.enableAO) {
				ao[UP] = new float[] { lightingHelper.ao[NORTHWEST], lightingHelper.ao[SOUTHWEST], lightingHelper.ao[SOUTHEAST], lightingHelper.ao[NORTHEAST] };
				brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
			}
		}

		if (allSides || side == NORTH) {
			lightingHelper.setLightness(LIGHTNESS_ZN).setLightingZNeg(block, TE.xCoord, TE.yCoord, TE.zCoord);
			if (renderBlocks.enableAO) {
				ao[NORTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
				brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
			}
		}

		if (allSides || side == SOUTH) {
			lightingHelper.setLightness(LIGHTNESS_ZP).setLightingZPos(block, TE.xCoord, TE.yCoord, TE.zCoord);
			if (renderBlocks.enableAO) {
				ao[SOUTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
				brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
			}
		}

		if (allSides || side == WEST) {
			lightingHelper.setLightness(LIGHTNESS_XN).setLightingXNeg(block, TE.xCoord, TE.yCoord, TE.zCoord);
			if (renderBlocks.enableAO) {
				ao[WEST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
				brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
			}
		}

		if (allSides || side == EAST) {
			lightingHelper.setLightness(LIGHTNESS_XP).setLightingXPos(block, TE.xCoord, TE.yCoord, TE.zCoord);
			if (renderBlocks.enableAO) {
				ao[EAST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
				brightness[EAST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
			}
		}
	}

	/**
	 * Fills separate AO and brightness tables for offset block coordinates.
	 */
	private void populateOffsetLighting(Block block, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			lightingHelper.setLightness(LIGHTNESS_YN).setLightingYNeg(block, x, y + 1, z);
			offset_ao[DOWN] = new float[] { lightingHelper.ao[NORTHWEST], lightingHelper.ao[SOUTHWEST], lightingHelper.ao[SOUTHEAST], lightingHelper.ao[NORTHEAST] };
			offset_brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };

			lightingHelper.setLightness(LIGHTNESS_YP).setLightingYPos(block, x, y - 1, z);
			offset_ao[UP] = new float[] { lightingHelper.ao[NORTHWEST], lightingHelper.ao[SOUTHWEST], lightingHelper.ao[SOUTHEAST], lightingHelper.ao[NORTHEAST] };
			offset_brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };

			lightingHelper.setLightness(LIGHTNESS_ZN).setLightingZNeg(block, x, y, z + 1);
			offset_ao[NORTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
			offset_brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };

			lightingHelper.setLightness(LIGHTNESS_ZP).setLightingZPos(block, x, y, z - 1);
			offset_ao[SOUTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
			offset_brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };

			lightingHelper.setLightness(LIGHTNESS_XN).setLightingXNeg(block, x + 1, y, z);
			offset_ao[WEST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
			offset_brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };

			lightingHelper.setLightness(LIGHTNESS_XP).setLightingXPos(block, x - 1, y, z);
			offset_ao[EAST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
			offset_brightness[EAST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
		}
	}

	/**
	 * Sets slope-specific renderID to identify which RenderHelper to use
	 * and passes control to delegateSideRender().
	 */
	private void setIDAndRender(Block block, int renderID, int x, int y, int z, int side)
	{
		slopeRenderID = renderID;
		delegateSideRender(block, x, y, z, side);
	}

	@Override
	/**
	 * Renders side.
	 */
	protected void renderSide(int x, int y, int z, int side, double offset, Icon icon)
	{
		if (coverRendering != 6) {

			super.renderSide(x, y, z, side, offset, icon);

		} else {

			int slopeID = BlockProperties.getData(TE);

			switch (slopeRenderID)
			{
			case NORMAL_YN:
				RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
				break;
			case NORMAL_YP:
				RenderHelper.renderFaceYPos(renderBlocks, x, y, z, icon);
				break;
			case NORMAL_ZN:
				RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
				break;
			case NORMAL_ZP:
				RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
				break;
			case NORMAL_XN:
				RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
				break;
			case NORMAL_XP:
				RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
				break;
			case TRIANGLE_XN:
				HelperTriangle.renderFaceXNeg(renderBlocks, x, y, z, icon);
				break;
			case TRIANGLE_XP:
				HelperTriangle.renderFaceXPos(renderBlocks, x, y, z, icon);
				break;
			case TRIANGLE_ZN:
				HelperTriangle.renderFaceZNeg(renderBlocks, x, y, z, icon);
				break;
			case TRIANGLE_ZP:
				HelperTriangle.renderFaceZPos(renderBlocks, x, y, z, icon);
				break;
			case WEDGE_YN:
				HelperOrthoWedge.renderFaceYNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_YP:
				HelperOrthoWedge.renderFaceYPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_SLOPED_ZN:
				HelperOblWedge.renderSlopeZNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_ZN:
				HelperOrthoWedge.renderFaceZNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_SLOPED_ZP:
				HelperOblWedge.renderSlopeZPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_ZP:
				HelperOrthoWedge.renderFaceZPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_SLOPED_XN:
				HelperOblWedge.renderSlopeXNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_XN:
				HelperOrthoWedge.renderFaceXNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_SLOPED_XP:
				HelperOblWedge.renderSlopeXPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_XP:
				HelperOrthoWedge.renderFaceXPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_CORNER_SLOPED_ZN:
				HelperCorner.renderSlopeZNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_CORNER_SLOPED_ZP:
				HelperCorner.renderSlopeZPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_CORNER_SLOPED_XN:
				HelperCorner.renderSlopeXNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_CORNER_SLOPED_XP:
				HelperCorner.renderSlopeXPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case OBL_CORNER_SLOPED_YN:
				HelperOblique.renderSlopeYNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case OBL_CORNER_SLOPED_YP:
				HelperOblique.renderSlopeYPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case PYR_YZNN:
				HelperPyramid.renderFaceYNegZNeg(renderBlocks, x, y, z, icon);
				break;
			case PYR_YZNP:
				HelperPyramid.renderFaceYNegZPos(renderBlocks, x, y, z, icon);
				break;
			case PYR_YXNN:
				HelperPyramid.renderFaceYNegXNeg(renderBlocks, x, y, z, icon);
				break;
			case PYR_YXNP:
				HelperPyramid.renderFaceYNegXPos(renderBlocks, x, y, z, icon);
				break;
			case PYR_YZPN:
				HelperPyramid.renderFaceYPosZNeg(renderBlocks, x, y, z, icon);
				break;
			case PYR_YZPP:
				HelperPyramid.renderFaceYPosZPos(renderBlocks, x, y, z, icon);
				break;
			case PYR_YXPN:
				HelperPyramid.renderFaceYPosXNeg(renderBlocks, x, y, z, icon);
				break;
			case PYR_YXPP:
				HelperPyramid.renderFaceYPosXPos(renderBlocks, x, y, z, icon);
				break;
			case PRISM_NORTH_XN:
				HelperPrism.renderNorthFaceXNeg(renderBlocks, x, y, z, icon);
				break;
			case PRISM_NORTH_XP:
				HelperPrism.renderNorthFaceXPos(renderBlocks, x, y, z, icon);
				break;
			case PRISM_SOUTH_XN:
				HelperPrism.renderSouthFaceXNeg(renderBlocks, x, y, z, icon);
				break;
			case PRISM_SOUTH_XP:
				HelperPrism.renderSouthFaceXPos(renderBlocks, x, y, z, icon);
				break;
			case PRISM_WEST_ZN:
				HelperPrism.renderWestFaceZNeg(renderBlocks, x, y, z, icon);
				break;
			case PRISM_WEST_ZP:
				HelperPrism.renderWestFaceZPos(renderBlocks, x, y, z, icon);
				break;
			case PRISM_EAST_ZN:
				HelperPrism.renderEastFaceZNeg(renderBlocks, x, y, z, icon);
				break;
			case PRISM_EAST_ZP:
				HelperPrism.renderEastFaceZPos(renderBlocks, x, y, z, icon);
				break;
			case PRISM_WEDGE_ZN:
				HelperPrism.renderSlopeZNeg(renderBlocks, x, y, z, icon);
				break;
			case PRISM_WEDGE_ZP:
				HelperPrism.renderSlopeZPos(renderBlocks, x, y, z, icon);
				break;
			case PRISM_WEDGE_XN:
				HelperPrism.renderSlopeXNeg(renderBlocks, x, y, z, icon);
				break;
			case PRISM_WEDGE_XP:
				HelperPrism.renderSlopeXPos(renderBlocks, x, y, z, icon);
				break;
			}
		}
	}

	@Override
	/**
	 * Renders slope.
	 */
	public boolean renderBlock(Block block, int x, int y, int z)
	{
		if (coverRendering != 6) {

			return super.renderBlock(block, x, y, z);

		} else {

			Slope slope = Slope.slopesList[BlockProperties.getData(TE)];

			renderBlocks.enableAO = Minecraft.isAmbientOcclusionEnabled() && !disableAO && Block.lightValue[block.blockID] == 0;

			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			populateOffsetLighting(block, x, y, z);

			/* Render sloped faces. */

			isSideSloped = true;

			switch (slope.type) {
			case WEDGE_XZ:
				prepareHorizontalWedge(block, slope, x, y, z);
				break;
			case WEDGE_Y:
				prepareVerticalWedge(block, slope, x, y, z);
				break;
			case WEDGE_INT:
				prepareWedgeIntCorner(block, slope, x, y, z);
				break;
			case WEDGE_EXT:
				prepareWedgeExtCorner(block, slope, x, y, z);
				break;
			case OBLIQUE_INT:
				prepareObliqueIntCorner(block, slope, x, y, z);
				break;
			case OBLIQUE_EXT:
				prepareObliqueExtCorner(block, slope, x, y, z);
				break;
			case PYRAMID:
				preparePyramid(block, slope, x, y, z);
				break;
			default: // PRISM TYPES
				preparePrism(block, slope, x, y, z);
				break;
			}

			isSideSloped = false;

			/* Render non-sloped faces. */

			/* BOTTOM FACE */
			if (slope.hasSide(ForgeDirection.DOWN) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, DOWN)) {
				prepareFaceYNeg(block, slope, x, y, z);
			}

			/* TOP FACE */
			if (slope.hasSide(ForgeDirection.UP) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, UP)) {
				prepareFaceYPos(block, slope, x, y, z);
			}

			/* NORTH FACE */
			if (slope.hasSide(ForgeDirection.NORTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, NORTH)) {
				prepareFaceZNeg(block, slope, x, y, z);
			}

			/* SOUTH FACE */
			if (slope.hasSide(ForgeDirection.SOUTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, SOUTH)) {
				prepareFaceZPos(block, slope, x, y, z);
			}

			/* WEST FACE */
			if (slope.hasSide(ForgeDirection.WEST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, WEST)) {
				prepareFaceXNeg(block, slope, x, y, z);
			}

			/* EAST FACE */
			if (slope.hasSide(ForgeDirection.EAST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, EAST)) {
				prepareFaceXPos(block, slope, x, y, z);
			}

			RenderHelper.startDrawing(QUADS);
		}

		renderBlocks.enableAO = false;
		return true;
	}



	/**
	 * Will set lighting and render prism sloped faces.
	 */
	private void preparePrism(Block block, Slope slope, int x, int y, int z)
	{
		int PRISM_N = 0;
		int PRISM_S = 1;
		int PRISM_W = 2;
		int PRISM_E = 3;
		int PYR_POS_N = 4;
		int PYR_POS_S = 5;
		int PYR_POS_W = 6;
		int PYR_POS_E = 7;
		int WEDGE_POS_N = 8;
		int WEDGE_POS_S = 9;
		int WEDGE_POS_W = 10;
		int WEDGE_POS_E = 11;

		List<Integer> pieceList = new ArrayList<Integer>();

		/* Add prism pieces. */

		if (slope.facings.contains(ForgeDirection.NORTH)) {
			pieceList.add(PRISM_N);
		}
		if (slope.facings.contains(ForgeDirection.SOUTH)) {
			pieceList.add(PRISM_S);
		}
		if (slope.facings.contains(ForgeDirection.WEST)) {
			pieceList.add(PRISM_W);
		}
		if (slope.facings.contains(ForgeDirection.EAST)) {
			pieceList.add(PRISM_E);
		}

		/* Add required non-prism pieces. */

		if (slope.type.equals(Type.PRISM_SLOPE)) {

			if (slope.facings.contains(ForgeDirection.NORTH)) {
				pieceList.add(WEDGE_POS_N);
			} else if (slope.facings.contains(ForgeDirection.SOUTH)) {
				pieceList.add(WEDGE_POS_S);
			} else if (slope.facings.contains(ForgeDirection.WEST)) {
				pieceList.add(WEDGE_POS_W);
			} else if (slope.facings.contains(ForgeDirection.EAST)){
				pieceList.add(WEDGE_POS_E);
			}

		} else {

			if (!pieceList.contains(PRISM_N)) {
				pieceList.add(PYR_POS_N);
			}
			if (!pieceList.contains(PRISM_S)) {
				pieceList.add(PYR_POS_S);
			}
			if (!pieceList.contains(PRISM_W)) {
				pieceList.add(PYR_POS_W);
			}
			if (!pieceList.contains(PRISM_E)) {
				pieceList.add(PYR_POS_E);
			}

		}

		/* Begin rendering sloped pieces. */

		RenderHelper.startDrawing(TRIANGLES);

		if (pieceList.contains(PRISM_N)) {
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
			lightingHelper.setLightness(LIGHTNESS_XYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_W);
			renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
			setIDAndRender(block, PRISM_NORTH_XN, x, y, z, WEST);
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
			lightingHelper.setLightness(LIGHTNESS_XYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_E);
			renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
			setIDAndRender(block, PRISM_NORTH_XP, x, y, z, EAST);
		}
		if (pieceList.contains(PRISM_S)) {
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
			lightingHelper.setLightness(LIGHTNESS_XYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_W);
			renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
			setIDAndRender(block, PRISM_SOUTH_XN, x, y, z, WEST);
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
			lightingHelper.setLightness(LIGHTNESS_XYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_E);
			renderBlocks.setRenderBounds(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
			setIDAndRender(block, PRISM_SOUTH_XP, x, y, z, EAST);
		}
		if (pieceList.contains(PRISM_W)) {
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
			lightingHelper.setLightness(LIGHTNESS_ZYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_N);
			renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
			setIDAndRender(block, PRISM_WEST_ZN, x, y, z, NORTH);
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
			lightingHelper.setLightness(LIGHTNESS_ZYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_S);
			renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
			setIDAndRender(block, PRISM_WEST_ZP, x, y, z, SOUTH);
		}
		if (pieceList.contains(PRISM_E)) {
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
			lightingHelper.setLightness(LIGHTNESS_ZYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_N);
			renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
			setIDAndRender(block, PRISM_EAST_ZN, x, y, z, NORTH);
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
			lightingHelper.setLightness(LIGHTNESS_ZYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_S);
			renderBlocks.setRenderBounds(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
			setIDAndRender(block, PRISM_EAST_ZP, x, y, z, SOUTH);
		}
		if (pieceList.contains(PYR_POS_N)) {
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
			lightingHelper.setLightness(LIGHTNESS_ZYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_N);
			setIDAndRender(block, PYR_YZPN, x, y, z, NORTH);
		}
		if (pieceList.contains(PYR_POS_S)) {
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
			lightingHelper.setLightness(LIGHTNESS_ZYPP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_S);
			setIDAndRender(block, PYR_YZPP, x, y, z, SOUTH);
		}
		if (pieceList.contains(PYR_POS_W)) {
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
			lightingHelper.setLightness(LIGHTNESS_XYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_W);
			setIDAndRender(block, PYR_YXPN, x, y, z, WEST);
		}
		if (pieceList.contains(PYR_POS_E)) {
			setRenderBoundsAndRelight(block, SIDE_ALL, 0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
			lightingHelper.setLightness(LIGHTNESS_XYPP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_E);
			setIDAndRender(block, PYR_YXPP, x, y, z, EAST);
		}

		RenderHelper.startDrawing(QUADS);

		/* Render wedge intersecting mask, if piece requires it. */

		setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

		if (pieceList.contains(WEDGE_POS_N)) {
			setWedgeSlopeLighting(Slope.WEDGE_POS_N);
			lightingHelper.setLightness(LIGHTNESS_ZYNP);
			setIDAndRender(block, PRISM_WEDGE_ZN, x, y, z, NORTH);
		} else if (pieceList.contains(WEDGE_POS_S)) {
			setWedgeSlopeLighting(Slope.WEDGE_POS_S);
			lightingHelper.setLightness(LIGHTNESS_ZYPP);
			setIDAndRender(block, PRISM_WEDGE_ZP, x, y, z, SOUTH);
		} else if (pieceList.contains(WEDGE_POS_W)) {
			setWedgeSlopeLighting(Slope.WEDGE_POS_W);
			lightingHelper.setLightness(LIGHTNESS_XYNP);
			setIDAndRender(block, PRISM_WEDGE_XN, x, y, z, WEST);
		} else if (pieceList.contains(WEDGE_POS_E)) {
			setWedgeSlopeLighting(Slope.WEDGE_POS_E);
			lightingHelper.setLightness(LIGHTNESS_XYPP);
			setIDAndRender(block, PRISM_WEDGE_XP, x, y, z, EAST);
		}
	}

	/**
	 * Will set lighting for wedge sloped faces.  Many slope types
	 * make use of these lighting parameters in addition to wedges.
	 */
	private void setWedgeSlopeLighting(Slope slope)
	{
		if (renderBlocks.enableAO)
		{
			switch (slope.slopeID)
			{
			case Slope.ID_WEDGE_NW:

				lightingHelper.ao[TOP_LEFT] 		= offset_ao[WEST][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[WEST][BOTTOM_LEFT];
				lightingHelper.ao[BOTTOM_RIGHT] 	= offset_ao[NORTH][BOTTOM_RIGHT];
				lightingHelper.ao[TOP_RIGHT] 		= offset_ao[NORTH][TOP_RIGHT];

				renderBlocks.brightnessTopLeft 		= offset_brightness[WEST][TOP_LEFT];
				renderBlocks.brightnessBottomLeft 	= offset_brightness[WEST][BOTTOM_LEFT];
				renderBlocks.brightnessBottomRight 	= offset_brightness[NORTH][BOTTOM_RIGHT];
				renderBlocks.brightnessTopRight 	= offset_brightness[NORTH][TOP_RIGHT];

				break;
			case Slope.ID_WEDGE_NE:

				lightingHelper.ao[TOP_LEFT] 		= offset_ao[NORTH][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[NORTH][BOTTOM_LEFT];
				lightingHelper.ao[BOTTOM_RIGHT] 	= offset_ao[EAST][BOTTOM_RIGHT];
				lightingHelper.ao[TOP_RIGHT] 		= offset_ao[EAST][TOP_RIGHT];

				renderBlocks.brightnessTopLeft 		= offset_brightness[NORTH][TOP_LEFT];
				renderBlocks.brightnessBottomLeft	= offset_brightness[NORTH][BOTTOM_LEFT];
				renderBlocks.brightnessBottomRight	= offset_brightness[EAST][BOTTOM_RIGHT];
				renderBlocks.brightnessTopRight 	= offset_brightness[EAST][TOP_RIGHT];

				break;
			case Slope.ID_WEDGE_SW:

				lightingHelper.ao[TOP_LEFT] 		= offset_ao[SOUTH][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[SOUTH][BOTTOM_LEFT];
				lightingHelper.ao[BOTTOM_RIGHT]		= offset_ao[WEST][BOTTOM_RIGHT];
				lightingHelper.ao[TOP_RIGHT] 		= offset_ao[WEST][TOP_RIGHT];

				renderBlocks.brightnessTopLeft 		= offset_brightness[SOUTH][TOP_LEFT];
				renderBlocks.brightnessBottomLeft	= offset_brightness[SOUTH][BOTTOM_LEFT];
				renderBlocks.brightnessBottomRight	= offset_brightness[WEST][BOTTOM_RIGHT];
				renderBlocks.brightnessTopRight 	= offset_brightness[WEST][TOP_RIGHT];

				break;
			case Slope.ID_WEDGE_SE:

				lightingHelper.ao[TOP_LEFT] 		= offset_ao[EAST][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[EAST][BOTTOM_LEFT];
				lightingHelper.ao[BOTTOM_RIGHT] 	= offset_ao[SOUTH][BOTTOM_RIGHT];
				lightingHelper.ao[TOP_RIGHT] 		= offset_ao[SOUTH][TOP_RIGHT];

				renderBlocks.brightnessTopLeft 		= offset_brightness[EAST][TOP_LEFT];
				renderBlocks.brightnessBottomLeft	= offset_brightness[EAST][BOTTOM_LEFT];
				renderBlocks.brightnessBottomRight	= offset_brightness[SOUTH][BOTTOM_RIGHT];
				renderBlocks.brightnessTopRight 	= offset_brightness[SOUTH][TOP_RIGHT];

				break;
			case Slope.ID_WEDGE_NEG_N:

				lightingHelper.ao[TOP_LEFT] 		= offset_ao[WEST][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[NORTH][BOTTOM_LEFT];
				lightingHelper.ao[BOTTOM_RIGHT] 	= offset_ao[NORTH][BOTTOM_RIGHT];
				lightingHelper.ao[TOP_RIGHT] 		= offset_ao[EAST][TOP_RIGHT];

				renderBlocks.brightnessTopLeft 		= offset_brightness[WEST][TOP_LEFT];
				renderBlocks.brightnessBottomLeft	= offset_brightness[NORTH][BOTTOM_LEFT];
				renderBlocks.brightnessBottomRight	= offset_brightness[NORTH][BOTTOM_RIGHT];
				renderBlocks.brightnessTopRight 	= offset_brightness[EAST][TOP_RIGHT];

				break;
			case Slope.ID_WEDGE_NEG_S:

				lightingHelper.ao[TOP_LEFT] 		= offset_ao[EAST][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[SOUTH][BOTTOM_LEFT];
				lightingHelper.ao[BOTTOM_RIGHT] 	= offset_ao[SOUTH][BOTTOM_RIGHT];
				lightingHelper.ao[TOP_RIGHT] 		= offset_ao[WEST][TOP_RIGHT];

				renderBlocks.brightnessTopLeft 		= offset_brightness[EAST][TOP_LEFT];
				renderBlocks.brightnessBottomLeft	= offset_brightness[SOUTH][BOTTOM_LEFT];
				renderBlocks.brightnessBottomRight	= offset_brightness[SOUTH][BOTTOM_RIGHT];
				renderBlocks.brightnessTopRight 	= offset_brightness[WEST][TOP_RIGHT];

				break;
			case Slope.ID_WEDGE_NEG_W:

				lightingHelper.ao[TOP_LEFT] 		= offset_ao[SOUTH][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[WEST][BOTTOM_LEFT];
				lightingHelper.ao[BOTTOM_RIGHT] 	= offset_ao[WEST][BOTTOM_RIGHT];
				lightingHelper.ao[TOP_RIGHT] 		= offset_ao[NORTH][TOP_RIGHT];

				renderBlocks.brightnessTopLeft 		= offset_brightness[SOUTH][TOP_LEFT];
				renderBlocks.brightnessBottomLeft	= offset_brightness[WEST][BOTTOM_LEFT];
				renderBlocks.brightnessBottomRight	= offset_brightness[WEST][BOTTOM_RIGHT];
				renderBlocks.brightnessTopRight 	= offset_brightness[NORTH][TOP_RIGHT];

				break;
			case Slope.ID_WEDGE_NEG_E:

				lightingHelper.ao[TOP_LEFT] 		= offset_ao[NORTH][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[EAST][BOTTOM_LEFT];
				lightingHelper.ao[BOTTOM_RIGHT] 	= offset_ao[EAST][BOTTOM_RIGHT];
				lightingHelper.ao[TOP_RIGHT] 		= offset_ao[SOUTH][TOP_RIGHT];

				renderBlocks.brightnessTopLeft 		= offset_brightness[NORTH][TOP_LEFT];
				renderBlocks.brightnessBottomLeft	= offset_brightness[EAST][BOTTOM_LEFT];
				renderBlocks.brightnessBottomRight	= offset_brightness[EAST][BOTTOM_RIGHT];
				renderBlocks.brightnessTopRight 	= offset_brightness[SOUTH][TOP_RIGHT];

				break;
			case Slope.ID_WEDGE_POS_N:

				lightingHelper.ao[TOP_LEFT] 		= ao[UP][SOUTHEAST];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[UP][NORTHEAST];
				lightingHelper.ao[BOTTOM_RIGHT] 	= offset_ao[UP][NORTHWEST];
				lightingHelper.ao[TOP_RIGHT] 		= ao[UP][SOUTHWEST];

				renderBlocks.brightnessTopLeft 		= brightness[UP][SOUTHEAST];
				renderBlocks.brightnessBottomLeft	= offset_brightness[UP][NORTHEAST];
				renderBlocks.brightnessBottomRight	= offset_brightness[UP][NORTHWEST];
				renderBlocks.brightnessTopRight 	= brightness[UP][SOUTHWEST];

				break;
			case Slope.ID_WEDGE_POS_S:

				lightingHelper.ao[TOP_LEFT] 		= ao[UP][NORTHWEST];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[UP][SOUTHWEST];
				lightingHelper.ao[BOTTOM_RIGHT] 	= offset_ao[UP][SOUTHEAST];
				lightingHelper.ao[TOP_RIGHT] 		= ao[UP][NORTHEAST];

				renderBlocks.brightnessTopLeft 		= brightness[UP][NORTHWEST];
				renderBlocks.brightnessBottomLeft	= offset_brightness[UP][SOUTHWEST];
				renderBlocks.brightnessBottomRight	= offset_brightness[UP][SOUTHEAST];
				renderBlocks.brightnessTopRight 	= brightness[UP][NORTHEAST];

				break;
			case Slope.ID_WEDGE_POS_W:

				lightingHelper.ao[TOP_LEFT] 		= ao[UP][NORTHEAST];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[UP][NORTHWEST];
				lightingHelper.ao[BOTTOM_RIGHT] 	= offset_ao[UP][SOUTHWEST];
				lightingHelper.ao[TOP_RIGHT] 		= ao[UP][SOUTHEAST];

				renderBlocks.brightnessTopLeft 		= brightness[UP][NORTHEAST];
				renderBlocks.brightnessBottomLeft	= offset_brightness[UP][NORTHWEST];
				renderBlocks.brightnessBottomRight	= offset_brightness[UP][SOUTHWEST];
				renderBlocks.brightnessTopRight 	= brightness[UP][SOUTHEAST];

				break;
			case Slope.ID_WEDGE_POS_E:

				lightingHelper.ao[TOP_LEFT] 		= ao[UP][SOUTHWEST];
				lightingHelper.ao[BOTTOM_LEFT] 		= offset_ao[UP][SOUTHEAST];
				lightingHelper.ao[BOTTOM_RIGHT] 	= offset_ao[UP][NORTHEAST];
				lightingHelper.ao[TOP_RIGHT] 		= ao[UP][NORTHWEST];

				renderBlocks.brightnessTopLeft 		= brightness[UP][SOUTHWEST];
				renderBlocks.brightnessBottomLeft	= offset_brightness[UP][SOUTHEAST];
				renderBlocks.brightnessBottomRight	= offset_brightness[UP][NORTHEAST];
				renderBlocks.brightnessTopRight 	= brightness[UP][NORTHWEST];

				break;
			}
		}
	}

	private void prepareHorizontalWedge(Block block, Slope slope, int x, int y, int z)
	{
		RenderHelper.startDrawing(QUADS);

		setWedgeSlopeLighting(slope);
		lightingHelper.setLightness(LIGHTNESS_SIDE_WEDGE);

		if (slope.facings.contains(ForgeDirection.NORTH)) {
			setIDAndRender(block, WEDGE_SLOPED_ZN, x, y, z, EAST);
		} else {
			setIDAndRender(block, WEDGE_SLOPED_ZP, x, y, z, WEST);
		}
	}

	private void prepareVerticalWedge(Block block, Slope slope, int x, int y, int z)
	{
		RenderHelper.startDrawing(QUADS);

		setWedgeSlopeLighting(slope);

		if (slope.facings.contains(ForgeDirection.NORTH)) {
			lightingHelper.setLightness(slope.isPositive ? LIGHTNESS_ZYNP : LIGHTNESS_ZYNN);
			setIDAndRender(block, WEDGE_SLOPED_ZN, x, y, z, NORTH);
		} else if (slope.facings.contains(ForgeDirection.SOUTH)) {
			lightingHelper.setLightness(slope.isPositive ? LIGHTNESS_ZYNP : LIGHTNESS_ZYNN);
			setIDAndRender(block, WEDGE_SLOPED_ZP, x, y, z, SOUTH);
		} else if (slope.facings.contains(ForgeDirection.WEST)) {
			lightingHelper.setLightness(slope.isPositive ? LIGHTNESS_XYNP : LIGHTNESS_XYNN);
			setIDAndRender(block, WEDGE_SLOPED_XN, x, y, z, WEST);
		} else { // ForgeDirection.EAST
			lightingHelper.setLightness(slope.isPositive ? LIGHTNESS_XYNP : LIGHTNESS_XYNN);
			setIDAndRender(block, WEDGE_SLOPED_XP, x, y, z, EAST);
		}
	}

	private void prepareWedgeIntCorner(Block block, Slope slope, int x, int y, int z)
	{
		RenderHelper.startDrawing(TRIANGLES);

		Slope slopeX = slope.facings.contains(ForgeDirection.WEST) ? slope.isPositive ? Slope.WEDGE_POS_W : Slope.WEDGE_NEG_W : slope.isPositive ? Slope.WEDGE_POS_E : Slope.WEDGE_NEG_E;

		setWedgeSlopeLighting(slopeX);
		lightingHelper.setLightness(slope.isPositive ? LIGHTNESS_XYNP : LIGHTNESS_XYNN);

		if (slopeX.facings.contains(ForgeDirection.WEST)) {
			setIDAndRender(block, WEDGE_CORNER_SLOPED_XN, x, y, z, WEST);
		} else { // ForgeDirection.EAST
			setIDAndRender(block, WEDGE_CORNER_SLOPED_XP, x, y, z, EAST);
		}

		Slope slopeZ = slope.facings.contains(ForgeDirection.NORTH) ? slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N : slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S;

		setWedgeSlopeLighting(slopeZ);
		lightingHelper.setLightness(slope.isPositive ? LIGHTNESS_ZYNP : LIGHTNESS_ZYNN);

		if (slope.facings.contains(ForgeDirection.NORTH)) {
			setIDAndRender(block, WEDGE_CORNER_SLOPED_ZN, x, y, z, NORTH);
		} else {
			setIDAndRender(block, WEDGE_CORNER_SLOPED_ZP, x, y, z, SOUTH);
		}
	}

	private void prepareWedgeExtCorner(Block block, Slope slope, int x, int y, int z)
	{
		RenderHelper.startDrawing(TRIANGLES);

		Slope slopeX = slope.facings.contains(ForgeDirection.WEST) ? slope.isPositive ? Slope.WEDGE_POS_W : Slope.WEDGE_NEG_W : slope.isPositive ? Slope.WEDGE_POS_E : Slope.WEDGE_NEG_E;

		setWedgeSlopeLighting(slopeX);
		lightingHelper.setLightness(slope.isPositive ? LIGHTNESS_XYNP : LIGHTNESS_XYNN);

		if (slopeX.facings.contains(ForgeDirection.WEST)) {
			setIDAndRender(block, WEDGE_CORNER_SLOPED_XN, x, y, z, WEST);
		} else { // ForgeDirection.EAST
			setIDAndRender(block, WEDGE_CORNER_SLOPED_XP, x, y, z, EAST);
		}

		Slope slopeZ = slope.facings.contains(ForgeDirection.NORTH) ? slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N : slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S;

		setWedgeSlopeLighting(slopeZ);
		lightingHelper.setLightness(slope.isPositive ? LIGHTNESS_ZYNP : LIGHTNESS_ZYNN);

		if (slope.facings.contains(ForgeDirection.NORTH)) {
			setIDAndRender(block, WEDGE_CORNER_SLOPED_ZN, x, y, z, NORTH);
		} else {
			setIDAndRender(block, WEDGE_CORNER_SLOPED_ZP, x, y, z, SOUTH);
		}
	}

	private void prepareObliqueIntCorner(Block block, Slope slope, int x, int y, int z)
	{
		RenderHelper.startDrawing(TRIANGLES);

		if (renderBlocks.enableAO) {

			switch (slope.slopeID) {
			case Slope.ID_OBL_INT_NEG_NW:

				lightingHelper.ao[TOP_LEFT] 	= offset_ao[DOWN][NORTHWEST];
				lightingHelper.ao[BOTTOM_LEFT] 	= (ao[NORTH][BOTTOM_LEFT] + offset_ao[WEST][BOTTOM_LEFT] + offset_ao[UP][NORTHEAST]) / 3;
				lightingHelper.ao[BOTTOM_RIGHT] = (ao[WEST][BOTTOM_RIGHT] + offset_ao[NORTH][BOTTOM_RIGHT] + offset_ao[UP][SOUTHWEST]) / 3;

				break;
			case Slope.ID_OBL_INT_NEG_NE:

				lightingHelper.ao[TOP_LEFT] 	= offset_ao[DOWN][NORTHEAST];
				lightingHelper.ao[BOTTOM_LEFT] 	= (ao[EAST][BOTTOM_LEFT] + offset_ao[NORTH][BOTTOM_LEFT] + offset_ao[UP][SOUTHEAST]) / 3;
				lightingHelper.ao[BOTTOM_RIGHT] = (ao[NORTH][BOTTOM_RIGHT] + offset_ao[EAST][BOTTOM_RIGHT] + offset_ao[UP][NORTHWEST]) / 3;

				break;
			case Slope.ID_OBL_INT_NEG_SW:

				lightingHelper.ao[TOP_LEFT] 	= offset_ao[DOWN][SOUTHWEST];
				lightingHelper.ao[BOTTOM_LEFT] 	= (ao[WEST][BOTTOM_LEFT] + offset_ao[SOUTH][BOTTOM_LEFT] + offset_ao[UP][NORTHWEST]) / 3;
				lightingHelper.ao[BOTTOM_RIGHT] = (ao[SOUTH][BOTTOM_RIGHT] + offset_ao[WEST][BOTTOM_RIGHT] + offset_ao[UP][SOUTHEAST]) / 3;

				break;
			case Slope.ID_OBL_INT_NEG_SE:

				lightingHelper.ao[TOP_LEFT] 	= offset_ao[DOWN][SOUTHEAST];
				lightingHelper.ao[BOTTOM_LEFT] 	= (ao[SOUTH][BOTTOM_LEFT] + offset_ao[EAST][BOTTOM_LEFT] + offset_ao[UP][SOUTHWEST]) / 3;
				lightingHelper.ao[BOTTOM_RIGHT] = (ao[EAST][BOTTOM_RIGHT] + offset_ao[SOUTH][BOTTOM_RIGHT] + offset_ao[UP][NORTHEAST]) / 3;

				break;
			case Slope.ID_OBL_INT_POS_NW:

				lightingHelper.ao[TOP_LEFT] 	= (ao[NORTH][TOP_LEFT] + offset_ao[WEST][TOP_LEFT] + ao[UP][NORTHEAST]) / 3;
				lightingHelper.ao[BOTTOM_LEFT] 	= offset_ao[UP][NORTHWEST];
				lightingHelper.ao[TOP_RIGHT]	= (ao[WEST][TOP_RIGHT] + offset_ao[NORTH][TOP_RIGHT] + ao[UP][SOUTHWEST]) / 3;

				break;
			case Slope.ID_OBL_INT_POS_NE:

				lightingHelper.ao[TOP_LEFT] 	= (ao[EAST][TOP_LEFT] + offset_ao[NORTH][TOP_LEFT] + ao[UP][NORTHWEST]) / 3;
				lightingHelper.ao[BOTTOM_LEFT] 	= offset_ao[UP][NORTHEAST];
				lightingHelper.ao[TOP_RIGHT] 	= (ao[NORTH][TOP_RIGHT] + offset_ao[EAST][TOP_RIGHT] + ao[UP][NORTHWEST]) / 3;

				break;
			case Slope.ID_OBL_INT_POS_SW:

				lightingHelper.ao[TOP_LEFT] 	= (ao[WEST][TOP_LEFT] + offset_ao[SOUTH][TOP_LEFT] + ao[UP][NORTHWEST]) / 3;
				lightingHelper.ao[BOTTOM_LEFT] 	= offset_ao[UP][SOUTHWEST];
				lightingHelper.ao[TOP_RIGHT] 	= (ao[SOUTH][TOP_RIGHT] + offset_ao[WEST][TOP_RIGHT] + ao[UP][SOUTHEAST]) / 3;

				break;
			case Slope.ID_OBL_INT_POS_SE:

				lightingHelper.ao[TOP_LEFT] 	= (ao[SOUTH][TOP_LEFT] + offset_ao[EAST][TOP_LEFT] + ao[UP][SOUTHWEST]) / 3;
				lightingHelper.ao[BOTTOM_LEFT] 	= offset_ao[UP][SOUTHEAST];
				lightingHelper.ao[TOP_RIGHT] 	= (ao[EAST][TOP_RIGHT] + offset_ao[SOUTH][TOP_RIGHT] + ao[UP][NORTHEAST]) / 3;

				break;
			}

		}

		if (slope.isPositive) {
			lightingHelper.setLightness(LIGHTNESS_POS_OBL);
			setIDAndRender(block, OBL_CORNER_SLOPED_YP, x, y, z, UP);
		} else {
			lightingHelper.setLightness(LIGHTNESS_NEG_OBL);
			setIDAndRender(block, OBL_CORNER_SLOPED_YN, x, y, z, DOWN);
		}
	}

	private void prepareObliqueExtCorner(Block block, Slope slope, int x, int y, int z)
	{
		RenderHelper.startDrawing(TRIANGLES);

		if (renderBlocks.enableAO) {

			switch (slope.slopeID) {
			case Slope.ID_OBL_EXT_NEG_NW:

				lightingHelper.ao[TOP_LEFT] 	= offset_ao[WEST][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 	= (offset_ao[WEST][BOTTOM_RIGHT] + offset_ao[NORTH][BOTTOM_LEFT]) / 2;
				lightingHelper.ao[TOP_RIGHT] 	= offset_ao[NORTH][TOP_RIGHT];

				break;
			case Slope.ID_OBL_EXT_NEG_NE:

				lightingHelper.ao[TOP_LEFT] 	= offset_ao[NORTH][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 	= (offset_ao[EAST][BOTTOM_LEFT] + offset_ao[NORTH][BOTTOM_RIGHT]) / 2;
				lightingHelper.ao[TOP_RIGHT] 	= offset_ao[EAST][TOP_RIGHT];

				break;
			case Slope.ID_OBL_EXT_NEG_SW:

				lightingHelper.ao[TOP_LEFT] 	= offset_ao[SOUTH][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 	= (offset_ao[WEST][BOTTOM_LEFT] + offset_ao[SOUTH][BOTTOM_RIGHT]) / 2;
				lightingHelper.ao[TOP_RIGHT] 	= offset_ao[WEST][TOP_RIGHT];

				break;
			case Slope.ID_OBL_EXT_NEG_SE:

				lightingHelper.ao[TOP_LEFT] 	= offset_ao[EAST][TOP_LEFT];
				lightingHelper.ao[BOTTOM_LEFT] 	= (offset_ao[SOUTH][BOTTOM_LEFT] + offset_ao[EAST][BOTTOM_RIGHT]) / 2;
				lightingHelper.ao[TOP_RIGHT] 	= offset_ao[SOUTH][TOP_RIGHT];

				break;
			case Slope.ID_OBL_EXT_POS_NW:

				lightingHelper.ao[TOP_LEFT] 	= (offset_ao[WEST][TOP_RIGHT] + offset_ao[NORTH][TOP_LEFT] + ao[UP][SOUTHEAST] + 1.0F) / 4F;
				lightingHelper.ao[BOTTOM_LEFT] 	= offset_ao[UP][NORTHEAST];
				lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][SOUTHWEST];

				break;
			case Slope.ID_OBL_EXT_POS_NE:

				lightingHelper.ao[TOP_LEFT] 	= (offset_ao[EAST][TOP_LEFT] + offset_ao[NORTH][TOP_RIGHT] + ao[UP][SOUTHWEST] + 1.0F) / 4F;
				lightingHelper.ao[BOTTOM_LEFT] 	= offset_ao[UP][SOUTHEAST];
				lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][NORTHWEST];

				break;
			case Slope.ID_OBL_EXT_POS_SW:

				lightingHelper.ao[TOP_LEFT] 	= (offset_ao[WEST][TOP_LEFT] + offset_ao[SOUTH][TOP_RIGHT] + ao[UP][NORTHEAST] + 1.0F) / 4F;
				lightingHelper.ao[BOTTOM_LEFT] 	= offset_ao[UP][NORTHWEST];
				lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][SOUTHEAST];

				break;
			case Slope.ID_OBL_EXT_POS_SE:

				lightingHelper.ao[TOP_LEFT] 	= (offset_ao[SOUTH][TOP_LEFT] + offset_ao[EAST][TOP_RIGHT] + ao[UP][NORTHWEST] + 1.0F) / 4F;
				lightingHelper.ao[BOTTOM_LEFT] 	= offset_ao[UP][SOUTHWEST];
				lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][NORTHEAST];

				break;
			}

		}

		if (slope.isPositive) {
			lightingHelper.setLightness(LIGHTNESS_POS_OBL);
			setIDAndRender(block, OBL_CORNER_SLOPED_YP, x, y, z, UP);
		} else {
			lightingHelper.setLightness(LIGHTNESS_NEG_OBL);
			setIDAndRender(block, OBL_CORNER_SLOPED_YN, x, y, z, DOWN);
		}
	}

	private void preparePyramid(Block block, Slope slope, int x, int y, int z)
	{
		RenderHelper.startDrawing(TRIANGLES);

		if (slope.isPositive) {

			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);

			lightingHelper.setLightness(LIGHTNESS_XYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_W);
			setIDAndRender(block, PYR_YXPN, x, y, z, UP);

			setRenderBoundsAndRelight(block, SIDE_ALL, 0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

			lightingHelper.setLightness(LIGHTNESS_XYPP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_E);
			setIDAndRender(block, PYR_YXPP, x, y, z, UP);

			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);

			lightingHelper.setLightness(LIGHTNESS_ZYNP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_N);
			setIDAndRender(block, PYR_YZPN, x, y, z, UP);

			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);

			lightingHelper.setLightness(LIGHTNESS_ZYPP);
			setWedgeSlopeLighting(Slope.WEDGE_POS_S);
			setIDAndRender(block, PYR_YZPP, x, y, z, UP);

		} else {

			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);

			lightingHelper.setLightness(LIGHTNESS_XYNN);
			setWedgeSlopeLighting(Slope.WEDGE_NEG_W);
			setIDAndRender(block, PYR_YXNN, x, y, z, DOWN);

			setRenderBoundsAndRelight(block, SIDE_ALL, 0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

			lightingHelper.setLightness(LIGHTNESS_XYPN);
			setWedgeSlopeLighting(Slope.WEDGE_NEG_E);
			setIDAndRender(block, PYR_YXNP, x, y, z, DOWN);

			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);

			lightingHelper.setLightness(LIGHTNESS_ZYNN);
			setWedgeSlopeLighting(Slope.WEDGE_NEG_N);
			setIDAndRender(block, PYR_YZNN, x, y, z, DOWN);

			setRenderBoundsAndRelight(block, SIDE_ALL, 0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);

			lightingHelper.setLightness(LIGHTNESS_ZYPN);
			setWedgeSlopeLighting(Slope.WEDGE_NEG_S);
			setIDAndRender(block, PYR_YZNP, x, y, z, DOWN);

		}
	}

	/**
	 * Prepare bottom face.
	 */
	private void prepareFaceYNeg(Block block, Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			lightingHelper.ao[TOP_LEFT] = ao[DOWN][TOP_LEFT];
			lightingHelper.ao[BOTTOM_LEFT] = ao[DOWN][BOTTOM_LEFT];
			lightingHelper.ao[BOTTOM_RIGHT] = ao[DOWN][BOTTOM_RIGHT];
			lightingHelper.ao[TOP_RIGHT] = ao[DOWN][TOP_RIGHT];
		}

		setRenderBoundsAndRelight(block, DOWN, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

		switch (slope.getFace(ForgeDirection.DOWN)) {
		case WEDGE:
			RenderHelper.startDrawing(TRIANGLES);
			setIDAndRender(block, WEDGE_YN, x, y, z, DOWN);
			break;
		default:
			RenderHelper.startDrawing(QUADS);
			setIDAndRender(block, NORMAL_YN, x, y, z, DOWN);
			break;
		}
	}

	/**
	 * Prepare top face.
	 */
	private void prepareFaceYPos(Block block, Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			lightingHelper.ao[TOP_LEFT] = ao[UP][TOP_LEFT];
			lightingHelper.ao[BOTTOM_LEFT] = ao[UP][BOTTOM_LEFT];
			lightingHelper.ao[BOTTOM_RIGHT] = ao[UP][BOTTOM_RIGHT];
			lightingHelper.ao[TOP_RIGHT] = ao[UP][TOP_RIGHT];
		}

		setRenderBoundsAndRelight(block, UP, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

		switch (slope.getFace(ForgeDirection.UP)) {
		case WEDGE:
			RenderHelper.startDrawing(TRIANGLES);
			setIDAndRender(block, WEDGE_YP, x, y, z, UP);
			break;
		default:
			RenderHelper.startDrawing(QUADS);
			setIDAndRender(block, NORMAL_YP, x, y, z, UP);
			break;
		}
	}

	/**
	 * Prepare North face.
	 */
	private void prepareFaceZNeg(Block block, Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			lightingHelper.ao[TOP_LEFT] = ao[NORTH][TOP_LEFT];
			lightingHelper.ao[BOTTOM_LEFT] = ao[NORTH][BOTTOM_LEFT];
			lightingHelper.ao[BOTTOM_RIGHT] = ao[NORTH][BOTTOM_RIGHT];
			lightingHelper.ao[TOP_RIGHT] = ao[NORTH][TOP_RIGHT];
		}

		switch (slope.getFace(ForgeDirection.NORTH)) {
		case WEDGE:
			RenderHelper.startDrawing(TRIANGLES);
			setRenderBoundsAndRelight(block, NORTH, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			setIDAndRender(block, WEDGE_ZN, x, y, z, NORTH);
			break;
		case TRIANGLE:
			RenderHelper.startDrawing(TRIANGLES);
			setRenderBoundsAndRelight(block, NORTH, 0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
			setIDAndRender(block, TRIANGLE_ZN, x, y, z, NORTH);
			break;
		default:
			RenderHelper.startDrawing(QUADS);
			setRenderBoundsAndRelight(block, NORTH, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			setIDAndRender(block, NORMAL_ZN, x, y, z, NORTH);
			break;
		}
	}

	/**
	 * Prepare South face.
	 */
	private void prepareFaceZPos(Block block, Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			lightingHelper.ao[TOP_LEFT] = ao[SOUTH][TOP_LEFT];
			lightingHelper.ao[BOTTOM_LEFT] = ao[SOUTH][BOTTOM_LEFT];
			lightingHelper.ao[BOTTOM_RIGHT] = ao[SOUTH][BOTTOM_RIGHT];
			lightingHelper.ao[TOP_RIGHT] = ao[SOUTH][TOP_RIGHT];
		}

		switch (slope.getFace(ForgeDirection.SOUTH)) {
		case WEDGE:
			RenderHelper.startDrawing(TRIANGLES);
			setRenderBoundsAndRelight(block, SOUTH, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			setIDAndRender(block, WEDGE_ZP, x, y, z, SOUTH);
			break;
		case TRIANGLE:
			RenderHelper.startDrawing(TRIANGLES);
			setRenderBoundsAndRelight(block, SOUTH, 0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
			setIDAndRender(block, TRIANGLE_ZP, x, y, z, SOUTH);
			break;
		default:
			RenderHelper.startDrawing(QUADS);
			setRenderBoundsAndRelight(block, SOUTH, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			setIDAndRender(block, NORMAL_ZP, x, y, z, SOUTH);
			break;
		}
	}

	/**
	 * Prepare West face.
	 */
	private void prepareFaceXNeg(Block block, Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			lightingHelper.ao[TOP_LEFT] = ao[WEST][TOP_LEFT];
			lightingHelper.ao[BOTTOM_LEFT] = ao[WEST][BOTTOM_LEFT];
			lightingHelper.ao[BOTTOM_RIGHT] = ao[WEST][BOTTOM_RIGHT];
			lightingHelper.ao[TOP_RIGHT] = ao[WEST][TOP_RIGHT];
		}

		switch (slope.getFace(ForgeDirection.WEST)) {
		case WEDGE:
			RenderHelper.startDrawing(TRIANGLES);
			setRenderBoundsAndRelight(block, WEST, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			setIDAndRender(block, WEDGE_XN, x, y, z, WEST);
			break;
		case TRIANGLE:
			RenderHelper.startDrawing(TRIANGLES);
			setRenderBoundsAndRelight(block, WEST, 0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
			setIDAndRender(block, TRIANGLE_XN, x, y, z, WEST);
			break;
		default:
			RenderHelper.startDrawing(QUADS);
			setRenderBoundsAndRelight(block, WEST, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			setIDAndRender(block, NORMAL_XN, x, y, z, WEST);
			break;
		}
	}

	/**
	 * Prepare East face.
	 */
	private void prepareFaceXPos(Block block, Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			lightingHelper.ao[TOP_LEFT] = ao[EAST][TOP_LEFT];
			lightingHelper.ao[BOTTOM_LEFT] = ao[EAST][BOTTOM_LEFT];
			lightingHelper.ao[BOTTOM_RIGHT] = ao[EAST][BOTTOM_RIGHT];
			lightingHelper.ao[TOP_RIGHT] = ao[EAST][TOP_RIGHT];
		}

		switch (slope.getFace(ForgeDirection.EAST)) {
		case WEDGE:
			RenderHelper.startDrawing(TRIANGLES);
			setRenderBoundsAndRelight(block, EAST, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			setIDAndRender(block, WEDGE_XP, x, y, z, EAST);
			break;
		case TRIANGLE:
			RenderHelper.startDrawing(TRIANGLES);
			setRenderBoundsAndRelight(block, EAST, 0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
			setIDAndRender(block, TRIANGLE_XP, x, y, z, EAST);
			break;
		default:
			RenderHelper.startDrawing(QUADS);
			setRenderBoundsAndRelight(block, EAST, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			setIDAndRender(block, NORMAL_XP, x, y, z, EAST);
			break;
		}
	}

}