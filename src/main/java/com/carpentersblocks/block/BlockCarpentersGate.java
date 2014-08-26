package com.carpentersblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import com.carpentersblocks.data.Barrier;
import com.carpentersblocks.data.Gate;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersGate extends BlockCoverable {

    public BlockCarpentersGate(Material material)
    {
        super(material);
    }

    @Override
    /**
     * Alters gate type or sub-type and returns result.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int type = Gate.getType(TE);

        if (entityPlayer.isSneaking()) {

            /*
             * Cycle through sub-types
             */
            if (type <= Gate.TYPE_VANILLA_X3) {
                if (++type > Gate.TYPE_VANILLA_X3) {
                    type = Gate.TYPE_VANILLA;
                }
            }

        } else {

            /*
             * Cycle through barrier types
             */
            if (type <= Gate.TYPE_VANILLA_X3) {
                type = Gate.TYPE_PICKET;
            } else if (++type > Gate.TYPE_WALL) {
                type = Gate.TYPE_VANILLA;
            }

        }

        Gate.setType(TE, type);

        return true;
    }

    @Override
    /**
     * Opens or closes gate on right click.
     */
    protected void postOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, ActionResult actionResult)
    {
        if (Gate.getState(TE) == Gate.STATE_OPEN) {

            Gate.setState(TE, Gate.STATE_CLOSED, true);
            cycleNeighborGate(TE, TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord);

        } else {

            int facing = (MathHelper.floor_double(entityPlayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;
            Gate.setState(TE, Gate.STATE_OPEN, true);

            if (Gate.getFacing(TE) == Gate.FACING_ON_X) {
                Gate.setDirOpen(TE, facing == 0 ? Gate.DIR_POS : Gate.DIR_NEG);
            } else {
                Gate.setDirOpen(TE, facing == 3 ? Gate.DIR_POS : Gate.DIR_NEG);
            }

            cycleNeighborGate(TE, TE.getWorldObj(), TE.xCoord, TE.yCoord, TE.zCoord);

        }

        actionResult.setAltered().setNoSound();
    }

    @Override
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return !world.getBlock(x, y - 1, z).getMaterial().isSolid() ? false : super.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            if (Gate.getState(TE) == Gate.STATE_OPEN) {
                return null;
            } else if (Gate.getFacing(TE) == Gate.FACING_ON_Z) {
                if (Gate.getType(TE) == Gate.TYPE_VANILLA || Gate.getType(TE) == Gate.TYPE_WALL) {
                    return AxisAlignedBB.getBoundingBox(x + 0.4375F, y, z, x + 0.5625F, y + 1.5F, z + 1.0F);
                } else {
                    return AxisAlignedBB.getBoundingBox(x + 0.375F, y, z, x + 0.625F, y + 1.5F, z + 1.0F);
                }
            } else {
                if (Gate.getType(TE) == Gate.TYPE_VANILLA || Gate.getType(TE) == Gate.TYPE_WALL) {
                    return AxisAlignedBB.getBoundingBox(x, y, z + 0.4375F, x + 1.0F, y + 1.5F, z + 0.5625F);
                } else {
                    return AxisAlignedBB.getBoundingBox(x, y, z + 0.375F, x + 1.0F, y + 1.5F, z + 0.625F);
                }
            }

        }

        return null;
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            if (Gate.getFacing(TE) == Gate.FACING_ON_Z) {
                if (Gate.getType(TE) == Gate.TYPE_VANILLA || Gate.getType(TE) == Gate.TYPE_WALL) {
                    setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
                } else {
                    setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
                }
            } else {
                if (Gate.getType(TE) == Gate.TYPE_VANILLA || Gate.getType(TE) == Gate.TYPE_WALL) {
                    setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
                } else {
                    setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
                }
            }

        }
    }

    /**
     * Opens or closes one neighboring gate above or below block.
     */
    private void cycleNeighborGate(TEBase TE, World world, int x, int y, int z)
    {
        boolean isGateBelow = world.getBlock(x, y - 1, z).equals(this);
        boolean isGateAbove = world.getBlock(x, y + 1, z).equals(this);

        /*
         * Will only check for gate above or below, and limit to only activating a single stacked gate.
         * It is done this way intentionally.
         */
        if (isGateBelow) {

            TEBase TE_YN = getTileEntity(world, x, y - 1, z);
            if (Gate.getFacing(TE_YN) == Gate.getFacing(TE)) {
                Gate.setDirOpen(TE_YN, Gate.getDirOpen(TE));
                Gate.setState(TE_YN, Gate.getState(TE), false);
            }

        } else if (isGateAbove) {

            TEBase TE_YP = getTileEntity(world, x, y + 1, z);
            if (Gate.getFacing(TE_YP) == Gate.getFacing(TE)) {
                Gate.setDirOpen(TE_YP, Gate.getDirOpen(TE));
                Gate.setState(TE_YP, Gate.getState(TE), false);
            }

        }
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {

            int facing = (MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;

            Gate.setFacing(TE, facing == 3 || facing == 1 ? Gate.FACING_ON_Z : Gate.FACING_ON_X);

            /* Match block type with adjacent type if possible. */

            TEBase[] TE_list = getAdjacentTileEntities(world, x, y, z);

            for (TEBase TE_current : TE_list) {

                if (TE_current != null) {

                    Block block = TE_current.getBlockType();

                    if (block.equals(this)) {
                        Gate.setType(TE, Gate.getType(TE_current));
                    } else if (block.equals(BlockRegistry.blockCarpentersGate)) {
                        Gate.setType(TE, Barrier.getType(TE_current));
                    }

                }

            }

        }

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (!world.isRemote) {

            TEBase TE = getTileEntity(world, x, y, z);

            if (TE != null) {

                boolean isPowered = world.isBlockIndirectlyGettingPowered(x, y, z);

                if (isPowered || block != null && block.canProvidePower())
                {
                    int state = Gate.getState(TE);

                    if (isPowered && state == Gate.STATE_CLOSED) {
                        Gate.setState(TE, Gate.STATE_OPEN, true);
                        cycleNeighborGate(TE, world, x, y, z);
                    } else if (!isPowered && state == Gate.STATE_OPEN) {
                        Gate.setState(TE, Gate.STATE_CLOSED, true);
                        cycleNeighborGate(TE, world, x, y, z);
                    }
                }

            }

        }

        super.onNeighborBlockChange(world, x, y, z, block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: world, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        return true;
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersGateRenderID;
    }

}
