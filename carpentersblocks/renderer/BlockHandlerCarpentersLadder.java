package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import carpentersblocks.block.BlockBase;
import carpentersblocks.data.Ladder;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;

public class BlockHandlerCarpentersLadder extends BlockHandlerBase
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
	{
		Tessellator tessellator = Tessellator.instance;
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		for (int box = 0; box < 6; ++box)
		{
			switch (box) {
			case 0: // Left vertical support
				renderBlocks.setRenderBounds(0.0D, 0.0D, 0.375D, 0.125D, 1.0D, 0.625D);
				break;
			case 1: // Right vertical support
				renderBlocks.setRenderBounds(0.875D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
				break;
			case 2: // bottom slat
				renderBlocks.setRenderBounds(0.125D, 0.125D, 0.4375D, 0.875D, 0.1875D, 0.5625D);
				break;
			case 3: // next slat
				renderBlocks.setRenderBounds(0.125D, 0.375D, 0.4375D, 0.875D, 0.4375D, 0.5625D);
				break;
			case 4: // next slat
				renderBlocks.setRenderBounds(0.125D, 0.625D, 0.4375D, 0.875D, 0.6875D, 0.5625D);
				break;
			case 5: // top slat
				renderBlocks.setRenderBounds(0.125D, 0.875D, 0.4375D, 0.875D, 0.9375D, 0.5625D);
				break;
			}

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
			renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(3));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(4));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(5));
			tessellator.draw();
		}

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	/**
	 * Renders ladder.
	 */
	public boolean renderCarpentersBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
	{
		disableAO = true;

		int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		Block coverBlock = BlockProperties.getCoverBlock(TE, 6);

		double xLow = 0.0D;
		double xHigh = 1.0D;
		double zLow = 0.0D;
		double zHigh = 1.0D;

		/*
		 * Gather adjacent ladder metadata.
		 */
		boolean connects_XN = TE.worldObj.getBlockId(x - 1, y, z) == srcBlock.blockID && TE.worldObj.getBlockMetadata(x - 1, y, z) == metadata;
		boolean connects_XP = TE.worldObj.getBlockId(x + 1, y, z) == srcBlock.blockID && TE.worldObj.getBlockMetadata(x + 1, y, z) == metadata;
		boolean connects_ZN = TE.worldObj.getBlockId(x, y, z - 1) == srcBlock.blockID && TE.worldObj.getBlockMetadata(x, y, z - 1) == metadata;
		boolean connects_ZP = TE.worldObj.getBlockId(x, y, z + 1) == srcBlock.blockID && TE.worldObj.getBlockMetadata(x, y, z + 1) == metadata;

		switch (metadata) {
		case Ladder.FACING_ON_X:

			// Side supports
			if (!connects_XN) {
				renderBlocks.setRenderBounds(0.0D, 0.0D, 0.375D, 0.125D, 1.0D, 0.625D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}
			if (!connects_XP) {
				renderBlocks.setRenderBounds(0.875D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			xLow  = connects_XN ? 0.0D : 0.125D;
			xHigh = connects_XP ? 1.0D : 0.875D;

			// Slats
			renderBlocks.setRenderBounds(xLow, 0.125D, 0.4375D, xHigh, 0.1875D, 0.5625D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(xLow, 0.375D, 0.4375D, xHigh, 0.4375D, 0.5625D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(xLow, 0.625D, 0.4375D, xHigh, 0.6875D, 0.5625D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(xLow, 0.875D, 0.4375D, xHigh, 0.9375D, 0.5625D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			break;
		case Ladder.FACING_ON_Z:

			// Side supports
			if (!connects_ZN) {
				renderBlocks.setRenderBounds(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.125D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}
			if (!connects_ZP) {
				renderBlocks.setRenderBounds(0.375D, 0.0D, 0.875D, 0.625D, 1.0D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			zLow  = connects_ZN ? 0.0D : 0.125D;
			zHigh = connects_ZP ? 1.0D : 0.875D;

			// Slats
			renderBlocks.setRenderBounds(0.4375D, 0.125D, zLow, 0.5625D, 0.1875D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(0.4375D, 0.375D, zLow, 0.5625D, 0.4375D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(0.4375D, 0.625D, zLow, 0.5625D, 0.6875D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(0.4375D, 0.875D, zLow, 0.5625D, 0.9375D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			break;
		case Ladder.FACING_NORTH: // Ladder on +Z

			// Side supports
			if (!connects_XN) {
				renderBlocks.setRenderBounds(0.0D, 0.0D, 0.8125D, 0.125D, 1.0D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}
			if (!connects_XP) {
				renderBlocks.setRenderBounds(0.875D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			xLow  = connects_XN ? 0.0D : 0.125D;
			xHigh = connects_XP ? 1.0D : 0.875D;

			// Slats
			renderBlocks.setRenderBounds(xLow, 0.125D, 0.875D, xHigh, 0.1875D, 1.0D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(xLow, 0.375D, 0.875D, xHigh, 0.4375D, 1.0D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(xLow, 0.625D, 0.875D, xHigh, 0.6875D, 1.0D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(xLow, 0.875D, 0.875D, xHigh, 0.9375D, 1.0D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			break;
		case Ladder.FACING_SOUTH: // Ladder on -Z

			// Side supports
			if (!connects_XN) {
				renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 0.1875D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}
			if (!connects_XP) {
				renderBlocks.setRenderBounds(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			xLow  = connects_XN ? 0.0D : 0.125D;
			xHigh = connects_XP ? 1.0D : 0.875D;

			// Slats
			renderBlocks.setRenderBounds(xLow, 0.125D, 0.0D, xHigh, 0.1875D, 0.1875D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(xLow, 0.375D, 0.0D, xHigh, 0.4375D, 0.1875D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(xLow, 0.625D, 0.0D, xHigh, 0.6875D, 0.1875D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(xLow, 0.875D, 0.0D, xHigh, 0.9375D, 0.1875D);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			break;
		case Ladder.FACING_WEST: // Ladder on +X

			// Side supports
			if (!connects_ZN) {
				renderBlocks.setRenderBounds(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}
			if (!connects_ZP) {
				renderBlocks.setRenderBounds(0.8125D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			zLow  = connects_ZN ? 0.0D : 0.125D;
			zHigh = connects_ZP ? 1.0D : 0.875D;

			// Slats
			renderBlocks.setRenderBounds(0.875D, 0.125D, zLow, 1.0D, 0.1875D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(0.875D, 0.375D, zLow, 1.0D, 0.4375D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(0.875D, 0.625D, zLow, 1.0D, 0.6875D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(0.875D, 0.875D, zLow, 1.0D, 0.9375D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			break;
		case Ladder.FACING_EAST: // Ladder on -X

			// Side supports
			if (!connects_ZN) {
				renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 0.125D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}
			if (!connects_ZP) {
				renderBlocks.setRenderBounds(0.0D, 0.0D, 0.875D, 0.1875D, 1.0D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			zLow  = connects_ZN ? 0.0D : 0.125D;
			zHigh = connects_ZP ? 1.0D : 0.875D;

			// Slats
			renderBlocks.setRenderBounds(0.0D, 0.125D, zLow, 0.1875D, 0.1875D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(0.0D, 0.375D, zLow, 0.1875D, 0.4375D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(0.0D, 0.625D, zLow, 0.1875D, 0.6875D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(0.0D, 0.875D, zLow, 0.1875D, 0.9375D, zHigh);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			break;
		}

		disableAO = false;

		return true;
	}

}