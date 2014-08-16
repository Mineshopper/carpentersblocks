package com.carpentersblocks.renderer;

import static com.carpentersblocks.renderer.helper.VertexHelper.BOTTOM_LEFT;
import static com.carpentersblocks.renderer.helper.VertexHelper.BOTTOM_RIGHT;
import static com.carpentersblocks.renderer.helper.VertexHelper.NORTHEAST;
import static com.carpentersblocks.renderer.helper.VertexHelper.NORTHWEST;
import static com.carpentersblocks.renderer.helper.VertexHelper.SOUTHEAST;
import static com.carpentersblocks.renderer.helper.VertexHelper.SOUTHWEST;
import static com.carpentersblocks.renderer.helper.VertexHelper.TOP_LEFT;
import static com.carpentersblocks.renderer.helper.VertexHelper.TOP_RIGHT;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.carpentersblocks.renderer.helper.LightingHelper;
import com.carpentersblocks.renderer.helper.RenderHelper;
import com.carpentersblocks.renderer.helper.RenderHelperCollapsible;
import com.carpentersblocks.util.collapsible.CollapsibleUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersCollapsibleBlock extends BlockHandlerSloped {

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
    protected void renderBaseSide(int x, int y, int z, int side, IIcon icon)
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
    protected void renderBaseBlock(ItemStack itemStack, int x, int y, int z)
    {
        renderBlocks.enableAO = getEnableAO(itemStack);
        CollapsibleUtil.computeOffsets(TE);

        /* Render top slopes. */

        if (srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, UP) || !CollapsibleUtil.isFullHeight(TE)) {
            RenderHelper.startDrawing(GL11.GL_TRIANGLES);
            prepareTopFace(itemStack, x, y, z);
        }

        /* Render all other faces. */

        RenderHelper.startDrawing(GL11.GL_QUADS);
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        prepareLighting(itemStack);

        /* BOTTOM FACE */
        if (srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, DOWN)) {
            lightingHelper.setupLightingYNeg(itemStack, x, y, z);
            setIDAndRender(itemStack, NORMAL_YN, x, y, z, DOWN);
        }

        double oneStep = 1.0D / 15.0D;

        /* NORTH FACE */
        if (srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, NORTH)) {
            if (CollapsibleUtil.offset_XZNN + CollapsibleUtil.offset_XZPN >= oneStep) {
                prepareFaceZNeg(itemStack, x, y, z);
            }
        }

        /* SOUTH FACE */
        if (srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, SOUTH)) {
            if (CollapsibleUtil.offset_XZNP + CollapsibleUtil.offset_XZPP >= oneStep) {
                prepareFaceZPos(itemStack, x, y, z);
            }
        }

        /* WEST FACE */
        if (srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, WEST)) {
            if (CollapsibleUtil.offset_XZNN + CollapsibleUtil.offset_XZNP >= oneStep) {
                prepareFaceXNeg(itemStack, x, y, z);
            }
        }

        /* EAST FACE */
        if (srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, EAST)) {
            if (CollapsibleUtil.offset_XZPN + CollapsibleUtil.offset_XZPP >= oneStep) {
                prepareFaceXPos(itemStack, x, y, z);
            }
        }

        renderBlocks.enableAO = false;
    }

    /**
     * Returns lightness for sloped face on side.
     */
    private float getInterpolatedLightness(ForgeDirection side)
    {
        switch (side) {
            case NORTH: {
                double lightness_WE = (1.0F - LightingHelper.LIGHTNESS[4]) * Math.abs(CollapsibleUtil.offset_XZNN - CollapsibleUtil.offset_XZPN);
                double lightness_NS = (1.0F - LightingHelper.LIGHTNESS[2]) * Math.abs(CollapsibleUtil.CENTER_YMAX - (CollapsibleUtil.offset_XZPN + CollapsibleUtil.offset_XZNN) / 2.0F) * 2.0F;

                return (float) (LightingHelper.LIGHTNESS[1] - (lightness_WE + lightness_NS) / 2.0F);
            }
            case SOUTH: {
                double lightness_WE = (1.0F - LightingHelper.LIGHTNESS[4]) * Math.abs(CollapsibleUtil.offset_XZNP - CollapsibleUtil.offset_XZPP);
                double lightness_NS = (1.0F - LightingHelper.LIGHTNESS[2]) * Math.abs(CollapsibleUtil.CENTER_YMAX - (CollapsibleUtil.offset_XZPP + CollapsibleUtil.offset_XZNP) / 2.0F) * 2.0F;

                return (float) (LightingHelper.LIGHTNESS[1] - (lightness_WE + lightness_NS) / 2.0F);
            }
            case WEST: {
                double lightness_WE = (1.0F - LightingHelper.LIGHTNESS[2]) * Math.abs(CollapsibleUtil.offset_XZNN - CollapsibleUtil.offset_XZNP);
                double lightness_NS = (1.0F - LightingHelper.LIGHTNESS[4]) * Math.abs(CollapsibleUtil.CENTER_YMAX - (CollapsibleUtil.offset_XZNP + CollapsibleUtil.offset_XZNN) / 2.0F) * 2.0F;

                return (float) (LightingHelper.LIGHTNESS[1] - (lightness_WE + lightness_NS) / 2.0F);
            }
            case EAST: {
                double lightness_WE = (1.0F - LightingHelper.LIGHTNESS[2]) * Math.abs(CollapsibleUtil.offset_XZPP - CollapsibleUtil.offset_XZPN);
                double lightness_NS = (1.0F - LightingHelper.LIGHTNESS[4]) * Math.abs(CollapsibleUtil.CENTER_YMAX - (CollapsibleUtil.offset_XZPP + CollapsibleUtil.offset_XZPN) / 2.0F) * 2.0F;

                return (float) (LightingHelper.LIGHTNESS[1] - (lightness_WE + lightness_NS) / 2.0F);
            }
            default:
                return 1.0F;
        }
    }

    /**
     * Draws the top face using four triangles.
     */
    private void prepareTopFace(ItemStack itemStack, int x, int y, int z)
    {
        /* Compute CENTER_YMAX lighting. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, CollapsibleUtil.CENTER_YMAX, 0.5D);
        lightingHelper.setupLightingYPos(itemStack, x, y, z);
        float aoCenter = lightingHelper.ao[SOUTHEAST];
        int brightnessCenter = renderBlocks.brightnessTopLeft;

        /* Compute XZPP corner lighting. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, CollapsibleUtil.offset_XZPP, 1.0D);
        lightingHelper.setupLightingYPos(itemStack, x, y, z);
        float aoXZPP = lightingHelper.ao[SOUTHEAST];
        int brightnessXZPP = renderBlocks.brightnessTopLeft;

        /* Compute XZPN corner lighting. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, CollapsibleUtil.offset_XZPN, 1.0D);
        lightingHelper.setupLightingYPos(itemStack, x, y, z);
        float aoXZPN = lightingHelper.ao[NORTHEAST];
        int brightnessXZPN = renderBlocks.brightnessBottomLeft;

        /* Compute XZNN corner lighting. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, CollapsibleUtil.offset_XZNN, 1.0D);
        lightingHelper.setupLightingYPos(itemStack, x, y, z);
        float aoXZNN = lightingHelper.ao[NORTHWEST];
        int brightnessXZNN = renderBlocks.brightnessBottomRight;

        /* Compute XZNP corner lighting. */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, CollapsibleUtil.offset_XZNP, 1.0D);
        lightingHelper.setupLightingYPos(itemStack, x, y, z);
        float aoXZNP = lightingHelper.ao[SOUTHWEST];
        int brightnessXZNP = renderBlocks.brightnessTopRight;

        /* Top North triangle. */

        lightingHelper.setLightnessOverride(getInterpolatedLightness(ForgeDirection.NORTH));
        renderBlocks.setRenderBounds(0.0D, Math.min(CollapsibleUtil.offset_XZNN, CollapsibleUtil.offset_XZPN), 0.0D, 1.0D, Math.max(CollapsibleUtil.offset_XZNN, CollapsibleUtil.offset_XZPN), 0.5D);
        lightingHelper.ao[TOP_LEFT] = lightingHelper.ao[TOP_RIGHT] = aoCenter;
        renderBlocks.brightnessTopLeft = renderBlocks.brightnessTopRight = brightnessCenter;
        lightingHelper.ao[BOTTOM_LEFT] = aoXZPN;
        renderBlocks.brightnessBottomLeft = brightnessXZPN;
        lightingHelper.ao[BOTTOM_RIGHT] = aoXZNN;
        renderBlocks.brightnessBottomRight = brightnessXZNN;
        setIDAndRender(itemStack, SLOPE_YZPN, x, y, z, UP);

        /* Top South triangle. */

        lightingHelper.setLightnessOverride(getInterpolatedLightness(ForgeDirection.SOUTH));
        renderBlocks.setRenderBounds(0.0D, Math.min(CollapsibleUtil.offset_XZNP, CollapsibleUtil.offset_XZPP), 0.5D, 1.0D, Math.max(CollapsibleUtil.offset_XZNP, CollapsibleUtil.offset_XZPP), 1.0D);
        lightingHelper.ao[BOTTOM_LEFT] = lightingHelper.ao[BOTTOM_RIGHT] = aoCenter;
        renderBlocks.brightnessBottomLeft = renderBlocks.brightnessBottomRight = brightnessCenter;
        lightingHelper.ao[TOP_LEFT] = aoXZPP;
        renderBlocks.brightnessTopLeft = brightnessXZPP;
        lightingHelper.ao[TOP_RIGHT] = aoXZNP;
        renderBlocks.brightnessTopRight = brightnessXZNP;
        setIDAndRender(itemStack, SLOPE_YZPP, x, y, z, UP);

        /* Top West triangle. */

        lightingHelper.setLightnessOverride(getInterpolatedLightness(ForgeDirection.WEST));
        renderBlocks.setRenderBounds(0.0D, Math.min(CollapsibleUtil.offset_XZNN, CollapsibleUtil.offset_XZNP), 0.0D, 0.5D, Math.max(CollapsibleUtil.offset_XZNN, CollapsibleUtil.offset_XZNP), 1.0D);
        lightingHelper.ao[TOP_LEFT] = lightingHelper.ao[BOTTOM_LEFT] = aoCenter;
        renderBlocks.brightnessTopLeft = renderBlocks.brightnessBottomLeft = brightnessCenter;
        lightingHelper.ao[TOP_RIGHT] = aoXZNP;
        renderBlocks.brightnessTopRight = brightnessXZNP;
        lightingHelper.ao[BOTTOM_RIGHT] = aoXZNN;
        renderBlocks.brightnessBottomRight = brightnessXZNN;
        setIDAndRender(itemStack, SLOPE_XYNP, x, y, z, UP);

        /* Top East triangle. */

        lightingHelper.setLightnessOverride(getInterpolatedLightness(ForgeDirection.EAST));
        renderBlocks.setRenderBounds(0.5D, Math.min(CollapsibleUtil.offset_XZPN, CollapsibleUtil.offset_XZPP), 0.0D, 1.0D, Math.max(CollapsibleUtil.offset_XZPN, CollapsibleUtil.offset_XZPP), 1.0D);
        lightingHelper.ao[TOP_RIGHT] = lightingHelper.ao[BOTTOM_RIGHT] = aoCenter;
        renderBlocks.brightnessTopRight = renderBlocks.brightnessBottomRight = brightnessCenter;
        lightingHelper.ao[TOP_LEFT] = aoXZPP;
        renderBlocks.brightnessTopLeft = brightnessXZPP;
        lightingHelper.ao[BOTTOM_LEFT] = aoXZPN;
        renderBlocks.brightnessBottomLeft = brightnessXZPN;
        setIDAndRender(itemStack, SLOPE_XYPP, x, y, z, UP);

        lightingHelper.clearLightnessOverride();
    }

    /**
     * Prepare North face.
     */
    private void prepareFaceZNeg(ItemStack itemStack, int x, int y, int z)
    {
        lightingHelper.setupLightingZNeg(itemStack, x, y, z);

        if (renderBlocks.enableAO) {
            lightingHelper.ao[TOP_LEFT]  = (float) (ao[NORTH][BOTTOM_LEFT] + (ao[NORTH][TOP_LEFT] - ao[NORTH][BOTTOM_LEFT]) * CollapsibleUtil.offset_XZPN);
            lightingHelper.ao[TOP_RIGHT] = (float) (ao[NORTH][BOTTOM_RIGHT] + (ao[NORTH][TOP_RIGHT] - ao[NORTH][BOTTOM_RIGHT]) * CollapsibleUtil.offset_XZNN);
        }

        setIDAndRender(itemStack, NORMAL_ZN, x, y, z, NORTH);
    }

    /**
     * Prepare South face.
     */
    private void prepareFaceZPos(ItemStack itemStack, int x, int y, int z)
    {
        lightingHelper.setupLightingZPos(itemStack, x, y, z);

        if (renderBlocks.enableAO) {
            lightingHelper.ao[TOP_LEFT]  = (float) (ao[SOUTH][BOTTOM_LEFT] + (ao[SOUTH][TOP_LEFT] - ao[SOUTH][BOTTOM_LEFT]) * CollapsibleUtil.offset_XZNP);
            lightingHelper.ao[TOP_RIGHT] = (float) (ao[SOUTH][BOTTOM_RIGHT] + (ao[SOUTH][TOP_RIGHT] - ao[SOUTH][BOTTOM_RIGHT]) * CollapsibleUtil.offset_XZPP);
        }

        setIDAndRender(itemStack, NORMAL_ZP, x, y, z, SOUTH);
    }

    /**
     * Prepare West face.
     */
    private void prepareFaceXNeg(ItemStack itemStack, int x, int y, int z)
    {
        lightingHelper.setupLightingXNeg(itemStack, x, y, z);

        if (renderBlocks.enableAO) {
            lightingHelper.ao[TOP_LEFT]  = (float) (ao[WEST][BOTTOM_LEFT] + (ao[WEST][TOP_LEFT] - ao[WEST][BOTTOM_LEFT]) * CollapsibleUtil.offset_XZNN);
            lightingHelper.ao[TOP_RIGHT] = (float) (ao[WEST][BOTTOM_RIGHT] + (ao[WEST][TOP_RIGHT] - ao[WEST][BOTTOM_RIGHT]) * CollapsibleUtil.offset_XZNP);
        }

        setIDAndRender(itemStack, NORMAL_XN, x, y, z, WEST);
    }

    /**
     * Prepare East face.
     */
    private void prepareFaceXPos(ItemStack itemStack, int x, int y, int z)
    {
        lightingHelper.setupLightingXPos(itemStack, x, y, z);

        if (renderBlocks.enableAO) {
            lightingHelper.ao[TOP_LEFT]  = (float) (ao[EAST][BOTTOM_LEFT] + (ao[EAST][TOP_LEFT] - ao[EAST][BOTTOM_LEFT]) * CollapsibleUtil.offset_XZPP);
            lightingHelper.ao[TOP_RIGHT] = (float) (ao[EAST][BOTTOM_RIGHT] + (ao[EAST][TOP_RIGHT] - ao[EAST][BOTTOM_RIGHT]) * CollapsibleUtil.offset_XZPN);
        }

        setIDAndRender(itemStack, NORMAL_XP, x, y, z, EAST);
    }

}
