package carpentersblocks.renderer;

import net.minecraft.util.Icon;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.RenderHelperCollapsible;

public class BlockHandlerCarpentersCollapsibleBlock extends BlockHandlerBase {

	/**
	 * Override for specific block rendering applications.
	 */
	@Override
	protected void setRenderBounds()
	{
		renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}

	@Override
	/**
	 * Renders side.
	 */
	protected void renderSide(int x, int y, int z, int side, double offset, Icon icon)
	{
		switch (side)
		{
		case DOWN:
			RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
			break;
		case UP:
			RenderHelperCollapsible.renderFaceYPos(TE, renderBlocks, x, y, z, icon);
			break;
		case NORTH:
			RenderHelperCollapsible.renderFaceZNeg(TE, renderBlocks, x, y, z, icon);
			break;
		case SOUTH:
			RenderHelperCollapsible.renderFaceZPos(TE, renderBlocks, x, y, z, icon);
			break;
		case WEST:
			RenderHelperCollapsible.renderFaceXNeg(TE, renderBlocks, x, y, z, icon);
			break;
		case EAST:
			RenderHelperCollapsible.renderFaceXPos(TE, renderBlocks, x, y, z, icon);
			break;
		}
	}

}