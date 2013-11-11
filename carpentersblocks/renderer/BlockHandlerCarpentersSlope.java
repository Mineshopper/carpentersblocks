package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.RenderHelperWedge;
import carpentersblocks.renderer.helper.slope.HelperOblique;
import carpentersblocks.renderer.helper.slope.HelperPyramid;
import carpentersblocks.renderer.helper.slope.HelperWedge;
import carpentersblocks.renderer.helper.slope.HelperWedgeCorner;
import carpentersblocks.tileentity.TEBase;
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
     * Sets slope-specific variables and calls prepareRender().
     */
    private void prepareSlopeRender(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int side, int renderID, int x, int y, int z, float lightness)
    {
		slopeRenderID = renderID;
		prepareRender(TE, renderBlocks, coverBlock, srcBlock, side, x, y, z, lightness);
    }

    @Override
    /**
     * Renders side.
     */
    protected void renderSide(TEBase TE, RenderBlocks renderBlocks, int side, double offset, int x, int y, int z, Icon icon)
    {
    	if (isSideCover) {

    		super.renderSide(TE, renderBlocks, side, offset, x, y, z, icon);

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
     * Render slope using Ambient Occlusion (both minimum and maximum are handled here)
     */
	public boolean renderStandardSlopeWithAmbientOcclusion(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z, float red, float green, float blue)
    {
    	int slopeID = BlockProperties.getData(TE);
    	Slope slope = Slope.slopesList[slopeID];
    	
        renderBlocks.enableAO = true;
        int mixedBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
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
         */
    	
    	isSideSloped = true;
    	
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
    	
    	isSideSloped = false;

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
    
    private void prepareHorizontalWedge(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
    {
    	setWedgeSlopeLighting(renderBlocks, slope);
    	
    	if (slope.facings.contains(ForgeDirection.NORTH)) {
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 5, WEDGE_SLOPED_ZN, x, y, z, 0.7F);
    	} else {
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 4, WEDGE_SLOPED_ZP, x, y, z, 0.7F);
    	}
    }
    
    private void prepareVerticalWedge(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
    {
    	setWedgeSlopeLighting(renderBlocks, slope);

    	// WEST and EAST are darker
    	
    	if (slope.facings.contains(ForgeDirection.NORTH)) {
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, NORTH, WEDGE_SLOPED_ZN, x, y, z, slope.isPositive ? 0.9F : 0.65F);
    	} else if (slope.facings.contains(ForgeDirection.SOUTH)) {
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, SOUTH, WEDGE_SLOPED_ZP, x, y, z, slope.isPositive ? 0.9F : 0.65F);
    	} else if (slope.facings.contains(ForgeDirection.WEST)) {
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, WEST, WEDGE_SLOPED_XN, x, y, z, slope.isPositive ? 0.8F : 0.55F);
    	} else { // ForgeDirection.EAST
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, EAST, WEDGE_SLOPED_XP, x, y, z, slope.isPositive ? 0.8F : 0.55F);
    	}
    }
    
    private void prepareWedgeIntCorner(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
    {
    	/* X Slope */

    	Slope slope1 = slope.facings.contains(ForgeDirection.WEST) ? (slope.isPositive ? Slope.WEDGE_POS_W : Slope.WEDGE_NEG_W) : (slope.isPositive ? Slope.WEDGE_POS_E : Slope.WEDGE_NEG_E);
    	    	
    	setWedgeSlopeLighting(renderBlocks, slope1);
        
    	if (slope1.facings.contains(ForgeDirection.WEST)) {
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, WEST, WEDGE_CORNER_SLOPED_XN, x, y, z, slope.isPositive ? 0.8F : 0.55F);
    	} else { // ForgeDirection.EAST
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, EAST, WEDGE_CORNER_SLOPED_XP, x, y, z, slope.isPositive ? 0.8F : 0.55F);
    	}
        	
        /* Z Slope */

        Slope slope2 = slope.facings.contains(ForgeDirection.NORTH) ? (slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N) : (slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S);

    	setWedgeSlopeLighting(renderBlocks, slope2);
        
    	if (slope.facings.contains(ForgeDirection.NORTH)) {
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, NORTH, WEDGE_CORNER_SLOPED_ZN, x, y, z, slope.isPositive ? 0.9F : 0.65F);
    	} else {
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, SOUTH, WEDGE_CORNER_SLOPED_ZP, x, y, z, slope.isPositive ? 0.9F : 0.65F);
    	}
    }
    
    private void prepareWedgeExtCorner(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
    {
    	/* X Slope */
    	
    	Slope slope1 = slope.facings.contains(ForgeDirection.WEST) ? (slope.isPositive ? Slope.WEDGE_POS_W : Slope.WEDGE_NEG_W) : (slope.isPositive ? Slope.WEDGE_POS_E : Slope.WEDGE_NEG_E);
    	
    	setWedgeSlopeLighting(renderBlocks, slope1);
        
    	if (slope1.facings.contains(ForgeDirection.WEST)) {
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, WEST, WEDGE_CORNER_SLOPED_XN, x, y, z, slope.isPositive ? 0.8F : 0.55F);
    	} else { // ForgeDirection.EAST
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, EAST, WEDGE_CORNER_SLOPED_XP, x, y, z, slope.isPositive ? 0.8F : 0.55F);
    	}
        	
        /* Z Slope */
        
        Slope slope2 = slope.facings.contains(ForgeDirection.NORTH) ? (slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N) : (slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S);

    	setWedgeSlopeLighting(renderBlocks, slope2);

    	if (slope.facings.contains(ForgeDirection.NORTH)) {
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, NORTH, WEDGE_CORNER_SLOPED_ZN, x, y, z, slope.isPositive ? 0.9F : 0.65F);
    	} else {
    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, SOUTH, WEDGE_CORNER_SLOPED_ZP, x, y, z, slope.isPositive ? 0.9F : 0.65F);
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
    
    private void prepareObliqueIntCorner(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
    {
    	if (renderBlocks.enableAO) {
    		setObliqueIntSlopeLighting(renderBlocks, slope);
    	}

    	if (slope.isPositive) {
        	prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, OBL_CORNER_SLOPED_YP, x, y, z, 0.85F);
    	} else {
        	prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, OBL_CORNER_SLOPED_YN, x, y, z, 0.6F);
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
    
    private void prepareObliqueExtCorner(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
    {
    	if (renderBlocks.enableAO) {
    		setObliqueExtSlopeLighting(renderBlocks, slope);
    	}

    	if (slope.isPositive) {
        	prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, OBL_CORNER_SLOPED_YP, x, y, z, 0.85F);
    	} else {
        	prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, OBL_CORNER_SLOPED_YN, x, y, z, 0.6F);
    	}
    }
    
    private void preparePyramid(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
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

    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, PYR_YXNN, x, y, z, 0.55F);

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

    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, PYR_YXNP, x, y, z, 0.55F);

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

    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, PYR_YZNN, x, y, z, 0.65F);

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

    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, PYR_YZNP, x, y, z, 0.65F);

    		break;
    	case Slope.ID_PYR_HALF_POS:

    		if (renderBlocks.enableAO)
    		{
	    		/* TOP - NW CORNER */
	    		base_ao[2] = offset_ao[UP][2];
	    		renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
	    		/* TOP - SW CORNER */
	    		base_ao[1] = offset_ao[UP][1];
	    		renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
	    		/* MIDDLE - CENTER */
	    		base_ao[0] = aoLightValue;
	    		renderBlocks.brightnessTopRight = mixedBrightness;
    		}

    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, PYR_YXPN, x, y, z, 0.8F);

    		if (renderBlocks.enableAO)
    		{
	    		/* MIDDLE - CENTER */
	    		base_ao[2] = aoLightValue;
	    		renderBlocks.brightnessBottomLeft = mixedBrightness;
	    		/* TOP - SE CORNER */
	    		base_ao[0] = offset_ao[UP][0];
	    		renderBlocks.brightnessTopRight = offset_brightness[UP][0];
	    		/* TOP - NE CORNER */
	    		base_ao[3] = offset_ao[UP][3];
	    		renderBlocks.brightnessBottomRight = offset_brightness[UP][3];
    		}

    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, PYR_YXPP, x, y, z, 0.8F);

    		if (renderBlocks.enableAO)
    		{
	    		/* TOP - NW CORNER */
	    		base_ao[2] = offset_ao[UP][2];
	    		renderBlocks.brightnessBottomLeft = offset_brightness[UP][2];
	    		/* MIDDLE - CENTER */
	    		base_ao[0] = aoLightValue;
	    		renderBlocks.brightnessTopRight = mixedBrightness;
	    		/* TOP - NE CORNER */
	    		base_ao[3] = offset_ao[UP][3];
	    		renderBlocks.brightnessBottomRight = offset_brightness[UP][3];
    		}

    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, PYR_YZPN, x, y, z, 0.9F);

    		if (renderBlocks.enableAO)
    		{
	    		/* TOP - SW CORNER */
	    		base_ao[1] = offset_ao[UP][1];
	    		renderBlocks.brightnessTopLeft = offset_brightness[UP][1];
	    		/* TOP - SE CORNER */
	    		base_ao[0] = offset_ao[UP][0];
	    		renderBlocks.brightnessTopRight = offset_brightness[UP][0];
	    		/* MIDDLE - CENTER */
	    		base_ao[3] = aoLightValue;
	    		renderBlocks.brightnessBottomRight = mixedBrightness;
    		}

    		prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, PYR_YZPP, x, y, z, 0.9F);

    		break;
    	}
    }

    /**
     * Prepare bottom face.
     */
    private void prepareFaceYNeg(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
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

    	prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 0, WEDGE_YN, x, y, z, 0.5F);
    }
    
    /**
     * Prepare top face.
     */
    private void prepareFaceYPos(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
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

    	prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 1, WEDGE_YP, x, y, z, 1.0F);
    }

    /**
     * Prepare North face.
     */
    private void prepareFaceZNeg(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
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

    	prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 2, WEDGE_ZN, x, y, z, 0.8F);
    }
    
    /**
     * Prepare South face.
     */
    private void prepareFaceZPos(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
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

        prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 3, WEDGE_ZP, x, y, z, 0.8F);
    }
    
    /**
     * Prepare West face.
     */
    private void prepareFaceXNeg(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
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

    	prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 4, WEDGE_XN, x, y, z, 0.6F);		
    }
    
    /**
     * Prepare East face.
     */
    private void prepareFaceXPos(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, Slope slope, int x, int y, int z)
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

        prepareSlopeRender(TE, renderBlocks, coverBlock, srcBlock, 5, WEDGE_XP, x, y, z, 0.6F);
    }
    
	@Override
    /**
     * Renders a slope block at the given coordinates, with a given color ratio.  Args: block, x, y, z, r, g, b
     */
	public boolean renderStandardSlopeWithColorMultiplier(TEBase TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z, float red, float green, float blue)
    {
    	int slopeID = BlockProperties.getData(TE);
    	Slope slope = Slope.slopesList[slopeID];
    	
        renderBlocks.enableAO = false;
        Tessellator tessellator = Tessellator.instance;

        /*
         * Render sloped faces.
         */
    	
        tessellator.setBrightness(coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
        
        isSideSloped = true;
        
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
    	
    	isSideSloped = false;
    	
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