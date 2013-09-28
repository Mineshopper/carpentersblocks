package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import carpentersblocks.block.BlockBase;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.slope.RenderHelperObliqueCorner;
import carpentersblocks.renderer.helper.slope.RenderHelperPyramid;
import carpentersblocks.renderer.helper.slope.RenderHelperWedge;
import carpentersblocks.renderer.helper.slope.RenderHelperWedgeCorner;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;

public class BlockHandlerCarpentersSlope extends BlockHandlerBase
{

	// FACE
	private static final int DOWN = 0;
	private static final int UP = 1;
	private static final int NORTH = 2;
	private static final int SOUTH = 3;
	private static final int WEST = 4;
	private static final int EAST = 5;

	// RENDER IDS
	private static final int WEDGE_SLOPED_YN = 0;
	private static final int WEDGE_YN = 1;
	private static final int WEDGE_SLOPED_YP = 2;
	private static final int WEDGE_YP = 3;
	private static final int WEDGE_ZN = 4;
	private static final int WEDGE_ZP = 5;
	private static final int WEDGE_SLOPED_ZN = 6;
	private static final int WEDGE_XN = 7;
	private static final int WEDGE_SLOPED_ZP = 8;
	private static final int WEDGE_XP = 9;
	private static final int WEDGE_CORNER_YZZNNP = 10;
	private static final int WEDGE_CORNER_YXXNNP = 11;
	private static final int WEDGE_CORNER_YZZPNP = 12;
	private static final int WEDGE_CORNER_YXXPNP = 13;
	private static final int WEDGE_CORNER_ZN = 14;
	private static final int WEDGE_CORNER_ZP = 15;
	private static final int WEDGE_CORNER_XN = 16;
	private static final int WEDGE_CORNER_XP = 17;
	private static final int OBL_CORNER_SLOPED_YN = 18;
	private static final int OBL_CORNER_YN = 19;
	private static final int OBL_CORNER_SLOPED_YP = 20;
	private static final int OBL_CORNER_YP = 21;
	private static final int OBL_CORNER_ZN = 22;
	private static final int OBL_CORNER_ZP = 23;
	private static final int OBL_CORNER_XN = 24;
	private static final int OBL_CORNER_XP = 25;
	private static final int PYR_YZNN = 26;
	private static final int PYR_YZNP = 27;
	private static final int PYR_YXNN = 28;
	private static final int PYR_YXNP = 29;
	private static final int PYR_YZPN = 30;
	private static final int PYR_YZPP = 31;
	private static final int PYR_YXPN = 32;
	private static final int PYR_YXPP = 33;

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
		RenderHelperWedge.renderFaceYNeg(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(0));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		RenderHelperWedge.renderSlopeYPos(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(1));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		RenderHelperWedge.renderFaceZNeg(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(2));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		RenderHelperWedge.renderFaceZPos(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(2));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		RenderHelperWedge.renderFaceXPos(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(2));
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	/**
	 * Sets slope-specific variables and calls prepareRender().
	 */
	 private void prepareSlopeRender(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int side, int renderID, boolean slopedUpOrDown, int x, int y, int z, float lightness)
	 {
		 isFaceSlopingUpOrDown = slopedUpOrDown;
		 slopeRenderID = renderID;

		 prepareRender(TE, renderBlocks, coverBlock, srcBlock, side, x, y, z, lightness);
	 }

	 @Override
	 /**
	  * Renders side.
	  */
	 protected void renderSide(TECarpentersBlock TE, RenderBlocks renderBlocks, int side, double offset, int x, int y, int z, Icon icon)
	 {
		 if (isSideCover) {

			 super.renderSide(TE, renderBlocks, side, offset, x, y, z, icon);

		 } else {

			 int slopeID = BlockProperties.getData(TE);

			 switch (slopeRenderID)
			 {
			 case WEDGE_SLOPED_YN:
				 RenderHelperWedge.renderSlopeYNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_YN:
				 RenderHelperWedge.renderFaceYNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_SLOPED_YP:
				 RenderHelperWedge.renderSlopeYPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_YP:
				 RenderHelperWedge.renderFaceYPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_ZN:
				 RenderHelperWedge.renderFaceZNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_ZP:
				 RenderHelperWedge.renderFaceZPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_SLOPED_ZN:
				 RenderHelperWedge.renderSlopeZNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_XN:
				 RenderHelperWedge.renderFaceXNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_SLOPED_ZP:
				 RenderHelperWedge.renderSlopeZPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_XP:
				 RenderHelperWedge.renderFaceXPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_CORNER_YZZNNP:
				 RenderHelperWedgeCorner.renderFaceYNegZNegZPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_CORNER_YXXNNP:
				 RenderHelperWedgeCorner.renderFaceYNegXNegXPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_CORNER_YZZPNP:
				 RenderHelperWedgeCorner.renderFaceYPosZNegZPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_CORNER_YXXPNP:
				 RenderHelperWedgeCorner.renderFaceYPosXNegXPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_CORNER_ZN:
				 RenderHelperWedgeCorner.renderFaceZNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_CORNER_ZP:
				 RenderHelperWedgeCorner.renderFaceZPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_CORNER_XN:
				 RenderHelperWedgeCorner.renderFaceXNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case WEDGE_CORNER_XP:
				 RenderHelperWedgeCorner.renderFaceXPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case OBL_CORNER_YN:
				 RenderHelperObliqueCorner.renderFaceYNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case OBL_CORNER_SLOPED_YN:
				 RenderHelperObliqueCorner.renderSlopeYNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case OBL_CORNER_YP:
				 RenderHelperObliqueCorner.renderFaceYPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case OBL_CORNER_SLOPED_YP:
				 RenderHelperObliqueCorner.renderSlopeYPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case OBL_CORNER_ZN:
				 RenderHelperObliqueCorner.renderFaceZNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case OBL_CORNER_ZP:
				 RenderHelperObliqueCorner.renderFaceZPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case OBL_CORNER_XN:
				 RenderHelperObliqueCorner.renderFaceXNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case OBL_CORNER_XP:
				 RenderHelperObliqueCorner.renderFaceXPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case PYR_YZNN:
				 RenderHelperPyramid.renderFaceYNegZNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case PYR_YZNP:
				 RenderHelperPyramid.renderFaceYNegZPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case PYR_YXNN:
				 RenderHelperPyramid.renderFaceYNegXNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case PYR_YXNP:
				 RenderHelperPyramid.renderFaceYNegXPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case PYR_YZPN:
				 RenderHelperPyramid.renderFaceYPosZNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case PYR_YZPP:
				 RenderHelperPyramid.renderFaceYPosZPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case PYR_YXPN:
				 RenderHelperPyramid.renderFaceYPosXNeg(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 case PYR_YXPP:
				 RenderHelperPyramid.renderFaceYPosXPos(renderBlocks, slopeID, x, y, z, icon);
				 break;
			 }
		 }
	 }

	 @Override
	 /**
	  * Render slope using Ambient Occlusion (both minimum and maximum are handled here)
	  */
	 public boolean renderStandardSlopeWithAmbientOcclusion(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z, float red, float green, float blue)
	 {
		 int slopeID = BlockProperties.getData(TE);
		 Slope slope = Slope.slopesList[slopeID];

		 renderBlocks.enableAO = true;
		 coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
		 Tessellator tessellator = Tessellator.instance;
		 tessellator.setBrightness(983055);

		 /*
		  * Populate AO and brightness tables.
		  */

		 setLightnessYNeg(renderBlocks, coverBlock, x, y, z);
		 ao[DOWN] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 setLightnessYPos(renderBlocks, coverBlock, x, y, z);
		 ao[UP] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 setLightnessZNeg(renderBlocks, coverBlock, x, y, z);
		 ao[NORTH] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 setLightnessZPos(renderBlocks, coverBlock, x, y, z);
		 ao[SOUTH] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 setLightnessXNeg(renderBlocks, coverBlock, x, y, z);
		 ao[WEST] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 setLightnessXPos(renderBlocks, coverBlock, x, y, z);
		 ao[EAST] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 brightness[EAST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 /*
		  * Populate offset AO and brightness tables.
		  * 
		  * Offset values are used because where a slope
		  * meets a 90 degree face, lighting will
		  * sometimes be very dark.
		  */

		 setLightnessYNeg(renderBlocks, coverBlock, x, y + 1, z);
		 offset_ao[DOWN] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 offset_brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 setLightnessYPos(renderBlocks, coverBlock, x, y - 1, z);
		 offset_ao[UP] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 offset_brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 setLightnessZNeg(renderBlocks, coverBlock, x, y, z + 1);
		 offset_ao[NORTH] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 offset_brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 setLightnessZPos(renderBlocks, coverBlock, x, y, z - 1);
		 offset_ao[SOUTH] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 offset_brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 setLightnessXNeg(renderBlocks, coverBlock, x + 1, y, z);
		 offset_ao[WEST] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 offset_brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 setLightnessXPos(renderBlocks, coverBlock, x - 1, y, z);
		 offset_ao[EAST] = new float[] { base_ao[0], base_ao[1], base_ao[2], base_ao[3] };
		 offset_brightness[EAST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessTopRight, renderBlocks.brightnessBottomRight, renderBlocks.brightnessBottomLeft };

		 /*
		  * Render sloped faces.
		  * 
		  * All sloped faces (excluding side slopes) are
		  * vertically-adjusted top and bottom faces.
		  * 
		  * Icons are mapped accordingly in setupIcon()
		  * in super class.
		  */

		 switch (slope.slopeType)
		 {
		 case WEDGE_XZ:
			 prepareHorizontalWedge(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case WEDGE_Y:
			 prepareVerticalWedge(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case WEDGE_INT:
			 prepareWedgeIntCorner(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case WEDGE_EXT:
			 prepareWedgeExtCorner(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case OBLIQUE_INT:
			 prepareObliqueIntCorner(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case OBLIQUE_EXT:
			 prepareObliqueExtCorner(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case PYRAMID:
			 preparePyramid(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 }

		 /*
		  * Render non-sloped faces.
		  */

		 /* BOTTOM FACE */
		 if (slope.hasSide(ForgeDirection.DOWN) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, 0))
			 prepareFaceYNeg(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);

		 /* TOP FACE */
		 if (slope.hasSide(ForgeDirection.UP) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, 1))
			 prepareFaceYPos(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);

		 /* NORTH FACE */
		 if (slope.hasSide(ForgeDirection.NORTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, 2))
			 prepareFaceZNeg(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);

		 /* SOUTH FACE */
		 if (slope.hasSide(ForgeDirection.SOUTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, 3))
			 prepareFaceZPos(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);

		 /* WEST FACE */
		 if (slope.hasSide(ForgeDirection.WEST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, 4))
			 prepareFaceXNeg(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);

		 /* EAST FACE */
		 if (slope.hasSide(ForgeDirection.EAST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, 5))
			 prepareFaceXPos(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);

		 renderBlocks.enableAO = false;
		 return true;
	 }

	 /**
	  * Will set lighting for wedge sloped faces.
	  */
	 private void setWedgeSlopeLighting(RenderBlocks renderBlocks, Slope slope)
	 {
		 switch (slope.slopeID)
		 {
		 case Slope.ID_WEDGE_NW:

			 /* WEST - TL CORNER */
			 base_ao[3] = offset_ao[WEST][3];
			 renderBlocks.brightnessBottomLeft = offset_brightness[WEST][3];
			 /* WEST - BL CORNER */
			 base_ao[2] = offset_ao[WEST][2];
			 renderBlocks.brightnessBottomRight = offset_brightness[WEST][2];
			 /* NORTH - BR CORNER */
			 base_ao[1] = offset_ao[NORTH][1];
			 renderBlocks.brightnessTopRight = offset_brightness[NORTH][1];
			 /* NORTH - TR CORNER */
			 base_ao[0] = offset_ao[NORTH][0];
			 renderBlocks.brightnessTopLeft = offset_brightness[NORTH][0];

			 break;
		 case Slope.ID_WEDGE_NE:

			 /* NORTH - TL CORNER */
			 base_ao[3] = offset_ao[NORTH][3];
			 renderBlocks.brightnessBottomLeft = offset_brightness[NORTH][3];
			 /* NORTH - BL CORNER */
			 base_ao[2] = offset_ao[NORTH][2];
			 renderBlocks.brightnessBottomRight = offset_brightness[NORTH][2];
			 /* EAST - BR CORNER */
			 base_ao[1] = offset_ao[EAST][3];
			 renderBlocks.brightnessTopRight = offset_brightness[EAST][3];
			 /* EAST - TR CORNER */
			 base_ao[0] = offset_ao[EAST][2];
			 renderBlocks.brightnessTopLeft = offset_brightness[EAST][2];

			 break;
		 case Slope.ID_WEDGE_SW:

			 /* SOUTH - TL CORNER */
			 base_ao[0] = offset_ao[SOUTH][0];
			 renderBlocks.brightnessTopLeft = offset_brightness[SOUTH][0];
			 /* SOUTH - BL CORNER */
			 base_ao[3] = offset_ao[SOUTH][3];
			 renderBlocks.brightnessBottomLeft = offset_brightness[SOUTH][3];
			 /* WEST - BR CORNER */
			 base_ao[2] = offset_ao[WEST][1];
			 renderBlocks.brightnessBottomRight = offset_brightness[WEST][1];
			 /* WEST - TR CORNER */
			 base_ao[1] = offset_ao[WEST][0];
			 renderBlocks.brightnessTopRight = offset_brightness[WEST][0];

			 break;
		 case Slope.ID_WEDGE_SE:

			 /* EAST - TL CORNER */
			 base_ao[0] = offset_ao[EAST][1];
			 renderBlocks.brightnessTopLeft = offset_brightness[EAST][1];
			 /* EAST - BL CORNER */
			 base_ao[3] = offset_ao[EAST][0];
			 renderBlocks.brightnessBottomLeft = offset_brightness[EAST][0];
			 /* SOUTH - BR CORNER */
			 base_ao[2] = offset_ao[SOUTH][2];
			 renderBlocks.brightnessBottomRight = offset_brightness[SOUTH][2];
			 /* SOUTH - TR CORNER */
			 base_ao[1] = offset_ao[SOUTH][1];
			 renderBlocks.brightnessTopRight = offset_brightness[SOUTH][1];

			 break;
		 case Slope.ID_WEDGE_NEG_N:

			 /* DOWN - NW CORNER */
			 base_ao[3] = offset_ao[DOWN][3];
			 renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
			 /* DOWN - SW CORNER */
			 base_ao[0] = ao[DOWN][0] == 1.0F ? 1.0F : offset_ao[NORTH][1];
			 renderBlocks.brightnessTopLeft = ao[DOWN][0] == 1.0F ? brightness[DOWN][0] : offset_brightness[NORTH][1];
			 /* DOWN - SE CORNER */
			 base_ao[1] = ao[DOWN][1] == 1.0F ? 1.0F : offset_ao[NORTH][2];
			 renderBlocks.brightnessTopRight = ao[DOWN][1] == 1.0F ? brightness[DOWN][1] : offset_brightness[NORTH][2];
			 /* DOWN - NE CORNER */
			 base_ao[2] = offset_ao[DOWN][2];
			 renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];

			 break;
		 case Slope.ID_WEDGE_NEG_S:

			 /* DOWN - NW CORNER */
			 base_ao[3] = ao[DOWN][3] == 1.0F ? 1.0F : offset_ao[SOUTH][3];
			 renderBlocks.brightnessBottomLeft = ao[DOWN][3] == 1.0F ? brightness[DOWN][3] : offset_brightness[SOUTH][3];
			 /* DOWN - SW CORNER */
			 base_ao[0] = offset_ao[DOWN][0];
			 renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
			 /* DOWN - SE CORNER */
			 base_ao[1] = offset_ao[DOWN][1];
			 renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
			 /* DOWN - NE CORNER */
			 base_ao[2] = ao[DOWN][2] == 1.0F ? 1.0F : offset_ao[SOUTH][2];
			 renderBlocks.brightnessBottomRight = ao[DOWN][2] == 1.0F ? brightness[DOWN][2] : offset_brightness[SOUTH][2];

			 break;
		 case Slope.ID_WEDGE_NEG_W:

			 /* DOWN - NW CORNER */
			 base_ao[3] = offset_ao[DOWN][3];
			 renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
			 /* DOWN - SW CORNER */
			 base_ao[0] = offset_ao[DOWN][0];
			 renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
			 /* DOWN - SE CORNER */
			 base_ao[1] = ao[DOWN][1] == 1.0F ? 1.0F : offset_ao[WEST][1];
			 renderBlocks.brightnessTopRight = ao[DOWN][1] == 1.0F ? brightness[DOWN][1] : offset_brightness[WEST][1];
			 /* DOWN - NE CORNER */
			 base_ao[2] = ao[DOWN][2] == 1.0F ? 1.0F : offset_ao[WEST][2];
			 renderBlocks.brightnessBottomRight = ao[DOWN][2] == 1.0F ? brightness[DOWN][2] : offset_brightness[WEST][2];

			 break;
		 case Slope.ID_WEDGE_NEG_E:

			 /* DOWN - NW CORNER */
			 base_ao[3] = ao[DOWN][3] == 1.0F ? 1.0F : offset_ao[EAST][3];
			 renderBlocks.brightnessBottomLeft = ao[DOWN][3] == 1.0F ? brightness[DOWN][3] : offset_brightness[EAST][3];
			 /* DOWN - SW CORNER */
			 base_ao[0] = ao[DOWN][0] == 1.0F ? 1.0F : offset_ao[EAST][0];
			 renderBlocks.brightnessTopLeft = ao[DOWN][0] == 1.0F ? brightness[DOWN][0] : offset_brightness[EAST][0];
			 /* DOWN - SE CORNER */
			 base_ao[1] = offset_ao[DOWN][1];
			 renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
			 /* DOWN - NE CORNER */
			 base_ao[2] = offset_ao[DOWN][2];
			 renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];

			 break;
		 case Slope.ID_WEDGE_POS_N:

			 /* TOP - NW CORNER */
			 base_ao[2] = offset_ao[UP][2];
			 renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
			 /* TOP - SW CORNER */
			 base_ao[1] = ao[UP][1] == 1.0F ? 1.0F : offset_ao[NORTH][0];
			 renderBlocks.brightnessTopLeft = ao[UP][1] == 1.0F ? brightness[UP][1] : offset_brightness[NORTH][0];
			 /* TOP - SE CORNER */
			 base_ao[0] = ao[UP][0] == 1.0F ? 1.0F : offset_ao[NORTH][3];
			 renderBlocks.brightnessTopRight = ao[UP][0] == 1.0F ? brightness[UP][0] : offset_brightness[NORTH][3];
			 /* TOP - NE CORNER */
			 base_ao[3] = offset_ao[UP][3];
			 renderBlocks.brightnessBottomRight = offset_brightness[UP][3];

			 break;
		 case Slope.ID_WEDGE_POS_S:

			 /* TOP - NW CORNER */
			 base_ao[2] = ao[UP][2] == 1.0F ? 1.0F : offset_ao[SOUTH][0];
			 renderBlocks.brightnessBottomLeft = ao[UP][2] == 1.0F ? brightness[UP][2] : offset_brightness[SOUTH][0];
			 /* TOP - SW CORNER */
			 base_ao[1] = offset_ao[UP][1];
			 renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
			 /* TOP - SE CORNER */
			 base_ao[0] = offset_ao[UP][0];
			 renderBlocks.brightnessTopRight = offset_brightness[UP][0];
			 /* TOP - NE CORNER */
			 base_ao[3] = ao[UP][3] == 1.0F ? 1.0F : offset_ao[SOUTH][1];
			 renderBlocks.brightnessBottomRight = ao[UP][3] == 1.0F ? brightness[UP][3] : offset_brightness[SOUTH][1];

			 break;
		 case Slope.ID_WEDGE_POS_W:

			 /* TOP - NW CORNER */
			 base_ao[2] = offset_ao[UP][2];
			 renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
			 /* TOP - SW CORNER */
			 base_ao[1] = offset_ao[UP][1];
			 renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
			 /* TOP - SE CORNER */
			 //
			 base_ao[0] = ao[UP][0] == 1.0F ? 1.0F : offset_ao[WEST][0];
			 renderBlocks.brightnessTopRight = ao[UP][0] == 1.0F ? brightness[UP][0] : offset_brightness[WEST][0];
			 /* TOP - NE CORNER */
			 //
			 base_ao[3] = ao[UP][3] == 1.0F ? 1.0F : offset_ao[WEST][3];
			 renderBlocks.brightnessBottomRight = ao[UP][3] == 1.0F ? brightness[UP][3] : offset_brightness[WEST][3];

			 break;
		 case Slope.ID_WEDGE_POS_E:

			 /* TOP - NW CORNER */
			 base_ao[2] = ao[UP][2] == 1.0F ? 1.0F : offset_ao[EAST][2];
			 renderBlocks.brightnessBottomLeft = ao[UP][2] == 1.0F ? brightness[UP][2] : offset_brightness[EAST][2];
			 /* TOP - SW CORNER */
			 base_ao[1] = ao[UP][1] == 1.0F ? 1.0F : offset_ao[EAST][1];
			 renderBlocks.brightnessTopLeft = ao[UP][1] == 1.0F ? brightness[UP][1] : offset_brightness[EAST][1];
			 /* TOP - SE CORNER */
			 base_ao[0] = offset_ao[UP][0];
			 renderBlocks.brightnessTopRight = offset_brightness[UP][0];
			 /* TOP - NE CORNER */
			 base_ao[3] = offset_ao[UP][3];
			 renderBlocks.brightnessBottomRight = offset_brightness[UP][3];

			 break;
		 }
	 }

	 private void prepareHorizontalWedge(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 setWedgeSlopeLighting(renderBlocks, slope);

		 if (slope.facings.contains(ForgeDirection.NORTH)) {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 5, WEDGE_SLOPED_ZN, false, x, y, z, 0.7F);
		 } else {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 4, WEDGE_SLOPED_ZP, false, x, y, z, 0.7F);
		 }
	 }

	 private void prepareVerticalWedge(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 setWedgeSlopeLighting(renderBlocks, slope);

		 if (slope.isPositive) {
			 float aoValue = slope.facings.contains(ForgeDirection.EAST) || slope.facings.contains(ForgeDirection.WEST) ? 0.8F : 0.9F;
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, WEDGE_SLOPED_YP, true, x, y, z, aoValue);
		 } else {
			 float aoValue = slope.facings.contains(ForgeDirection.EAST) || slope.facings.contains(ForgeDirection.WEST) ? 0.55F : 0.65F;
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, WEDGE_SLOPED_YN, true, x, y, z, aoValue);
		 }
	 }

	 private void prepareWedgeIntCorner(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 /* X Slope */

		 Slope slope1 = slope.facings.contains(ForgeDirection.WEST) ? (slope.isPositive ? Slope.WEDGE_POS_W : Slope.WEDGE_NEG_W) : (slope.isPositive ? Slope.WEDGE_POS_E : Slope.WEDGE_NEG_E);

		 setWedgeSlopeLighting(renderBlocks, slope1);

		 if (slope1.isPositive) {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, WEDGE_CORNER_YXXPNP, true, x, y, z, 0.8F);
		 } else {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, WEDGE_CORNER_YXXNNP, true, x, y, z, 0.55F);
		 }

		 /* Z Slope */

		 Slope slope2 = slope.facings.contains(ForgeDirection.NORTH) ? (slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N) : (slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S);

		 setWedgeSlopeLighting(renderBlocks, slope2);

		 if (slope2.isPositive) {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, WEDGE_CORNER_YZZPNP, true, x, y, z, 0.9F);
		 } else {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, WEDGE_CORNER_YZZNNP, true, x, y, z, 0.65F);
		 }
	 }

	 private void prepareWedgeExtCorner(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 /* X Slope */

		 Slope slope1 = slope.facings.contains(ForgeDirection.WEST) ? (slope.isPositive ? Slope.WEDGE_POS_W : Slope.WEDGE_NEG_W) : (slope.isPositive ? Slope.WEDGE_POS_E : Slope.WEDGE_NEG_E);

		 setWedgeSlopeLighting(renderBlocks, slope1);

		 if (slope1.isPositive) {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, WEDGE_CORNER_YXXPNP, true, x, y, z, 0.8F);
		 } else {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, WEDGE_CORNER_YXXNNP, true, x, y, z, 0.55F);
		 }

		 /* Z Slope */

		 Slope slope2 = slope.facings.contains(ForgeDirection.NORTH) ? (slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N) : (slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S);

		 setWedgeSlopeLighting(renderBlocks, slope2);

		 if (slope2.isPositive) {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, WEDGE_CORNER_YZZPNP, true, x, y, z, 0.9F);
		 } else {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, WEDGE_CORNER_YZZNNP, true, x, y, z, 0.65F);
		 }
	 }

	 /**
	  * Will set lighting for oblique interior sloped faces.
	  */
	 private void setObliqueIntSlopeLighting(RenderBlocks renderBlocks, Slope slope)
	 {
		 switch (slope.slopeID)
		 {
		 case Slope.ID_OBL_INT_NEG_NW:

			 /* DOWN - NW CORNER */
			 base_ao[3] = offset_ao[DOWN][3];
			 renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
			 /* DOWN - SW CORNER */
			 base_ao[0] = ao[DOWN][0] == 1.0F ? 1.0F : offset_ao[NORTH][1];
			 renderBlocks.brightnessTopLeft = ao[DOWN][0] == 1.0F ? brightness[DOWN][0] : offset_brightness[NORTH][1];
			 /* DOWN - NE CORNER */
			 base_ao[2] = ao[DOWN][2] == 1.0F ? 1.0F : offset_ao[WEST][2];
			 renderBlocks.brightnessBottomRight = ao[DOWN][2] == 1.0F ? brightness[DOWN][2] : offset_brightness[WEST][2];

			 break;
		 case Slope.ID_OBL_INT_NEG_NE:

			 /* DOWN - NW CORNER */
			 base_ao[3] = ao[DOWN][3] == 1.0F ? 1.0F : offset_ao[EAST][3];
			 renderBlocks.brightnessBottomLeft = ao[DOWN][3] == 1.0F ? brightness[DOWN][3] : offset_brightness[EAST][3];
			 /* DOWN - SE CORNER */
			 base_ao[1] = ao[DOWN][1] == 1.0F ? 1.0F : offset_ao[NORTH][2];
			 renderBlocks.brightnessTopRight = ao[DOWN][1] == 1.0F ? brightness[DOWN][1] : offset_brightness[NORTH][2];
			 /* DOWN - NE CORNER */
			 base_ao[2] = offset_ao[DOWN][2];
			 renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];

			 break;
		 case Slope.ID_OBL_INT_NEG_SW:

			 /* DOWN - NW CORNER */
			 base_ao[3] = ao[DOWN][3] == 1.0F ? 1.0F : offset_ao[SOUTH][3];
			 renderBlocks.brightnessBottomLeft = ao[DOWN][3] == 1.0F ? brightness[DOWN][3] : offset_brightness[SOUTH][3];
			 /* DOWN - SW CORNER */
			 base_ao[0] = offset_ao[DOWN][0];
			 renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
			 /* DOWN - SE CORNER */
			 base_ao[1] = ao[DOWN][1] == 1.0F ? 1.0F : offset_ao[WEST][1];
			 renderBlocks.brightnessTopRight = ao[DOWN][1] == 1.0F ? brightness[DOWN][1] : offset_brightness[WEST][1];

			 break;
		 case Slope.ID_OBL_INT_NEG_SE:

			 /* DOWN - SW CORNER */
			 base_ao[0] = ao[DOWN][0] == 1.0F ? 1.0F : offset_ao[EAST][0];
			 renderBlocks.brightnessTopLeft = ao[DOWN][0] == 1.0F ? brightness[DOWN][0] : offset_brightness[EAST][0];
			 /* DOWN - SE CORNER */
			 base_ao[1] = offset_ao[DOWN][1];
			 renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
			 /* DOWN - NE CORNER */
			 base_ao[2] = ao[DOWN][2] == 1.0F ? 1.0F : offset_ao[SOUTH][2];
			 renderBlocks.brightnessBottomRight = ao[DOWN][2] == 1.0F ? brightness[DOWN][2] : offset_brightness[SOUTH][2];

			 break;
		 case Slope.ID_OBL_INT_POS_NW:

			 /* TOP - NW CORNER */
			 base_ao[2] = offset_ao[UP][2];
			 renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
			 /* TOP - SW CORNER */
			 base_ao[1] = ao[UP][1] == 1.0F ? 1.0F : offset_ao[NORTH][0];
			 renderBlocks.brightnessTopLeft = ao[UP][1] == 1.0F ? brightness[UP][1] : offset_brightness[NORTH][0];
			 /* TOP - NE CORNER */
			 base_ao[3] = ao[UP][3] == 1.0F ? 1.0F : offset_ao[WEST][3];
			 renderBlocks.brightnessBottomRight = ao[UP][3] == 1.0F ? brightness[UP][3] : offset_brightness[WEST][3];

			 break;
		 case Slope.ID_OBL_INT_POS_NE:

			 /* TOP - NW CORNER */
			 base_ao[2] = ao[UP][2] == 1.0F ? 1.0F : offset_ao[EAST][2];
			 renderBlocks.brightnessBottomLeft = ao[UP][2] == 1.0F ? brightness[UP][2] : offset_brightness[EAST][2];
			 /* TOP - SE CORNER */
			 base_ao[0] = ao[UP][0] == 1.0F ? 1.0F : offset_ao[NORTH][3];
			 renderBlocks.brightnessTopRight = ao[UP][0] == 1.0F ? brightness[UP][0] : offset_brightness[NORTH][3];
			 /* TOP - NE CORNER */
			 base_ao[3] = offset_ao[UP][3];
			 renderBlocks.brightnessBottomRight = offset_brightness[UP][3];

			 break;
		 case Slope.ID_OBL_INT_POS_SW:

			 /* TOP - NW CORNER */
			 base_ao[2] = ao[UP][2] == 1.0F ? 1.0F : offset_ao[SOUTH][0];
			 renderBlocks.brightnessBottomLeft = ao[UP][2] == 1.0F ? brightness[UP][2] : offset_brightness[SOUTH][0];
			 /* TOP - SW CORNER */
			 base_ao[1] = offset_ao[UP][1];
			 renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
			 /* TOP - SE CORNER */
			 base_ao[0] = ao[UP][0] == 1.0F ? 1.0F : offset_ao[WEST][0];
			 renderBlocks.brightnessTopRight = ao[UP][0] == 1.0F ? brightness[UP][0] : offset_brightness[WEST][0];

			 break;
		 case Slope.ID_OBL_INT_POS_SE:

			 /* TOP - SW CORNER */
			 base_ao[1] = ao[UP][1] == 1.0F ? 1.0F : offset_ao[EAST][1];
			 renderBlocks.brightnessTopLeft = ao[UP][1] == 1.0F ? brightness[UP][1] : offset_brightness[EAST][1];
			 /* TOP - SE CORNER */
			 base_ao[0] = offset_ao[UP][0];
			 renderBlocks.brightnessTopRight = offset_brightness[UP][0];
			 /* TOP - NE CORNER */
			 base_ao[3] = ao[UP][3] == 1.0F ? 1.0F : offset_ao[SOUTH][1];
			 renderBlocks.brightnessBottomRight = ao[UP][3] == 1.0F ? brightness[UP][3] : offset_brightness[SOUTH][1];

			 break;
		 }
	 }

	 private void prepareObliqueIntCorner(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 if (renderBlocks.enableAO) {
			 setObliqueIntSlopeLighting(renderBlocks, slope);
		 }

		 if (slope.isPositive) {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, OBL_CORNER_SLOPED_YP, true, x, y, z, 0.85F);
		 } else {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, OBL_CORNER_SLOPED_YN, true, x, y, z, 0.6F);
		 }
	 }

	 /**
	  * Will set lighting for oblique exterior sloped faces.
	  */
	 private void setObliqueExtSlopeLighting(RenderBlocks renderBlocks, Slope slope)
	 {
		 switch (slope.slopeID)
		 {
		 case Slope.ID_OBL_EXT_NEG_NW:

			 /* DOWN - SW CORNER */
			 base_ao[0] = offset_ao[DOWN][0];
			 renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
			 /* DOWN - SE CORNER */
			 base_ao[1] = ao[DOWN][1] == 1.0F ? 1.0F : ((offset_ao[NORTH][2] + offset_ao[WEST][1]) / 2.0F);
			 renderBlocks.brightnessTopRight = ao[DOWN][1] == 1.0F ? brightness[DOWN][1] : ((offset_brightness[NORTH][2] + offset_brightness[WEST][1]) / 2);
			 /* DOWN - NE CORNER */
			 base_ao[2] = offset_ao[DOWN][2];
			 renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];

			 break;
		 case Slope.ID_OBL_EXT_NEG_NE:

			 /* DOWN - NW CORNER */
			 base_ao[3] = offset_ao[DOWN][3];
			 renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
			 /* DOWN - SW CORNER */
			 base_ao[0] = ao[DOWN][0] == 1.0F ? 1.0F : ((offset_ao[NORTH][1] + offset_ao[EAST][0]) / 2.0F);
			 renderBlocks.brightnessTopLeft = ao[DOWN][0] == 1.0F ? brightness[DOWN][0] : ((offset_brightness[NORTH][1] + offset_brightness[EAST][0]) / 2);
			 /* DOWN - SE CORNER */
			 base_ao[1] = offset_ao[DOWN][1];
			 renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];

			 break;
		 case Slope.ID_OBL_EXT_NEG_SW:

			 /* DOWN - NW CORNER */
			 base_ao[3] = offset_ao[DOWN][3];
			 renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
			 /* DOWN - SE CORNER */
			 base_ao[1] = offset_ao[DOWN][1];
			 renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
			 /* DOWN - NE CORNER */
			 base_ao[2] = ao[DOWN][2] == 1.0F ? 1.0F : ((offset_ao[SOUTH][2] + offset_ao[WEST][2]) / 2.0F);
			 renderBlocks.brightnessBottomRight = ao[DOWN][2] == 1.0F ? brightness[DOWN][2] : ((offset_brightness[SOUTH][2] + offset_brightness[WEST][2]) / 2);

			 break;
		 case Slope.ID_OBL_EXT_NEG_SE:

			 /* DOWN - NW CORNER */
			 base_ao[3] = ao[DOWN][3] == 1.0F ? 1.0F : ((offset_ao[SOUTH][3] + offset_ao[EAST][3]) / 2.0F);
			 renderBlocks.brightnessBottomLeft = ao[DOWN][3] == 1.0F ? brightness[DOWN][3] : ((offset_brightness[SOUTH][3] + offset_brightness[EAST][3]) / 2);
			 /* DOWN - SW CORNER */
			 base_ao[0] = offset_ao[DOWN][0];
			 renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
			 /* DOWN - NE CORNER */
			 base_ao[2] = offset_ao[DOWN][2];
			 renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];

			 break;
		 case Slope.ID_OBL_EXT_POS_NW:

			 /* TOP - SW CORNER */
			 base_ao[1] = offset_ao[UP][1];
			 renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
			 /* TOP - SE CORNER */
			 base_ao[0] = ao[UP][0] == 1.0F ? 1.0F : ((offset_ao[WEST][0] + offset_ao[NORTH][3]) / 2.0F);
			 renderBlocks.brightnessTopRight = ao[UP][0] == 1.0F ? brightness[UP][0] : ((offset_brightness[WEST][0] + offset_brightness[NORTH][3]) / 2);
			 /* TOP - NE CORNER */
			 base_ao[3] = offset_ao[UP][3];
			 renderBlocks.brightnessBottomRight = offset_brightness[UP][3];

			 break;
		 case Slope.ID_OBL_EXT_POS_NE:

			 /* TOP - NW CORNER */
			 base_ao[2] = offset_ao[UP][2];
			 renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
			 /* TOP - SW CORNER */
			 base_ao[1] = ao[UP][1] == 1.0F ? 1.0F : ((offset_ao[NORTH][0] + offset_ao[EAST][1]) / 2.0F);
			 renderBlocks.brightnessTopLeft = ao[UP][1] == 1.0F ? brightness[UP][1] : ((offset_brightness[NORTH][0] + offset_brightness[EAST][1]) / 2);
			 /* TOP - SE CORNER */
			 base_ao[0] = offset_ao[UP][0];
			 renderBlocks.brightnessTopRight = offset_brightness[UP][0];

			 break;
		 case Slope.ID_OBL_EXT_POS_SW:

			 /* TOP - NW CORNER */
			 base_ao[2] = offset_ao[UP][2];
			 renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
			 /* TOP - SE CORNER */
			 base_ao[0] = offset_ao[UP][0];
			 renderBlocks.brightnessTopRight = offset_brightness[UP][0];
			 /* TOP - NE CORNER */
			 base_ao[3] = ao[UP][3] == 1.0F ? 1.0F : ((offset_ao[SOUTH][1] + offset_ao[WEST][3]) / 2.0F);
			 renderBlocks.brightnessBottomRight = ao[UP][3] == 1.0F ? brightness[UP][3] : ((offset_brightness[SOUTH][1] + offset_brightness[WEST][3]) / 2);

			 break;
		 case Slope.ID_OBL_EXT_POS_SE:

			 /* TOP - NW CORNER */
			 base_ao[2] = ao[UP][2] == 1.0F ? 1.0F : ((offset_ao[EAST][2] + offset_ao[SOUTH][0]) / 2.0F);
			 renderBlocks.brightnessBottomLeft = ao[UP][2] == 1.0F ? brightness[UP][2] : ((offset_brightness[EAST][2] + offset_brightness[SOUTH][0]) / 2);
			 /* TOP - SW CORNER */
			 base_ao[1] = offset_ao[UP][1];
			 renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
			 /* TOP - NE CORNER */
			 base_ao[3] = offset_ao[UP][3];
			 renderBlocks.brightnessBottomRight = offset_brightness[UP][3];

			 break;
		 }
	 }

	 private void prepareObliqueExtCorner(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 if (renderBlocks.enableAO) {
			 setObliqueExtSlopeLighting(renderBlocks, slope);
		 }

		 if (slope.isPositive) {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, OBL_CORNER_SLOPED_YP, true, x, y, z, 0.85F);
		 } else {
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, OBL_CORNER_SLOPED_YN, true, x, y, z, 0.6F);
		 }
	 }

	 private void preparePyramid(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 float aoLightValue = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z);
		 int mixedBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		 switch (slope.slopeID)
		 {
		 case Slope.ID_PYR_HALF_NEG:

			 if (renderBlocks.enableAO)
			 {
				 /* DOWN - NW CORNER */
				 base_ao[3] = offset_ao[DOWN][3];
				 renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
				 /* DOWN - SW CORNER */
				 base_ao[0] = offset_ao[DOWN][0];
				 renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
				 /* MIDDLE - CENTER */
				 base_ao[2] = aoLightValue;
				 renderBlocks.brightnessBottomRight = mixedBrightness;
			 }

			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, PYR_YXNN, true, x, y, z, 0.55F);

			 if (renderBlocks.enableAO)
			 {
				 /* MIDDLE - CENTER */
				 base_ao[0] = aoLightValue;
				 renderBlocks.brightnessTopLeft = mixedBrightness;
				 /* DOWN - SE CORNER */
				 base_ao[1] = offset_ao[DOWN][1];
				 renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
				 /* DOWN - NE CORNER */
				 base_ao[2] = offset_ao[DOWN][2];
				 renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];
			 }

			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, PYR_YXNP, true, x, y, z, 0.55F);

			 if (renderBlocks.enableAO)
			 {
				 /* DOWN - NW CORNER */
				 base_ao[3] = offset_ao[DOWN][3];
				 renderBlocks.brightnessBottomLeft = offset_brightness[DOWN][3];
				 /* MIDDLE - CENTER */
				 base_ao[0] = aoLightValue;
				 renderBlocks.brightnessTopLeft = mixedBrightness;
				 /* DOWN - NE CORNER */
				 base_ao[2] = offset_ao[DOWN][2];
				 renderBlocks.brightnessBottomRight = offset_brightness[DOWN][2];
			 }

			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, PYR_YZNN, true, x, y, z, 0.65F);

			 if (renderBlocks.enableAO)
			 {
				 /* MIDDLE - CENTER */
				 base_ao[3] = aoLightValue;
				 renderBlocks.brightnessBottomLeft = mixedBrightness;
				 /* DOWN - SW CORNER */
				 base_ao[0] = offset_ao[DOWN][0];
				 renderBlocks.brightnessTopLeft = offset_brightness[DOWN][0];
				 /* DOWN - SE CORNER */
				 base_ao[1] = offset_ao[DOWN][1];
				 renderBlocks.brightnessTopRight = offset_brightness[DOWN][1];
			 }

			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, PYR_YZNP, true, x, y, z, 0.65F);

			 break;
		 case Slope.ID_PYR_HALF_POS:

			 if (renderBlocks.enableAO)
			 {
				 /* TOP - NW CORNER */
				 base_ao[2] = ao[UP][2];
				 renderBlocks.brightnessBottomLeft = brightness[UP][2];
				 /* TOP - SW CORNER */
				 base_ao[1] = ao[UP][1];
				 renderBlocks.brightnessTopLeft = brightness[UP][1];
				 /* MIDDLE - CENTER */
				 base_ao[0] = aoLightValue;
				 renderBlocks.brightnessTopRight = mixedBrightness;
			 }

			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, PYR_YXPN, true, x, y, z, 0.8F);

			 if (renderBlocks.enableAO)
			 {
				 /* MIDDLE - CENTER */
				 base_ao[2] = aoLightValue;
				 renderBlocks.brightnessBottomLeft = mixedBrightness;
				 /* TOP - SE CORNER */
				 base_ao[0] = ao[UP][0];
				 renderBlocks.brightnessTopRight = brightness[UP][0];
				 /* TOP - NE CORNER */
				 base_ao[3] = ao[UP][3];
				 renderBlocks.brightnessBottomRight = brightness[UP][3];
			 }

			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, PYR_YXPP, true, x, y, z, 0.8F);

			 if (renderBlocks.enableAO)
			 {
				 /* TOP - NW CORNER */
				 base_ao[2] = ao[UP][2];
				 renderBlocks.brightnessBottomLeft = brightness[UP][2];
				 /* MIDDLE - CENTER */
				 base_ao[0] = aoLightValue;
				 renderBlocks.brightnessTopRight = mixedBrightness;
				 /* TOP - NE CORNER */
				 base_ao[3] = ao[UP][3];
				 renderBlocks.brightnessBottomRight = brightness[UP][3];
			 }

			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, PYR_YZPN, true, x, y, z, 0.9F);

			 if (renderBlocks.enableAO)
			 {
				 /* TOP - SW CORNER */
				 base_ao[1] = ao[UP][1];
				 renderBlocks.brightnessTopLeft = brightness[UP][1];
				 /* TOP - SE CORNER */
				 base_ao[0] = ao[UP][0];
				 renderBlocks.brightnessTopRight = brightness[UP][0];
				 /* MIDDLE - CENTER */
				 base_ao[3] = aoLightValue;
				 renderBlocks.brightnessBottomRight = mixedBrightness;
			 }

			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, PYR_YZPP, true, x, y, z, 0.9F);

			 break;
		 }
	 }

	 /**
	  * Prepare bottom face.
	  */
	 private void prepareFaceYNeg(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 if (renderBlocks.enableAO)
		 {
			 /* DOWN - NW CORNER */
			 base_ao[3] = ao[DOWN][3];
			 renderBlocks.brightnessBottomLeft = brightness[DOWN][3];
			 /* DOWN - SW CORNER */
			 base_ao[0] = ao[DOWN][0];
			 renderBlocks.brightnessTopLeft = brightness[DOWN][0];
			 /* DOWN - SE CORNER */
			 base_ao[1] = ao[DOWN][1];
			 renderBlocks.brightnessTopRight = brightness[DOWN][1];
			 /* DOWN - NE CORNER */
			 base_ao[2] = ao[DOWN][2];
			 renderBlocks.brightnessBottomRight = brightness[DOWN][2];
		 }

		 switch (slope.slopeType) {
		 case OBLIQUE_INT: case OBLIQUE_EXT:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, OBL_CORNER_YN, false, x, y, z, 0.5F);
			 break;
		 default:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, WEDGE_YN, false, x, y, z, 0.5F);
		 }
	 }

	 /**
	  * Prepare top face.
	  */
	 private void prepareFaceYPos(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 if (renderBlocks.enableAO)
		 {
			 /* TOP - NW CORNER */
			 base_ao[2] = ao[UP][2];
			 renderBlocks.brightnessBottomLeft = brightness[UP][2];
			 /* TOP - SW CORNER */
			 base_ao[1] = ao[UP][1];
			 renderBlocks.brightnessTopLeft = brightness[UP][1];
			 /* TOP - SE CORNER */
			 base_ao[0] = ao[UP][0];
			 renderBlocks.brightnessTopRight = brightness[UP][0];
			 /* TOP - NE CORNER */
			 base_ao[3] = ao[UP][3];
			 renderBlocks.brightnessBottomRight = brightness[UP][3];
		 }

		 switch (slope.slopeType) {
		 case OBLIQUE_INT: case OBLIQUE_EXT:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, OBL_CORNER_YP, false, x, y, z, 1.0F);
			 break;
		 default:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, WEDGE_YP, false, x, y, z, 1.0F);
		 }
	 }

	 /**
	  * Prepare North face.
	  */
	 private void prepareFaceZNeg(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 if (renderBlocks.enableAO)
		 {
			 /* NORTH - TL CORNER */
			 base_ao[3] = ao[NORTH][3];
			 renderBlocks.brightnessBottomLeft = brightness[NORTH][3];
			 /* NORTH - BL CORNER */
			 base_ao[2] = ao[NORTH][2];
			 renderBlocks.brightnessBottomRight = brightness[NORTH][2];
			 /* NORTH - BR CORNER */
			 base_ao[1] = ao[NORTH][1];
			 renderBlocks.brightnessTopRight = brightness[NORTH][1];
			 /* NORTH - TR CORNER */
			 base_ao[0] = ao[NORTH][0];
			 renderBlocks.brightnessTopLeft = brightness[NORTH][0];
		 }

		 switch (slope.slopeType) {
		 case WEDGE_INT: case WEDGE_EXT:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 2, WEDGE_CORNER_ZN, false, x, y, z, 0.8F);
			 break;
		 case OBLIQUE_INT: case OBLIQUE_EXT:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 2, OBL_CORNER_ZN, false, x, y, z, 0.8F);
			 break;
		 default:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 2, WEDGE_ZN, false, x, y, z, 0.8F);
		 }
	 }

	 /**
	  * Prepare South face.
	  */
	 private void prepareFaceZPos(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 if (renderBlocks.enableAO)
		 {
			 /* SOUTH - TL CORNER */
			 base_ao[0] = ao[SOUTH][0];
			 renderBlocks.brightnessTopLeft = brightness[SOUTH][0];
			 /* SOUTH - BL CORNER */
			 base_ao[3] = ao[SOUTH][3];
			 renderBlocks.brightnessBottomLeft = brightness[SOUTH][3];
			 /* SOUTH - BR CORNER */
			 base_ao[2] = ao[SOUTH][2];
			 renderBlocks.brightnessBottomRight = brightness[SOUTH][2];
			 /* SOUTH - TR CORNER */
			 base_ao[1] = ao[SOUTH][1];
			 renderBlocks.brightnessTopRight = brightness[SOUTH][1];
		 }

		 switch (slope.slopeType) {
		 case WEDGE_INT: case WEDGE_EXT:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 3, WEDGE_CORNER_ZP, false, x, y, z, 0.8F);
			 break;
		 case OBLIQUE_INT: case OBLIQUE_EXT:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 3, OBL_CORNER_ZP, false, x, y, z, 0.8F);
			 break;
		 default:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 3, WEDGE_ZP, false, x, y, z, 0.8F);
		 }
	 }

	 /**
	  * Prepare West face.
	  */
	 private void prepareFaceXNeg(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 if (renderBlocks.enableAO)
		 {
			 /* WEST - TL CORNER */
			 base_ao[3] = ao[WEST][3];
			 renderBlocks.brightnessBottomLeft = brightness[WEST][3];
			 /* WEST - BL CORNER */
			 base_ao[2] = ao[WEST][2];
			 renderBlocks.brightnessBottomRight = brightness[WEST][2];
			 /* WEST - BR CORNER */
			 base_ao[1] = ao[WEST][1];
			 renderBlocks.brightnessTopRight = brightness[WEST][1];
			 /* WEST - TR CORNER */
			 base_ao[0] = ao[WEST][0];
			 renderBlocks.brightnessTopLeft = brightness[WEST][0];
		 }

		 switch (slope.slopeType) {
		 case WEDGE_INT: case WEDGE_EXT:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 4, WEDGE_CORNER_XN, false, x, y, z, 0.6F);
			 break;
		 case OBLIQUE_INT: case OBLIQUE_EXT:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 4, OBL_CORNER_XN, false, x, y, z, 0.6F);
			 break;
		 default:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 4, WEDGE_XN, false, x, y, z, 0.6F);
		 }
	 }

	 /**
	  * Prepare East face.
	  */
	 private void prepareFaceXPos(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
	 {
		 if (renderBlocks.enableAO)
		 {
			 /* EAST - TL CORNER */
			 base_ao[1] = ao[EAST][1];
			 renderBlocks.brightnessBottomLeft = brightness[EAST][1];
			 /* EAST - BL CORNER */
			 base_ao[0] = ao[EAST][0];
			 renderBlocks.brightnessTopLeft = brightness[EAST][0];
			 /* EAST - BR CORNER */
			 base_ao[3] = ao[EAST][3];
			 renderBlocks.brightnessTopRight = brightness[EAST][3];
			 /* EAST - TR CORNER */
			 base_ao[2] = ao[EAST][2];
			 renderBlocks.brightnessBottomRight = brightness[EAST][2];
		 }

		 switch (slope.slopeType) {
		 case WEDGE_INT: case WEDGE_EXT:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 5, WEDGE_CORNER_XP, false, x, y, z, 0.6F);
			 break;
		 case OBLIQUE_INT: case OBLIQUE_EXT:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 5, OBL_CORNER_XP, false, x, y, z, 0.6F);
			 break;
		 default:
			 prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 5, WEDGE_XP, false, x, y, z, 0.6F);
		 }
	 }

	 @Override
	 /**
	  * Renders a slope block at the given coordinates, with a given color ratio.  Args: block, x, y, z, r, g, b
	  */
	 public boolean renderStandardSlopeWithColorMultiplier(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z, float red, float green, float blue)
	 {
		 int slopeID = BlockProperties.getData(TE);
		 Slope slope = Slope.slopesList[slopeID];

		 renderBlocks.enableAO = false;
		 Tessellator tessellator = Tessellator.instance;

		 /*
		  * Render sloped faces.
		  * 
		  * All sloped faces (excluding side slopes) are
		  * vertically-adjusted top and bottom faces.
		  * 
		  * Icons are mapped accordingly in setupIcon()
		  * in super class.
		  */

		 tessellator.setBrightness(coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

		 switch (slope.slopeType)
		 {
		 case WEDGE_XZ:
			 prepareHorizontalWedge(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case WEDGE_Y:
			 prepareVerticalWedge(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case WEDGE_INT:
			 prepareWedgeIntCorner(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case WEDGE_EXT:
			 prepareWedgeExtCorner(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case OBLIQUE_INT:
			 prepareObliqueIntCorner(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case OBLIQUE_EXT:
			 prepareObliqueExtCorner(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 case PYRAMID:
			 preparePyramid(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
			 break;
		 }

		 /*
		  * Render non-sloped faces.
		  */

		  /* BOTTOM FACE */
		 if (slope.hasSide(ForgeDirection.DOWN) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, 0)) {
			 tessellator.setBrightness(coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z));
			 prepareFaceYNeg(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
		 }

		 /* TOP FACE */
		 if (slope.hasSide(ForgeDirection.UP) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, 1)) {
			 tessellator.setBrightness(coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z));
			 prepareFaceYPos(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
		 }

		 /* NORTH FACE */
		 if (slope.hasSide(ForgeDirection.NORTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, 2)) {
			 tessellator.setBrightness(coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1));
			 prepareFaceZNeg(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
		 }

		 /* SOUTH FACE */
		 if (slope.hasSide(ForgeDirection.SOUTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, 3)) {
			 tessellator.setBrightness(coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1));
			 prepareFaceZPos(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
		 }

		 /* WEST FACE */
		 if (slope.hasSide(ForgeDirection.WEST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, 4)) {
			 tessellator.setBrightness(coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z));
			 prepareFaceXNeg(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
		 }

		 /* EAST FACE */
		 if (slope.hasSide(ForgeDirection.EAST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, 5)) {
			 tessellator.setBrightness(coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z));
			 prepareFaceXPos(TE, renderBlocks, coverBlock, srcBlock, slope, x, y, z);
		 }

		 return true;
	 }

}