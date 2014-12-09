package com.carpentersblocks.renderer;

import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
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
     * Renders pane like glass or screen.
     */
    protected final void renderPartPane(IIcon icon, int x, int y, int z)
    {
        int dir = side.ordinal();
        renderBlocks.setRenderBounds(paneBounds[dir][0], paneBounds[dir][1], paneBounds[dir][2], paneBounds[dir][3], paneBounds[dir][4], paneBounds[dir][5]);
        renderPane(icon, x, y, z, side, true, true);
    }

}
