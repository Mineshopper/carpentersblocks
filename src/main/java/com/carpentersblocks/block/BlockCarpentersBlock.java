package com.carpentersblocks.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.data.Slab;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.handler.EventHandler;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersBlock extends BlockCoverable {

    private static float[][] bounds = {
        { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F }, // FULL BLOCK
        { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F }, // SLAB WEST
        { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F }, // SLAB EAST
        { 0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F }, // SLAB DOWN
        { 0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F }, // SLAB UP
        { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F }, // SLAB NORTH
        { 0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F }  // SLAB SOUTH
    };

    public BlockCarpentersBlock(Material material)
    {
        super(material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns a base icon that doesn't rely on blockIcon, which
     * is set prior to texture stitch events.
     */
    public IIcon getIcon()
    {
        return IconRegistry.icon_uncovered_quartered;
    }

    @Override
    /**
     * Alter type.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int data = BlockProperties.getMetadata(TE);

        if (++data > Slab.SLAB_Z_POS) {
            data = Slab.BLOCK_FULL;
        }

        BlockProperties.setMetadata(TE, data);

        return true;
    }

    @Override
    /**
     * Alternate between full 1m cube and slab.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int data = BlockProperties.getMetadata(TE);

        if (data == Slab.BLOCK_FULL) {
            switch (EventHandler.eventFace)
            {
                case 0:
                    data = Slab.SLAB_Y_POS;
                    break;
                case 1:
                    data = Slab.SLAB_Y_NEG;
                    break;
                case 2:
                    data = Slab.SLAB_Z_POS;
                    break;
                case 3:
                    data = Slab.SLAB_Z_NEG;
                    break;
                case 4:
                    data = Slab.SLAB_X_POS;
                    break;
                case 5:
                    data = Slab.SLAB_X_NEG;
                    break;
            }
        } else {
            data = Slab.BLOCK_FULL;
        }

        BlockProperties.setMetadata(TE, data);

        return true;
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            int data = BlockProperties.getMetadata(TE);
            if (data < bounds.length) {
                setBlockBounds(bounds[data][0], bounds[data][1], bounds[data][2], bounds[data][3], bounds[data][4], bounds[data][5]);
            }
        }
    }

    @Override
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
    {
        setBlockBoundsBasedOnState(world, x, y, z);
        super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            int data = Slab.BLOCK_FULL;

            if (!entityLiving.isSneaking()) {

                /* Match block type with adjacent type if possible. */

                TEBase[] TE_list = getAdjacentTileEntities(world, x, y, z);

                for (TEBase TE_current : TE_list) {
                    if (TE_current != null) {
                        if (TE_current.getBlockType().equals(this)) {
                            data = BlockProperties.getMetadata(TE_current);
                        }
                    }
                }

            }

            BlockProperties.setMetadata(TE, data);
        }
    }

    @Override
    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     */
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            if (isBlockSolid(world, x, y, z)) {

                int data = BlockProperties.getMetadata(TE);

                if (data == Slab.BLOCK_FULL) {
                    return true;
                } else if (data == Slab.SLAB_Y_NEG && side == ForgeDirection.DOWN) {
                    return true;
                } else if (data == Slab.SLAB_Y_POS && side == ForgeDirection.UP) {
                    return true;
                } else if (data == Slab.SLAB_Z_NEG && side == ForgeDirection.NORTH) {
                    return true;
                } else if (data == Slab.SLAB_Z_POS && side == ForgeDirection.SOUTH) {
                    return true;
                } else if (data == Slab.SLAB_X_NEG && side == ForgeDirection.WEST) {
                    return true;
                } else if (data == Slab.SLAB_X_POS && side == ForgeDirection.EAST) {
                    return true;
                }

            }

        }

        return false;
    }

    /**
     * Called to determine whether to allow the a block to handle its own indirect power rather than using the default rules.
     * @param world The world
     * @param x The x position of this block instance
     * @param y The y position of this block instance
     * @param z The z position of this block instance
     * @param side The INPUT side of the block to be powered - ie the opposite of this block's output side
     * @return Whether Block#isProvidingWeakPower should be called when determining indirect power
     */
    @Override
    public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            int data = BlockProperties.getMetadata(TE);
            return data == Slab.BLOCK_FULL;
        }

        return super.shouldCheckWeakPower(world, x, y, z, side);
    }

    @Override
    /**
     * Compares dimensions and coordinates of two opposite
     * sides to determine whether they share faces.
     */
    protected boolean shareFaces(TEBase TE_adj, TEBase TE_src, ForgeDirection side_adj, ForgeDirection side_src)
    {
        if (TE_adj.getBlockType() == this) {

            setBlockBoundsBasedOnState(TE_src.getWorldObj(), TE_src.xCoord, TE_src.yCoord, TE_src.zCoord);
            double[] bnds_src = { getBlockBoundsMinX(), getBlockBoundsMinY(), getBlockBoundsMinZ(), getBlockBoundsMaxX(), getBlockBoundsMaxY(), getBlockBoundsMaxZ() };
            setBlockBoundsBasedOnState(TE_adj.getWorldObj(), TE_adj.xCoord, TE_adj.yCoord, TE_adj.zCoord);

            switch (side_src) {
                case DOWN:
                    return maxY == 1.0D && bnds_src[1] == 0.0D && minX == bnds_src[0] && maxX == bnds_src[3] && minZ == bnds_src[2] && maxZ == bnds_src[5];
                case UP:
                    return minY == 0.0D && bnds_src[4] == 1.0D && minX == bnds_src[0] && maxX == bnds_src[3] && minZ == bnds_src[2] && maxZ == bnds_src[5];
                case NORTH:
                    return maxZ == 1.0D && bnds_src[2] == 0.0D && minX == bnds_src[0] && maxX == bnds_src[3] && minY == bnds_src[1] && maxY == bnds_src[4];
                case SOUTH:
                    return minZ == 0.0D && bnds_src[5] == 1.0D && minX == bnds_src[0] && maxX == bnds_src[3] && minY == bnds_src[1] && maxY == bnds_src[4];
                case WEST:
                    return maxX == 1.0D && bnds_src[0] == 0.0D && minY == bnds_src[1] && maxY == bnds_src[4] && minZ == bnds_src[2] && maxZ == bnds_src[5];
                case EAST:
                    return minX == 0.0D && bnds_src[3] == 1.0D && minY == bnds_src[1] && maxY == bnds_src[4] && minZ == bnds_src[2] && maxZ == bnds_src[5];
                default:
                    return false;
            }

        }

        return super.shareFaces(TE_adj, TE_src, side_adj, side_src);
    }

    @Override
    /**
     * Returns whether block can support cover on side.
     */
    public boolean canCoverSide(TEBase TE, World world, int x, int y, int z, int side)
    {
        return true;
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersBlockRenderID;
    }

}
