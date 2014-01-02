package carpentersblocks.renderer;

import net.minecraft.block.Block;
import carpentersblocks.block.BlockBase;

public class BlockDeterminantRender extends BlockHandlerBase {

	protected float REDUCED_OFFSET = -0.05F;

	/**
	 * Returns whether blocks using cover should render.
	 */
	@Override
	protected boolean shouldRenderBlock(Block block)
	{
		if (renderAlphaOverride) {
			return renderPass == 1;
		} else {
			return	renderBlocks.hasOverrideBlockTexture() ||
					block.getRenderBlockPass() == renderPass ||
					block instanceof BlockBase && renderPass == 0 ||
					shouldRenderPattern();
		}
	}

	/**
	 * Returns whether non-cover blocks should render.
	 * These will always be rendered on the opaque pass.
	 */
	protected boolean shouldRenderOpaque()
	{
		return renderPass == 0;
	}

}
