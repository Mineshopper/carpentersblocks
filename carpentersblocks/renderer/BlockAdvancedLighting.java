package carpentersblocks.renderer;

import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_LEFT;
import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_RIGHT;
import static carpentersblocks.renderer.helper.VertexHelper.TOP_LEFT;
import static carpentersblocks.renderer.helper.VertexHelper.TOP_RIGHT;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import carpentersblocks.renderer.helper.VertexHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockAdvancedLighting extends BlockHandlerBase {

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
    protected final void setIDAndRender(Block block, int renderID, int x, int y, int z, int side)
    {
        this.renderID = renderID;
        delegateSideRender(block, x, y, z, side);
    }

    /**
     * Prepares lighting for block and offset faces.
     */
    protected void prepareLighting(Block block)
    {
        for (int side = 0; side < 6; ++side) {
            populateLighting(block, side);
            populateOffsetLighting(block, side);
        }
    }

    /**
     * Fills ambient occlusion and brightness tables.
     */
    private final void populateLighting(Block block, int side)
    {
        switch (side) {
            case DOWN:
                lightingHelper.setLightingYNeg(block, TE.xCoord, TE.yCoord, TE.zCoord);
                if (renderBlocks.enableAO) {
                    ao[DOWN] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }
                break;
            case UP:
                lightingHelper.setLightingYPos(block, TE.xCoord, TE.yCoord, TE.zCoord);
                if (renderBlocks.enableAO) {
                    ao[UP] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }
                break;
            case NORTH:
                lightingHelper.setLightingZNeg(block, TE.xCoord, TE.yCoord, TE.zCoord);
                if (renderBlocks.enableAO) {
                    ao[NORTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }
                break;
            case SOUTH:
                lightingHelper.setLightingZPos(block, TE.xCoord, TE.yCoord, TE.zCoord);
                if (renderBlocks.enableAO) {
                    ao[SOUTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }
                break;
            case WEST:
                lightingHelper.setLightingXNeg(block, TE.xCoord, TE.yCoord, TE.zCoord);
                if (renderBlocks.enableAO) {
                    ao[WEST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }
                break;
            case EAST:
                lightingHelper.setLightingXPos(block, TE.xCoord, TE.yCoord, TE.zCoord);
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
    private final void populateOffsetLighting(Block block, int side)
    {
        switch (side) {
            case DOWN:
                lightingHelper.setLightingYNeg(block, TE.xCoord, TE.yCoord + 1, TE.zCoord);
                if (renderBlocks.enableAO) {
                    offset_ao[DOWN] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    offset_brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }
                break;
            case UP:
                lightingHelper.setLightingYPos(block, TE.xCoord, TE.yCoord - 1, TE.zCoord);
                if (renderBlocks.enableAO) {
                    offset_ao[UP] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    offset_brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }
                break;
            case NORTH:
                lightingHelper.setLightingZNeg(block, TE.xCoord, TE.yCoord, TE.zCoord + 1);
                if (renderBlocks.enableAO) {
                    offset_ao[NORTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    offset_brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }
                break;
            case SOUTH:
                lightingHelper.setLightingZPos(block, TE.xCoord, TE.yCoord, TE.zCoord - 1);
                if (renderBlocks.enableAO) {
                    offset_ao[SOUTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    offset_brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }
                break;
            case WEST:
                lightingHelper.setLightingXNeg(block, TE.xCoord + 1, TE.yCoord, TE.zCoord);
                if (renderBlocks.enableAO) {
                    offset_ao[WEST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                    offset_brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
                }
                break;
            case EAST:
                lightingHelper.setLightingXPos(block, TE.xCoord - 1, TE.yCoord, TE.zCoord);
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
    protected void renderBaseSide(int x, int y, int z, int side, Icon icon) { }

    @Override
    /**
     * Renders side.
     */
    protected final void renderSide(int x, int y, int z, int side, double offset, Icon icon)
    {
        if (coverRendering != 6) {
            super.renderSide(x, y, z, side, offset, icon);
        } else {
            VertexHelper.setOffset(offset);
            renderBaseSide(x, y, z, side, icon);
            VertexHelper.clearOffset();
        }
    }

    /**
     * Renders the base cover block.
     */
    protected boolean renderBaseBlock(Block block, int x, int y, int z)
    {
        return false;
    }

    @Override
    /**
     * Renders the block.
     */
    protected final boolean renderBlock(Block block, int x, int y, int z)
    {
        if (coverRendering != 6) {
            return super.renderBlock(block, x, y, z);
        } else {
            return renderBaseBlock(block, x, y, z);
        }
    }

}
