package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import carpentersblocks.data.Slope;
import carpentersblocks.data.Slope.SlopeType;
import carpentersblocks.renderer.helper.RenderHelperWedge;
import carpentersblocks.renderer.helper.slope.HelperOblique;
import carpentersblocks.renderer.helper.slope.HelperPyramid;
import carpentersblocks.renderer.helper.slope.HelperWedge;
import carpentersblocks.renderer.helper.slope.HelperWedgeCorner;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.IconRegistry;

public class BlockHandlerCarpentersSlope extends BlockHandlerBase {

	// RENDER IDS
	private static final int WEDGE_YN = 0;
	private static final int WEDGE_YP = 1;
	private static final int WEDGE_SLOPED_ZN = 2;
	private static final int WEDGE_ZN = 3;
	private static final int WEDGE_SLOPED_ZP = 4;
	private static final int WEDGE_ZP = 5;
	private static final int WEDGE_SLOPED_XN = 6;
	private static final int WEDGE_XN = 7;
	private static final int WEDGE_SLOPED_XP = 8;
	private static final int WEDGE_XP = 9;
	private static final int WEDGE_CORNER_SLOPED_ZN = 10;
	private static final int WEDGE_CORNER_SLOPED_ZP = 11;
	private static final int WEDGE_CORNER_SLOPED_XN = 12;
	private static final int WEDGE_CORNER_SLOPED_XP = 13;
	private static final int OBL_CORNER_SLOPED_YN = 14;
	private static final int OBL_CORNER_SLOPED_YP = 15;
	private static final int PYR_YZNN = 16;
	private static final int PYR_YZNP = 17;
	private static final int PYR_YXNN = 18;
	private static final int PYR_YXNP = 19;
	private static final int PYR_YZPN = 20;
	private static final int PYR_YZPP = 21;
	private static final int PYR_YXPN = 22;
	private static final int PYR_YXPP = 23;

	/**
	 * Holds AO values for all six faces.
	 */
	private float[][] ao =
		{
			{ 1.0F, 1.0F, 1.0F, 1.0F }, // -Y DOWN
			{ 1.0F, 1.0F, 1.0F, 1.0F }, // +Y UP
			{ 1.0F, 1.0F, 1.0F, 1.0F }, // -Z NORTH / OLD IS EAST
			{ 1.0F, 1.0F, 1.0F, 1.0F }, // +Z SOUTH / OLD IS WEST
			{ 1.0F, 1.0F, 1.0F, 1.0F }, // -X WEST / OLD IS NORTH
			{ 1.0F, 1.0F, 1.0F, 1.0F }  // +X EAST / OLD IS SOUTH
		};

	/**
	 * Holds brightness values for all six faces.
	 */
	private int[][] brightness =
		{
			{ 0, 0, 0, 0 }, // -Y DOWN
			{ 0, 0, 0, 0 }, // +Y UP
			{ 0, 0, 0, 0 }, // -Z NORTH
			{ 0, 0, 0, 0 }, // +Z SOUTH
			{ 0, 0, 0, 0 }, // -X WEST
			{ 0, 0, 0, 0 }  // +X EAST
		};

	/**
	 * Holds offset AO values for all six faces.
	 * 
	 * When face AO lighting is computed for slopes,
	 * it also computes the AO values of the adjacent
	 * block's neighboring face.
	 */
	private float[][] offset_ao =
		{
			{ 1.0F, 1.0F, 1.0F, 1.0F }, // -Y DOWN
			{ 1.0F, 1.0F, 1.0F, 1.0F }, // +Y UP
			{ 1.0F, 1.0F, 1.0F, 1.0F }, // -Z NORTH
			{ 1.0F, 1.0F, 1.0F, 1.0F }, // +Z SOUTH
			{ 1.0F, 1.0F, 1.0F, 1.0F }, // -X WEST
			{ 1.0F, 1.0F, 1.0F, 1.0F }  // +X EAST
		};

	/**
	 * Holds offset brightness values for all six faces.
	 * 
	 * When face brightness is computed for slopes,
	 * it also computes the brightness of the adjacent
	 * block's neighboring face.
	 */
	private int[][] offset_brightness =
		{
			{ 0, 0, 0, 0 }, // -Y DOWN
			{ 0, 0, 0, 0 }, // +Y UP
			{ 0, 0, 0, 0 }, // -Z NORTH
			{ 0, 0, 0, 0 }, // +Z SOUTH
			{ 0, 0, 0, 0 }, // -X WEST
			{ 0, 0, 0, 0 }  // +X EAST
		};

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks) {

		Tessellator tessellator = Tessellator.instance;
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		renderBlocks.setRenderBounds(0, 0, 0, 1.0D, 1.0D, 1.0D);

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		RenderHelperWedge.renderFaceYNeg(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		HelperWedge.renderSlopeXNeg(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		RenderHelperWedge.renderFaceZNeg(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		RenderHelperWedge.renderFaceZPos(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		RenderHelperWedge.renderFaceXPos(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	/**
	 * Override to provide custom icons.
	 */
	protected Icon getUniqueIcon(Icon icon)
	{
		if (isSideSloped)
		{
			Slope slope = Slope.slopesList[BlockProperties.getData(TE)];
			int metadata = BlockProperties.getCoverMetadata(TE, coverRendering);

			/* Uncovered sloped oblique faces use triangular frame icons. */
			if (!BlockProperties.hasCover(TE, 6))
			{
				if (slope.slopeType.equals(SlopeType.OBLIQUE_INT)) {
					icon = slope.isPositive ? IconRegistry.icon_slope_oblique_pt_low : IconRegistry.icon_slope_oblique_pt_high;
				} else if (slope.slopeType.equals(SlopeType.OBLIQUE_EXT)) {
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
			} else if (block instanceof BlockDirectional && !slope.slopeType.equals(SlopeType.WEDGE_Y)) {
				icon = block.getBlockTextureFromSide(1);
			}
		}

		return icon;
	}

	/**
	 * Sets render bounds to full dimensions.
	 */
	@Override
	protected void setRenderBounds()
	{
		renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}

	/**
	 * Sets slope-specific renderID to identify which RenderHelper to use
	 * and passes control to side renderer.
	 */
	private void setIDAndRender(int renderID, int x, int y, int z, int side)
	{
		slopeRenderID = renderID;
		delegateSideRender(x, y, z, side);
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
			case WEDGE_YN:
				RenderHelperWedge.renderFaceYNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_YP:
				RenderHelperWedge.renderFaceYPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_SLOPED_ZN:
				HelperWedge.renderSlopeZNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_ZN:
				RenderHelperWedge.renderFaceZNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_SLOPED_ZP:
				HelperWedge.renderSlopeZPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_ZP:
				RenderHelperWedge.renderFaceZPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_SLOPED_XN:
				HelperWedge.renderSlopeXNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_XN:
				RenderHelperWedge.renderFaceXNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_SLOPED_XP:
				HelperWedge.renderSlopeXPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_XP:
				RenderHelperWedge.renderFaceXPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_CORNER_SLOPED_ZN:
				HelperWedgeCorner.renderSlopeZNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_CORNER_SLOPED_ZP:
				HelperWedgeCorner.renderSlopeZPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_CORNER_SLOPED_XN:
				HelperWedgeCorner.renderSlopeXNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case WEDGE_CORNER_SLOPED_XP:
				HelperWedgeCorner.renderSlopeXPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case OBL_CORNER_SLOPED_YN:
				HelperOblique.renderSlopeYNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case OBL_CORNER_SLOPED_YP:
				HelperOblique.renderSlopeYPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case PYR_YZNN:
				HelperPyramid.renderFaceYNegZNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case PYR_YZNP:
				HelperPyramid.renderFaceYNegZPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case PYR_YXNN:
				HelperPyramid.renderFaceYNegXNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case PYR_YXNP:
				HelperPyramid.renderFaceYNegXPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case PYR_YZPN:
				HelperPyramid.renderFaceYPosZNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case PYR_YZPP:
				HelperPyramid.renderFaceYPosZPos(renderBlocks, slopeID, x, y, z, icon);
				break;
			case PYR_YXPN:
				HelperPyramid.renderFaceYPosXNeg(renderBlocks, slopeID, x, y, z, icon);
				break;
			case PYR_YXPP:
				HelperPyramid.renderFaceYPosXPos(renderBlocks, slopeID, x, y, z, icon);
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

			int slopeID = BlockProperties.getData(TE);
			Slope slope = Slope.slopesList[slopeID];

			renderBlocks.enableAO = Minecraft.isAmbientOcclusionEnabled() && !disableAO && Block.lightValue[block.blockID] == 0;

			/*
			 * Populate AO and brightness tables.
			 */

			lightingHelper.setLightness(0.5F).setLightingYNeg(block, x, y, z);
			if (renderBlocks.enableAO) {
				ao[DOWN] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };
			}

			lightingHelper.setLightness(1.0F).setLightingYPos(block, x, y, z);
			if (renderBlocks.enableAO) {
				ao[UP] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };
			}

			lightingHelper.setLightness(0.8F).setLightingZNeg(block, x, y, z);
			if (renderBlocks.enableAO) {
				ao[NORTH] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };
			}

			lightingHelper.setLightness(0.8F).setLightingZPos(block, x, y, z);
			if (renderBlocks.enableAO) {
				ao[SOUTH] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };
			}

			lightingHelper.setLightness(0.6F).setLightingXNeg(block, x, y, z);
			if (renderBlocks.enableAO) {
				ao[WEST] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };
			}

			lightingHelper.setLightness(0.6F).setLightingXPos(block, x, y, z);
			if (renderBlocks.enableAO) {
				ao[EAST] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				brightness[EAST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };
			}

			/*
			 * Populate offset AO and brightness tables.
			 * 
			 * Offset values are used because where a slope
			 * meets a 90 degree face, lighting will
			 * sometimes be very dark.
			 */

			if (renderBlocks.enableAO)
			{
				lightingHelper.setLightness(0.5F).setLightingYNeg(block, x, y + 1, z);
				offset_ao[DOWN] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				offset_brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

				lightingHelper.setLightness(1.0F).setLightingYPos(block, x, y - 1, z);
				offset_ao[UP] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				offset_brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

				lightingHelper.setLightness(0.8F).setLightingZNeg(block, x, y, z + 1);
				offset_ao[NORTH] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				offset_brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

				lightingHelper.setLightness(0.8F).setLightingZPos(block, x, y, z - 1);
				offset_ao[SOUTH] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				offset_brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

				lightingHelper.setLightness(0.6F).setLightingXNeg(block, x + 1, y, z);
				offset_ao[WEST] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				offset_brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

				lightingHelper.setLightness(0.6F).setLightingXPos(block, x - 1, y, z);
				offset_ao[EAST] = new float[] { lightingHelper.base_ao[0], lightingHelper.base_ao[1], lightingHelper.base_ao[2], lightingHelper.base_ao[3] };
				offset_brightness[EAST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };
			}

			/*
			 * Render sloped faces.
			 */

			isSideSloped = true;

			switch (slope.slopeType)
			{
			case WEDGE_XZ:
				prepareHorizontalWedge(slope, x, y, z);
				break;
			case WEDGE_Y:
				prepareVerticalWedge(slope, x, y, z);
				break;
			case WEDGE_INT:
				prepareWedgeIntCorner(slope, x, y, z);
				break;
			case WEDGE_EXT:
				prepareWedgeExtCorner(slope, x, y, z);
				break;
			case OBLIQUE_INT:
				prepareObliqueIntCorner(slope, x, y, z);
				break;
			case OBLIQUE_EXT:
				prepareObliqueExtCorner(slope, x, y, z);
				break;
			case PYRAMID:
				preparePyramid(slope, x, y, z);
				break;
			}

			isSideSloped = false;

			/*
			 * Render non-sloped faces.
			 */

			/* BOTTOM FACE */
			if (slope.hasSide(ForgeDirection.DOWN) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, 0)) {
				prepareFaceYNeg(slope, x, y, z);
			}

			/* TOP FACE */
			if (slope.hasSide(ForgeDirection.UP) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, 1)) {
				prepareFaceYPos(slope, x, y, z);
			}

			/* NORTH FACE */
			if (slope.hasSide(ForgeDirection.NORTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, 2)) {
				prepareFaceZNeg(slope, x, y, z);
			}

			/* SOUTH FACE */
			if (slope.hasSide(ForgeDirection.SOUTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, 3)) {
				prepareFaceZPos(slope, x, y, z);
			}

			/* WEST FACE */
			if (slope.hasSide(ForgeDirection.WEST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, 4)) {
				prepareFaceXNeg(slope, x, y, z);
			}

			/* EAST FACE */
			if (slope.hasSide(ForgeDirection.EAST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, 5)) {
				prepareFaceXPos(slope, x, y, z);
			}

		}

		renderBlocks.enableAO = false;
		return true;
	}

	/**
	 * Will set lighting for wedge sloped faces.
	 */
	private void setWedgeSlopeLighting(Slope slope)
	{
		if (renderBlocks.enableAO)
		{
			switch (slope.slopeID)
			{
			case Slope.ID_WEDGE_NW:

				/* WEST - TL CORNER */
				lightingHelper.base_ao[3] = offset_ao[WEST][3];
				renderBlocks.brightnessBottomLeft = offset_brightness[WEST][3];
				/* WEST - BL CORNER */
				lightingHelper.base_ao[2] = offset_ao[WEST][2];
				renderBlocks.brightnessBottomRight = offset_brightness[WEST][2];
				/* NORTH - BR CORNER */
				lightingHelper.base_ao[1] = offset_ao[NORTH][1];
				renderBlocks.brightnessTopRight = offset_brightness[NORTH][1];
				/* NORTH - TR CORNER */
				lightingHelper.base_ao[0] = offset_ao[NORTH][0];
				renderBlocks.brightnessTopLeft = offset_brightness[NORTH][0];

				break;
			case Slope.ID_WEDGE_NE:

				/* NORTH - TL CORNER */
				lightingHelper.base_ao[3] = offset_ao[NORTH][3];
				renderBlocks.brightnessBottomLeft = offset_brightness[NORTH][3];
				/* NORTH - BL CORNER */
				lightingHelper.base_ao[2] = offset_ao[NORTH][2];
				renderBlocks.brightnessBottomRight = offset_brightness[NORTH][2];
				/* EAST - BR CORNER */
				lightingHelper.base_ao[1] = offset_ao[EAST][3];
				renderBlocks.brightnessTopRight = offset_brightness[EAST][3];
				/* EAST - TR CORNER */
				lightingHelper.base_ao[0] = offset_ao[EAST][2];
				renderBlocks.brightnessTopLeft = offset_brightness[EAST][2];

				break;
			case Slope.ID_WEDGE_SW:

				/* SOUTH - TL CORNER */
				lightingHelper.base_ao[0] = offset_ao[SOUTH][0];
				renderBlocks.brightnessTopLeft = offset_brightness[SOUTH][0];
				/* SOUTH - BL CORNER */
				lightingHelper.base_ao[3] = offset_ao[SOUTH][3];
				renderBlocks.brightnessBottomLeft = offset_brightness[SOUTH][3];
				/* WEST - BR CORNER */
				lightingHelper.base_ao[2] = offset_ao[WEST][1];
				renderBlocks.brightnessBottomRight = offset_brightness[WEST][1];
				/* WEST - TR CORNER */
				lightingHelper.base_ao[1] = offset_ao[WEST][0];
				renderBlocks.brightnessTopRight = offset_brightness[WEST][0];

				break;
			case Slope.ID_WEDGE_SE:

				/* EAST - TL CORNER */
				lightingHelper.base_ao[0] = offset_ao[EAST][1];
				renderBlocks.brightnessTopLeft = offset_brightness[EAST][1];
				/* EAST - BL CORNER */
				lightingHelper.base_ao[3] = offset_ao[EAST][0];
				renderBlocks.brightnessBottomLeft = offset_brightness[EAST][0];
				/* SOUTH - BR CORNER */
				lightingHelper.base_ao[2] = offset_ao[SOUTH][2];
				renderBlocks.brightnessBottomRight = offset_brightness[SOUTH][2];
				/* SOUTH - TR CORNER */
				lightingHelper.base_ao[1] = offset_ao[SOUTH][1];
				renderBlocks.brightnessTopRight = offset_brightness[SOUTH][1];

				break;
			case Slope.ID_WEDGE_NEG_N:

				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = offset_ao[DOWN][3];
				renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = ao[DOWN][0] == 1.0F ? 1.0F : offset_ao[NORTH][1];
				renderBlocks.brightnessTopLeft = ao[DOWN][0] == 1.0F ? brightness[DOWN][0] : offset_brightness[NORTH][1];
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = ao[DOWN][1] == 1.0F ? 1.0F : offset_ao[NORTH][2];
				renderBlocks.brightnessTopRight = ao[DOWN][1] == 1.0F ? brightness[DOWN][1] : offset_brightness[NORTH][2];
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = offset_ao[DOWN][2];
				renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];

				break;
			case Slope.ID_WEDGE_NEG_S:

				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = ao[DOWN][3] == 1.0F ? 1.0F : offset_ao[SOUTH][3];
				renderBlocks.brightnessBottomLeft = ao[DOWN][3] == 1.0F ? brightness[DOWN][3] : offset_brightness[SOUTH][3];
				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = offset_ao[DOWN][0];
				renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = offset_ao[DOWN][1];
				renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = ao[DOWN][2] == 1.0F ? 1.0F : offset_ao[SOUTH][2];
				renderBlocks.brightnessBottomRight = ao[DOWN][2] == 1.0F ? brightness[DOWN][2] : offset_brightness[SOUTH][2];

				break;
			case Slope.ID_WEDGE_NEG_W:

				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = offset_ao[DOWN][3];
				renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = offset_ao[DOWN][0];
				renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = ao[DOWN][1] == 1.0F ? 1.0F : offset_ao[WEST][1];
				renderBlocks.brightnessTopRight = ao[DOWN][1] == 1.0F ? brightness[DOWN][1] : offset_brightness[WEST][1];
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = ao[DOWN][2] == 1.0F ? 1.0F : offset_ao[WEST][2];
				renderBlocks.brightnessBottomRight = ao[DOWN][2] == 1.0F ? brightness[DOWN][2] : offset_brightness[WEST][2];

				break;
			case Slope.ID_WEDGE_NEG_E:

				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = ao[DOWN][3] == 1.0F ? 1.0F : offset_ao[EAST][3];
				renderBlocks.brightnessBottomLeft = ao[DOWN][3] == 1.0F ? brightness[DOWN][3] : offset_brightness[EAST][3];
				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = ao[DOWN][0] == 1.0F ? 1.0F : offset_ao[EAST][0];
				renderBlocks.brightnessTopLeft = ao[DOWN][0] == 1.0F ? brightness[DOWN][0] : offset_brightness[EAST][0];
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = offset_ao[DOWN][1];
				renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = offset_ao[DOWN][2];
				renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];

				break;
			case Slope.ID_WEDGE_POS_N:

				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = offset_ao[UP][2];
				renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = ao[UP][1] == 1.0F ? 1.0F : offset_ao[NORTH][0];
				renderBlocks.brightnessTopLeft = ao[UP][1] == 1.0F ? brightness[UP][1] : offset_brightness[NORTH][0];
				/* TOP - SE CORNER */
				lightingHelper.base_ao[0] = ao[UP][0] == 1.0F ? 1.0F : offset_ao[NORTH][3];
				renderBlocks.brightnessTopRight = ao[UP][0] == 1.0F ? brightness[UP][0] : offset_brightness[NORTH][3];
				/* TOP - NE CORNER */
				lightingHelper.base_ao[3] = offset_ao[UP][3];
				renderBlocks.brightnessBottomRight = offset_brightness[UP][3];

				break;
			case Slope.ID_WEDGE_POS_S:

				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = ao[UP][2] == 1.0F ? 1.0F : offset_ao[SOUTH][0];
				renderBlocks.brightnessBottomLeft = ao[UP][2] == 1.0F ? brightness[UP][2] : offset_brightness[SOUTH][0];
				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = offset_ao[UP][1];
				renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
				/* TOP - SE CORNER */
				lightingHelper.base_ao[0] = offset_ao[UP][0];
				renderBlocks.brightnessTopRight = offset_brightness[UP][0];
				/* TOP - NE CORNER */
				lightingHelper.base_ao[3] = ao[UP][3] == 1.0F ? 1.0F : offset_ao[SOUTH][1];
				renderBlocks.brightnessBottomRight = ao[UP][3] == 1.0F ? brightness[UP][3] : offset_brightness[SOUTH][1];

				break;
			case Slope.ID_WEDGE_POS_W:

				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = offset_ao[UP][2];
				renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = offset_ao[UP][1];
				renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
				/* TOP - SE CORNER */
				//
				lightingHelper.base_ao[0] = ao[UP][0] == 1.0F ? 1.0F : offset_ao[WEST][0];
				renderBlocks.brightnessTopRight = ao[UP][0] == 1.0F ? brightness[UP][0] : offset_brightness[WEST][0];
				/* TOP - NE CORNER */
				//
				lightingHelper.base_ao[3] = ao[UP][3] == 1.0F ? 1.0F : offset_ao[WEST][3];
				renderBlocks.brightnessBottomRight = ao[UP][3] == 1.0F ? brightness[UP][3] : offset_brightness[WEST][3];

				break;
			case Slope.ID_WEDGE_POS_E:

				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = ao[UP][2] == 1.0F ? 1.0F : offset_ao[EAST][2];
				renderBlocks.brightnessBottomLeft = ao[UP][2] == 1.0F ? brightness[UP][2] : offset_brightness[EAST][2];
				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = ao[UP][1] == 1.0F ? 1.0F : offset_ao[EAST][1];
				renderBlocks.brightnessTopLeft = ao[UP][1] == 1.0F ? brightness[UP][1] : offset_brightness[EAST][1];
				/* TOP - SE CORNER */
				lightingHelper.base_ao[0] = offset_ao[UP][0];
				renderBlocks.brightnessTopRight = offset_brightness[UP][0];
				/* TOP - NE CORNER */
				lightingHelper.base_ao[3] = offset_ao[UP][3];
				renderBlocks.brightnessBottomRight = offset_brightness[UP][3];

				break;
			}
		}
	}

	private void prepareHorizontalWedge(Slope slope, int x, int y, int z)
	{
		lightingHelper.setLightness(0.7F);
		setWedgeSlopeLighting(slope);

		if (slope.facings.contains(ForgeDirection.NORTH)) {
			setIDAndRender(WEDGE_SLOPED_ZN, x, y, z, EAST);
		} else {
			setIDAndRender(WEDGE_SLOPED_ZP, x, y, z, WEST);
		}
	}

	private void prepareVerticalWedge(Slope slope, int x, int y, int z)
	{
		setWedgeSlopeLighting(slope);

		if (slope.facings.contains(ForgeDirection.NORTH)) {
			lightingHelper.setLightness(slope.isPositive ? 0.9F : 0.65F);
			setIDAndRender(WEDGE_SLOPED_ZN, x, y, z, NORTH);
		} else if (slope.facings.contains(ForgeDirection.SOUTH)) {
			lightingHelper.setLightness(slope.isPositive ? 0.9F : 0.65F);
			setIDAndRender(WEDGE_SLOPED_ZP, x, y, z, SOUTH);
		} else if (slope.facings.contains(ForgeDirection.WEST)) {
			lightingHelper.setLightness(slope.isPositive ? 0.8F : 0.55F);
			setIDAndRender(WEDGE_SLOPED_XN, x, y, z, WEST);
		} else { // ForgeDirection.EAST
			lightingHelper.setLightness(slope.isPositive ? 0.8F : 0.55F);
			setIDAndRender(WEDGE_SLOPED_XP, x, y, z, EAST);
		}
	}

	private void prepareWedgeIntCorner(Slope slope, int x, int y, int z)
	{
		/* X Slope */

		Slope slope1 = slope.facings.contains(ForgeDirection.WEST) ? slope.isPositive ? Slope.WEDGE_POS_W : Slope.WEDGE_NEG_W : slope.isPositive ? Slope.WEDGE_POS_E : Slope.WEDGE_NEG_E;

		setWedgeSlopeLighting(slope1);
		lightingHelper.setLightness(slope.isPositive ? 0.8F : 0.55F);

		if (slope1.facings.contains(ForgeDirection.WEST)) {
			setIDAndRender(WEDGE_CORNER_SLOPED_XN, x, y, z, WEST);
		} else { // ForgeDirection.EAST
			setIDAndRender(WEDGE_CORNER_SLOPED_XP, x, y, z, EAST);
		}

		/* Z Slope */

		Slope slope2 = slope.facings.contains(ForgeDirection.NORTH) ? slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N : slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S;

		setWedgeSlopeLighting(slope2);
		lightingHelper.setLightness(slope.isPositive ? 0.9F : 0.65F);

		if (slope.facings.contains(ForgeDirection.NORTH)) {
			setIDAndRender(WEDGE_CORNER_SLOPED_ZN, x, y, z, NORTH);
		} else {
			setIDAndRender(WEDGE_CORNER_SLOPED_ZP, x, y, z, SOUTH);
		}
	}

	private void prepareWedgeExtCorner(Slope slope, int x, int y, int z)
	{
		/* X Slope */

		Slope slope1 = slope.facings.contains(ForgeDirection.WEST) ? slope.isPositive ? Slope.WEDGE_POS_W : Slope.WEDGE_NEG_W : slope.isPositive ? Slope.WEDGE_POS_E : Slope.WEDGE_NEG_E;

		setWedgeSlopeLighting(slope1);
		lightingHelper.setLightness(slope.isPositive ? 0.8F : 0.55F);

		if (slope1.facings.contains(ForgeDirection.WEST)) {
			setIDAndRender(WEDGE_CORNER_SLOPED_XN, x, y, z, WEST);
		} else { // ForgeDirection.EAST
			setIDAndRender(WEDGE_CORNER_SLOPED_XP, x, y, z, EAST);
		}

		/* Z Slope */

		Slope slope2 = slope.facings.contains(ForgeDirection.NORTH) ? slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N : slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S;

		setWedgeSlopeLighting(slope2);
		lightingHelper.setLightness(slope.isPositive ? 0.9F : 0.65F);

		if (slope.facings.contains(ForgeDirection.NORTH)) {
			setIDAndRender(WEDGE_CORNER_SLOPED_ZN, x, y, z, NORTH);
		} else {
			setIDAndRender(WEDGE_CORNER_SLOPED_ZP, x, y, z, SOUTH);
		}
	}

	/**
	 * Will set lighting for oblique interior sloped faces.
	 */
	private void setObliqueIntSlopeLighting(Slope slope)
	{
		if (renderBlocks.enableAO)
		{
			switch (slope.slopeID)
			{
			case Slope.ID_OBL_INT_NEG_NW:

				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = offset_ao[DOWN][3];
				renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = ao[DOWN][0] == 1.0F ? 1.0F : offset_ao[NORTH][1];
				renderBlocks.brightnessTopLeft = ao[DOWN][0] == 1.0F ? brightness[DOWN][0] : offset_brightness[NORTH][1];
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = ao[DOWN][2] == 1.0F ? 1.0F : offset_ao[WEST][2];
				renderBlocks.brightnessBottomRight = ao[DOWN][2] == 1.0F ? brightness[DOWN][2] : offset_brightness[WEST][2];

				break;
			case Slope.ID_OBL_INT_NEG_NE:

				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = ao[DOWN][3] == 1.0F ? 1.0F : offset_ao[EAST][3];
				renderBlocks.brightnessBottomLeft = ao[DOWN][3] == 1.0F ? brightness[DOWN][3] : offset_brightness[EAST][3];
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = ao[DOWN][1] == 1.0F ? 1.0F : offset_ao[NORTH][2];
				renderBlocks.brightnessTopRight = ao[DOWN][1] == 1.0F ? brightness[DOWN][1] : offset_brightness[NORTH][2];
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = offset_ao[DOWN][2];
				renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];

				break;
			case Slope.ID_OBL_INT_NEG_SW:

				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = ao[DOWN][3] == 1.0F ? 1.0F : offset_ao[SOUTH][3];
				renderBlocks.brightnessBottomLeft = ao[DOWN][3] == 1.0F ? brightness[DOWN][3] : offset_brightness[SOUTH][3];
				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = offset_ao[DOWN][0];
				renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = ao[DOWN][1] == 1.0F ? 1.0F : offset_ao[WEST][1];
				renderBlocks.brightnessTopRight = ao[DOWN][1] == 1.0F ? brightness[DOWN][1] : offset_brightness[WEST][1];

				break;
			case Slope.ID_OBL_INT_NEG_SE:

				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = ao[DOWN][0] == 1.0F ? 1.0F : offset_ao[EAST][0];
				renderBlocks.brightnessTopLeft = ao[DOWN][0] == 1.0F ? brightness[DOWN][0] : offset_brightness[EAST][0];
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = offset_ao[DOWN][1];
				renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = ao[DOWN][2] == 1.0F ? 1.0F : offset_ao[SOUTH][2];
				renderBlocks.brightnessBottomRight = ao[DOWN][2] == 1.0F ? brightness[DOWN][2] : offset_brightness[SOUTH][2];

				break;
			case Slope.ID_OBL_INT_POS_NW:

				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = offset_ao[UP][2];
				renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = ao[UP][1] == 1.0F ? 1.0F : offset_ao[NORTH][0];
				renderBlocks.brightnessTopLeft = ao[UP][1] == 1.0F ? brightness[UP][1] : offset_brightness[NORTH][0];
				/* TOP - NE CORNER */
				lightingHelper.base_ao[3] = ao[UP][3] == 1.0F ? 1.0F : offset_ao[WEST][3];
				renderBlocks.brightnessBottomRight = ao[UP][3] == 1.0F ? brightness[UP][3] : offset_brightness[WEST][3];

				break;
			case Slope.ID_OBL_INT_POS_NE:

				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = ao[UP][2] == 1.0F ? 1.0F : offset_ao[EAST][2];
				renderBlocks.brightnessBottomLeft = ao[UP][2] == 1.0F ? brightness[UP][2] : offset_brightness[EAST][2];
				/* TOP - SE CORNER */
				lightingHelper.base_ao[0] = ao[UP][0] == 1.0F ? 1.0F : offset_ao[NORTH][3];
				renderBlocks.brightnessTopRight = ao[UP][0] == 1.0F ? brightness[UP][0] : offset_brightness[NORTH][3];
				/* TOP - NE CORNER */
				lightingHelper.base_ao[3] = offset_ao[UP][3];
				renderBlocks.brightnessBottomRight = offset_brightness[UP][3];

				break;
			case Slope.ID_OBL_INT_POS_SW:

				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = ao[UP][2] == 1.0F ? 1.0F : offset_ao[SOUTH][0];
				renderBlocks.brightnessBottomLeft = ao[UP][2] == 1.0F ? brightness[UP][2] : offset_brightness[SOUTH][0];
				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = offset_ao[UP][1];
				renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
				/* TOP - SE CORNER */
				lightingHelper.base_ao[0] = ao[UP][0] == 1.0F ? 1.0F : offset_ao[WEST][0];
				renderBlocks.brightnessTopRight = ao[UP][0] == 1.0F ? brightness[UP][0] : offset_brightness[WEST][0];

				break;
			case Slope.ID_OBL_INT_POS_SE:

				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = ao[UP][1] == 1.0F ? 1.0F : offset_ao[EAST][1];
				renderBlocks.brightnessTopLeft = ao[UP][1] == 1.0F ? brightness[UP][1] : offset_brightness[EAST][1];
				/* TOP - SE CORNER */
				lightingHelper.base_ao[0] = offset_ao[UP][0];
				renderBlocks.brightnessTopRight = offset_brightness[UP][0];
				/* TOP - NE CORNER */
				lightingHelper.base_ao[3] = ao[UP][3] == 1.0F ? 1.0F : offset_ao[SOUTH][1];
				renderBlocks.brightnessBottomRight = ao[UP][3] == 1.0F ? brightness[UP][3] : offset_brightness[SOUTH][1];

				break;
			}
		}
	}

	private void prepareObliqueIntCorner(Slope slope, int x, int y, int z)
	{
		setObliqueIntSlopeLighting(slope);

		if (slope.isPositive) {
			lightingHelper.setLightness(0.85F);
			setIDAndRender(OBL_CORNER_SLOPED_YP, x, y, z, UP);
		} else {
			lightingHelper.setLightness(0.6F);
			setIDAndRender(OBL_CORNER_SLOPED_YN, x, y, z, DOWN);
		}
	}

	/**
	 * Will set lighting for oblique exterior sloped faces.
	 */
	private void setObliqueExtSlopeLighting(Slope slope)
	{
		if (renderBlocks.enableAO)
		{
			switch (slope.slopeID)
			{
			case Slope.ID_OBL_EXT_NEG_NW:

				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = offset_ao[DOWN][0];
				renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = ao[DOWN][1] == 1.0F ? 1.0F : (offset_ao[NORTH][2] + offset_ao[WEST][1]) / 2.0F;
				renderBlocks.brightnessTopRight = ao[DOWN][1] == 1.0F ? brightness[DOWN][1] : (offset_brightness[NORTH][2] + offset_brightness[WEST][1]) / 2;
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = offset_ao[DOWN][2];
				renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];

				break;
			case Slope.ID_OBL_EXT_NEG_NE:

				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = offset_ao[DOWN][3];
				renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = ao[DOWN][0] == 1.0F ? 1.0F : (offset_ao[NORTH][1] + offset_ao[EAST][0]) / 2.0F;
				renderBlocks.brightnessTopLeft = ao[DOWN][0] == 1.0F ? brightness[DOWN][0] : (offset_brightness[NORTH][1] + offset_brightness[EAST][0]) / 2;
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = offset_ao[DOWN][1];
				renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];

				break;
			case Slope.ID_OBL_EXT_NEG_SW:

				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = offset_ao[DOWN][3];
				renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = offset_ao[DOWN][1];
				renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = ao[DOWN][2] == 1.0F ? 1.0F : (offset_ao[SOUTH][2] + offset_ao[WEST][2]) / 2.0F;
				renderBlocks.brightnessBottomRight = ao[DOWN][2] == 1.0F ? brightness[DOWN][2] : (offset_brightness[SOUTH][2] + offset_brightness[WEST][2]) / 2;

				break;
			case Slope.ID_OBL_EXT_NEG_SE:

				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = ao[DOWN][3] == 1.0F ? 1.0F : (offset_ao[SOUTH][3] + offset_ao[EAST][3]) / 2.0F;
				renderBlocks.brightnessBottomLeft = ao[DOWN][3] == 1.0F ? brightness[DOWN][3] : (offset_brightness[SOUTH][3] + offset_brightness[EAST][3]) / 2;
				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = offset_ao[DOWN][0];
				renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = offset_ao[DOWN][2];
				renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];

				break;
			case Slope.ID_OBL_EXT_POS_NW:

				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = offset_ao[UP][1];
				renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
				/* TOP - SE CORNER */
				lightingHelper.base_ao[0] = ao[UP][0] == 1.0F ? 1.0F : (offset_ao[WEST][0] + offset_ao[NORTH][3]) / 2.0F;
				renderBlocks.brightnessTopRight = ao[UP][0] == 1.0F ? brightness[UP][0] : (offset_brightness[WEST][0] + offset_brightness[NORTH][3]) / 2;
				/* TOP - NE CORNER */
				lightingHelper.base_ao[3] = offset_ao[UP][3];
				renderBlocks.brightnessBottomRight = offset_brightness[UP][3];

				break;
			case Slope.ID_OBL_EXT_POS_NE:

				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = offset_ao[UP][2];
				renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = ao[UP][1] == 1.0F ? 1.0F : (offset_ao[NORTH][0] + offset_ao[EAST][1]) / 2.0F;
				renderBlocks.brightnessTopLeft = ao[UP][1] == 1.0F ? brightness[UP][1] : (offset_brightness[NORTH][0] + offset_brightness[EAST][1]) / 2;
				/* TOP - SE CORNER */
				lightingHelper.base_ao[0] = offset_ao[UP][0];
				renderBlocks.brightnessTopRight = offset_brightness[UP][0];

				break;
			case Slope.ID_OBL_EXT_POS_SW:

				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = offset_ao[UP][2];
				renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
				/* TOP - SE CORNER */
				lightingHelper.base_ao[0] = offset_ao[UP][0];
				renderBlocks.brightnessTopRight = offset_brightness[UP][0];
				/* TOP - NE CORNER */
				lightingHelper.base_ao[3] = ao[UP][3] == 1.0F ? 1.0F : (offset_ao[SOUTH][1] + offset_ao[WEST][3]) / 2.0F;
				renderBlocks.brightnessBottomRight = ao[UP][3] == 1.0F ? brightness[UP][3] : (offset_brightness[SOUTH][1] + offset_brightness[WEST][3]) / 2;

				break;
			case Slope.ID_OBL_EXT_POS_SE:

				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = ao[UP][2] == 1.0F ? 1.0F : (offset_ao[EAST][2] + offset_ao[SOUTH][0]) / 2.0F;
				renderBlocks.brightnessBottomLeft = ao[UP][2] == 1.0F ? brightness[UP][2] : (offset_brightness[EAST][2] + offset_brightness[SOUTH][0]) / 2;
				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = offset_ao[UP][1];
				renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
				/* TOP - NE CORNER */
				lightingHelper.base_ao[3] = offset_ao[UP][3];
				renderBlocks.brightnessBottomRight = offset_brightness[UP][3];

				break;
			}
		}
	}

	private void prepareObliqueExtCorner(Slope slope, int x, int y, int z)
	{
		setObliqueExtSlopeLighting(slope);

		if (slope.isPositive) {
			lightingHelper.setLightness(0.85F);
			setIDAndRender(OBL_CORNER_SLOPED_YP, x, y, z, UP);
		} else {
			lightingHelper.setLightness(0.6F);
			setIDAndRender(OBL_CORNER_SLOPED_YN, x, y, z, DOWN);
		}
	}

	private void preparePyramid(Slope slope, int x, int y, int z)
	{
		float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z);
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		switch (slope.slopeID)
		{
		case Slope.ID_PYR_HALF_NEG:

			if (renderBlocks.enableAO)
			{
				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = offset_ao[DOWN][3];
				renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = offset_ao[DOWN][0];
				renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
				/* MIDDLE - CENTER */
				lightingHelper.base_ao[2] = aoLightValue;
				renderBlocks.brightnessBottomRight = mixedBrightness;
			}

			lightingHelper.setLightness(0.55F);
			setIDAndRender(PYR_YXNN, x, y, z, DOWN);

			if (renderBlocks.enableAO)
			{
				/* MIDDLE - CENTER */
				lightingHelper.base_ao[0] = aoLightValue;
				renderBlocks.brightnessTopLeft = mixedBrightness;
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = offset_ao[DOWN][1];
				renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = offset_ao[DOWN][2];
				renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];
			}

			lightingHelper.setLightness(0.55F);
			setIDAndRender(PYR_YXNP, x, y, z, DOWN);

			if (renderBlocks.enableAO)
			{
				/* DOWN - NW CORNER */
				lightingHelper.base_ao[3] = offset_ao[DOWN][3];
				renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
				/* MIDDLE - CENTER */
				lightingHelper.base_ao[0] = aoLightValue;
				renderBlocks.brightnessTopLeft = mixedBrightness;
				/* DOWN - NE CORNER */
				lightingHelper.base_ao[2] = offset_ao[DOWN][2];
				renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];
			}

			lightingHelper.setLightness(0.65F);
			setIDAndRender(PYR_YZNN, x, y, z, DOWN);

			if (renderBlocks.enableAO)
			{
				/* MIDDLE - CENTER */
				lightingHelper.base_ao[3] = aoLightValue;
				renderBlocks.brightnessBottomLeft = mixedBrightness;
				/* DOWN - SW CORNER */
				lightingHelper.base_ao[0] = offset_ao[DOWN][0];
				renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
				/* DOWN - SE CORNER */
				lightingHelper.base_ao[1] = offset_ao[DOWN][1];
				renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
			}

			lightingHelper.setLightness(0.65F);
			setIDAndRender(PYR_YZNP, x, y, z, DOWN);

			break;
		case Slope.ID_PYR_HALF_POS:

			if (renderBlocks.enableAO)
			{
				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = offset_ao[UP][2];
				renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = offset_ao[UP][1];
				renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
				/* MIDDLE - CENTER */
				lightingHelper.base_ao[0] = aoLightValue;
				renderBlocks.brightnessTopRight = mixedBrightness;
			}

			lightingHelper.setLightness(0.8F);
			setIDAndRender(PYR_YXPN, x, y, z, UP);

			if (renderBlocks.enableAO)
			{
				/* MIDDLE - CENTER */
				lightingHelper.base_ao[2] = aoLightValue;
				renderBlocks.brightnessBottomLeft = mixedBrightness;
				/* TOP - SE CORNER */
				lightingHelper.base_ao[0] = offset_ao[UP][0];
				renderBlocks.brightnessTopRight = offset_brightness[UP][0];
				/* TOP - NE CORNER */
				lightingHelper.base_ao[3] = offset_ao[UP][3];
				renderBlocks.brightnessBottomRight = offset_brightness[UP][3];
			}

			lightingHelper.setLightness(0.8F);
			setIDAndRender(PYR_YXPP, x, y, z, UP);

			if (renderBlocks.enableAO)
			{
				/* TOP - NW CORNER */
				lightingHelper.base_ao[2] = offset_ao[UP][2];
				renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
				/* MIDDLE - CENTER */
				lightingHelper.base_ao[0] = aoLightValue;
				renderBlocks.brightnessTopRight = mixedBrightness;
				/* TOP - NE CORNER */
				lightingHelper.base_ao[3] = offset_ao[UP][3];
				renderBlocks.brightnessBottomRight = offset_brightness[UP][3];
			}

			lightingHelper.setLightness(0.9F);
			setIDAndRender(PYR_YZPN, x, y, z, UP);

			if (renderBlocks.enableAO)
			{
				/* TOP - SW CORNER */
				lightingHelper.base_ao[1] = offset_ao[UP][1];
				renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
				/* TOP - SE CORNER */
				lightingHelper.base_ao[0] = offset_ao[UP][0];
				renderBlocks.brightnessTopRight = offset_brightness[UP][0];
				/* MIDDLE - CENTER */
				lightingHelper.base_ao[3] = aoLightValue;
				renderBlocks.brightnessBottomRight = mixedBrightness;
			}

			lightingHelper.setLightness(0.9F);
			setIDAndRender(PYR_YZPP, x, y, z, UP);

			break;
		}
	}

	/**
	 * Prepare bottom face.
	 */
	private void prepareFaceYNeg(Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			/* DOWN - NW CORNER */
			lightingHelper.base_ao[3] = ao[DOWN][3];
			renderBlocks.brightnessBottomLeft = brightness[DOWN][3];
			/* DOWN - SW CORNER */
			lightingHelper.base_ao[0] = ao[DOWN][0];
			renderBlocks.brightnessTopLeft = brightness[DOWN][0];
			/* DOWN - SE CORNER */
			lightingHelper.base_ao[1] = ao[DOWN][1];
			renderBlocks.brightnessTopRight = brightness[DOWN][1];
			/* DOWN - NE CORNER */
			lightingHelper.base_ao[2] = ao[DOWN][2];
			renderBlocks.brightnessBottomRight = brightness[DOWN][2];
		}

		lightingHelper.setLightness(0.5F);
		setIDAndRender(WEDGE_YN, x, y, z, DOWN);
	}

	/**
	 * Prepare top face.
	 */
	private void prepareFaceYPos(Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			/* TOP - NW CORNER */
			lightingHelper.base_ao[2] = ao[UP][2];
			renderBlocks.brightnessBottomLeft = brightness[UP][2];
			/* TOP - SW CORNER */
			lightingHelper.base_ao[1] = ao[UP][1];
			renderBlocks.brightnessTopLeft = brightness[UP][1];
			/* TOP - SE CORNER */
			lightingHelper.base_ao[0] = ao[UP][0];
			renderBlocks.brightnessTopRight = brightness[UP][0];
			/* TOP - NE CORNER */
			lightingHelper.base_ao[3] = ao[UP][3];
			renderBlocks.brightnessBottomRight = brightness[UP][3];
		}

		lightingHelper.setLightness(1.0F);
		setIDAndRender(WEDGE_YP, x, y, z, UP);
	}

	/**
	 * Prepare North face.
	 */
	private void prepareFaceZNeg(Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			/* NORTH - TL CORNER */
			lightingHelper.base_ao[3] = ao[NORTH][3];
			renderBlocks.brightnessBottomLeft = brightness[NORTH][3];
			/* NORTH - BL CORNER */
			lightingHelper.base_ao[2] = ao[NORTH][2];
			renderBlocks.brightnessBottomRight = brightness[NORTH][2];
			/* NORTH - BR CORNER */
			lightingHelper.base_ao[1] = ao[NORTH][1];
			renderBlocks.brightnessTopRight = brightness[NORTH][1];
			/* NORTH - TR CORNER */
			lightingHelper.base_ao[0] = ao[NORTH][0];
			renderBlocks.brightnessTopLeft = brightness[NORTH][0];
		}

		lightingHelper.setLightness(0.8F);
		setIDAndRender(WEDGE_ZN, x, y, z, NORTH);
	}

	/**
	 * Prepare South face.
	 */
	private void prepareFaceZPos(Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			/* SOUTH - TL CORNER */
			lightingHelper.base_ao[0] = ao[SOUTH][0];
			renderBlocks.brightnessTopLeft = brightness[SOUTH][0];
			/* SOUTH - BL CORNER */
			lightingHelper.base_ao[3] = ao[SOUTH][3];
			renderBlocks.brightnessBottomLeft = brightness[SOUTH][3];
			/* SOUTH - BR CORNER */
			lightingHelper.base_ao[2] = ao[SOUTH][2];
			renderBlocks.brightnessBottomRight = brightness[SOUTH][2];
			/* SOUTH - TR CORNER */
			lightingHelper.base_ao[1] = ao[SOUTH][1];
			renderBlocks.brightnessTopRight = brightness[SOUTH][1];
		}

		lightingHelper.setLightness(0.8F);
		setIDAndRender(WEDGE_ZP, x, y, z, SOUTH);
	}

	/**
	 * Prepare West face.
	 */
	private void prepareFaceXNeg(Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			/* WEST - TL CORNER */
			lightingHelper.base_ao[3] = ao[WEST][3];
			renderBlocks.brightnessBottomLeft = brightness[WEST][3];
			/* WEST - BL CORNER */
			lightingHelper.base_ao[2] = ao[WEST][2];
			renderBlocks.brightnessBottomRight = brightness[WEST][2];
			/* WEST - BR CORNER */
			lightingHelper.base_ao[1] = ao[WEST][1];
			renderBlocks.brightnessTopRight = brightness[WEST][1];
			/* WEST - TR CORNER */
			lightingHelper.base_ao[0] = ao[WEST][0];
			renderBlocks.brightnessTopLeft = brightness[WEST][0];
		}

		lightingHelper.setLightness(0.6F);
		setIDAndRender(WEDGE_XN, x, y, z, WEST);
	}

	/**
	 * Prepare East face.
	 */
	private void prepareFaceXPos(Slope slope, int x, int y, int z)
	{
		if (renderBlocks.enableAO)
		{
			/* EAST - TL CORNER */
			lightingHelper.base_ao[1] = ao[EAST][1];
			renderBlocks.brightnessBottomLeft = brightness[EAST][1];
			/* EAST - BL CORNER */
			lightingHelper.base_ao[0] = ao[EAST][0];
			renderBlocks.brightnessTopLeft = brightness[EAST][0];
			/* EAST - BR CORNER */
			lightingHelper.base_ao[3] = ao[EAST][3];
			renderBlocks.brightnessTopRight = brightness[EAST][3];
			/* EAST - TR CORNER */
			lightingHelper.base_ao[2] = ao[EAST][2];
			renderBlocks.brightnessBottomRight = brightness[EAST][2];
		}

		lightingHelper.setLightness(0.6F);
		setIDAndRender(WEDGE_XP, x, y, z, EAST);
	}

}