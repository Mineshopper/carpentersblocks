package carpentersblocks.renderer;

import carpentersblocks.block.BlockBase;

public class HingedBase extends BlockHandlerBase {

	/**
	 * Returns whether hinged frame should render.
	 */
	protected boolean shouldRenderFrame()
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
	 * Returns whether hinged screen, glass or handles should render.
	 */
	protected boolean shouldRenderPieces()
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
