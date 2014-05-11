package carpentersblocks.renderer.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;
import carpentersblocks.tileentity.TEBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FancyFluidsHelper {

    /**
     * Renders fancy fluids in block space.
     */
    public static boolean render(TEBase TE, LightingHelper lightingHelper, RenderBlocks renderBlocks, int x, int y, int z, int renderPass)
    {
        Block block_XN   = renderBlocks.blockAccess.getBlock(x - 1, y, z);
        Block block_XP   = renderBlocks.blockAccess.getBlock(x + 1, y, z);
        Block block_ZN   = renderBlocks.blockAccess.getBlock(x, y, z - 1);
        Block block_ZP   = renderBlocks.blockAccess.getBlock(x, y, z + 1);
        Block block_XZNP = renderBlocks.blockAccess.getBlock(x - 1, y, z + 1);
        Block block_XZPP = renderBlocks.blockAccess.getBlock(x + 1, y, z + 1);
        Block block_XZNN = renderBlocks.blockAccess.getBlock(x - 1, y, z - 1);
        Block block_XZPN = renderBlocks.blockAccess.getBlock(x + 1, y, z - 1);

        boolean isFluid_XN   = block_XN != null ? block_XN instanceof BlockLiquid || block_XN instanceof IFluidBlock : false;
        boolean isFluid_XP   = block_XP != null ? block_XP instanceof BlockLiquid || block_XP instanceof IFluidBlock : false;
        boolean isFluid_ZN   = block_ZN != null ? block_ZN instanceof BlockLiquid || block_ZN instanceof IFluidBlock : false;
        boolean isFluid_ZP   = block_ZP != null ? block_ZP instanceof BlockLiquid || block_ZP instanceof IFluidBlock : false;
        boolean isFluid_XZNP = block_XZNP != null ? block_XZNP instanceof BlockLiquid || block_XZNP instanceof IFluidBlock : false;
        boolean isFluid_XZPP = block_XZPP != null ? block_XZPP instanceof BlockLiquid || block_XZPP instanceof IFluidBlock : false;
        boolean isFluid_XZNN = block_XZNN != null ? block_XZNN instanceof BlockLiquid || block_XZNN instanceof IFluidBlock : false;
        boolean isFluid_XZPN = block_XZPN != null ? block_XZPN instanceof BlockLiquid || block_XZPN instanceof IFluidBlock : false;

        Block block = renderBlocks.blockAccess.getBlock(x, y, z);

        boolean isSolid_XN = block.isSideSolid(renderBlocks.blockAccess, x, y, z, ForgeDirection.WEST);
        boolean isSolid_XP = block.isSideSolid(renderBlocks.blockAccess, x, y, z, ForgeDirection.EAST);
        boolean isSolid_ZN = block.isSideSolid(renderBlocks.blockAccess, x, y, z, ForgeDirection.NORTH);
        boolean isSolid_ZP = block.isSideSolid(renderBlocks.blockAccess, x, y, z, ForgeDirection.SOUTH);

        int metadata = 0;
        int diagMetadata = 0;

        Block fluidBlock = null;
        Block diagFluidBlock = null;
        for (int count = 2; count < 10 && fluidBlock == null; ++count)
        {
            switch (count) {
                case 2:
                    if (isFluid_ZN && !isSolid_ZN) {
                        fluidBlock = renderBlocks.blockAccess.getBlock(x, y, z - 1);
                        if (metadata < renderBlocks.blockAccess.getBlockMetadata(x, y, z - 1)) {
                            metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z - 1);
                        }
                    }
                    break;
                case 3:
                    if (isFluid_ZP && !isSolid_ZP) {
                        fluidBlock = renderBlocks.blockAccess.getBlock(x, y, z + 1);
                        if (metadata < renderBlocks.blockAccess.getBlockMetadata(x, y, z + 1)) {
                            metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z + 1);
                        }
                    }
                    break;
                case 4:
                    if (isFluid_XN && !isSolid_XN) {
                        fluidBlock = renderBlocks.blockAccess.getBlock(x - 1, y, z);
                        if (metadata < renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z)) {
                            metadata = renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z);
                        }
                    }
                    break;
                case 5:
                    if (isFluid_XP && !isSolid_XP) {
                        fluidBlock = renderBlocks.blockAccess.getBlock(x + 1, y, z);
                        if (metadata < renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z)) {
                            metadata = renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z);
                        }
                    }
                    break;
                case 6:
                    if (isFluid_XZPN)  {
                        diagFluidBlock = renderBlocks.blockAccess.getBlock(x + 1, y, z - 1);
                        if (diagMetadata < renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z - 1)) {
                            diagMetadata = renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z - 1);
                        }
                    }
                    break;
                case 7:
                    if (isFluid_XZPP) {
                        diagFluidBlock = renderBlocks.blockAccess.getBlock(x + 1, y, z + 1);
                        if (diagMetadata < renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z + 1)) {
                            diagMetadata = renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z + 1);
                        }
                    }
                    break;
                case 8:
                    if (isFluid_XZNN) {
                        diagFluidBlock = renderBlocks.blockAccess.getBlock(x - 1, y, z - 1);
                        if (diagMetadata < renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z - 1)) {
                            diagMetadata = renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z - 1);
                        }
                    }
                    break;
                case 9:
                    if (isFluid_XZNP) {
                        diagFluidBlock = renderBlocks.blockAccess.getBlock(x - 1, y, z + 1);
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
                        if (
                                !isSolid_ZP &&
                                isFluid_ZP || !block_ZP.isSideSolid(renderBlocks.blockAccess, x, y, z + 1, ForgeDirection.NORTH) &&
                                (isFluid_XZNP && !block_ZP.isSideSolid(renderBlocks.blockAccess, x, y, z + 1, ForgeDirection.WEST) || isFluid_XZPP && !block_ZP.isSideSolid(renderBlocks.blockAccess, x, y, z + 1, ForgeDirection.EAST))
                                ) {
                            renderFluid = true;
                        }
                        break;
                    case 3:
                        if (
                                !isSolid_ZN &&
                                isFluid_ZN || !block_ZP.isSideSolid(renderBlocks.blockAccess, x, y, z - 1, ForgeDirection.SOUTH) &&
                                (isFluid_XZNN && !block_ZP.isSideSolid(renderBlocks.blockAccess, x, y, z - 1, ForgeDirection.WEST) || isFluid_XZPN && !block_ZP.isSideSolid(renderBlocks.blockAccess, x, y, z - 1, ForgeDirection.EAST))
                                ) {
                            renderFluid = true;
                        }
                        break;
                    case 4:
                        if (
                                !isSolid_XP &&
                                isFluid_XP || !block_XP.isSideSolid(renderBlocks.blockAccess, x + 1, y, z, ForgeDirection.WEST) &&
                                (isFluid_XZPP && !block_XP.isSideSolid(renderBlocks.blockAccess, x + 1, y, z, ForgeDirection.SOUTH) || isFluid_XZPN && !block_XP.isSideSolid(renderBlocks.blockAccess, x + 1, y, z, ForgeDirection.NORTH))
                                ) {
                            renderFluid = true;
                        }
                        break;
                    case 5:
                        if (
                                !isSolid_XN &&
                                isFluid_XN || !block_XN.isSideSolid(renderBlocks.blockAccess, x - 1, y, z, ForgeDirection.EAST) &&
                                (isFluid_XZNP && !block_XN.isSideSolid(renderBlocks.blockAccess, x - 1, y, z, ForgeDirection.SOUTH) || isFluid_XZNN && !block_XN.isSideSolid(renderBlocks.blockAccess, x - 1, y, z, ForgeDirection.NORTH))
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
                    double fluidHeight = (fluidBlock instanceof BlockLiquid ? 1.0D - 1.0F / 9.0F : 0.875F) - 0.0010000000474974513D;
                    renderBlocks.setRenderBounds(minX, offset, minZ, maxX, fluidHeight, maxZ);
                    float rgb[] = lightingHelper.getBlockRGB(new ItemStack(fluidBlock), fluidBlock, x, y, z);
                    renderBlocks.renderStandardBlockWithColorMultiplier(fluidBlock, x, y, z, rgb[0], rgb[1], rgb[2]);
                    return true;
                }
            }
        }

        return false;
    }

}
