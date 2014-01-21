package carpentersblocks.renderer;

import static carpentersblocks.renderer.helper.RenderHelper.QUADS;
import static carpentersblocks.renderer.helper.RenderHelper.TRIANGLES;
import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_LEFT;
import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_RIGHT;
import static carpentersblocks.renderer.helper.VertexHelper.TOP_LEFT;
import static carpentersblocks.renderer.helper.VertexHelper.TOP_RIGHT;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.RenderHelperCollapsible;
import carpentersblocks.util.collapsible.CollapsibleUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersCollapsibleBlock extends BlockAdvancedLighting {

    /* RENDER IDS */

    private static final int NORMAL_YN  = 0;
    private static final int NORMAL_ZN  = 1;
    private static final int NORMAL_ZP  = 2;
    private static final int NORMAL_XN  = 3;
    private static final int NORMAL_XP  = 4;
    private static final int SLOPE_YZPN = 5;
    private static final int SLOPE_YZPP = 6;
    private static final int SLOPE_XYNP = 7;
    private static final int SLOPE_XYPP = 8;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
    }

    @Override
    /**
     * Renders side.
     */
    protected void renderBaseSide(int x, int y, int z, int side, Icon icon)
    {
        switch (renderID)
        {
        case NORMAL_YN:
            RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
            break;
        case NORMAL_ZN:
            RenderHelperCollapsible.renderFaceZNeg(renderBlocks, x, y, z, icon);
            break;
        case NORMAL_ZP:
            RenderHelperCollapsible.renderFaceZPos(renderBlocks, x, y, z, icon);
            break;
        case NORMAL_XN:
            RenderHelperCollapsible.renderFaceXNeg(renderBlocks, x, y, z, icon);
            break;
        case NORMAL_XP:
            RenderHelperCollapsible.renderFaceXPos(renderBlocks, x, y, z, icon);
            break;
        case SLOPE_YZPN:
            RenderHelperCollapsible.renderSlopeYPosZNeg(renderBlocks, x, y, z, icon);
            break;
        case SLOPE_YZPP:
            RenderHelperCollapsible.renderSlopeYPosZPos(renderBlocks, x, y, z, icon);
            break;
        case SLOPE_XYNP:
            RenderHelperCollapsible.renderSlopeXNegYPos(renderBlocks, x, y, z, icon);
            break;
        case SLOPE_XYPP:
            RenderHelperCollapsible.renderSlopeXPosYPos(renderBlocks, x, y, z, icon);
            break;
        }
    }

    @Override
    /**
     * Renders base block.
     */
    protected boolean renderBaseBlock(Block block, int x, int y, int z)
    {
        renderBlocks.enableAO = getEnableAO(block);
        CollapsibleUtil.computeOffsets(TE);

        /* Render top slopes. */

        RenderHelper.startDrawing(TRIANGLES);

        prepareLighting(block);

        prepareTopFace(block, x, y, z);

        /* Render all other faces. */

        RenderHelper.startDrawing(QUADS);

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

        /* BOTTOM FACE */
        if (srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, DOWN)) {
            lightingHelper.setLightingYNeg(block, x, y, z);
            setIDAndRender(block, NORMAL_YN, x, y, z, DOWN);
        }

        /* NORTH FACE */
        if (srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, NORTH)) {
            lightingHelper.setLightingZNeg(block, x, y, z);
            prepareFaceZNeg(block, x, y, z);
        }

        /* SOUTH FACE */
        if (srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, SOUTH)) {
            lightingHelper.setLightingZPos(block, x, y, z);
            prepareFaceZPos(block, x, y, z);
        }

        /* WEST FACE */
        if (srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, WEST)) {
            lightingHelper.setLightingXNeg(block, x, y, z);
            prepareFaceXNeg(block, x, y, z);
        }

        /* EAST FACE */
        if (srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, EAST)) {
            lightingHelper.setLightingXPos(block, x, y, z);
            prepareFaceXPos(block, x, y, z);
        }

        renderBlocks.enableAO = false;
        return true;
    }

    /**
     * Returns lightness for sloped face on side.
     */
    private float getInterpolatedLightness(ForgeDirection side)
    {
        switch (side) {
        case NORTH: {
            double lightness_WE = (1.0F - lightingHelper.LIGHTNESS_XN) * Math.abs(CollapsibleUtil.offset_XZNN - CollapsibleUtil.offset_XZPN);
            double lightness_NS = (1.0F - lightingHelper.LIGHTNESS_ZN) * Math.abs(CollapsibleUtil.CENTER_YMAX - (CollapsibleUtil.offset_XZPN + CollapsibleUtil.offset_XZNN) / 2.0F) * 2.0F;

            return (float) (lightingHelper.LIGHTNESS_YP - (lightness_WE + lightness_NS) / 2.0F);
        }
        case SOUTH: {
            double lightness_WE = (1.0F - lightingHelper.LIGHTNESS_XN) * Math.abs(CollapsibleUtil.offset_XZNP - CollapsibleUtil.offset_XZPP);
            double lightness_NS = (1.0F - lightingHelper.LIGHTNESS_ZN) * Math.abs(CollapsibleUtil.CENTER_YMAX - (CollapsibleUtil.offset_XZPP + CollapsibleUtil.offset_XZNP) / 2.0F) * 2.0F;

            return (float) (lightingHelper.LIGHTNESS_YP - (lightness_WE + lightness_NS) / 2.0F);
        }
        case WEST: {
            double lightness_WE = (1.0F - lightingHelper.LIGHTNESS_ZN) * Math.abs(CollapsibleUtil.offset_XZNN - CollapsibleUtil.offset_XZNP);
            double lightness_NS = (1.0F - lightingHelper.LIGHTNESS_XN) * Math.abs(CollapsibleUtil.CENTER_YMAX - (CollapsibleUtil.offset_XZNP + CollapsibleUtil.offset_XZNN) / 2.0F) * 2.0F;

            return (float) (lightingHelper.LIGHTNESS_YP - (lightness_WE + lightness_NS) / 2.0F);
        }
        case EAST: {
            double lightness_WE = (1.0F - lightingHelper.LIGHTNESS_ZN) * Math.abs(CollapsibleUtil.offset_XZPP - CollapsibleUtil.offset_XZPN);
            double lightness_NS = (1.0F - lightingHelper.LIGHTNESS_XN) * Math.abs(CollapsibleUtil.CENTER_YMAX - (CollapsibleUtil.offset_XZPP + CollapsibleUtil.offset_XZPN) / 2.0F) * 2.0F;

            return (float) (lightingHelper.LIGHTNESS_YP - (lightness_WE + lightness_NS) / 2.0F);
        }
        default:
            return 1.0F;
        }
    }

    /**
     * Draws the top face using four triangles.
     */
    private void prepareTopFace(Block block, int x, int y, int z)
    {
        /* Top North triangle. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
        lightingHelper.setLightingYPos(block, x, y, z).setLightness(getInterpolatedLightness(ForgeDirection.NORTH));
        setIDAndRender(block, SLOPE_YZPN, x, y, z, UP);

        /* Top South triangle. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setLightingYPos(block, x, y, z).setLightness(getInterpolatedLightness(ForgeDirection.SOUTH));
        setIDAndRender(block, SLOPE_YZPP, x, y, z, UP);

        /* Top West triangle. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D);
        lightingHelper.setLightingYPos(block, x, y, z).setLightness(getInterpolatedLightness(ForgeDirection.WEST));
        setIDAndRender(block, SLOPE_XYNP, x, y, z, UP);

        /* Top East triangle. */

        renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setLightingYPos(block, x, y, z).setLightness(getInterpolatedLightness(ForgeDirection.EAST));
        setIDAndRender(block, SLOPE_XYPP, x, y, z, UP);
    }

    /**
     * Prepare North face.
     */
    private void prepareFaceZNeg(Block block, int x, int y, int z)
    {
        if (renderBlocks.enableAO) {
            lightingHelper.ao[TOP_LEFT]  = (float) (ao[NORTH][BOTTOM_LEFT] + (ao[NORTH][TOP_LEFT] - ao[NORTH][BOTTOM_LEFT]) * CollapsibleUtil.offset_XZPN);
            lightingHelper.ao[TOP_RIGHT] = (float) (ao[NORTH][BOTTOM_RIGHT] + (ao[NORTH][TOP_RIGHT] - ao[NORTH][BOTTOM_RIGHT]) * CollapsibleUtil.offset_XZNN);
        }

        setIDAndRender(block, NORMAL_ZN, x, y, z, NORTH);
    }

    /**
     * Prepare South face.
     */
    private void prepareFaceZPos(Block block, int x, int y, int z)
    {
        if (renderBlocks.enableAO) {
            lightingHelper.ao[TOP_LEFT]  = (float) (ao[SOUTH][BOTTOM_LEFT] + (ao[SOUTH][TOP_LEFT] - ao[SOUTH][BOTTOM_LEFT]) * CollapsibleUtil.offset_XZNP);
            lightingHelper.ao[TOP_RIGHT] = (float) (ao[SOUTH][BOTTOM_RIGHT] + (ao[SOUTH][TOP_RIGHT] - ao[SOUTH][BOTTOM_RIGHT]) * CollapsibleUtil.offset_XZPP);
        }

        setIDAndRender(block, NORMAL_ZP, x, y, z, SOUTH);
    }

    /**
     * Prepare West face.
     */
    private void prepareFaceXNeg(Block block, int x, int y, int z)
    {
        if (renderBlocks.enableAO) {
            lightingHelper.ao[TOP_LEFT]  = (float) (ao[WEST][BOTTOM_LEFT] + (ao[WEST][TOP_LEFT] - ao[WEST][BOTTOM_LEFT]) * CollapsibleUtil.offset_XZNN);
            lightingHelper.ao[TOP_RIGHT] = (float) (ao[WEST][BOTTOM_RIGHT] + (ao[WEST][TOP_RIGHT] - ao[WEST][BOTTOM_RIGHT]) * CollapsibleUtil.offset_XZNP);
        }

        setIDAndRender(block, NORMAL_XN, x, y, z, WEST);
    }

    /**
     * Prepare East face.
     */
    private void prepareFaceXPos(Block block, int x, int y, int z)
    {
        if (renderBlocks.enableAO) {
            lightingHelper.ao[TOP_LEFT]  = (float) (ao[EAST][BOTTOM_LEFT] + (ao[EAST][TOP_LEFT] - ao[EAST][BOTTOM_LEFT]) * CollapsibleUtil.offset_XZPP);
            lightingHelper.ao[TOP_RIGHT] = (float) (ao[EAST][BOTTOM_RIGHT] + (ao[EAST][TOP_RIGHT] - ao[EAST][BOTTOM_RIGHT]) * CollapsibleUtil.offset_XZPN);
        }

        setIDAndRender(block, NORMAL_XP, x, y, z, EAST);
    }

}
