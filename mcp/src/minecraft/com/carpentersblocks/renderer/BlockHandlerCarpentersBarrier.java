package com.carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import com.carpentersblocks.block.BlockCarpentersBarrier;
import com.carpentersblocks.data.Barrier;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersBarrier extends BlockHandlerBase {

    private boolean[] barrier;
    private boolean[] connect;
    private static final int YN = 0;
    private static final int YP = 1;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        renderBlocks.setRenderBounds(0.125D, 0.0D, 0.375D, 0.375D, 1.0D, 0.625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.625D, 0.0D, 0.375D, 0.875D, 1.0D, 0.625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0D, 0.8125D, 0.4375D, 1.0D, 0.9375D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0D, 0.4375D, 0.4375D, 1.0D, 0.5625D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
    }

    @Override
    /**
     * Renders barrier
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        int type = Barrier.getType(TE);
        ItemStack itemStack = getCoverForRendering();

        findBarriers(x, y, z);

        switch (type) {
            case Barrier.TYPE_PICKET:
                renderTypePicket(itemStack, x, y, z);
                break;
            case Barrier.TYPE_SHADOWBOX:
                renderTypeShadowbox(itemStack, x, y, z);
                break;
            case Barrier.TYPE_WALL:
                renderTypeWall(itemStack, x, y, z);
                break;
            default:
                renderTypeVanilla(itemStack, x, y, z);
        }
    }

    private void findBarriers(int x, int y, int z)
    {
        BlockCarpentersBarrier tempBlock = (BlockCarpentersBarrier) srcBlock;
        World world = TE.getWorldObj();

        connect = new boolean[] {
                tempBlock.canConnectBarrierTo(world, x, y - 1, z, ForgeDirection.UP),
                tempBlock.canConnectBarrierTo(world, x, y + 1, z, ForgeDirection.DOWN),
                tempBlock.canConnectBarrierTo(world, x, y, z - 1, ForgeDirection.SOUTH),
                tempBlock.canConnectBarrierTo(world, x, y, z + 1, ForgeDirection.NORTH),
                tempBlock.canConnectBarrierTo(world, x - 1, y, z, ForgeDirection.EAST),
                tempBlock.canConnectBarrierTo(world, x + 1, y, z, ForgeDirection.WEST),
                tempBlock.canConnectBarrierTo(world, x, y + 1, z - 1, ForgeDirection.SOUTH),
                tempBlock.canConnectBarrierTo(world, x, y + 1, z + 1, ForgeDirection.NORTH),
                tempBlock.canConnectBarrierTo(world, x - 1, y + 1, z, ForgeDirection.EAST),
                tempBlock.canConnectBarrierTo(world, x + 1, y + 1, z, ForgeDirection.WEST),
        };

        Block blockYN = Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y - 1, z)];
        Block blockYP = Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y + 1, z)];

        barrier = new boolean[] {
                blockYN != null ? blockYN.equals(srcBlock) : false,
                blockYP != null ? blockYP.equals(srcBlock) : false
        };
    }

    /**
     * Check if barrier at coordinates has a host.
     */
    private boolean hasPost(TEBase TE)
    {
        BlockCarpentersBarrier tempBlock = (BlockCarpentersBarrier) srcBlock;
        World world = TE.getWorldObj();

        Block blockYP = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord, TE.yCoord + 1, TE.zCoord)];

        boolean flowerPotYP = blockYP != null && blockYP.blockMaterial.equals(Material.circuits);

        boolean connectZN = tempBlock.canConnectBarrierTo(world, TE.xCoord, TE.yCoord, TE.zCoord - 1, ForgeDirection.SOUTH);
        boolean connectZP = tempBlock.canConnectBarrierTo(world, TE.xCoord, TE.yCoord, TE.zCoord + 1, ForgeDirection.NORTH);
        boolean connectXN = tempBlock.canConnectBarrierTo(world, TE.xCoord - 1, TE.yCoord, TE.zCoord, ForgeDirection.EAST);
        boolean connectXP = tempBlock.canConnectBarrierTo(world, TE.xCoord + 1, TE.yCoord, TE.zCoord, ForgeDirection.WEST);

        Block blockZN = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord - 1)];
        Block blockZP = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord + 1)];
        Block blockXN = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord - 1, TE.yCoord, TE.zCoord)];
        Block blockXP = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord + 1, TE.yCoord, TE.zCoord)];

        boolean adjGate = blockZN != null && blockZN.equals(BlockRegistry.blockCarpentersGate) ||
                          blockZP != null && blockZP.equals(BlockRegistry.blockCarpentersGate) ||
                          blockXN != null && blockXN.equals(BlockRegistry.blockCarpentersGate) ||
                          blockXP != null && blockXP.equals(BlockRegistry.blockCarpentersGate);

        boolean pathOnX = connectXN && connectXP;
        boolean pathOnZ = connectZN && connectZP;

        return Barrier.getPost(TE) == Barrier.HAS_POST ||
               flowerPotYP ||
               adjGate ||
               pathOnX == pathOnZ ||
               (connectXN || connectXP) && (connectZN || connectZP);
    }

    /**
     * Returns whether barrier has forced post or naturally forms a post at coordinates.
     */
    private boolean isPostAt()
    {
        if (hasPost(TE)) {
            return true;
        }

        /* Search up */

        for (int y = TE.yCoord + 1; y < renderBlocks.blockAccess.getHeight(); ++y) {

            Block block = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord, y, TE.zCoord)];

            if (block != null && block.equals(srcBlock)) {
                if (hasPost((TEBase)renderBlocks.blockAccess.getBlockTileEntity(TE.xCoord, y, TE.zCoord))) {
                    return true;
                }
            } else {
                break;
            }
        }

        /* Search down */

        for (int y = TE.yCoord - 1; y > 0; --y) {
            Block block = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord, y, TE.zCoord)];

            if (block != null && block.equals(srcBlock)) {
                if (hasPost((TEBase)renderBlocks.blockAccess.getBlockTileEntity(TE.xCoord, y, TE.zCoord))) {
                    return true;
                }
            } else {
                break;
            }
        }

        return false;
    }

    /**
     * If needed, renders a post at coordinates with given radius.
     */
    private void renderPost(ItemStack itemStack, int x, int y, int z, double radius)
    {
        if (isPostAt()) {
            double radiusLow = 0.5D - radius;
            double radiusHigh = 0.5D + radius;
            renderBlocks.setRenderBounds(radiusLow, 0.0D, radiusLow, radiusHigh, 1.0D, radiusHigh);
            renderBlock(itemStack, x, y, z);
        }
    }

    /**
     * If needed, renders horizontal plank at coordinates with given y values and thickness.
     */
    private void renderSupportPlank(ItemStack itemStack, int x, int y, int z, ForgeDirection dir, double depthRadius, double yMin, double yMax, boolean enforce)
    {
        boolean isTop = yMax - 0.5D > 0.5D - yMin;
        double radiusLow = 0.5D - depthRadius;
        double radiusHigh = 0.5D + depthRadius;

        if (isTop) {
            if (enforce || !connect[dir.ordinal() + 4] || !barrier[YP]) {
                renderBlockWithRotation(itemStack, x, y, z, radiusLow, yMin, 0.5D, radiusHigh, yMax, 1.0D, dir);
            }
        } else {
            if (enforce || !barrier[YN]) {
                renderBlockWithRotation(itemStack, x, y, z, radiusLow, yMin, 0.5D, radiusHigh, yMax, 1.0D, dir);
            }
        }
    }

    /**
     * Renders vanilla barrier at given coordinates.
     */
    private void renderTypeVanilla(ItemStack itemStack, int x, int y, int z)
    {
        renderPost(itemStack, x, y, z, 0.125D);

        double yOffset = Barrier.getType(TE) * 0.0625D;
        boolean singlePlank = Barrier.getType(TE) == Barrier.TYPE_VANILLA_X3;

        for (int side = 2; side < 6; ++side) {

            ForgeDirection dir = ForgeDirection.getOrientation(side);

            if (connect[side]) {
                if (singlePlank) {
                    renderSupportPlank(itemStack, x, y, z, dir, 0.0625D, 0.1875D, 0.9375D, true);
                } else {
                    renderSupportPlank(itemStack, x, y, z, dir, 0.0625D, 0.75D - yOffset, 0.9375D, true);
                    renderSupportPlank(itemStack, x, y, z, dir, 0.0625D, 0.375D - yOffset, 0.5625D, true);
                }
            }

        }
    }

    /**
     * Renders picket barrier at given coordinates.
     */
    private void renderTypePicket(ItemStack itemStack, int x, int y, int z)
    {
        renderPost(itemStack, x, y, z, 0.0625D);

        double yMin = barrier[YN] ? 0.0D : 0.0625D;

        for (int side = 2; side < 6; ++side)
        {
            ForgeDirection dir = ForgeDirection.getOrientation(side);

            if (connect[side]) {

                double yMax = connect[side + 4] && connect[YP] ? 1.0D : 0.875D;
                renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.6875D, 0.625D, yMax, 0.8125D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.375D, yMin, 0.6875D, 0.4375D, yMax, 0.8125D, dir);

                yMax = connect[side + 4] && connect[YP] ? 1.0D : 0.8125D;
                renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.9375D, 0.625D, yMax, 1.0D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.375D, yMin, 0.9375D, 0.4375D, yMax, 1.0D, dir);

                renderSupportPlank(itemStack, x, y, z, dir, 0.0625D, 0.625D, 0.6875D, false);
                renderSupportPlank(itemStack, x, y, z, dir, 0.0625D, 0.1875D, 0.25D, false);

            } else {

                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, yMin, 0.5625D, 0.5625D, 1.0D, 0.625D, dir);

            }
        }
    }

    /**
     * Renders wall barrier at given coordinates.
     */
    private void renderTypeWall(ItemStack itemStack, int x, int y, int z)
    {
        renderPost(itemStack, x, y, z, 0.25D);

        for (int side = 2; side < 6; ++side) {
            if (connect[side]) {
                double yMax = connect[side + 4] && connect[YP] ? 1.0D : 0.8125D;
                renderBlockWithRotation(itemStack, x, y, z, 0.3125D, 0.0D, 0.5D, 0.6875D, yMax, 1.0D, ForgeDirection.getOrientation(side));
            }
        }
    }

    /**
     * Renders shadowbox barrier at given coordinates.
     */
    private void renderTypeShadowbox(ItemStack itemStack, int x, int y, int z)
    {
        renderPost(itemStack, x, y, z, 0.0625D);

        for (int side = 2; side < 6; ++side) {
            if (connect[side]) {
                ForgeDirection dir = ForgeDirection.getOrientation(side);
                renderSupportPlank(itemStack, x, y, z, dir, 0.0625D, 0.75D, 0.875D, false);
                renderSupportPlank(itemStack, x, y, z, dir, 0.0625D, 0.125D, 0.25D, false);
                renderBlockWithRotation(itemStack, x, y, z, 0.5625D, 0.0D, 0.5D, 0.625D, 1.0D, 0.75D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.375D, 0.0D, 0.75D, 0.4375D, 1.0D, 1.0D, dir);
            }
        }
    }

}
