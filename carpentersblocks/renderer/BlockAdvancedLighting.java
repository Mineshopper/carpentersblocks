package carpentersblocks.renderer;

import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_LEFT;
import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_RIGHT;
import static carpentersblocks.renderer.helper.VertexHelper.NORTHEAST;
import static carpentersblocks.renderer.helper.VertexHelper.NORTHWEST;
import static carpentersblocks.renderer.helper.VertexHelper.SOUTHEAST;
import static carpentersblocks.renderer.helper.VertexHelper.SOUTHWEST;
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
     * Applies render bounds and calculates lighting for side.
     * Side SIDE_ALL represents all sides.
     */
    protected final void setRenderBoundsAndRelight(Block block, int side, double xMin, double yMin, double zMin, double xMax, double yMax, double zMax)
    {
        renderBlocks.setRenderBounds(xMin, yMin, zMin, xMax, yMax, zMax);

        boolean allSides = side == 6;

        if (allSides || side == DOWN) {
            lightingHelper.setLightness(lightingHelper.LIGHTNESS_YN).setLightingYNeg(block, TE.xCoord, TE.yCoord, TE.zCoord);
            if (renderBlocks.enableAO) {
                ao[DOWN] = new float[] { lightingHelper.ao[NORTHWEST], lightingHelper.ao[SOUTHWEST], lightingHelper.ao[SOUTHEAST], lightingHelper.ao[NORTHEAST] };
                brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
            }
        }

        if (allSides || side == UP) {
            lightingHelper.setLightness(lightingHelper.LIGHTNESS_YP).setLightingYPos(block, TE.xCoord, TE.yCoord, TE.zCoord);
            if (renderBlocks.enableAO) {
                ao[UP] = new float[] { lightingHelper.ao[NORTHWEST], lightingHelper.ao[SOUTHWEST], lightingHelper.ao[SOUTHEAST], lightingHelper.ao[NORTHEAST] };
                brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
            }
        }

        if (allSides || side == NORTH) {
            lightingHelper.setLightness(lightingHelper.LIGHTNESS_ZN).setLightingZNeg(block, TE.xCoord, TE.yCoord, TE.zCoord);
            if (renderBlocks.enableAO) {
                ao[NORTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
            }
        }

        if (allSides || side == SOUTH) {
            lightingHelper.setLightness(lightingHelper.LIGHTNESS_ZP).setLightingZPos(block, TE.xCoord, TE.yCoord, TE.zCoord);
            if (renderBlocks.enableAO) {
                ao[SOUTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
            }
        }

        if (allSides || side == WEST) {
            lightingHelper.setLightness(lightingHelper.LIGHTNESS_XN).setLightingXNeg(block, TE.xCoord, TE.yCoord, TE.zCoord);
            if (renderBlocks.enableAO) {
                ao[WEST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
            }
        }

        if (allSides || side == EAST) {
            lightingHelper.setLightness(lightingHelper.LIGHTNESS_XP).setLightingXPos(block, TE.xCoord, TE.yCoord, TE.zCoord);
            if (renderBlocks.enableAO) {
                ao[EAST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
                brightness[EAST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
            }
        }
    }

    /**
     * Fills separate AO and brightness tables for offset block coordinates.
     */
    protected final void populateOffsetLighting(Block block, int x, int y, int z)
    {
        if (renderBlocks.enableAO)
        {
            lightingHelper.setLightness(lightingHelper.LIGHTNESS_YN).setLightingYNeg(block, x, y + 1, z);
            offset_ao[DOWN] = new float[] { lightingHelper.ao[NORTHWEST], lightingHelper.ao[SOUTHWEST], lightingHelper.ao[SOUTHEAST], lightingHelper.ao[NORTHEAST] };
            offset_brightness[DOWN] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };

            lightingHelper.setLightness(lightingHelper.LIGHTNESS_YP).setLightingYPos(block, x, y - 1, z);
            offset_ao[UP] = new float[] { lightingHelper.ao[NORTHWEST], lightingHelper.ao[SOUTHWEST], lightingHelper.ao[SOUTHEAST], lightingHelper.ao[NORTHEAST] };
            offset_brightness[UP] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };

            lightingHelper.setLightness(lightingHelper.LIGHTNESS_ZN).setLightingZNeg(block, x, y, z + 1);
            offset_ao[NORTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
            offset_brightness[NORTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };

            lightingHelper.setLightness(lightingHelper.LIGHTNESS_ZP).setLightingZPos(block, x, y, z - 1);
            offset_ao[SOUTH] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
            offset_brightness[SOUTH] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };

            lightingHelper.setLightness(lightingHelper.LIGHTNESS_XN).setLightingXNeg(block, x + 1, y, z);
            offset_ao[WEST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
            offset_brightness[WEST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };

            lightingHelper.setLightness(lightingHelper.LIGHTNESS_XP).setLightingXPos(block, x - 1, y, z);
            offset_ao[EAST] = new float[] { lightingHelper.ao[TOP_LEFT], lightingHelper.ao[BOTTOM_LEFT], lightingHelper.ao[BOTTOM_RIGHT], lightingHelper.ao[TOP_RIGHT] };
            offset_brightness[EAST] = new int[] { renderBlocks.brightnessTopLeft, renderBlocks.brightnessBottomLeft, renderBlocks.brightnessBottomRight, renderBlocks.brightnessTopRight };
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
