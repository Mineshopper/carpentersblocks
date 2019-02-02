package com.carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersPath extends BlockHandlerBase {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
    }

}
