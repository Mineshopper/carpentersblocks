package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.RenderHelperCollapsible;
import carpentersblocks.tileentity.TECarpentersBlock;

public class BlockHandlerCarpentersCollapsibleBlock extends BlockHandlerBase
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
	{
		Tessellator tessellator = Tessellator.instance;
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	@Override
	/**
	 * Renders side.
	 */
	protected void renderSide(TECarpentersBlock TE, RenderBlocks renderBlocks, int side, double offset, int x, int y, int z, Icon icon)
	{
		switch (side)
		{
		case 0:
			RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
			break;
		case 1:
			RenderHelperCollapsible.renderFaceYPos(TE, renderBlocks, x, y, z, icon);
			break;
		case 2:
			RenderHelperCollapsible.renderFaceZNeg(TE, renderBlocks, x, y, z, icon);
			break;
		case 3:
			RenderHelperCollapsible.renderFaceZPos(TE, renderBlocks, x, y, z, icon);
			break;
		case 4:
			RenderHelperCollapsible.renderFaceXNeg(TE, renderBlocks, x, y, z, icon);
			break;
		case 5:
			RenderHelperCollapsible.renderFaceXPos(TE, renderBlocks, x, y, z, icon);
			break;
		}
	}

}