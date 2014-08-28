package com.carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import com.carpentersblocks.renderer.helper.LightingHelper;
import com.carpentersblocks.renderer.helper.RenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerHinged extends BlockHandlerBase {

    /** Side block renders against. */
    protected ForgeDirection side;

    /** Whether block is in open state. */
    protected boolean isOpen;

    /** Bounds for glass or other type of pane. */
    protected static final double[][] paneBounds = new double[][] {
            { 0.0D, 0.09375D, 0.0D, 1.0D, 0.09375D, 1.0D },
            { 0.0D, 0.90625D, 0.0D, 1.0D, 0.90625D, 1.0D },
            { 0.0D, 0.0D, 0.09375D, 1.0D, 1.0D, 0.09375D },
            { 0.0D, 0.0D, 0.90625D, 1.0D, 1.0D, 0.90625D },
            { 0.09375D, 0.0D, 0.0D, 0.09375D, 1.0D, 1.0D },
            { 0.90625D, 0.0D, 0.0D, 0.90625D, 1.0D, 1.0D }
    };

    /**
     * Renders pane like the glass or screen.
     * <p>
     * TODO: Revisit when alpha pass is properly implemented since alpha renders
     * both sides during a single quad draw.
     */
    protected final void renderPartPane(Icon icon, int x, int y, int z)
    {
        if (renderPass == PASS_OPAQUE) {
            int dir = side.ordinal();
            float LIGHTNESS = LightingHelper.LIGHTNESS[dir];

            Tessellator.instance.setBrightness(Block.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
            Tessellator.instance.setColorOpaque_F(LIGHTNESS, LIGHTNESS, LIGHTNESS);

            renderBlocks.setRenderBounds(paneBounds[dir][0], paneBounds[dir][1], paneBounds[dir][2], paneBounds[dir][3], paneBounds[dir][4], paneBounds[dir][5]);

            switch (side) {
                case DOWN:
                case UP:
                    RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
                    Tessellator.instance.setColorOpaque_F(LightingHelper.LIGHTNESS[1], LightingHelper.LIGHTNESS[1], LightingHelper.LIGHTNESS[1]);
                    RenderHelper.renderFaceYPos(renderBlocks, x, y, z, icon);
                    break;
                case NORTH:
                case SOUTH:
                    RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
                    RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
                    break;
                case WEST:
                case EAST:
                    RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
                    RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
                    break;
                default: {}
            }
        }
    }

}
