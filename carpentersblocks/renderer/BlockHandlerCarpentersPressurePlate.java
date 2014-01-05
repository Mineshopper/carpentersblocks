package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersPressurePlate extends BlockHandlerBase {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
	{
		renderBlocks.setRenderBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
		super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
	}

}