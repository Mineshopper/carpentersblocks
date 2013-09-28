package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import carpentersblocks.block.BlockBase;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;

public class BlockHandlerCarpentersButton extends BlockHandlerBase
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
	{
		Tessellator tessellator = Tessellator.instance;
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		renderBlocks.setRenderBounds(0.3125F, 0.375F, 0.375F, 0.6875F, 0.625F, 0.625F);

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(0));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(1));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(2));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D,((BlockBase)block).getDefaultIconFromSide(3));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(4));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(5));
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	/**
	 * Renders block
	 */
	 public boolean renderCarpentersBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
	{
		Block coverBlock = isSideCover ? BlockProperties.getCoverBlock(TE, coverRendering) : BlockProperties.getCoverBlock(TE, 6);

		renderBlocks.renderAllFaces = true;
		renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		renderBlocks.renderAllFaces = false;

		return true;
	}

}