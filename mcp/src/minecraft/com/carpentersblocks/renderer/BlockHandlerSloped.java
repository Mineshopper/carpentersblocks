package com.carpentersblocks.renderer;

import static com.carpentersblocks.renderer.helper.VertexHelper.BOTTOM_LEFT;
import static com.carpentersblocks.renderer.helper.VertexHelper.BOTTOM_RIGHT;
import static com.carpentersblocks.renderer.helper.VertexHelper.TOP_LEFT;
import static com.carpentersblocks.renderer.helper.VertexHelper.TOP_RIGHT;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class BlockHandlerSloped extends BlockHandlerBase {

    /** Identifies which render helper to use. */
    protected int renderID = 0;

    /** Holds AO values for all six faces. */
    protected float[][] ao = new float[6][4];

    /** Holds brightness values for all six faces. */
    protected int[][] brightness = new int[6][4];

    /** Holds offset AO values for all six faces. */
    protected float[][] offset_ao = new float[6][4];

    /** Holds offset brightness values for all six faces. */
    protected int[][] offset_brightness = new int[6][4];

    /**
     * Sets renderID to identify which RenderHelper to use
     * and passes control to delegateSideRender().
     */
    protected final void setIDAndRender(ItemStack itemStack, int renderID, int x, int y, int z, int side)
    {
        this.renderID = renderID;
        delegateSideRender(itemStack, x, y, z, side);
    }

    /**
     * Prepares lighting for block and offset faces.
     */
    protected void prepareLighting(ItemStack itemStack)
    {
        for (int side = 0; side < 6; ++side) {
            populateLighting(itemStack, side);
            populateOffsetLighting(itemStack, side);
        }
    }

    /**
     * Fills ambient occlusion and brightness tables.
     */
    private void populateLighting(ItemStack itemStack, int side)
    {
        switch (side) {
            case DOWN:

                lightingHelper.setupLightingYNeg(itemStack, TE.xCoord, TE.yCoord, TE.zCoord);

                if (renderBlocks.enableAO) {
                    ao[DOWN] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
            case UP:

                lightingHelper.setupLightingYPos(itemStack, TE.xCoord, TE.yCoord, TE.zCoord);

                if (renderBlocks.enableAO) {
                    ao[UP] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
            case NORTH:

                lightingHelper.setupLightingZNeg(itemStack, TE.xCoord, TE.yCoord, TE.zCoord);

                if (renderBlocks.enableAO) {
                    ao[NORTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
            case SOUTH:

                lightingHelper.setupLightingZPos(itemStack, TE.xCoord, TE.yCoord, TE.zCoord);

                if (renderBlocks.enableAO) {
                    ao[SOUTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
            case WEST:

                lightingHelper.setupLightingXNeg(itemStack, TE.xCoord, TE.yCoord, TE.zCoord);

                if (renderBlocks.enableAO) {
                    ao[WEST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
            case EAST:

                lightingHelper.setupLightingXPos(itemStack, TE.xCoord, TE.yCoord, TE.zCoord);

                if (renderBlocks.enableAO) {
                    ao[EAST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    brightness[EAST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
        }
    }

    /**
     * Fills ambient occlusion and brightness tables for offset block coordinates.
     */
    private void populateOffsetLighting(ItemStack itemStack, int side)
    {
        double renderTemp;

        switch (side) {
            case DOWN:

                renderTemp = renderBlocks.renderMinY;
                renderBlocks.renderMinY = 0.0D;
                lightingHelper.setupLightingYNeg(itemStack, TE.xCoord, TE.yCoord + 1, TE.zCoord);
                renderBlocks.renderMinY = renderTemp;

                if (renderBlocks.enableAO) {
                    offset_ao[DOWN] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    offset_brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
            case UP:

                renderTemp = renderBlocks.renderMaxY;
                renderBlocks.renderMaxY = 1.0D;
                lightingHelper.setupLightingYPos(itemStack, TE.xCoord, TE.yCoord - 1, TE.zCoord);
                renderBlocks.renderMaxY = renderTemp;

                if (renderBlocks.enableAO) {
                    offset_ao[UP] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    offset_brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
            case NORTH:

                renderTemp = renderBlocks.renderMinZ;
                renderBlocks.renderMinZ = 0.0D;
                lightingHelper.setupLightingZNeg(itemStack, TE.xCoord, TE.yCoord, TE.zCoord + 1);
                renderBlocks.renderMinZ = renderTemp;

                if (renderBlocks.enableAO) {
                    offset_ao[NORTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    offset_brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
            case SOUTH:

                renderTemp = renderBlocks.renderMaxZ;
                renderBlocks.renderMaxZ = 1.0D;
                lightingHelper.setupLightingZPos(itemStack, TE.xCoord, TE.yCoord, TE.zCoord - 1);
                renderBlocks.renderMaxZ = renderTemp;

                if (renderBlocks.enableAO) {
                    offset_ao[SOUTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    offset_brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
            case WEST:

                renderTemp = renderBlocks.renderMinX;
                renderBlocks.renderMinX = 0.0D;
                lightingHelper.setupLightingXNeg(itemStack, TE.xCoord + 1, TE.yCoord, TE.zCoord);
                renderBlocks.renderMinX = renderTemp;

                if (renderBlocks.enableAO) {
                    offset_ao[WEST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    offset_brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
            case EAST:

                renderTemp = renderBlocks.renderMaxX;
                renderBlocks.renderMaxX = 1.0D;
                lightingHelper.setupLightingXPos(itemStack, TE.xCoord - 1, TE.yCoord, TE.zCoord);
                renderBlocks.renderMaxX = renderTemp;

                if (renderBlocks.enableAO) {
                    offset_ao[EAST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    offset_brightness[EAST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }

                break;
        }
    }

    /**
     * A renderSide method that happens after checking if side cover is rendering.
     */
    abstract void renderBaseSide(int x, int y, int z, int side, Icon icon);

    @Override
    /**
     * Renders side.
     */
    protected final void render(int x, int y, int z, int side, Icon icon)
    {
        if (coverRendering != 6) {
            super.render(x, y, z, side, icon);
        } else {
            renderBaseSide(x, y, z, side, icon);
        }
    }

    /**
     * Renders the base cover block.
     */
    abstract void renderBaseBlock(ItemStack itemStack, int x, int y, int z);

    @Override
    /**
     * Renders the block.
     */
    protected final void renderBlock(ItemStack itemStack, int x, int y, int z)
    {
        if (coverRendering != 6) {
            super.renderBlock(itemStack, x, y, z);
        } else {
            renderBaseBlock(itemStack, x, y, z);
        }
    }

}
