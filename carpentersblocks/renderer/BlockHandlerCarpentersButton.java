package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import carpentersblocks.util.BlockProperties;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersButton extends BlockHandlerBase {
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        renderBlocks.setRenderBounds(0.3125F, 0.375F, 0.375F, 0.6875F, 0.625F, 0.625F);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
    }
    
    @Override
    /**
     * Renders block
     */
    protected boolean renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;
        renderBlock(BlockProperties.getCover(TE, 6), x, y, z);
        renderBlocks.renderAllFaces = false;
        
        return true;
    }
    
}
