package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Bed;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BedDesignHandler;
import carpentersblocks.util.handler.IconHandler;

public class BlockHandlerCarpentersBed extends BlockHandlerBase
{

	@Override
	/**
	 * Renders bed
	 */
	public boolean renderCarpentersBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
	{
		Block coverBlock = BlockProperties.getCoverBlock(TE, 6);

		renderNormalBed(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

		return true;
	}

	public boolean renderNormalBed(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z)
	{
		int data = BlockProperties.getData(TE);
		ForgeDirection dir = Bed.getDirection(TE);

		disableAO = true;

		boolean isHead = Bed.isHeadOfBed(TE);

		TECarpentersBlock TE_opp = Bed.getOppositeTE(TE);

		boolean isOccupied = Bed.isOccupied(TE);
		int	blanketColor = 0;
		int frameColor = 0;

		/*
		 * The bed piece will render before it's companion
		 * is created, and just after it is destroyed.
		 * Therefore, we must null check it.
		 */
		if (TE_opp != null)
		{
			isOccupied |= Bed.isOccupied(TE_opp);

			/*
			 * Get bed dye colors.
			 * 
			 * Blanket color is foot dye color.
			 * Frame color is head dye color.
			 */
			blanketColor = BlockProperties.getDyeColor(isHead ? TE_opp : TE, 6);
			frameColor = BlockProperties.getDyeColor(isHead ? TE : TE_opp, 6);
		}


		int design = Bed.getDesign(data);

		boolean hasCustomBlanket = design > 0 && BedDesignHandler.hasBlanket[design];

		Icon icon_pillow = hasCustomBlanket && BedDesignHandler.hasPillow[design] ? IconHandler.icon_bed_pillow_custom[design] : IconHandler.icon_bed_pillow;

		/*
		 * Check for adjacent bed pieces that can connect.
		 */
		boolean	bedParallelPos = false;
		boolean bedParallelNeg = false;

		if (dir.equals(ForgeDirection.NORTH) || dir.equals(ForgeDirection.SOUTH)) 
		{
			if (renderBlocks.blockAccess.getBlockId(x + 1, y, z) == srcBlock.blockID) {
				TECarpentersBlock TE_adj = (TECarpentersBlock) renderBlocks.blockAccess.getBlockTileEntity(x + 1,  y,  z);
				bedParallelPos = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
			}
			if (renderBlocks.blockAccess.getBlockId(x - 1, y, z) == srcBlock.blockID) {
				TECarpentersBlock TE_adj = (TECarpentersBlock) renderBlocks.blockAccess.getBlockTileEntity(x - 1,  y,  z);
				bedParallelNeg = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
			}
			
		} else {
			
			if (renderBlocks.blockAccess.getBlockId(x, y, z + 1) == srcBlock.blockID) {
				TECarpentersBlock TE_adj = (TECarpentersBlock) renderBlocks.blockAccess.getBlockTileEntity(x,  y, z + 1);
				bedParallelPos = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
			}
			if (renderBlocks.blockAccess.getBlockId(x, y, z - 1) == srcBlock.blockID) {
				TECarpentersBlock TE_adj = (TECarpentersBlock) renderBlocks.blockAccess.getBlockTileEntity(x,  y, z - 1);
				bedParallelNeg = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
			}
			
		}

		switch (dir)
		{
		case NORTH: // -Z
		{
			if (isHead) {

				setMetadataOverride(BlockProperties.getCoverMetadata(TE, 6));

				// Render headboard
				renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.875D, 0.875D, 0.875D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				// Render legs
				renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.875D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				// Render support board
				renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 0.875D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				clearMetadataOverride();

				suppressDyeColor = true;
				suppressOverlay = true;
				suppressPattern = true;

				// Render mattress
				setMetadataOverride(0);
				renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 0.875D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearMetadataOverride();

				// Render pillow
				renderBlocks.uvRotateTop = 2;
				setIconOverride(6, icon_pillow);
				renderBlocks.setRenderBounds(0.125D, 0.5625D, 0.4375D, 0.875D, 0.6875D, 0.8125D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearIconOverride(6);
				renderBlocks.uvRotateTop = 0;

				// Render blanket
				if (!hasCustomBlanket) {
					setMetadataOverride(blanketColor);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 0.5D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 0.5D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.5D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					clearMetadataOverride();
				}

				suppressDyeColor = false;
				suppressOverlay = false;
				suppressPattern = false;

			} else {

				setDyeColorOverride(frameColor);
				setMetadataOverride(BlockProperties.getCoverMetadata(TE, 6));

				// Render legs
				if (!bedParallelNeg) {
					renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.125D, 0.1875D, 0.125D);
					renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				}
				if (!bedParallelPos) {
					renderBlocks.setRenderBounds(0.875D, 0.0D, 0.0D, 1.0D, 0.1875D, 0.125D);
					renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				}

				// Render support board
				renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				clearMetadataOverride();
				clearDyeColorOverride();

				suppressDyeColor = true;
				suppressOverlay = true;
				suppressPattern = true;

				// Render mattress
				setMetadataOverride(0);
				renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0625D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 1.0D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearMetadataOverride();

				// Render blanket
				if (!hasCustomBlanket) {
					setMetadataOverride(blanketColor);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0625D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.9375D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					clearMetadataOverride();
				}

				suppressDyeColor = false;
				suppressOverlay = false;
				suppressPattern = false;

			}
			break;
		}
		case SOUTH: // +Z
		{
			if (isHead) {

				setMetadataOverride(BlockProperties.getCoverMetadata(TE, 6));

				// Render headboard
				renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.0D, 0.875D, 0.875D, 0.125D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				// Render legs
				renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.0D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 0.125D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				// Render support board
				renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.125D, 1.0D, 0.3125D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				clearMetadataOverride();

				suppressDyeColor = true;
				suppressOverlay = true;
				suppressPattern = true;

				// Render mattress
				setMetadataOverride(0);
				renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.125D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 1.0D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearMetadataOverride();

				// Render pillow
				setIconOverride(6, icon_pillow);
				renderBlocks.setRenderBounds(0.125D, 0.5625D, 0.1875D, 0.875D, 0.6875D, 0.5625D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearIconOverride(6);
				renderBlocks.uvRotateTop = 0;

				// Render blanket
				if (!hasCustomBlanket) {
					setMetadataOverride(blanketColor);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.5D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					clearMetadataOverride();
				}

				suppressDyeColor = false;
				suppressOverlay = false;
				suppressPattern = false;

			} else {

				setDyeColorOverride(frameColor);
				setMetadataOverride(BlockProperties.getCoverMetadata(TE, 6));

				// Render legs
				if (!bedParallelNeg) {
					renderBlocks.setRenderBounds(0.0D, 0.0D, 0.875D, 0.125D, 0.1875D, 1.0D);
					renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				}
				if (!bedParallelPos) {
					renderBlocks.setRenderBounds(0.875D, 0.0D, 0.875D, 1.0D, 0.1875D, 1.0D);
					renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				}

				// Render support board
				renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				clearMetadataOverride();
				clearDyeColorOverride();

				suppressDyeColor = true;
				suppressOverlay = true;
				suppressPattern = true;

				// Render mattress
				setMetadataOverride(0);
				renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 0.9375D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearMetadataOverride();

				// Render blanket
				if (!hasCustomBlanket) {
					setMetadataOverride(blanketColor);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0625D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 0.9375D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					clearMetadataOverride();
				}

				suppressDyeColor = false;
				suppressOverlay = false;
				suppressPattern = false;

			}
			break;
		}
		case WEST: // -X
		{
			if (isHead) {

				setMetadataOverride(BlockProperties.getCoverMetadata(TE, 6));

				// Render headboard
				renderBlocks.setRenderBounds(0.875D, 0.1875D, 0.125D, 1.0D, 0.875D, 0.875D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				// Render legs
				renderBlocks.setRenderBounds(0.875D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 1.0D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				// Render support board
				renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 0.875D, 0.3125D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				clearMetadataOverride();

				suppressDyeColor = true;
				suppressOverlay = true;
				suppressPattern = true;

				// Render mattress
				setMetadataOverride(0);
				renderBlocks.setRenderBounds(0.0D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 0.875D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearMetadataOverride();

				// Render pillow
				renderBlocks.uvRotateTop = 1;
				setIconOverride(6, icon_pillow);
				renderBlocks.setRenderBounds(0.4375D, 0.5625D, 0.125D, 0.8125D, 0.6875D, 0.875D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearIconOverride(6);
				renderBlocks.uvRotateTop = 0;

				// Render blanket
				if (!hasCustomBlanket) {
					setMetadataOverride(blanketColor);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.5D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 0.5D, isOccupied ? 0.875D : 0.625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 0.5D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					clearMetadataOverride();
				}

				suppressDyeColor = false;
				suppressOverlay = false;
				suppressPattern = false;

			} else {

				setDyeColorOverride(frameColor);
				setMetadataOverride(BlockProperties.getCoverMetadata(TE, 6));

				// Render legs
				if (!bedParallelNeg) {
					renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.125D, 0.1875D, 0.125D);
					renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				}
				if (!bedParallelPos) {
					renderBlocks.setRenderBounds(0.0D, 0.0D, 0.875D, 0.125D, 0.1875D, 1.0D);
					renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				}

				// Render support board
				renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				clearMetadataOverride();
				clearDyeColorOverride();

				suppressDyeColor = true;
				suppressOverlay = true;
				suppressPattern = true;

				// Render mattress
				setMetadataOverride(0);
				renderBlocks.setRenderBounds(0.0625D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 1.0D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearMetadataOverride();

				// Render blanket
				if (!hasCustomBlanket) {
					setMetadataOverride(blanketColor);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0625D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 0.9375D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					clearMetadataOverride();
				}

				suppressDyeColor = false;
				suppressOverlay = false;
				suppressPattern = false;

			}
			break;
		}
		default: // EAST +X
		{
			if (isHead) {

				setMetadataOverride(BlockProperties.getCoverMetadata(TE, 6));

				// Render headboard
				renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.125D, 0.125D, 0.875D, 0.875D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				// Render legs
				renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				renderBlocks.setRenderBounds(0.0D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 0.125D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				// Render support board
				renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				clearMetadataOverride();

				suppressDyeColor = true;
				suppressOverlay = true;
				suppressPattern = true;

				// Render mattress
				setMetadataOverride(0);
				renderBlocks.setRenderBounds(0.125D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 1.0D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearMetadataOverride();

				// Render pillow
				renderBlocks.uvRotateTop = 3;
				setIconOverride(6, icon_pillow);
				renderBlocks.setRenderBounds(0.1875D, 0.5625D, 0.125D, 0.5625D, 0.6875D, 0.875D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearIconOverride(6);
				renderBlocks.uvRotateTop = 0;

				// Render blanket
				if (!hasCustomBlanket) {
					setMetadataOverride(blanketColor);
					renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					clearMetadataOverride();
				}

				suppressDyeColor = false;
				suppressOverlay = false;
				suppressPattern = false;

			} else {

				setDyeColorOverride(frameColor);
				setMetadataOverride(BlockProperties.getCoverMetadata(TE, 6));

				// Render legs
				if (!bedParallelNeg) {
					renderBlocks.setRenderBounds(0.875D, 0.0D, 0.0D, 1.0D, 0.1875D, 0.125D);
					renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				}
				if (!bedParallelPos) {
					renderBlocks.setRenderBounds(0.875D, 0.0D, 0.875D, 1.0D, 0.1875D, 1.0D);
					renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				}

				// Render support board
				renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				clearMetadataOverride();
				clearDyeColorOverride();

				suppressDyeColor = true;
				suppressOverlay = true;
				suppressPattern = true;

				// Render mattress
				setMetadataOverride(0);
				renderBlocks.setRenderBounds(0.0D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 0.9375D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
				renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
				clearMetadataOverride();

				// Render blanket
				if (!hasCustomBlanket) {
					setMetadataOverride(blanketColor);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0625D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.9375D);
					renderStandardBlock(TE, renderBlocks, Block.cloth, srcBlock, x, y, z);
					clearMetadataOverride();
				}

				suppressDyeColor = false;
				suppressOverlay = false;
				suppressPattern = false;

			}
			break;
		}
		}

		/*
		 * If this bed has a blanket design, we'll render part of the blanket
		 * here to fill in the gaps (face at head of bed, bottom side).
		 */
		if (hasCustomBlanket)
		{
			setMetadataOverride(blanketColor);
			suppressDyeColor = true;
			suppressOverlay = true;
			suppressPattern = true;

			if (isHead)
			{
				switch (dir)
				{
				case NORTH: // -Z
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 0.5D);
					prepareRender(TE, renderBlocks, Block.cloth, srcBlock, 3, x, y, z, 0.8F);
					break;
				case SOUTH: // +Z
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
					prepareRender(TE, renderBlocks, Block.cloth, srcBlock, 2, x, y, z, 0.8F);
					break;
				case WEST: 	// -X
					renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.5D, isOccupied ? 0.875D : 0.625D, 1.0D);
					prepareRender(TE, renderBlocks, Block.cloth, srcBlock, 5, x, y, z, 0.6F);
					break;
				default: 	// EAST +X
					renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
					prepareRender(TE, renderBlocks, Block.cloth, srcBlock, 4, x, y, z, 0.6F);
					break;
				}

				prepareRender(TE, renderBlocks, Block.cloth, srcBlock, 0, x, y, z, 0.5F);

			} else {

				prepareRender(TE, renderBlocks, Block.cloth, srcBlock, 0, x, y, z, 0.5F);
			}

			suppressDyeColor = false;
			suppressOverlay = false;
			suppressPattern = false;
			clearMetadataOverride();
		}

		disableAO = false;

		return true;
	}

}