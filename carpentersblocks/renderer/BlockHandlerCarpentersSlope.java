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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersSlope extends BlockAdvancedLighting {

    /* RENDER IDS */
    private static final int NORMAL_YN              = 0;
    private static final int NORMAL_YP              = 1;
    private static final int NORMAL_ZN              = 2;
    private static final int NORMAL_ZP              = 3;
    private static final int NORMAL_XN              = 4;
    private static final int NORMAL_XP              = 5;
    private static final int TRIANGLE_XZNN          = 6;
    private static final int TRIANGLE_XZNP          = 7;
    private static final int TRIANGLE_XZPP          = 8;
    private static final int TRIANGLE_XZPN          = 9;
    private static final int TRIANGLE_ZXNP          = 10;
    private static final int TRIANGLE_ZXNN          = 11;
    private static final int TRIANGLE_ZXPN          = 12;
    private static final int TRIANGLE_ZXPP          = 13;
    private static final int WEDGE_YN               = 14;
    private static final int WEDGE_YP               = 15;
    private static final int WEDGE_SLOPED_ZN        = 16;
    private static final int WEDGE_ZN               = 17;
    private static final int WEDGE_SLOPED_ZP        = 18;
    private static final int WEDGE_ZP               = 19;
    private static final int WEDGE_SLOPED_XN        = 20;
    private static final int WEDGE_XN               = 21;
    private static final int WEDGE_SLOPED_XP        = 22;
    private static final int WEDGE_XP               = 23;
    private static final int WEDGE_CORNER_SLOPED_ZN = 24;
    private static final int WEDGE_CORNER_SLOPED_ZP = 25;
    private static final int WEDGE_CORNER_SLOPED_XN = 26;
    private static final int WEDGE_CORNER_SLOPED_XP = 27;
    private static final int OBL_INT_YN             = 28;
    private static final int OBL_INT_YP             = 29;
    private static final int OBL_EXT_LEFT_YP        = 30;
    private static final int OBL_EXT_RIGHT_YP       = 31;
    private static final int OBL_EXT_LEFT_YN        = 32;
    private static final int OBL_EXT_RIGHT_YN       = 33;
    private static final int PYR_YZNN               = 34;
    private static final int PYR_YZNP               = 35;
    private static final int PYR_YXNN               = 36;
    private static final int PYR_YXNP               = 37;
    private static final int PYR_YZPN               = 38;
    private static final int PYR_YZPP               = 39;
    private static final int PYR_YXPN               = 40;
    private static final int PYR_YXPP               = 41;
    private static final int PRISM_NORTH_XN         = 42;
    private static final int PRISM_NORTH_XP         = 43;
    private static final int PRISM_SOUTH_XN         = 44;
    private static final int PRISM_SOUTH_XP         = 45;
    private static final int PRISM_WEST_ZN          = 46;
    private static final int PRISM_WEST_ZP          = 47;
    private static final int PRISM_EAST_ZN          = 48;
    private static final int PRISM_EAST_ZP          = 49;
    private static final int PRISM_WEDGE_ZN         = 50;
    private static final int PRISM_WEDGE_ZP         = 51;
    private static final int PRISM_WEDGE_XN         = 52;
    private static final int PRISM_WEDGE_XP         = 53;

    private final float LIGHTNESS_XYNN              = 0.6F;
    private final float LIGHTNESS_XYPN              = 0.55F;
    private final float LIGHTNESS_ZYNN              = 0.7F;
    private final float LIGHTNESS_ZYPN              = 0.65F;
    private final float LIGHTNESS_XYNP              = 0.85F;
    private final float LIGHTNESS_XYPP              = 0.8F;
    private final float LIGHTNESS_ZYNP              = 0.95F;
    private final float LIGHTNESS_ZYPP              = 0.9F;
    private final float LIGHTNESS_SIDE_WEDGE        = 0.7F;
    private final float LIGHTNESS_POS_OBL           = 0.9F;
    private final float LIGHTNESS_NEG_OBL           = 0.65F;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
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

            /* Uncovered sloped oblique faces use triangular frame icon. */
            if (!BlockProperties.hasCover(TE, 6)) {
                icon = IconRegistry.icon_slope_oblique;
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

    @Override
    /**
     * Renders side.
     */
    protected void renderBaseSide(int x, int y, int z, int side, Icon icon)
    {
        int slopeID = BlockProperties.getData(TE);

        switch (renderID)
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
            case TRIANGLE_XZNN:
                HelperTriangle.renderFaceXNegZNeg(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_XZNP:
                HelperTriangle.renderFaceXNegZPos(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_XZPP:
                HelperTriangle.renderFaceXPosZPos(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_XZPN:
                HelperTriangle.renderFaceXPosZNeg(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_ZXNP:
                HelperTriangle.renderFaceZNegXPos(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_ZXNN:
                HelperTriangle.renderFaceZNegXNeg(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_ZXPN:
                HelperTriangle.renderFaceZPosXNeg(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_ZXPP:
                HelperTriangle.renderFaceZPosXPos(renderBlocks, x, y, z, icon);
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
            case OBL_INT_YN:
                HelperOblique.renderSlopeYNeg(renderBlocks, slopeID, x, y, z, icon);
                break;
            case OBL_INT_YP:
                HelperOblique.renderSlopeYPos(renderBlocks, slopeID, x, y, z, icon);
                break;
            case OBL_EXT_LEFT_YP:
                HelperOblique.renderExtObliqueYPosLeft(renderBlocks, slopeID, x, y, z, icon);
                break;
            case OBL_EXT_RIGHT_YP:
                HelperOblique.renderExtObliqueYPosRight(renderBlocks, slopeID, x, y, z, icon);
                break;
            case OBL_EXT_LEFT_YN:
                HelperOblique.renderExtObliqueYNegLeft(renderBlocks, slopeID, x, y, z, icon);
                break;
            case OBL_EXT_RIGHT_YN:
                HelperOblique.renderExtObliqueYNegRight(renderBlocks, slopeID, x, y, z, icon);
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

    @Override
    /**
     * Renders slope.
     */
    public boolean renderBaseBlock(Block block, int x, int y, int z)
    {
        Slope slope = Slope.slopesList[BlockProperties.getData(TE)];

        renderBlocks.enableAO = getEnableAO(block);

        prepareLighting(block);

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
            default:
                preparePrism(block, slope, x, y, z);
                break;
        }

        isSideSloped = false;

        /* Render non-sloped faces. */

        if (slope.hasSide(ForgeDirection.DOWN) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, DOWN)) {
            prepareFaceYNeg(block, slope, x, y, z);
        }
        if (slope.hasSide(ForgeDirection.UP) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, UP)) {
            prepareFaceYPos(block, slope, x, y, z);
        }
        if (slope.hasSide(ForgeDirection.NORTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, NORTH)) {
            prepareFaceZNeg(block, slope, x, y, z);
        }
        if (slope.hasSide(ForgeDirection.SOUTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, SOUTH)) {
            prepareFaceZPos(block, slope, x, y, z);
        }
        if (slope.hasSide(ForgeDirection.WEST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, WEST)) {
            prepareFaceXNeg(block, slope, x, y, z);
        }
        if (slope.hasSide(ForgeDirection.EAST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, EAST)) {
            prepareFaceXPos(block, slope, x, y, z);
        }

        RenderHelper.startDrawing(QUADS);

        renderBlocks.enableAO = false;
        return true;
    }

    /**
     * Will set lighting and render prism sloped faces.
     */
    private void preparePrism(Block block, Slope slope, int x, int y, int z)
    {
        int PRISM_N     = 0;
        int PRISM_S     = 1;
        int PRISM_W     = 2;
        int PRISM_E     = 3;
        int PYR_POS_N   = 4;
        int PYR_POS_S   = 5;
        int PYR_POS_W   = 6;
        int PYR_POS_E   = 7;
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
            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
            setWedgeLighting(block, Slope.WEDGE_POS_W);
            setIDAndRender(block, PRISM_NORTH_XN, x, y, z, WEST);
            renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
            setWedgeLighting(block, Slope.WEDGE_POS_E);
            setIDAndRender(block, PRISM_NORTH_XP, x, y, z, EAST);
        }
        if (pieceList.contains(PRISM_S)) {
            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
            setWedgeLighting(block, Slope.WEDGE_POS_W);
            setIDAndRender(block, PRISM_SOUTH_XN, x, y, z, WEST);
            renderBlocks.setRenderBounds(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
            setWedgeLighting(block, Slope.WEDGE_POS_E);
            setIDAndRender(block, PRISM_SOUTH_XP, x, y, z, EAST);
        }
        if (pieceList.contains(PRISM_W)) {
            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
            setWedgeLighting(block, Slope.WEDGE_POS_N);
            setIDAndRender(block, PRISM_WEST_ZN, x, y, z, NORTH);
            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
            setWedgeLighting(block, Slope.WEDGE_POS_S);
            setIDAndRender(block, PRISM_WEST_ZP, x, y, z, SOUTH);
        }
        if (pieceList.contains(PRISM_E)) {
            renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
            setWedgeLighting(block, Slope.WEDGE_POS_N);
            setIDAndRender(block, PRISM_EAST_ZN, x, y, z, NORTH);
            renderBlocks.setRenderBounds(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
            setWedgeLighting(block, Slope.WEDGE_POS_S);
            setIDAndRender(block, PRISM_EAST_ZP, x, y, z, SOUTH);
        }
        if (pieceList.contains(PYR_POS_N)) {
            setPyramidLighting(block, x, y, z, Slope.PYR_HALF_POS, NORTH);
            setIDAndRender(block, PYR_YZPN, x, y, z, NORTH);
        }
        if (pieceList.contains(PYR_POS_S)) {
            setPyramidLighting(block, x, y, z, Slope.PYR_HALF_POS, SOUTH);
            setIDAndRender(block, PYR_YZPP, x, y, z, SOUTH);
        }
        if (pieceList.contains(PYR_POS_W)) {
            setPyramidLighting(block, x, y, z, Slope.PYR_HALF_POS, WEST);
            setIDAndRender(block, PYR_YXPN, x, y, z, WEST);
        }
        if (pieceList.contains(PYR_POS_E)) {
            setPyramidLighting(block, x, y, z, Slope.PYR_HALF_POS, EAST);
            setIDAndRender(block, PYR_YXPP, x, y, z, EAST);
        }

        RenderHelper.startDrawing(QUADS);

        /* Render wedge intersecting mask, if piece requires it. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

        if (pieceList.contains(WEDGE_POS_N)) {
            setWedgeLighting(block, Slope.WEDGE_POS_N);
            setIDAndRender(block, PRISM_WEDGE_ZN, x, y, z, NORTH);
        } else if (pieceList.contains(WEDGE_POS_S)) {
            setWedgeLighting(block, Slope.WEDGE_POS_S);
            setIDAndRender(block, PRISM_WEDGE_ZP, x, y, z, SOUTH);
        } else if (pieceList.contains(WEDGE_POS_W)) {
            setWedgeLighting(block, Slope.WEDGE_POS_W);
            setIDAndRender(block, PRISM_WEDGE_XN, x, y, z, WEST);
        } else if (pieceList.contains(WEDGE_POS_E)) {
            setWedgeLighting(block, Slope.WEDGE_POS_E);
            setIDAndRender(block, PRISM_WEDGE_XP, x, y, z, EAST);
        }
    }

    /**
     * Will set lighting for wedge sloped faces.  Many slope types
     * make use of these lighting parameters in addition to wedges.
     */
    private void setWedgeLighting(Block block, Slope slope)
    {
        if (renderBlocks.enableAO) {

            prepareLighting(block);

            switch (slope.slopeID) {
                case Slope.ID_WEDGE_NW:

                    lightingHelper.setLightness(LIGHTNESS_SIDE_WEDGE);

                    lightingHelper.ao[TOP_LEFT]     = offset_ao[WEST][TOP_LEFT];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[WEST][BOTTOM_LEFT];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[NORTH][BOTTOM_RIGHT];
                    lightingHelper.ao[TOP_RIGHT]    = offset_ao[NORTH][TOP_RIGHT];

                    renderBlocks.brightnessTopLeft     = offset_brightness[WEST][TOP_LEFT];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[WEST][BOTTOM_LEFT];
                    renderBlocks.brightnessBottomRight = offset_brightness[NORTH][BOTTOM_RIGHT];
                    renderBlocks.brightnessTopRight    = offset_brightness[NORTH][TOP_RIGHT];

                    break;
                case Slope.ID_WEDGE_NE:

                    lightingHelper.setLightness(LIGHTNESS_SIDE_WEDGE);

                    lightingHelper.ao[TOP_LEFT]     = offset_ao[NORTH][TOP_LEFT];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[NORTH][BOTTOM_LEFT];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[EAST][BOTTOM_RIGHT];
                    lightingHelper.ao[TOP_RIGHT]    = offset_ao[EAST][TOP_RIGHT];

                    renderBlocks.brightnessTopLeft     = offset_brightness[NORTH][TOP_LEFT];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[NORTH][BOTTOM_LEFT];
                    renderBlocks.brightnessBottomRight = offset_brightness[EAST][BOTTOM_RIGHT];
                    renderBlocks.brightnessTopRight    = offset_brightness[EAST][TOP_RIGHT];

                    break;
                case Slope.ID_WEDGE_SW:

                    lightingHelper.setLightness(LIGHTNESS_SIDE_WEDGE);

                    lightingHelper.ao[TOP_LEFT]     = offset_ao[SOUTH][TOP_LEFT];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[SOUTH][BOTTOM_LEFT];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[WEST][BOTTOM_RIGHT];
                    lightingHelper.ao[TOP_RIGHT]    = offset_ao[WEST][TOP_RIGHT];

                    renderBlocks.brightnessTopLeft     = offset_brightness[SOUTH][TOP_LEFT];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[SOUTH][BOTTOM_LEFT];
                    renderBlocks.brightnessBottomRight = offset_brightness[WEST][BOTTOM_RIGHT];
                    renderBlocks.brightnessTopRight    = offset_brightness[WEST][TOP_RIGHT];

                    break;
                case Slope.ID_WEDGE_SE:

                    lightingHelper.setLightness(LIGHTNESS_SIDE_WEDGE);

                    lightingHelper.ao[TOP_LEFT]     = offset_ao[EAST][TOP_LEFT];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[EAST][BOTTOM_LEFT];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[SOUTH][BOTTOM_RIGHT];
                    lightingHelper.ao[TOP_RIGHT]    = offset_ao[SOUTH][TOP_RIGHT];

                    renderBlocks.brightnessTopLeft     = offset_brightness[EAST][TOP_LEFT];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[EAST][BOTTOM_LEFT];
                    renderBlocks.brightnessBottomRight = offset_brightness[SOUTH][BOTTOM_RIGHT];
                    renderBlocks.brightnessTopRight    = offset_brightness[SOUTH][TOP_RIGHT];

                    break;
                case Slope.ID_WEDGE_NEG_N:

                    lightingHelper.setLightness(LIGHTNESS_ZYNN);

                    lightingHelper.ao[SOUTHEAST] = ao[DOWN][SOUTHEAST] == 1.0F ? 1.0F : offset_ao[NORTH][BOTTOM_LEFT];
                    lightingHelper.ao[NORTHEAST] = offset_ao[DOWN][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = offset_ao[DOWN][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = ao[DOWN][SOUTHWEST] == 1.0F ? 1.0F : offset_ao[NORTH][BOTTOM_RIGHT];

                    renderBlocks.brightnessTopLeft     = ao[DOWN][SOUTHEAST] == 1.0F ? brightness[DOWN][SOUTHEAST] : offset_brightness[NORTH][BOTTOM_LEFT];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight    = ao[DOWN][SOUTHWEST] == 1.0F ? brightness[DOWN][SOUTHWEST] : offset_brightness[NORTH][BOTTOM_RIGHT];

                    break;
                case Slope.ID_WEDGE_POS_N:

                    lightingHelper.setLightness(LIGHTNESS_ZYNP);

                    lightingHelper.ao[SOUTHEAST] = ao[UP][SOUTHEAST] == 1.0F ? 1.0F : offset_ao[NORTH][TOP_LEFT];
                    lightingHelper.ao[NORTHEAST] = offset_ao[UP][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = offset_ao[UP][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = ao[UP][SOUTHWEST] == 1.0F ? 1.0F : offset_ao[NORTH][TOP_RIGHT];

                    renderBlocks.brightnessTopLeft     = ao[UP][SOUTHEAST] == 1.0F ? brightness[UP][SOUTHEAST] : offset_brightness[NORTH][TOP_LEFT];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][NORTHWEST];
                    renderBlocks.brightnessTopRight    = ao[UP][SOUTHWEST] == 1.0F ? brightness[UP][SOUTHWEST] : offset_brightness[NORTH][TOP_RIGHT];

                    break;
                case Slope.ID_WEDGE_NEG_S:

                    lightingHelper.setLightness(LIGHTNESS_ZYPN);

                    lightingHelper.ao[SOUTHEAST] = offset_ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = ao[DOWN][NORTHEAST] == 1.0F ? 1.0F : offset_ao[SOUTH][BOTTOM_RIGHT];
                    lightingHelper.ao[NORTHWEST] = ao[DOWN][NORTHWEST] == 1.0F ? 1.0F : offset_ao[SOUTH][BOTTOM_LEFT];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = offset_brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = ao[DOWN][NORTHEAST] == 1.0F ? brightness[DOWN][NORTHEAST] : offset_brightness[SOUTH][BOTTOM_RIGHT];
                    renderBlocks.brightnessBottomRight = ao[DOWN][NORTHWEST] == 1.0F ? brightness[DOWN][NORTHWEST] : offset_brightness[SOUTH][BOTTOM_LEFT];
                    renderBlocks.brightnessTopRight    = offset_brightness[DOWN][SOUTHWEST];

                    break;
                case Slope.ID_WEDGE_POS_S:

                    lightingHelper.setLightness(LIGHTNESS_ZYPP);

                    lightingHelper.ao[SOUTHEAST] = offset_ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = ao[UP][NORTHEAST] == 1.0F ? 1.0F : offset_ao[SOUTH][TOP_RIGHT];
                    lightingHelper.ao[NORTHWEST] = ao[UP][NORTHWEST] == 1.0F ? 1.0F : offset_ao[SOUTH][TOP_LEFT];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = offset_brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = ao[UP][NORTHEAST] == 1.0F ? brightness[UP][NORTHEAST] : offset_brightness[SOUTH][TOP_RIGHT];
                    renderBlocks.brightnessBottomRight = ao[UP][NORTHWEST] == 1.0F ? brightness[UP][NORTHWEST] : offset_brightness[SOUTH][TOP_LEFT];
                    renderBlocks.brightnessTopRight    = offset_brightness[UP][SOUTHWEST];

                    break;
                case Slope.ID_WEDGE_NEG_W:

                    lightingHelper.setLightness(LIGHTNESS_XYNN);

                    lightingHelper.ao[SOUTHEAST] = ao[DOWN][SOUTHEAST] == 1.0F ? 1.0F : offset_ao[WEST][BOTTOM_RIGHT];
                    lightingHelper.ao[NORTHEAST] = ao[DOWN][NORTHEAST] == 1.0F ? 1.0F : offset_ao[WEST][BOTTOM_LEFT];
                    lightingHelper.ao[NORTHWEST] = offset_ao[DOWN][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = ao[DOWN][SOUTHEAST] == 1.0F ? brightness[DOWN][SOUTHEAST] : offset_brightness[WEST][BOTTOM_RIGHT];
                    renderBlocks.brightnessBottomLeft  = ao[DOWN][NORTHEAST] == 1.0F ? brightness[DOWN][NORTHEAST] : offset_brightness[WEST][BOTTOM_LEFT];
                    renderBlocks.brightnessBottomRight = offset_brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight    = offset_brightness[DOWN][SOUTHWEST];

                    break;
                case Slope.ID_WEDGE_POS_W:

                    lightingHelper.setLightness(LIGHTNESS_XYNP);

                    lightingHelper.ao[SOUTHEAST] = ao[UP][SOUTHEAST] == 1.0F ? 1.0F : offset_ao[WEST][TOP_RIGHT];
                    lightingHelper.ao[NORTHEAST] = ao[UP][NORTHEAST] == 1.0F ? 1.0F : offset_ao[WEST][TOP_LEFT];
                    lightingHelper.ao[NORTHWEST] = offset_ao[UP][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = ao[UP][SOUTHEAST] == 1.0F ? brightness[UP][SOUTHEAST] : offset_brightness[WEST][TOP_RIGHT];
                    renderBlocks.brightnessBottomLeft  = ao[UP][NORTHEAST] == 1.0F ? brightness[UP][NORTHEAST] : offset_brightness[WEST][TOP_LEFT];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][NORTHWEST];
                    renderBlocks.brightnessTopRight    = offset_brightness[UP][SOUTHWEST];

                    break;
                case Slope.ID_WEDGE_NEG_E:

                    lightingHelper.setLightness(LIGHTNESS_XYPN);

                    lightingHelper.ao[SOUTHEAST] = offset_ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = offset_ao[DOWN][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = ao[DOWN][NORTHWEST] == 1.0F ? 1.0F : offset_ao[EAST][BOTTOM_RIGHT];
                    lightingHelper.ao[SOUTHWEST] = ao[DOWN][SOUTHWEST] == 1.0F ? 1.0F : offset_ao[EAST][BOTTOM_LEFT];

                    renderBlocks.brightnessTopLeft     = offset_brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomRight = ao[DOWN][NORTHWEST] == 1.0F ? brightness[DOWN][NORTHWEST] : offset_brightness[EAST][BOTTOM_RIGHT];
                    renderBlocks.brightnessTopRight    = ao[DOWN][SOUTHWEST] == 1.0F ? brightness[DOWN][SOUTHWEST] : offset_brightness[EAST][BOTTOM_LEFT];

                    break;
                case Slope.ID_WEDGE_POS_E:

                    lightingHelper.setLightness(LIGHTNESS_XYPP);

                    lightingHelper.ao[SOUTHEAST] = offset_ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = offset_ao[UP][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = ao[UP][NORTHWEST] == 1.0F ? 1.0F : offset_ao[EAST][TOP_RIGHT];
                    lightingHelper.ao[SOUTHWEST] = ao[UP][SOUTHWEST] == 1.0F ? 1.0F : offset_ao[EAST][TOP_LEFT];

                    renderBlocks.brightnessTopLeft     = offset_brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = ao[UP][NORTHWEST] == 1.0F ? brightness[UP][NORTHWEST] : offset_brightness[EAST][TOP_RIGHT];
                    renderBlocks.brightnessTopRight    = ao[UP][SOUTHWEST] == 1.0F ? brightness[UP][SOUTHWEST] : offset_brightness[EAST][TOP_LEFT];

                    break;
            }

        }
    }

    private void prepareHorizontalWedge(Block block, Slope slope, int x, int y, int z)
    {
        RenderHelper.startDrawing(QUADS);

        setWedgeLighting(block, slope);

        if (slope.facings.contains(ForgeDirection.NORTH)) {
            setIDAndRender(block, WEDGE_SLOPED_ZN, x, y, z, EAST);
        } else {
            setIDAndRender(block, WEDGE_SLOPED_ZP, x, y, z, WEST);
        }
    }

    private void prepareVerticalWedge(Block block, Slope slope, int x, int y, int z)
    {
        RenderHelper.startDrawing(QUADS);

        setWedgeLighting(block, slope);

        if (slope.facings.contains(ForgeDirection.NORTH)) {
            setIDAndRender(block, WEDGE_SLOPED_ZN, x, y, z, NORTH);
        } else if (slope.facings.contains(ForgeDirection.SOUTH)) {
            setIDAndRender(block, WEDGE_SLOPED_ZP, x, y, z, SOUTH);
        } else if (slope.facings.contains(ForgeDirection.WEST)) {
            setIDAndRender(block, WEDGE_SLOPED_XN, x, y, z, WEST);
        } else { // ForgeDirection.EAST
            setIDAndRender(block, WEDGE_SLOPED_XP, x, y, z, EAST);
        }
    }

    private void prepareWedgeIntCorner(Block block, Slope slope, int x, int y, int z)
    {
        RenderHelper.startDrawing(TRIANGLES);

        Slope slopeX = slope.facings.contains(ForgeDirection.WEST) ? slope.isPositive ? Slope.WEDGE_POS_W : Slope.WEDGE_NEG_W : slope.isPositive ? Slope.WEDGE_POS_E : Slope.WEDGE_NEG_E;

        setWedgeLighting(block, slopeX);

        if (slopeX.facings.contains(ForgeDirection.WEST)) {
            setIDAndRender(block, WEDGE_CORNER_SLOPED_XN, x, y, z, WEST);
        } else { // ForgeDirection.EAST
            setIDAndRender(block, WEDGE_CORNER_SLOPED_XP, x, y, z, EAST);
        }

        Slope slopeZ = slope.facings.contains(ForgeDirection.NORTH) ? slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N : slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S;

        setWedgeLighting(block, slopeZ);

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

        setWedgeLighting(block, slopeX);

        if (slopeX.facings.contains(ForgeDirection.WEST)) {
            setIDAndRender(block, WEDGE_CORNER_SLOPED_XN, x, y, z, WEST);
        } else { // ForgeDirection.EAST
            setIDAndRender(block, WEDGE_CORNER_SLOPED_XP, x, y, z, EAST);
        }

        Slope slopeZ = slope.facings.contains(ForgeDirection.NORTH) ? slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N : slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S;

        setWedgeLighting(block, slopeZ);

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

                    lightingHelper.ao[NORTHEAST] = ao[DOWN][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = offset_ao[DOWN][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessBottomLeft  = brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight    = brightness[DOWN][SOUTHWEST];

                    break;
                case Slope.ID_OBL_INT_NEG_NE:

                    lightingHelper.ao[SOUTHEAST] = ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = offset_ao[DOWN][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = ao[DOWN][NORTHWEST];

                    renderBlocks.brightnessTopLeft     = brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomRight = brightness[DOWN][NORTHWEST];

                    break;
                case Slope.ID_OBL_INT_NEG_SW:

                    lightingHelper.ao[SOUTHEAST] = ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHWEST] = ao[DOWN][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomRight = brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight    = offset_brightness[DOWN][SOUTHWEST];

                    break;
                case Slope.ID_OBL_INT_NEG_SE:

                    lightingHelper.ao[SOUTHEAST] = offset_ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = ao[DOWN][NORTHEAST];
                    lightingHelper.ao[SOUTHWEST] = ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft = brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessTopRight   = brightness[DOWN][SOUTHWEST];

                    break;
                case Slope.ID_OBL_INT_POS_NW:

                    lightingHelper.ao[NORTHEAST] = ao[UP][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = offset_ao[UP][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = ao[UP][SOUTHWEST];

                    renderBlocks.brightnessBottomLeft  = brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][NORTHWEST];
                    renderBlocks.brightnessTopRight    = brightness[UP][SOUTHWEST];

                    break;
                case Slope.ID_OBL_INT_POS_NE:

                    lightingHelper.ao[SOUTHEAST] = ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = offset_ao[UP][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = ao[UP][NORTHWEST];

                    renderBlocks.brightnessTopLeft     = brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = brightness[UP][NORTHWEST];

                    break;
                case Slope.ID_OBL_INT_POS_SW:

                    lightingHelper.ao[SOUTHEAST] = ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHWEST] = ao[UP][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomRight = brightness[UP][NORTHWEST];
                    renderBlocks.brightnessTopRight    = offset_brightness[UP][SOUTHWEST];

                    break;
                case Slope.ID_OBL_INT_POS_SE:

                    lightingHelper.ao[SOUTHEAST] = offset_ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = ao[UP][NORTHEAST];
                    lightingHelper.ao[SOUTHWEST] = ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft = brightness[UP][NORTHEAST];
                    renderBlocks.brightnessTopRight   = brightness[UP][SOUTHWEST];

                    break;
            }

        }

        if (slope.isPositive) {
            lightingHelper.setLightness(LIGHTNESS_POS_OBL);
            setIDAndRender(block, OBL_INT_YP, x, y, z, NORTH);
        } else {
            lightingHelper.setLightness(LIGHTNESS_NEG_OBL);
            setIDAndRender(block, OBL_INT_YN, x, y, z, NORTH);
        }
    }

    private void prepareObliqueExtCorner(Block block, Slope slope, int x, int y, int z)
    {
        RenderHelper.startDrawing(TRIANGLES);

        if (renderBlocks.enableAO) {

            switch (slope.slopeID) {
                case Slope.ID_OBL_EXT_NEG_NW:

                    lightingHelper.ao[TOP_LEFT]    = offset_ao[DOWN][NORTHEAST];
                    lightingHelper.ao[BOTTOM_LEFT] = ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[TOP_RIGHT]   = offset_ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomLeft = brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessTopRight   = offset_brightness[DOWN][SOUTHWEST];

                    break;
                case Slope.ID_OBL_EXT_NEG_NE:

                    lightingHelper.ao[TOP_LEFT]    = offset_ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[BOTTOM_LEFT] = ao[DOWN][SOUTHWEST];
                    lightingHelper.ao[TOP_RIGHT]   = offset_ao[DOWN][NORTHWEST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft = brightness[DOWN][SOUTHWEST];
                    renderBlocks.brightnessTopRight   = offset_brightness[DOWN][NORTHWEST];

                    break;
                case Slope.ID_OBL_EXT_NEG_SW:

                    lightingHelper.ao[TOP_LEFT]    = offset_ao[DOWN][NORTHWEST];
                    lightingHelper.ao[BOTTOM_LEFT] = ao[DOWN][NORTHEAST];
                    lightingHelper.ao[TOP_RIGHT]   = offset_ao[DOWN][SOUTHEAST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessBottomLeft = brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessTopRight   = offset_brightness[DOWN][SOUTHEAST];

                    break;
                case Slope.ID_OBL_EXT_NEG_SE:

                    lightingHelper.ao[TOP_LEFT]    = offset_ao[DOWN][SOUTHWEST];
                    lightingHelper.ao[BOTTOM_LEFT] = ao[DOWN][NORTHWEST];
                    lightingHelper.ao[TOP_RIGHT]   = offset_ao[DOWN][NORTHEAST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[DOWN][SOUTHWEST];
                    renderBlocks.brightnessBottomLeft = brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight   = offset_brightness[DOWN][NORTHEAST];

                    break;
                case Slope.ID_OBL_EXT_POS_NW:

                    lightingHelper.ao[TOP_LEFT]     = ao[UP][TOP_LEFT];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[UP][NORTHEAST];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][SOUTHWEST];

                    break;
                case Slope.ID_OBL_EXT_POS_NE:

                    lightingHelper.ao[TOP_LEFT]     = ao[UP][SOUTHWEST];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[UP][SOUTHEAST];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][NORTHWEST];

                    renderBlocks.brightnessTopLeft     = brightness[UP][SOUTHWEST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][NORTHWEST];

                    break;
                case Slope.ID_OBL_EXT_POS_SW:

                    lightingHelper.ao[TOP_LEFT]     = ao[UP][NORTHEAST];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[UP][NORTHWEST];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][SOUTHEAST];

                    renderBlocks.brightnessTopLeft     = brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][NORTHWEST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][SOUTHEAST];

                    break;
                case Slope.ID_OBL_EXT_POS_SE:

                    lightingHelper.ao[TOP_LEFT]     = ao[UP][NORTHWEST];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[UP][SOUTHWEST];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][NORTHEAST];

                    renderBlocks.brightnessTopLeft     = brightness[UP][NORTHWEST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][SOUTHWEST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][NORTHEAST];

                    break;
            }

        }

        if (slope.isPositive) {
            lightingHelper.setLightness(LIGHTNESS_POS_OBL);
            setIDAndRender(block, OBL_EXT_LEFT_YP, x, y, z, NORTH);
            setIDAndRender(block, OBL_EXT_RIGHT_YP, x, y, z, NORTH);
        } else {
            lightingHelper.setLightness(LIGHTNESS_NEG_OBL);
            setIDAndRender(block, OBL_EXT_LEFT_YN, x, y, z, NORTH);
            setIDAndRender(block, OBL_EXT_RIGHT_YN, x, y, z, NORTH);
        }
    }

    private void setPyramidLighting(Block block, int x, int y, int z, Slope slope, int side)
    {
        if (slope.isPositive) {

            switch (side) {
                case NORTH:
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
                    setWedgeLighting(block, Slope.WEDGE_POS_N);
                    break;
                case SOUTH:
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                    setWedgeLighting(block, Slope.WEDGE_POS_S);
                    break;
                case WEST:
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
                    setWedgeLighting(block, Slope.WEDGE_POS_W);
                    break;
                case EAST:
                    renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
                    setWedgeLighting(block, Slope.WEDGE_POS_E);
                    break;
            }

        } else {

            switch (side) {
                case NORTH:
                    renderBlocks.setRenderBounds(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
                    setWedgeLighting(block, Slope.WEDGE_NEG_N);
                    break;
                case SOUTH:
                    renderBlocks.setRenderBounds(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
                    setWedgeLighting(block, Slope.WEDGE_NEG_S);
                    break;
                case WEST:
                    renderBlocks.setRenderBounds(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
                    setWedgeLighting(block, Slope.WEDGE_NEG_W);
                    break;
                case EAST:
                    renderBlocks.setRenderBounds(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
                    setWedgeLighting(block, Slope.WEDGE_NEG_E);
                    break;
            }

        }
    }

    private void preparePyramid(Block block, Slope slope, int x, int y, int z)
    {
        RenderHelper.startDrawing(TRIANGLES);

        if (slope.isPositive) {

            setPyramidLighting(block, x, y, z, slope, NORTH);
            setIDAndRender(block, PYR_YZPN, x, y, z, NORTH);

            setPyramidLighting(block, x, y, z, slope, SOUTH);
            setIDAndRender(block, PYR_YZPP, x, y, z, SOUTH);

            setPyramidLighting(block, x, y, z, slope, WEST);
            setIDAndRender(block, PYR_YXPN, x, y, z, WEST);

            setPyramidLighting(block, x, y, z, slope, EAST);
            setIDAndRender(block, PYR_YXPP, x, y, z, EAST);

        } else {

            setPyramidLighting(block, x, y, z, slope, NORTH);
            setIDAndRender(block, PYR_YZNN, x, y, z, NORTH);

            setPyramidLighting(block, x, y, z, slope, SOUTH);
            setIDAndRender(block, PYR_YZNP, x, y, z, SOUTH);

            setPyramidLighting(block, x, y, z, slope, WEST);
            setIDAndRender(block, PYR_YXNN, x, y, z, WEST);

            setPyramidLighting(block, x, y, z, slope, EAST);
            setIDAndRender(block, PYR_YXNP, x, y, z, EAST);

        }
    }

    /**
     * Prepare bottom face.
     */
    private void prepareFaceYNeg(Block block, Slope slope, int x, int y, int z)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setLightingYNeg(block, x, y, z);

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
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setLightingYPos(block, x, y, z);

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
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setLightingZNeg(block, x, y, z);

        switch (slope.getFace(ForgeDirection.NORTH)) {
            case WEDGE:
                RenderHelper.startDrawing(TRIANGLES);
                setIDAndRender(block, WEDGE_ZN, x, y, z, NORTH);
                break;
            case TRIANGLE:
                RenderHelper.startDrawing(TRIANGLES);

                renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
                lightingHelper.setLightingZNeg(block, x, y, z);
                setIDAndRender(block, TRIANGLE_ZXNP, x, y, z, NORTH);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
                lightingHelper.setLightingZNeg(block, x, y, z);
                setIDAndRender(block, TRIANGLE_ZXNN, x, y, z, NORTH);

                break;
            default:
                RenderHelper.startDrawing(QUADS);
                setIDAndRender(block, NORMAL_ZN, x, y, z, NORTH);
                break;
        }
    }

    /**
     * Prepare South face.
     */
    private void prepareFaceZPos(Block block, Slope slope, int x, int y, int z)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setLightingZPos(block, x, y, z);

        switch (slope.getFace(ForgeDirection.SOUTH)) {
            case WEDGE:
                RenderHelper.startDrawing(TRIANGLES);
                setIDAndRender(block, WEDGE_ZP, x, y, z, SOUTH);
                break;
            case TRIANGLE:
                RenderHelper.startDrawing(TRIANGLES);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
                lightingHelper.setLightingZPos(block, x, y, z);
                setIDAndRender(block, TRIANGLE_ZXPN, x, y, z, SOUTH);

                renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
                lightingHelper.setLightingZPos(block, x, y, z);
                setIDAndRender(block, TRIANGLE_ZXPP, x, y, z, SOUTH);

                break;
            default:
                RenderHelper.startDrawing(QUADS);
                setIDAndRender(block, NORMAL_ZP, x, y, z, SOUTH);
                break;
        }
    }

    /**
     * Prepare West face.
     */
    private void prepareFaceXNeg(Block block, Slope slope, int x, int y, int z)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setLightingXNeg(block, x, y, z);

        switch (slope.getFace(ForgeDirection.WEST)) {
            case WEDGE:
                RenderHelper.startDrawing(TRIANGLES);
                setIDAndRender(block, WEDGE_XN, x, y, z, WEST);
                break;
            case TRIANGLE:
                RenderHelper.startDrawing(TRIANGLES);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
                lightingHelper.setLightingXNeg(block, x, y, z);
                setIDAndRender(block, TRIANGLE_XZNN, x, y, z, WEST);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                lightingHelper.setLightingXNeg(block, x, y, z);
                setIDAndRender(block, TRIANGLE_XZNP, x, y, z, WEST);

                break;
            default:
                RenderHelper.startDrawing(QUADS);
                setIDAndRender(block, NORMAL_XN, x, y, z, WEST);
                break;
        }
    }

    /**
     * Prepare East face.
     */
    private void prepareFaceXPos(Block block, Slope slope, int x, int y, int z)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setLightingXPos(block, x, y, z);

        switch (slope.getFace(ForgeDirection.EAST)) {
            case WEDGE:
                RenderHelper.startDrawing(TRIANGLES);
                setIDAndRender(block, WEDGE_XP, x, y, z, EAST);
                break;
            case TRIANGLE:
                RenderHelper.startDrawing(TRIANGLES);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                lightingHelper.setLightingXPos(block, x, y, z);
                setIDAndRender(block, TRIANGLE_XZPP, x, y, z, EAST);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
                lightingHelper.setLightingXPos(block, x, y, z);
                setIDAndRender(block, TRIANGLE_XZPN, x, y, z, EAST);

                break;
            default:
                RenderHelper.startDrawing(QUADS);
                setIDAndRender(block, NORMAL_XP, x, y, z, EAST);
                break;
        }
    }

}
