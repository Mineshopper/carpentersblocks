package com.carpentersblocks.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
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
    protected final void renderPartPane(IIcon icon, int x, int y, int z)
    {
        int dir = side.ordinal();
        float LIGHTNESS = lightingHelper.LIGHTNESS[dir];

        Tessellator.instance.setBrightness(Blocks.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
        Tessellator.instance.setColorOpaque_F(LIGHTNESS, LIGHTNESS, LIGHTNESS);

        renderBlocks.setRenderBounds(paneBounds[dir][0], paneBounds[dir][1], paneBounds[dir][2], paneBounds[dir][3], paneBounds[dir][4], paneBounds[dir][5]);

        switch (side) {
            case DOWN:
            case UP:
                RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
                Tessellator.instance.setColorOpaque_F(lightingHelper.LIGHTNESS[1], lightingHelper.LIGHTNESS[1], lightingHelper.LIGHTNESS[1]);
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
