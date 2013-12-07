package carpentersblocks.renderer.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.liquids.ILiquid;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.registry.FeatureRegistry;

public class FancyFluidsHelper {

	/**
	 * Renders fancy fluids in block space.
	 */
	public boolean render(TEBase TE, LightingHelper lightingHelper, RenderBlocks renderBlocks, int x, int y, int z, int renderPass)
	{
			Block block_XN = !renderBlocks.blockAccess.isAirBlock(x - 1, y, z) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z)] : null;
			Block block_XP = !renderBlocks.blockAccess.isAirBlock(x + 1, y, z) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z)] : null;
			Block block_ZN = !renderBlocks.blockAccess.isAirBlock(x, y, z - 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z - 1)] : null;
			Block block_ZP = !renderBlocks.blockAccess.isAirBlock(x, y, z + 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z + 1)] : null;
			Block block_XZNP = !renderBlocks.blockAccess.isAirBlock(x - 1, y, z + 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z + 1)] : null;
			Block block_XZPP = !renderBlocks.blockAccess.isAirBlock(x + 1, y, z + 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z + 1)] : null;
			Block block_XZNN = !renderBlocks.blockAccess.isAirBlock(x - 1, y, z - 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z - 1)] : null;
			Block block_XZPN = !renderBlocks.blockAccess.isAirBlock(x + 1, y, z - 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z - 1)] : null;

			boolean isFluid_XN = block_XN != null ? block_XN instanceof ILiquid || block_XN instanceof IFluidBlock || block_XN instanceof BlockFluid : false;
			boolean	isFluid_XP = block_XP != null ? block_XP instanceof ILiquid || block_XP instanceof IFluidBlock || block_XP instanceof BlockFluid : false;
			boolean	isFluid_ZN = block_ZN != null ? block_ZN instanceof ILiquid || block_ZN instanceof IFluidBlock || block_ZN instanceof BlockFluid : false;
			boolean	isFluid_ZP = block_ZP != null ? block_ZP instanceof ILiquid || block_ZP instanceof IFluidBlock || block_ZP instanceof BlockFluid : false;
			boolean	isFluid_XZNP = block_XZNP != null ? block_XZNP instanceof ILiquid || block_XZNP instanceof IFluidBlock || block_XZNP instanceof BlockFluid : false;
			boolean	isFluid_XZPP = block_XZPP != null ? block_XZPP instanceof ILiquid || block_XZPP instanceof IFluidBlock || block_XZPP instanceof BlockFluid : false;
			boolean	isFluid_XZNN = block_XZNN != null ? block_XZNN instanceof ILiquid || block_XZNN instanceof IFluidBlock || block_XZNN instanceof BlockFluid : false;
			boolean	isFluid_XZPN = block_XZPN != null ? block_XZPN instanceof ILiquid || block_XZPN instanceof IFluidBlock || block_XZPN instanceof BlockFluid : false;

			boolean isSolid_XN = renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z, ForgeDirection.WEST, true);
			boolean	isSolid_XP = renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z, ForgeDirection.EAST, true);
			boolean	isSolid_ZN = renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z, ForgeDirection.NORTH, true);
			boolean	isSolid_ZP = renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z, ForgeDirection.SOUTH, true);

			int metadata = 0;
			int diagMetadata = 0;

			Block fluidBlock = null;
			Block diagFluidBlock = null;
			for (int count = 2; count < 10 && fluidBlock == null; ++count)
			{
				switch (count) {
				case 2:
					if (isFluid_ZN && !isSolid_ZN) {
						fluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z - 1)];
						if (metadata < renderBlocks.blockAccess.getBlockMetadata(x, y, z - 1)) {
							metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z - 1);
						}
					}
					break;
				case 3:
					if (isFluid_ZP && !isSolid_ZP) {
						fluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z + 1)];
						if (metadata < renderBlocks.blockAccess.getBlockMetadata(x, y, z + 1)) {
							metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z + 1);
						}
					}
					break;
				case 4:
					if (isFluid_XN && !isSolid_XN) {
						fluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z)];
						if (metadata < renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z)) {
							metadata = renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z);
						}
					}
					break;
				case 5:
					if (isFluid_XP && !isSolid_XP) {
						fluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z)];
						if (metadata < renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z)) {
							metadata = renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z);
						}
					}
					break;
				case 6:
					if (isFluid_XZPN)  {
						diagFluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z - 1)];
						if (diagMetadata < renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z - 1)) {
							diagMetadata = renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z - 1);
						}
					}
					break;
				case 7:
					if (isFluid_XZPP) {
						diagFluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z + 1)];
						if (diagMetadata < renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z + 1)) {
							diagMetadata = renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z + 1);
						}
					}
					break;
				case 8:
					if (isFluid_XZNN) {
						diagFluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z - 1)];
						if (diagMetadata < renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z - 1)) {
							diagMetadata = renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z - 1);
						}
					}
					break;
				case 9:
					if (isFluid_XZNP) {
						diagFluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z + 1)];
						if (diagMetadata < renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z + 1)) {
							diagMetadata = renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z + 1);
						}
					}
					break;
				}
			}

			if (fluidBlock != null && renderPass == fluidBlock.getRenderBlockPass() || diagFluidBlock != null && renderPass == diagFluidBlock.getRenderBlockPass())
			{
				boolean renderFluid = false;

				float minX = 0;
				float minZ = 0;
				float maxX = 1.0F;
				float maxZ = 1.0F;
				float offset = 0.01F;

				for (int side = 2; side < 6; ++side)
				{
					switch (side)
					{
					case 2:
						if (	!isSolid_ZP &&
								(
										isFluid_ZP ||
										!renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z + 1, ForgeDirection.NORTH, true) &&
										(isFluid_XZNP && !renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z + 1, ForgeDirection.WEST, true) || isFluid_XZPP && !renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z + 1, ForgeDirection.EAST, true))
										)
								)
						{
							renderFluid = true;
						}
						break;
					case 3:
						if (	!isSolid_ZN &&
								(
										isFluid_ZN ||
										!renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z - 1, ForgeDirection.SOUTH, true) &&
										(isFluid_XZNN && !renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z - 1, ForgeDirection.WEST, true) || isFluid_XZPN && !renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z - 1, ForgeDirection.EAST, true))
										)
								)
						{
							renderFluid = true;
						}
						break;
					case 4:
						if (	!isSolid_XP &&
								(
										isFluid_XP ||
										!renderBlocks.blockAccess.isBlockSolidOnSide(x + 1, y, z, ForgeDirection.WEST, true) &&
										(isFluid_XZPP && !renderBlocks.blockAccess.isBlockSolidOnSide(x + 1, y, z, ForgeDirection.SOUTH, true) || isFluid_XZPN && !renderBlocks.blockAccess.isBlockSolidOnSide(x + 1, y, z, ForgeDirection.NORTH, true))
										)
								)
						{
							renderFluid = true;
						}
						break;
					case 5:
						if (	!isSolid_XN &&
								(
										isFluid_XN ||
										!renderBlocks.blockAccess.isBlockSolidOnSide(x - 1, y, z, ForgeDirection.EAST, true) &&
										(isFluid_XZNP && !renderBlocks.blockAccess.isBlockSolidOnSide(x - 1, y, z, ForgeDirection.SOUTH, true) || isFluid_XZNN && !renderBlocks.blockAccess.isBlockSolidOnSide(x - 1, y, z, ForgeDirection.NORTH, true))
										)
								)
						{
							renderFluid = true;
						}
						break;
					}
				}

				if (renderFluid)
				{
					if (isSolid_XN) {
						minX += offset;
					}
					if (isSolid_XP) {
						maxX -= offset;
					}
					if (isSolid_ZP) {
						maxZ -= offset;
					}
					if (isSolid_ZN) {
						minZ += offset;
					}

					if (fluidBlock == null)
					{
						fluidBlock = diagFluidBlock;
						metadata = diagMetadata;
					}

					if (!fluidBlock.hasTileEntity(metadata) && metadata == 0)
					{
						double fluidHeight = (fluidBlock instanceof BlockFluid ? 1.0D - 1.0F / 9.0F : 0.875F) - 0.0010000000474974513D;
						renderBlocks.setRenderBounds(minX, offset, minZ, maxX, fluidHeight, maxZ);
						float rgb[] = lightingHelper.getRGB(fluidBlock, x, y, z);
						renderBlocks.renderStandardBlockWithColorMultiplier(fluidBlock, x, y, z, rgb[0], rgb[1], rgb[2]);
						return true;
					}
				}
			}
		
		return false;
	}
	
}
