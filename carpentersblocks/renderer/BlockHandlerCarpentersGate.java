package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Gate;
import carpentersblocks.util.BlockProperties;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockHandlerCarpentersGate extends BlockHandlerBase implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
	{
		renderBlocks.setRenderBounds(0.0D, 0.3125D, 0.4375D, 0.125D, 1.0D, 0.5625D);
		super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

		renderBlocks.setRenderBounds(0.125D, 0.5D, 0.4375D, 0.875D, 0.9375D, 0.5625D);
		super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

		renderBlocks.setRenderBounds(0.875D, 0.3125D, 0.4375D, 1.0D, 1.0D, 0.5625D);
		super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
	}

	@Override
	/**
	 * Renders gate
	 */
	protected boolean renderCarpentersBlock(int x, int y, int z)
	{
		renderBlocks.renderAllFaces = true;

		Block block = BlockProperties.getCoverBlock(TE, 6);
		int type = Gate.getType(TE);

		switch (type) {
		case Gate.TYPE_PICKET:
			renderPicketGate(block, x, y, z);
			break;
		case Gate.TYPE_PLANK_VERTICAL:
			renderVerticalPlankGate(block, x, y, z);
			break;
		case Gate.TYPE_WALL:
			renderWallGate(block, x, y, z);
			break;
		default: // Gate.VANILLA
			renderVanillaGate(block, x, y, z);
			break;
		}

		renderBlocks.renderAllFaces = false;

		return true;
	}

	/**
	 * Renders gate at given coordinates
	 */
	private void renderVanillaGate(Block block, int x, int y, int z)
	{
		boolean isGateOpen = Gate.getState(TE) == Gate.STATE_OPEN;
		int dir = Gate.getDirOpen(TE);
		int facing = Gate.getFacing(TE);

		float y_Low2 = 0.375F;
		float y_High3 = 0.5625F;
		float y_Low3 = 0.75F;
		float y_High2 = 0.9375F;
		float y_Low = 0.3125F;
		float y_High = 1.0F;

		float yPlankOffset = Gate.getType(TE) * 0.0625F;

		boolean isGateAbove = renderBlocks.blockAccess.getBlockId(x, y + 1, z) == srcBlock.blockID;
		boolean isGateBelow = renderBlocks.blockAccess.getBlockId(x, y - 1, z) == srcBlock.blockID;

		boolean joinPlanks = Gate.getType(TE) == Gate.TYPE_VANILLA_X3;

		/*
		 * Render supports on sides of gate
		 */
		if (facing == Gate.FACING_ON_X)
		{
			renderBlocks.setRenderBounds(0.0F, isGateBelow ? 0.0F : y_Low - yPlankOffset, 0.4375F, 0.125F, isGateAbove ? 1.0F : y_High, 0.5625F);
			renderBlock(block, x, y, z);
			renderBlocks.setRenderBounds(0.875F, isGateBelow ? 0.0F : y_Low - yPlankOffset, 0.4375F, 1.0F, isGateAbove ? 1.0F : y_High, 0.5625F);
			renderBlock(block, x, y, z);
		}
		else
		{
			renderBlocks.setRenderBounds(0.4375F, isGateBelow ? 0.0F : y_Low - yPlankOffset, 0.0F, 0.5625F, isGateAbove ? 1.0F : y_High, 0.125F);
			renderBlock(block, x, y, z);
			renderBlocks.setRenderBounds(0.4375F, isGateBelow ? 0.0F : y_Low - yPlankOffset, 0.875F, 0.5625F, isGateAbove ? 1.0F : y_High, 1.0F);
			renderBlock(block, x, y, z);
		}

		if (isGateOpen)
		{
			if (facing == Gate.FACING_ON_Z)
			{
				if (dir == Gate.DIR_POS)
				{
					if (!joinPlanks) {

						renderBlocks.setRenderBounds(0.8125D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.0D, 0.9375D, isGateAbove ? 1.0F : y_High2, 0.125D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.8125D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.875D, 0.9375D, isGateAbove ? 1.0F : y_High2, 1.0D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625D, y_Low2 - yPlankOffset, 0.0D, 0.8125D, y_High3, 0.125D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625D, y_Low2 - yPlankOffset, 0.875D, 0.8125D, y_High3, 1.0D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625D, y_Low3 - yPlankOffset, 0.0D, 0.8125D, y_High2, 0.125D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625D, y_Low3 - yPlankOffset, 0.875D, 0.8125D, y_High2, 1.0D);
						renderBlock(block, x, y, z);

					} else {

						renderBlocks.setRenderBounds(0.5625D, isGateBelow ? 0.0F : 0.1875F, 0.0D, 0.9375D, isGateAbove ? 1.0F : y_High2, 0.125D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625D, isGateBelow ? 0.0F : 0.1875F, 0.875D, 0.9375D, isGateAbove ? 1.0F : y_High2, 1.0D);
						renderBlock(block, x, y, z);

					}

				}
				else
				{
					if (!joinPlanks) {

						renderBlocks.setRenderBounds(0.0625D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.0D, 0.1875D, isGateAbove ? 1.0F : y_High2, 0.125D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0625D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.875D, 0.1875D, isGateAbove ? 1.0F : y_High2, 1.0D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.1875D, y_Low2 - yPlankOffset, 0.0D, 0.4375D, y_High3, 0.125D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.1875D, y_Low2 - yPlankOffset, 0.875D, 0.4375D, y_High3, 1.0D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.1875D, y_Low3 - yPlankOffset, 0.0D, 0.4375D, y_High2, 0.125D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.1875D, y_Low3 - yPlankOffset, 0.875D, 0.4375D, y_High2, 1.0D);
						renderBlock(block, x, y, z);

					} else {

						renderBlocks.setRenderBounds(0.0625D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.0D, 0.4375D, isGateAbove ? 1.0F : y_High2, 0.125D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0625D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.875D, 0.4375D, isGateAbove ? 1.0F : y_High2, 1.0D);
						renderBlock(block, x, y, z);

					}

				}
			}
			else
			{
				if (dir == Gate.DIR_POS)
				{
					if (!joinPlanks) {

						renderBlocks.setRenderBounds(0.0D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.8125D, 0.125D, isGateAbove ? 1.0F : y_High2, 0.9375D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.875D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.8125D, 1.0D, isGateAbove ? 1.0F : y_High2, 0.9375D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0D, y_Low2 - yPlankOffset, 0.5625D, 0.125D, y_High3, 0.8125D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.875D, y_Low2 - yPlankOffset, 0.5625D, 1.0D, y_High3, 0.8125D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0D, y_Low3 - yPlankOffset, 0.5625D, 0.125D, y_High2, 0.8125D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.875D, y_Low3 - yPlankOffset, 0.5625D, 1.0D, y_High2, 0.8125D);
						renderBlock(block, x, y, z);

					} else {

						renderBlocks.setRenderBounds(0.875D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.5625D, 1.0D, isGateAbove ? 1.0F : y_High2, 0.9375D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.5625D, 0.125D, isGateAbove ? 1.0F : y_High2, 0.9375D);
						renderBlock(block, x, y, z);

					}

				}
				else
				{
					if (!joinPlanks) {

						renderBlocks.setRenderBounds(0.0D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.0625D, 0.125D, isGateAbove ? 1.0F : y_High2, 0.1875D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.875D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.0625D, 1.0D, isGateAbove ? 1.0F : y_High2, 0.1875D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0D, y_Low2 - yPlankOffset, 0.1875D, 0.125D, y_High3, 0.4375D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.875D, y_Low2 - yPlankOffset, 0.1875D, 1.0D, y_High3, 0.4375D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0D, y_Low3 - yPlankOffset, 0.1875D, 0.125D, y_High2, 0.4375D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.875D, y_Low3 - yPlankOffset, 0.1875D, 1.0D, y_High2, 0.4375D);
						renderBlock(block, x, y, z);

					} else {

						renderBlocks.setRenderBounds(0.875D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.0625D, 1.0D, isGateAbove ? 1.0F : y_High2, 0.4375D);
						renderBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.0625D, 0.125D, isGateAbove ? 1.0F : y_High2, 0.4375D);
						renderBlock(block, x, y, z);

					}

				}
			}
		}
		else
		{
			if (facing == Gate.FACING_ON_X)
			{
				if (!joinPlanks) {

					renderBlocks.setRenderBounds(0.375F, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.4375F, 0.5F, isGateAbove ? 1.0F : y_High2, 0.5625F);
					renderBlock(block, x, y, z); // Center Post
					renderBlocks.setRenderBounds(0.5F, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.4375F, 0.625F, isGateAbove ? 1.0F : y_High2, 0.5625F);
					renderBlock(block, x, y, z); // Center Post
					renderBlocks.setRenderBounds(0.625F, y_Low2 - yPlankOffset, 0.4375F, 0.875F, y_High3, 0.5625F);
					renderBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.625F, y_Low3 - yPlankOffset, 0.4375F, 0.875F, y_High2, 0.5625F);
					renderBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.125F, y_Low2 - yPlankOffset, 0.4375F, 0.375F, y_High3, 0.5625F);
					renderBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.125F, y_Low3 - yPlankOffset, 0.4375F, 0.375F, y_High2, 0.5625F);
					renderBlock(block, x, y, z);

				} else {

					renderBlocks.setRenderBounds(0.125F, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.4375F, 0.875F, isGateAbove ? 1.0F : y_High2, 0.5625F);
					renderBlock(block, x, y, z);

				}

			}
			else
			{
				if (!joinPlanks) {

					renderBlocks.setRenderBounds(0.4375F, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.375F, 0.5625F, isGateAbove ? 1.0F : y_High2, 0.5F);
					renderBlock(block, x, y, z); // Center Post
					renderBlocks.setRenderBounds(0.4375F, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.5F, 0.5625F, isGateAbove ? 1.0F : y_High2, 0.625F);
					renderBlock(block, x, y, z); // Center Post
					renderBlocks.setRenderBounds(0.4375F, y_Low2 - yPlankOffset, 0.625F, 0.5625F, y_High3, 0.875F);
					renderBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.4375F, y_Low3 - yPlankOffset, 0.625F, 0.5625F, y_High2, 0.875F);
					renderBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.4375F, y_Low2 - yPlankOffset, 0.125F, 0.5625F, y_High3, 0.375F);
					renderBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.4375F, y_Low3 - yPlankOffset, 0.125F, 0.5625F, y_High2, 0.375F);
					renderBlock(block, x, y, z);

				} else {

					renderBlocks.setRenderBounds(0.4375F, isGateBelow ? 0.0F : y_Low2 - yPlankOffset, 0.125F, 0.5625F, isGateAbove ? 1.0F : y_High2, 0.875F);
					renderBlock(block, x, y, z);

				}

			}
		}

		renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}

	/**
	 * Renders gate at given coordinates
	 */
	private void renderPicketGate(Block block, int x, int y, int z)
	{
		boolean isGateOpen = Gate.getState(TE) == Gate.STATE_OPEN;
		int dir = Gate.getDirOpen(TE);
		int facing = Gate.getFacing(TE);

		float x_Low = 0.0F;
		float x_High = 1.0F;
		float y_Low = 0.0F;
		float y_High = 1.0F;
		float z_Low = 0.0F;
		float z_High = 1.0F;

		boolean isGateAbove = renderBlocks.blockAccess.getBlockId(x, y + 1, z) == srcBlock.blockID;
		boolean isGateBelow = renderBlocks.blockAccess.getBlockId(x, y - 1, z) == srcBlock.blockID;

		if (isGateOpen)
		{
			if (facing == Gate.FACING_ON_Z)
			{
				if (dir == Gate.DIR_POS)
				{
					/*
					 * Render horizontal beams
					 */

					x_Low = 0.5F;
					z_Low = 0.0625F;
					z_High = 0.1875F;

					// Render top beam
					if (!isGateAbove) {
						y_Low = 0.625F;
						y_High = 0.6875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						z_Low = 0.8125F;
						z_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					// Render bottom beam
					if (!isGateBelow) {
						y_Low = 0.1875F;
						y_High = 0.25F;
						z_Low = 0.0625F;
						z_High = 0.1875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						z_Low = 0.8125F;
						z_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					/*
					 * Render vertical slats
					 */
					{
						y_Low = isGateBelow ? 0.0F : 0.0625F;

						for (int count = 0; count < 3; ++count)
						{
							switch (count) {
							case 0:
								x_High = 0.5625F;
								y_High = isGateAbove ? 1.0F : 0.8125F;
								break;
							case 1:
								x_Low = 0.6875F;
								x_High = 0.8125F;
								y_High = isGateAbove ? 1.0F : 0.875F;
								break;
							case 2:
								x_Low = 0.9375F;
								x_High = 1.0F;
								y_High = isGateAbove ? 1.0F : 0.875F;
								break;
							}

							z_Low = 0.0F;
							z_High = 0.0625F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low += 0.1875F;
							z_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low = 0.75F;
							z_High = 0.8125F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low += 0.1875F;
							z_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
						}
					}
				}
				else
				{
					/*
					 * Render horizontal beams
					 */

					x_High = 0.5F;
					z_Low = 0.0625F;
					z_High = 0.1875F;

					// Render top beam
					if (!isGateAbove) {
						y_Low = 0.625F;
						y_High = 0.6875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						z_Low = 0.8125F;
						z_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					// Render bottom beam
					if (!isGateBelow) {
						y_Low = 0.1875F;
						y_High = 0.25F;
						z_Low = 0.0625F;
						z_High = 0.1875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						z_Low = 0.8125F;
						z_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					/*
					 * Render vertical slats
					 */
					{
						y_Low = isGateBelow ? 0.0F : 0.0625F;

						for (int count = 0; count < 3; ++count)
						{
							switch (count) {
							case 0:
								x_Low = 0.4375F;
								y_High = isGateAbove ? 1.0F : 0.8125F;
								break;
							case 1:
								x_Low = 0.1875F;
								x_High = 0.3125F;
								y_High = isGateAbove ? 1.0F : 0.875F;
								break;
							case 2:
								x_Low = 0.0F;
								x_High = 0.0625F;
								y_High = isGateAbove ? 1.0F : 0.875F;
								break;
							}

							z_Low = 0.0F;
							z_High = 0.0625F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low += 0.1875F;
							z_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low = 0.75F;
							z_High = 0.8125F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low += 0.1875F;
							z_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
						}
					}
				}
			}
			else
			{
				if (dir == Gate.DIR_POS)
				{
					/*
					 * Render horizontal beams
					 */

					x_Low = 0.0625F;
					x_High = 0.1875F;
					z_Low = 0.5F;

					// Render top beam
					if (!isGateAbove) {
						y_Low = 0.625F;
						y_High = 0.6875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						x_Low = 0.8125F;
						x_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					// Render bottom beam
					if (!isGateBelow) {
						x_Low = 0.0625F;
						x_High = 0.1875F;
						y_Low = 0.1875F;
						y_High = 0.25F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						x_Low = 0.8125F;
						x_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					/*
					 * Render vertical slats
					 */
					{
						y_Low = isGateBelow ? 0.0F : 0.0625F;

						for (int count = 0; count < 3; ++count)
						{
							switch (count) {
							case 0:
								y_High = isGateAbove ? 1.0F : 0.8125F;
								z_High = 0.5625F;
								break;
							case 1:
								y_High = isGateAbove ? 1.0F : 0.875F;
								z_Low = 0.6875F;
								z_High = 0.8125F;
								break;
							case 2:
								y_High = isGateAbove ? 1.0F : 0.875F;
								z_Low = 0.9375F;
								z_High = 1.0F;
								break;
							}

							x_Low = 0.0F;
							x_High = 0.0625F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low += 0.1875F;
							x_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low = 0.75F;
							x_High = 0.8125F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low += 0.1875F;
							x_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
						}
					}
				}
				else
				{
					/*
					 * Render horizontal beams
					 */

					x_Low = 0.0625F;
					x_High = 0.1875F;
					z_High = 0.5F;

					// Render top beam
					if (!isGateAbove) {
						y_Low = 0.625F;
						y_High = 0.6875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						x_Low = 0.8125F;
						x_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					// Render bottom beam
					if (!isGateBelow) {
						x_Low = 0.0625F;
						x_High = 0.1875F;
						y_Low = 0.1875F;
						y_High = 0.25F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						x_Low = 0.8125F;
						x_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					/*
					 * Render vertical slats
					 */
					{
						y_Low = isGateBelow ? 0.0F : 0.0625F;

						for (int count = 0; count < 3; ++count)
						{
							switch (count) {
							case 0:
								y_High = isGateAbove ? 1.0F : 0.8125F;
								z_Low = 0.4375F;
								break;
							case 1:
								y_High = isGateAbove ? 1.0F : 0.875F;
								z_Low = 0.1875F;
								z_High = 0.3125F;
								break;
							case 2:
								y_High = isGateAbove ? 1.0F : 0.875F;
								z_Low = 0.0F;
								z_High = 0.0625F;
								break;
							}

							x_Low = 0.0F;
							x_High = 0.0625F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low += 0.1875F;
							x_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low = 0.75F;
							x_High = 0.8125F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low += 0.1875F;
							x_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
						}
					}
				}
			}
		}
		else
		{
			if (facing == Gate.FACING_ON_X)
			{
				/*
				 * Render horizontal beams
				 */

				z_Low = 0.4375F;
				z_High = 0.5625F;

				// Render top beam
				if (!isGateAbove) {
					y_Low = 0.625F;
					y_High = 0.6875F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}

				// Render bottom beam
				if (!isGateBelow) {
					y_Low = 0.1875F;
					y_High = 0.25F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}

				/*
				 * Render vertical slats
				 */
				{
					y_Low = isGateBelow ? 0.0F : 0.0625F;

					for (int count = 0; count < 5; ++count)
					{
						switch (count) {
						case 0:
							x_Low = 0.0F;
							x_High = 0.0625F;
							y_High = isGateAbove ? 1.0F : 0.8125F;
							break;
						case 1:
							x_Low = 0.1875F;
							x_High = 0.3125F;
							y_High = isGateAbove ? 1.0F : 0.875F;
							break;
						case 2:
							x_Low = 0.4375F;
							x_High = 0.5625F;
							y_High = isGateAbove ? 1.0F : 0.875F;
							break;
						case 3:
							x_Low = 0.6875F;
							x_High = 0.8125F;
							y_High = isGateAbove ? 1.0F : 0.875F;
							break;
						case 4:
							x_Low = 0.9375F;
							x_High = 1.0F;
							y_High = isGateAbove ? 1.0F : 0.8125F;
							break;
						}

						z_Low = 0.5625F;
						z_High = 0.625F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						z_Low -= 0.1875F;
						z_High -= 0.1875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}
				}
			}
			else
			{
				/*
				 * Render horizontal beams
				 */

				x_Low = 0.4375F;
				x_High = 0.5625F;

				// Render top beam
				if (!isGateAbove) {
					y_Low = 0.625F;
					y_High = 0.6875F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}

				// Render bottom beam
				if (!isGateBelow) {
					y_Low = 0.1875F;
					y_High = 0.25F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}

				/*
				 * Render vertical slats
				 */
				{
					y_Low = isGateBelow ? 0.0F : 0.0625F;

					for (int count = 0; count < 5; ++count)
					{
						switch (count) {
						case 0:
							y_High = isGateAbove ? 1.0F : 0.8125F;
							z_Low = 0.0F;
							z_High = 0.0625F;
							break;
						case 1:
							y_High = isGateAbove ? 1.0F : 0.875F;
							z_Low = 0.1875F;
							z_High = 0.3125F;
							break;
						case 2:
							y_High = isGateAbove ? 1.0F : 0.875F;
							z_Low = 0.4375F;
							z_High = 0.5625F;
							break;
						case 3:
							y_High = isGateAbove ? 1.0F : 0.875F;
							z_Low = 0.6875F;
							z_High = 0.8125F;
							break;
						case 4:
							y_High = isGateAbove ? 1.0F : 0.8125F;
							z_Low = 0.9375F;
							z_High = 1.0F;
							break;
						}

						x_Low = 0.5625F;
						x_High = 0.625F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						x_Low -= 0.1875F;
						x_High -= 0.1875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}
				}
			}
		}

		renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}

	/**
	 * Renders gate at given coordinates
	 */
	private void renderVerticalPlankGate(Block block, int x, int y, int z)
	{
		boolean isGateOpen = Gate.getState(TE) == Gate.STATE_OPEN;
		int dir = Gate.getDirOpen(TE);
		int facing = Gate.getFacing(TE);

		float x_Low = 0.0F;
		float x_High = 1.0F;
		float y_Low = 0.0F;
		float y_High = 1.0F;
		float z_Low = 0.0F;
		float z_High = 1.0F;

		boolean isGateAbove = renderBlocks.blockAccess.getBlockId(x, y + 1, z) == srcBlock.blockID;
		boolean isGateBelow = renderBlocks.blockAccess.getBlockId(x, y - 1, z) == srcBlock.blockID;

		if (isGateOpen)
		{
			if (facing == Gate.FACING_ON_Z)
			{
				if (dir == Gate.DIR_POS)
				{
					/*
					 * Render horizontal beams
					 */

					x_Low = 0.5F;
					z_Low = 0.0625F;
					z_High = 0.1875F;

					// Render top beam
					if (!isGateAbove) {
						y_Low = 0.75F;
						y_High = 0.875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						z_Low = 0.8125F;
						z_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					// Render bottom beam
					if (!isGateBelow) {
						y_Low = 0.125F;
						y_High = 0.25F;
						z_Low = 0.0625F;
						z_High = 0.1875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						z_Low = 0.8125F;
						z_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					/*
					 * Render vertical slats
					 */
					{
						y_Low = 0.0F;
						y_High = 1.0F;
						z_Low = 0.0F;

						for (int count = 0; count < 2; ++count)
						{
							switch (count) {
							case 0:
								x_High = 0.6875F;
								z_High = 0.0625F;
								break;
							case 1:
								x_Low = 0.8125F;
								x_High = 1.0F;
								z_Low = 0.0F;
								z_High = 0.0625F;
								break;
							}

							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low += 0.1875F;
							z_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low = 0.75F;
							z_High = 0.8125F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low += 0.1875F;
							z_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
						}
					}
				}
				else
				{
					/*
					 * Render horizontal beams
					 */

					x_High = 0.5F;
					z_Low = 0.0625F;
					z_High = 0.1875F;

					// Render top beam
					if (!isGateAbove) {
						y_Low = 0.75F;
						y_High = 0.875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						z_Low = 0.8125F;
						z_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					// Render bottom beam
					if (!isGateBelow) {
						y_Low = 0.125F;
						y_High = 0.25F;
						z_Low = 0.0625F;
						z_High = 0.1875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						z_Low = 0.8125F;
						z_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					/*
					 * Render vertical slats
					 */
					{
						y_Low = 0.0F;
						y_High = 1.0F;
						z_Low = 0.0F;

						for (int count = 0; count < 2; ++count)
						{
							switch (count) {
							case 0:
								x_Low = 0.3125F;
								z_High = 0.0625F;
								break;
							case 1:
								x_Low = 0.0F;
								x_High = 0.1875F;
								z_Low = 0.0F;
								z_High = 0.0625F;
								break;
							}

							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low += 0.1875F;
							z_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low = 0.75F;
							z_High = 0.8125F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							z_Low += 0.1875F;
							z_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
						}
					}
				}
			}
			else
			{
				if (dir == Gate.DIR_POS)
				{
					/*
					 * Render horizontal beams
					 */

					x_Low = 0.0625F;
					x_High = 0.1875F;
					z_Low = 0.5F;

					// Render top beam
					if (!isGateAbove) {
						y_Low = 0.75F;
						y_High = 0.875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						x_Low = 0.8125F;
						x_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					// Render bottom beam
					if (!isGateBelow) {
						x_Low = 0.0625F;
						x_High = 0.1875F;
						y_Low = 0.125F;
						y_High = 0.25F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						x_Low = 0.8125F;
						x_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					/*
					 * Render vertical slats
					 */
					{
						x_Low = 0.0F;
						y_Low = 0.0F;
						y_High = 1.0F;

						for (int count = 0; count < 2; ++count)
						{
							switch (count) {
							case 0:
								z_High = 0.6875F;
								x_High = 0.0625F;
								break;
							case 1:
								x_Low = 0.0F;
								x_High = 0.0625F;
								z_Low = 0.8125F;
								z_High = 1.0F;
								break;
							}

							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low += 0.1875F;
							x_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low = 0.75F;
							x_High = 0.8125F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low += 0.1875F;
							x_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
						}
					}
				}
				else
				{
					/*
					 * Render horizontal beams
					 */

					x_Low = 0.0625F;
					x_High = 0.1875F;
					z_High = 0.5F;

					// Render top beam
					if (!isGateAbove) {
						y_Low = 0.75F;
						y_High = 0.875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						x_Low = 0.8125F;
						x_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					// Render bottom beam
					if (!isGateBelow) {
						x_Low = 0.0625F;
						x_High = 0.1875F;
						y_Low = 0.125F;
						y_High = 0.25F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						x_Low = 0.8125F;
						x_High = 0.9375F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}

					/*
					 * Render vertical slats
					 */
					{
						x_Low = 0.0F;
						y_Low = 0.0F;
						y_High = 1.0F;

						for (int count = 0; count < 2; ++count)
						{
							switch (count) {
							case 0:
								x_High = 0.0625F;
								z_Low = 0.3125F;
								break;
							case 1:
								x_Low = 0.0F;
								x_High = 0.0625F;
								z_Low = 0.0F;
								z_High = 0.1875F;
								break;
							}

							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low += 0.1875F;
							x_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low = 0.75F;
							x_High = 0.8125F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
							x_Low += 0.1875F;
							x_High += 0.1875F;
							renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
							renderBlock(block, x, y, z);
						}
					}
				}
			}
		}
		else
		{
			if (facing == Gate.FACING_ON_X)
			{
				/*
				 * Render horizontal beams
				 */

				z_Low = 0.4375F;
				z_High = 0.5625F;

				// Render top beam
				if (!isGateAbove) {
					y_Low = 0.75F;
					y_High = 0.875F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}

				// Render bottom beam
				if (!isGateBelow) {
					y_Low = 0.125F;
					y_High = 0.25F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}

				/*
				 * Render vertical slats
				 */
				{
					y_Low = 0.0F;
					y_High = 1.0F;

					for (int count = 0; count < 3; ++count)
					{
						switch (count) {
						case 0:
							x_Low = 0.0F;
							x_High = 0.1875F;
							break;
						case 1:
							x_Low = 0.3125F;
							x_High = 0.6875F;
							break;
						case 2:
							x_Low = 0.8125F;
							x_High = 1.0F;
							break;
						}

						z_Low = 0.5625F;
						z_High = 0.625F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						z_Low -= 0.1875F;
						z_High -= 0.1875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}
				}
			}
			else
			{
				/*
				 * Render horizontal beams
				 */

				x_Low = 0.4375F;
				x_High = 0.5625F;

				// Render top beam
				if (!isGateAbove) {
					y_Low = 0.75F;
					y_High = 0.875F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}

				// Render bottom beam
				if (!isGateBelow) {
					y_Low = 0.125F;
					y_High = 0.25F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}

				/*
				 * Render planks
				 */
				{
					y_Low = 0.0F;
					y_High = 1.0F;

					for (int count = 0; count < 3; ++count)
					{
						switch (count) {
						case 0:
							z_Low = 0.0F;
							z_High = 0.1875F;
							break;
						case 1:
							z_Low = 0.3125F;
							z_High = 0.6875F;
							break;
						case 2:
							z_Low = 0.8125F;
							z_High = 1.0F;
							break;
						}

						x_Low = 0.5625F;
						x_High = 0.625F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
						x_Low -= 0.1875F;
						x_High -= 0.1875F;
						renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
						renderBlock(block, x, y, z);
					}
				}
			}
		}

		renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}

	/**
	 * Renders gate at given coordinates
	 */
	private void renderWallGate(Block block, int x, int y, int z)
	{
		boolean isGateOpen = Gate.getState(TE) == Gate.STATE_OPEN;
		int dir = Gate.getDirOpen(TE);
		int facing = Gate.getFacing(TE);

		float x_Low = 0.0F;
		float x_High = 1.0F;
		float y_Low = 0.0F;
		float y_High = 1.0F;
		float z_Low = 0.0F;
		float z_High = 1.0F;

		boolean isGateAbove = renderBlocks.blockAccess.getBlockId(x, y + 1, z) == srcBlock.blockID;
		y_High = isGateAbove || renderBlocks.blockAccess.isBlockSolidOnSide(x, y + 1, z, ForgeDirection.DOWN, true) ? 1.0F : 0.8125F;

		if (isGateOpen)
		{
			if (facing == Gate.FACING_ON_Z)
			{
				if (dir == Gate.DIR_POS)
				{
					x_Low = 0.5F;
					z_High = 0.125F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
					z_Low = 0.875F;
					z_High = 1.0F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}
				else
				{
					x_High = 0.5F;
					z_High = 0.125F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
					z_Low = 0.875F;
					z_High = 1.0F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}
			}
			else
			{
				if (dir == Gate.DIR_POS)
				{
					x_High = 0.125F;
					z_Low = 0.5F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
					x_Low = 0.875F;
					x_High = 1.0F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}
				else
				{
					x_High = 0.125F;
					z_High = 0.5F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
					x_Low = 0.875F;
					x_High = 1.0F;
					renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
					renderBlock(block, x, y, z);
				}
			}
		}
		else
		{
			if (facing == Gate.FACING_ON_X)
			{
				z_Low = 0.4375F;
				z_High = 0.5625F;
				renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
				renderBlock(block, x, y, z);
			}
			else
			{
				x_Low = 0.4375F;
				x_High = 0.5625F;
				renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
				renderBlock(block, x, y, z);
			}
		}

		renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}

}