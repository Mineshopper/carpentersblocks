package com.carpentersblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.data.ISided;
import com.carpentersblocks.tileentity.TEBase;

public class BlockSided extends BlockCoverable {

    private ISided data = null;

    public BlockSided(Material material, ISided data)
    {
        super(material);
        this.data = data;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    /**
     * Checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        if (canAttachToSide(side)) {
            ForgeDirection dir = ForgeDirection.getOrientation(side);
            return world.getBlock(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ).isSideSolid(world, x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ, dir);
        } else {
            return false;
        }
    }

    @Override
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        return side;
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
            int meta = world.getBlockMetadata(x, y, z);
            data.setDirection(TE, ForgeDirection.getOrientation(meta));
        }
    }

    /**
     * Called after a block is placed
     */
    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int metadata)
    {
        /*
         * Part of world.setBlock() involves updating neighbors.  Since we
         * prevent this in ItemBlockSided, we'll invoke it here.
         */

        world.notifyBlocksOfNeighborChange(x, y, z, this);
    }

    @Override
    /**
     * How many world ticks before ticking
     */
    public int tickRate(World world)
    {
        return 20;
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);

        if (!world.isRemote) {
            TEBase TE = getTileEntity(world, x, y, z);
            if (TE != null && !canPlaceBlockOnSide(world, x, y, z, data.getDirection(TE).ordinal())) {
                dropBlockAsItem(world, x, y, z, createStackedBlock(0));
                world.setBlockToAir(x, y, z);
            }
        }
    }

    /**
     * Notifies relevant blocks of a change in power output.
     *
     * @param  world
     * @param  x
     * @param  y
     * @param  z
     * @return nothing
     */
    public void notifyBlocksOfPowerChange(World world, int x, int y, int z)
    {
        /* Notify strong power change. */

        world.notifyBlockChange(x, y, z, this);

        /* Notify weak power change. */

        if (canProvidePower()) {
            TEBase TE = getTileEntity(world, x, y, z);
            if (TE != null) {
                ForgeDirection dir = data.getDirection(TE);
                world.notifyBlocksOfNeighborChange(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ, this);
            } else {

                /* When block is destroyed, notify neighbors in all directions. */

                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    world.notifyBlocksOfNeighborChange(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ, this);
                }

            }
        }
    }

    @Override
    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        int power = super.isProvidingWeakPower(blockAccess, x, y, z, side);

        if (canProvidePower()) {
            TEBase TE = getTileEntity(blockAccess, x, y, z);
            if (TE != null) {
                int tempPower = getPowerOutput(TE);
                if (tempPower > power) {
                    power = tempPower;
                }
            }
        }

        return power;
    }

    @Override
    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        int power = super.isProvidingStrongPower(blockAccess, x, y, z, side);

        if (canProvidePower()) {
            TEBase TE = getTileEntity(blockAccess, x, y, z);
            if (TE != null) {
                if (side == data.getDirection(TE).ordinal()) {
                    int tempPower = getPowerOutput(TE);
                    if (tempPower > power) {
                        power = tempPower;
                    }
                }
            }
        }

        return power;
    }

    @Override
    /**
     * Ejects contained items into the world, and notifies neighbors of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        if (canProvidePower()) {
            notifyBlocksOfPowerChange(world, x, y, z);
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    /**
     * Gets block-specific power level from 0 to 15.
     *
     * @param  TE  the {@link TEBase}
     * @return the power output
     */
    public int getPowerOutput(TEBase TE)
    {
        return 0;
    }

    /**
     * Whether block can be attached to specified side of another block.
     *
     * @param  side the side
     * @return whether side is supported
     */
    public boolean canAttachToSide(int side)
    {
        return true;
    }

}
