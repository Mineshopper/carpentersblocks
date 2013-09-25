package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import carpentersblocks.block.BlockBase;
import carpentersblocks.tileentity.TECarpentersBlock;

public class HingedBase extends BlockHandlerBase
{
	/**
	 * Returns whether hinged frame should render.
	 */
	protected boolean shouldRenderFrame(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, int renderPass)
	{
		if (renderAlphaOverride) {
			return renderPass == 1;
		} else {
			return	renderBlocks.hasOverrideBlockTexture() ||
					coverBlock.getRenderBlockPass() == renderPass ||
					coverBlock instanceof BlockBase && renderPass == 0 ||
					shouldRenderPattern(TE, renderPass);
		}
	}
	
	/**
	 * Returns whether hinged screen, glass or handles should render.
	 */
	protected boolean shouldRenderPieces(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, int renderPass)
	{
		/*
		 * Door pieces like the screen and glass will automatically
		 * render both sides.  Alpha pass will force two faces to
		 * be drawn in the same location, one normal, and on mirrored.
		 * Only render on solid pass to remedy this.
		 * 
		 * Door handles are iron, and should always render solid.
		 */
		return renderPass == 0;
	}
	
}
