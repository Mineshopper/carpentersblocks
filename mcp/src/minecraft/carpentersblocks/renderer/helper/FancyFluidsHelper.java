package carpentersblocks.renderer.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;
import carpentersblocks.renderer.BlockHandlerBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FancyFluidsHelper {

    /**
     * Renders fancy fluids in block space.
     */
    public static boolean render(BlockHandlerBase blockHandler, int x, int y, int z)
    {
        World world = blockHandler.TE.getWorldObj();

        Block block_XN   = Block.blocksList[world.getBlockId(x - 1, y, z)];
        Block block_XP   = Block.blocksList[world.getBlockId(x + 1, y, z)];
        Block block_ZN   = Block.blocksList[world.getBlockId(x, y, z - 1)];
        Block block_ZP   = Block.blocksList[world.getBlockId(x, y, z + 1)];
        Block block_XZNP = Block.blocksList[world.getBlockId(x - 1, y, z + 1)];
        Block block_XZPP = Block.blocksList[world.getBlockId(x + 1, y, z + 1)];
        Block block_XZNN = Block.blocksList[world.getBlockId(x - 1, y, z - 1)];
        Block block_XZPN = Block.blocksList[world.getBlockId(x + 1, y, z - 1)];

        boolean isFluid_XN   = block_XN != null ? block_XN instanceof BlockFluid || block_XN instanceof IFluidBlock : false;
        boolean isFluid_XP   = block_XP != null ? block_XP instanceof BlockFluid || block_XP instanceof IFluidBlock : false;
        boolean isFluid_ZN   = block_ZN != null ? block_ZN instanceof BlockFluid || block_ZN instanceof IFluidBlock : false;
        boolean isFluid_ZP   = block_ZP != null ? block_ZP instanceof BlockFluid || block_ZP instanceof IFluidBlock : false;
        boolean isFluid_XZNP = block_XZNP != null ? block_XZNP instanceof BlockFluid || block_XZNP instanceof IFluidBlock : false;
        boolean isFluid_XZPP = block_XZPP != null ? block_XZPP instanceof BlockFluid || block_XZPP instanceof IFluidBlock : false;
        boolean isFluid_XZNN = block_XZNN != null ? block_XZNN instanceof BlockFluid || block_XZNN instanceof IFluidBlock : false;
        boolean isFluid_XZPN = block_XZPN != null ? block_XZPN instanceof BlockFluid || block_XZPN instanceof IFluidBlock : false;

        Block block = Block.blocksList[world.getBlockId(x, y, z)];

        boolean isSolid_XN = block.isBlockSolidOnSide(world, x, y, z, ForgeDirection.WEST);
        boolean isSolid_XP = block.isBlockSolidOnSide(world, x, y, z, ForgeDirection.EAST);
        boolean isSolid_ZN = block.isBlockSolidOnSide(world, x, y, z, ForgeDirection.NORTH);
        boolean isSolid_ZP = block.isBlockSolidOnSide(world, x, y, z, ForgeDirection.SOUTH);

        int metadata = 0;
        int diagMetadata = 0;

        Block fluidBlock = null;
        Block diagFluidBlock = null;
        for (int count = 2; count < 10 && fluidBlock == null; ++count)
        {
            switch (count) {
                case 2:
                    if (isFluid_ZN && !isSolid_ZN) {
                        fluidBlock = Block.blocksList[world.getBlockId(x, y, z - 1)];
                        if (metadata < world.getBlockMetadata(x, y, z - 1)) {
                            metadata = world.getBlockMetadata(x, y, z - 1);
                        }
                    }
                    break;
                case 3:
                    if (isFluid_ZP && !isSolid_ZP) {
                        fluidBlock = Block.blocksList[world.getBlockId(x, y, z + 1)];
                        if (metadata < world.getBlockMetadata(x, y, z + 1)) {
                            metadata = world.getBlockMetadata(x, y, z + 1);
                        }
                    }
                    break;
                case 4:
                    if (isFluid_XN && !isSolid_XN) {
                        fluidBlock = Block.blocksList[world.getBlockId(x - 1, y, z)];
                        if (metadata < world.getBlockMetadata(x - 1, y, z)) {
                            metadata = world.getBlockMetadata(x - 1, y, z);
                        }
                    }
                    break;
                case 5:
                    if (isFluid_XP && !isSolid_XP) {
                        fluidBlock = Block.blocksList[world.getBlockId(x + 1, y, z)];
                        if (metadata < world.getBlockMetadata(x + 1, y, z)) {
                            metadata = world.getBlockMetadata(x + 1, y, z);
                        }
                    }
                    break;
                case 6:
                    if (isFluid_XZPN)  {
                        diagFluidBlock = Block.blocksList[world.getBlockId(x + 1, y, z - 1)];
                        if (diagMetadata < world.getBlockMetadata(x + 1, y, z - 1)) {
                            diagMetadata = world.getBlockMetadata(x + 1, y, z - 1);
                        }
                    }
                    break;
                case 7:
                    if (isFluid_XZPP) {
                        diagFluidBlock = Block.blocksList[world.getBlockId(x + 1, y, z + 1)];
                        if (diagMetadata < world.getBlockMetadata(x + 1, y, z + 1)) {
                            diagMetadata = world.getBlockMetadata(x + 1, y, z + 1);
                        }
                    }
                    break;
                case 8:
                    if (isFluid_XZNN) {
                        diagFluidBlock = Block.blocksList[world.getBlockId(x - 1, y, z - 1)];
                        if (diagMetadata < world.getBlockMetadata(x - 1, y, z - 1)) {
                            diagMetadata = world.getBlockMetadata(x - 1, y, z - 1);
                        }
                    }
                    break;
                case 9:
                    if (isFluid_XZNP) {
                        diagFluidBlock = Block.blocksList[world.getBlockId(x - 1, y, z + 1)];
                        if (diagMetadata < world.getBlockMetadata(x - 1, y, z + 1)) {
                            diagMetadata = world.getBlockMetadata(x - 1, y, z + 1);
                        }
                    }
                    break;
            }
        }

        if (fluidBlock != null && fluidBlock.getRenderBlockPass() == 0 || diagFluidBlock != null && diagFluidBlock.getRenderBlockPass() == 0)
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
                        if (
                                !isSolid_ZP &&
                                isFluid_ZP || !block_ZP.isBlockSolidOnSide(world, x, y, z + 1, ForgeDirection.NORTH) &&
                                (isFluid_XZNP && !block_ZP.isBlockSolidOnSide(world, x, y, z + 1, ForgeDirection.WEST) || isFluid_XZPP && !block_ZP.isBlockSolidOnSide(world, x, y, z + 1, ForgeDirection.EAST))
                                ) {
                            renderFluid = true;
                        }
                        break;
                    case 3:
                        if (
                                !isSolid_ZN &&
                                isFluid_ZN || !block_ZP.isBlockSolidOnSide(world, x, y, z - 1, ForgeDirection.SOUTH) &&
                                (isFluid_XZNN && !block_ZP.isBlockSolidOnSide(world, x, y, z - 1, ForgeDirection.WEST) || isFluid_XZPN && !block_ZP.isBlockSolidOnSide(world, x, y, z - 1, ForgeDirection.EAST))
                                ) {
                            renderFluid = true;
                        }
                        break;
                    case 4:
                        if (
                                !isSolid_XP &&
                                isFluid_XP || !block_XP.isBlockSolidOnSide(world, x + 1, y, z, ForgeDirection.WEST) &&
                                (isFluid_XZPP && !block_XP.isBlockSolidOnSide(world, x + 1, y, z, ForgeDirection.SOUTH) || isFluid_XZPN && !block_XP.isBlockSolidOnSide(world, x + 1, y, z, ForgeDirection.NORTH))
                                ) {
                            renderFluid = true;
                        }
                        break;
                    case 5:
                        if (
                                !isSolid_XN &&
                                isFluid_XN || !block_XN.isBlockSolidOnSide(world, x - 1, y, z, ForgeDirection.EAST) &&
                                (isFluid_XZNP && !block_XN.isBlockSolidOnSide(world, x - 1, y, z, ForgeDirection.SOUTH) || isFluid_XZNN && !block_XN.isBlockSolidOnSide(world, x - 1, y, z, ForgeDirection.NORTH))
                                ) {
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
                    blockHandler.renderBlocks.setRenderBounds(minX, offset, minZ, maxX, fluidHeight, maxZ);
                    float rgb[] = blockHandler.getBlockRGB(fluidBlock, metadata, x, y, z, 1, null);
                    blockHandler.renderBlocks.renderStandardBlockWithColorMultiplier(fluidBlock, x, y, z, rgb[0], rgb[1], rgb[2]);
                    return true;
                }
            }
        }

        return false;
    }

}
